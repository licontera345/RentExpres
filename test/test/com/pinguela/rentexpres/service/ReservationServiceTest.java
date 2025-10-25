package test.com.pinguela.rentexpres.service;

import java.time.LocalDateTime;
import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.ReservationCriteria;
import com.pinguela.rentexpres.model.ReservationDTO;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.service.ReservationService;
import com.pinguela.rentexpres.service.impl.ReservationServiceImpl;

public class ReservationServiceTest {
    public static void main(String[] args) {
        ReservationService service = new ReservationServiceImpl();

        try {
            ReservationDTO newReservation = new ReservationDTO();
            newReservation.setVehicleId(1);
            newReservation.setUserId(1);
            newReservation.setEmployeeId(1);
            newReservation.setReservationStatusId(1);
            newReservation.setPickupHeadquartersId(1);
            newReservation.setReturnHeadquartersId(1);
            newReservation.setStartDate(LocalDateTime.of(2025, 4, 1, 10, 0));
            newReservation.setEndDate(LocalDateTime.of(2025, 4, 10, 10, 0));

            boolean created = service.create(newReservation);
            System.out.println("Create result: " + created);
            if (created) {
                System.out.println("Reservation created with ID: " + newReservation.getReservationId());
            }

            ReservationDTO reservation = service.findById(newReservation.getReservationId());
            System.out.println("Reservation found: " + reservation);

            if (reservation != null) {
                reservation.setEndDate(LocalDateTime.of(2025, 4, 12, 10, 0));
                boolean updated = service.update(reservation);
                System.out.println("Update result: " + updated);
                ReservationDTO updatedReservation = service.findById(reservation.getReservationId());
                System.out.println("Reservation after update: " + updatedReservation);
            }

            List<ReservationDTO> reservations = service.findAll();
            System.out.println("Total reservations: " + (reservations != null ? reservations.size() : 0));
            if (reservations != null) {
                for (ReservationDTO dto : reservations) {
                    System.out.println(dto);
                }
            }

            ReservationCriteria criteria = new ReservationCriteria();
            criteria.setVehicleId(1);
            criteria.setStartDateFrom(LocalDateTime.of(2025, 4, 1, 0, 0));
            criteria.setEndDateTo(LocalDateTime.of(2025, 4, 12, 23, 59));
            criteria.setPageNumber(1);
            criteria.setPageSize(10);

            Results<ReservationDTO> results = service.findByCriteria(criteria);
            System.out.println("Results findByCriteria - Total records: " + results.getTotalRecords());
            if (results.getResults() != null) {
                for (ReservationDTO dto : results.getResults()) {
                    System.out.println(dto);
                }
            }

            boolean deleted = service.delete(newReservation.getReservationId());
            System.out.println("Delete result: " + deleted);

        } catch (RentexpresException e) {
            e.printStackTrace();
        }
    }
}