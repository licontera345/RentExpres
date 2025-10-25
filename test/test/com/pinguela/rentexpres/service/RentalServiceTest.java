package test.com.pinguela.rentexpres.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.RentalCriteria;
import com.pinguela.rentexpres.model.RentalDTO;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.service.RentalService;
import com.pinguela.rentexpres.service.impl.RentalServiceImpl;

public class RentalServiceTest {
    public static void main(String[] args) {
        RentalService service = new RentalServiceImpl();

        try {
            RentalDTO rental = new RentalDTO();
            rental.setReservationId(1);
            rental.setStartDateEffective(LocalDateTime.of(2025, 4, 1, 9, 0));
            rental.setEndDateEffective(LocalDateTime.of(2025, 4, 5, 18, 0));
            rental.setInitialKm(100);
            rental.setFinalKm(150);
            rental.setRentalStatusId(1);
            rental.setTotalCost(new BigDecimal("250.00"));
            rental.setPickupHeadquartersId(1);
            rental.setReturnHeadquartersId(1);

            boolean created = service.create(rental);
            System.out.println("Create result: " + created);
            System.out.println("New rental ID: " + rental.getRentalId());

            RentalDTO found = service.findById(rental.getRentalId());
            System.out.println("Rental found: " + found);

            if (found != null) {
                found.setEndDateEffective(LocalDateTime.of(2025, 4, 6, 12, 0));
                boolean updated = service.update(found);
                System.out.println("Update result: " + updated);
                RentalDTO updatedRental = service.findById(found.getRentalId());
                System.out.println("Rental after update: " + updatedRental);
            }

            List<RentalDTO> rentals = service.findAll();
            System.out.println("Total rentals: " + (rentals != null ? rentals.size() : 0));
            if (rentals != null) {
                for (RentalDTO dto : rentals) {
                    System.out.println(dto);
                }
            }

            RentalCriteria criteria = new RentalCriteria();
            criteria.setRentalStatusId(1);
            criteria.setPageNumber(1);
            criteria.setPageSize(10);
            Results<RentalDTO> results = service.findByCriteria(criteria);
            System.out.println("Results by criteria - Total records: " + results.getTotalRecords());
            if (results.getResults() != null) {
                for (RentalDTO dto : results.getResults()) {
                    System.out.println(dto);
                }
            }

            boolean deleted = service.delete(rental.getRentalId());
            System.out.println("Delete result: " + deleted);

        } catch (RentexpresException e) {
            e.printStackTrace();
        }
    }
}