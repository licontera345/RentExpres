package test.com.pinguela.rentexpres.service;

import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.RentalStatusDTO;
import com.pinguela.rentexpres.service.RentalStatusService;
import com.pinguela.rentexpres.service.impl.RentalStatusServiceImpl;

public class RentalStatusServiceTest {
    public static void main(String[] args) {
        RentalStatusService service = new RentalStatusServiceImpl();

        try {
            RentalStatusDTO status = service.findById(1, "en");
            System.out.println("Rental status found: " + status);

            List<RentalStatusDTO> statuses = service.findAll("en");
            System.out.println("Total rental statuses: " + (statuses != null ? statuses.size() : 0));
            if (statuses != null) {
                for (RentalStatusDTO dto : statuses) {
                    System.out.println(dto);
                }
            }
        } catch (RentexpresException e) {
            e.printStackTrace();
        }
    }
}