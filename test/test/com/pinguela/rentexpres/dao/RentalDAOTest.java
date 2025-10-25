package test.com.pinguela.rentexpres.dao;

import com.pinguela.rentexpres.dao.impl.*;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.*;
import com.pinguela.rentexpres.util.JDBCUtils;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;

public class RentalDAOTest {

    public static void main(String[] args) {

        Connection connection = null;
        RentalDAOImpl rentalDAO = new RentalDAOImpl();
        ReservationDAOImpl reservationDAO = new ReservationDAOImpl();
        VehicleDAOImpl vehicleDAO = new VehicleDAOImpl();
        HeadquartersDAOImpl hqDAO = new HeadquartersDAOImpl();
        EmployeeDAOImpl employeeDAO = new EmployeeDAOImpl();

        try {
            connection = JDBCUtils.getConnection();
            System.out.println(" Connection established successfully.");

    
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT IGNORE INTO province (province_id, province_name) VALUES (1, 'Madrid')")) {
                ps.executeUpdate();
            }
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT IGNORE INTO city (city_id, city_name, province_id) VALUES (1, 'Madrid', 1)")) {
                ps.executeUpdate();
            }

            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT IGNORE INTO address (address_id, city_id, street, number) VALUES (1, 1, 'Main Avenue', '101')")) {
                ps.executeUpdate();
            }

            HeadquartersDTO hq = new HeadquartersDTO();
            hq.setName("Oficina Central");
            hq.setPhone("910000111");
            hq.setEmail("centralHQ@rentexpres.com");
            hq.setAddressId(1);
            hqDAO.create(connection, hq);

            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT IGNORE INTO role (role_id, role_name) VALUES (2, 'EMPLOYEE')")) {
                ps.executeUpdate();
            }

            EmployeeDTO emp = new EmployeeDTO();
            emp.setEmployeeName("emp.rental");
            emp.setPassword("abc123.");
            emp.setRoleId(2);
            emp.setHeadquartersId(hq.getId());
            emp.setFirstName("Laura");
            emp.setLastName1("Gomez");
            emp.setPhone("620333444");
            emp.setEmail("laura.gomez@rentexpres.com");
            employeeDAO.create(connection, emp);

            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT IGNORE INTO vehicle_status (vehicle_status_id, status_name) VALUES (1, 'Disponible')")) {
                ps.executeUpdate();
            }
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT IGNORE INTO vehicle_category (category_id, category_name) VALUES (1, 'Economy')")) {
                ps.executeUpdate();
            }

            VehicleDTO vehicle = new VehicleDTO();
            vehicle.setBrand("Toyota");
            vehicle.setModel("Yaris");
            vehicle.setManufactureYear(2020);
            vehicle.setDailyPrice(new BigDecimal("35.00")); //BigDecimal
            vehicle.setLicensePlate("0000AAA");
            vehicle.setVinNumber("VIN0000AAA0000AAA");
            vehicle.setCurrentMileage(45000);
            vehicle.setVehicleStatusId(1);
            vehicle.setCategoryId(1);
            vehicle.setCurrentHeadquartersId(hq.getId());
            vehicleDAO.create(connection, vehicle);

            // Usuario (cliente)
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT IGNORE INTO role (role_id, role_name) VALUES (3, 'USER')")) {
                ps.executeUpdate();
            }

            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT IGNORE INTO user (user_id, username, first_name, last_name1, birth_date, email, password, role_id, phone, address_id, active_status) " +
                    "VALUES (1, 'user.test', 'John', 'Lopez', '1985-05-01', 'juan@test.com', 'abc123.', 3, '611111111', 1, 1)")) {
                ps.executeUpdate();
            }

            // Estado reserva + reserva
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT IGNORE INTO reservation_status (reservation_status_id, status_name) VALUES (1, 'Confirmed')")) {
                ps.executeUpdate();
            }

            ReservationDTO reservation = new ReservationDTO();
            reservation.setVehicleId(vehicle.getVehicleId());
            reservation.setUserId(1);
            reservation.setEmployeeId(emp.getId());
            reservation.setReservationStatusId(1);
            reservation.setPickupHeadquartersId(hq.getId());
            reservation.setReturnHeadquartersId(hq.getId());
            reservation.setStartDate(LocalDateTime.now().minusDays(2));
            reservation.setEndDate(LocalDateTime.now().plusDays(3));
            reservationDAO.create(connection, reservation);

            // Estado alquiler
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT IGNORE INTO rental_status (rental_status_id, status_name) VALUES (1, 'In Progress')")) {
                ps.executeUpdate();
            }


            RentalDTO rental = new RentalDTO();
            rental.setStartDateEffective(LocalDateTime.now());
            rental.setEndDateEffective(LocalDateTime.now().plusDays(3));
            rental.setInitialKm(45000);
            rental.setFinalKm(45100);
            rental.setRentalStatusId(1);
            rental.setTotalCost(new BigDecimal("175.50")); //BigDecimal
            rental.setReservationId(reservation.getReservationId());
            rental.setPickupHeadquartersId(hq.getId());
            rental.setReturnHeadquartersId(hq.getId());

            rentalDAO.create(connection, rental);
            System.out.println(" Rental creado correctamente con ID: " + rental.getRentalId());

            // Buscar por ID
            RentalDTO found = rentalDAO.findById(connection, rental.getRentalId());
            if (found != null) {
                System.out.println("🔍 Rental encontrado con ID: " + found.getRentalId());
            }

            // Actualizar
            found.setFinalKm(45200);
            found.setTotalCost(new BigDecimal("200.00")); // BigDecimal
            rentalDAO.update(connection, found);
            System.out.println(" Rental actualizado correctamente.");

            // Listar todos
            List<RentalDTO> all = rentalDAO.findAll(connection);
            System.out.println("\n Listado de rentals (" + all.size() + "):");
            for (RentalDTO r : all) {
                System.out.println("  ID: " + r.getRentalId() + " | Reserva: " + r.getReservationId() + " | Total: " + r.getTotalCost());
            }

            // Eliminar
            rentalDAO.delete(connection, found.getRentalId());
            System.out.println(" Rental eliminado correctamente.");
        } catch (DataException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(connection);
        }
    }
}