package test.com.pinguela.rentexpres.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import com.pinguela.rentexpres.dao.ReservationDAO;
import com.pinguela.rentexpres.dao.impl.ReservationDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.ReservationCriteria;
import com.pinguela.rentexpres.model.ReservationDTO;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.util.JDBCUtils;

public class ReservationDAOTest {
    public static void main(String[] args) {
        Connection connection = null;
        ReservationDAO dao = new ReservationDAOImpl();
        try {
            connection = JDBCUtils.getConnection();

            // Create a new reservation
            ReservationDTO newReservation = new ReservationDTO();
            newReservation.setVehicleId(1);
            newReservation.setUserId(1);
            newReservation.setEmployeeId(1);
            newReservation.setReservationStatusId(1);
            newReservation.setPickupHeadquartersId(1);
            newReservation.setReturnHeadquartersId(1);
            newReservation.setStartDate(LocalDateTime.of(2025, 4, 1, 10, 0));
            newReservation.setEndDate(LocalDateTime.of(2025, 4, 5, 10, 0));

            boolean created = dao.create(connection, newReservation);
            System.out.println("Create result: " + created);
            if (created) {
                System.out.println("New reservation ID: " + newReservation.getReservationId());
            }

            // Find by ID
            ReservationDTO found = dao.findById(connection, newReservation.getReservationId());
            System.out.println("Reservation found: " + found);

            // Update reservation
            if (found != null) {
                found.setEndDate(LocalDateTime.of(2025, 4, 6, 10, 0));
                boolean updated = dao.update(connection, found);
                System.out.println("Update result: " + updated);
                ReservationDTO updatedReservation = dao.findById(connection, found.getReservationId());
                System.out.println("Reservation after update: " + updatedReservation);
            }

            // List all reservations
            List<ReservationDTO> list = dao.findAll(connection);
            System.out.println("Total reservations found: " + list.size());
            for (ReservationDTO reservation : list) {
                System.out.println(reservation);
            }

            // Search reservations using criteria
            ReservationCriteria criteria = new ReservationCriteria();
            criteria.setVehicleId(1);
            criteria.setStartDateFrom(LocalDateTime.of(2025, 4, 1, 0, 0));
            criteria.setEndDateTo(LocalDateTime.of(2025, 4, 30, 23, 59));
            criteria.setPageNumber(1);
            criteria.setPageSize(10);
            Results<ReservationDTO> results = dao.findByCriteria(connection, criteria);
            System.out.println("Results with criteria - Total records: " + results.getTotalRecords());
            if (results.getResults() != null) {
                for (ReservationDTO reservation : results.getResults()) {
                    System.out.println(reservation);
                }
            }

            // Delete reservation
            boolean deleted = dao.delete(connection, newReservation.getReservationId());
            System.out.println("Delete result: " + deleted);

        } catch (DataException | SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(connection);
        }
    }
}