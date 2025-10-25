package test.com.pinguela.rentexpres.service;

import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.ReservationStatusDTO;
import com.pinguela.rentexpres.service.ReservationStatusService;
import com.pinguela.rentexpres.service.impl.ReservationStatusServiceImpl;

public class ReservationStatusServiceTest {
    public static void main(String[] args) {
        ReservationStatusService service = new ReservationStatusServiceImpl();

        try {
            ReservationStatusDTO status = service.findById(1, "en");
            System.out.println("Reservation status found: " + status);

            List<ReservationStatusDTO> statuses = service.findAll("en");
            System.out.println("Total reservation statuses: " + (statuses != null ? statuses.size() : 0));
            if (statuses != null) {
                for (ReservationStatusDTO dto : statuses) {
                    System.out.println(dto);
                }
            }
        } catch (RentexpresException e) {
            e.printStackTrace();
        }
    }
}