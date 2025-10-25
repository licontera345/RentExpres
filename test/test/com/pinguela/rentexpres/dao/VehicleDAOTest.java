package test.com.pinguela.rentexpres.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import com.pinguela.rentexpres.dao.VehicleDAO;
import com.pinguela.rentexpres.dao.impl.VehicleDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.model.VehicleCriteria;
import com.pinguela.rentexpres.model.VehicleDTO;
import com.pinguela.rentexpres.util.JDBCUtils;

public class VehicleDAOTest {
    public static void main(String[] args) {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            VehicleDAO dao = new VehicleDAOImpl();

            VehicleDTO vehicle = new VehicleDTO();
            vehicle.setBrand("Toyota");
            vehicle.setModel("Corolla");
            vehicle.setManufactureYear(2015);
            vehicle.setDailyPrice(new BigDecimal("50.0"));
            vehicle.setLicensePlate("AGC-123");
            vehicle.setVinNumber("XYZ987654321");
            vehicle.setCurrentMileage(120000);
            vehicle.setCategoryId(1);
            vehicle.setVehicleStatusId(1);
            vehicle.setCurrentHeadquartersId(1);
            vehicle.setCreatedAt(LocalDateTime.now());
            vehicle.setUpdatedAt(LocalDateTime.now());

            boolean created = dao.create(connection, vehicle);
            System.out.println("Create result: " + created);
            if (created) {
                System.out.println("New vehicle ID: " + vehicle.getVehicleId());
            }

            VehicleDTO found = dao.findById(connection, vehicle.getVehicleId());
            System.out.println("Vehicle found: " + found);

            if (found != null) {
                found.setCurrentMileage(125000);
                boolean updated = dao.update(connection, found);
                System.out.println("Update result: " + updated);
                VehicleDTO updatedVehicle = dao.findById(connection, found.getVehicleId());
                System.out.println("Vehicle after update: " + updatedVehicle);
            }

            List<VehicleDTO> list = dao.findAll(connection);
            System.out.println("Total vehicles: " + list.size());
            for (VehicleDTO dto : list) {
                System.out.println(dto);
            }

            VehicleCriteria criteria = new VehicleCriteria();
            criteria.setBrand("Toyota");
            criteria.setPageNumber(1);
            criteria.setPageSize(10);
            Results<VehicleDTO> results = dao.findByCriteria(connection, criteria);
            System.out.println("Results with criteria - Total records: " + results.getTotalRecords());
            if (results.getResults() != null) {
                for (VehicleDTO dto : results.getResults()) {
                    System.out.println(dto);
                }
            }

            boolean deleted = dao.delete(connection, vehicle.getVehicleId());
            System.out.println("Delete result: " + deleted);

        } catch (DataException | SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(connection);
        }
    }
}