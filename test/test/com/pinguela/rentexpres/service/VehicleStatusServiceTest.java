package test.com.pinguela.rentexpres.service;

import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.VehicleStatusDTO;
import com.pinguela.rentexpres.service.VehicleStatusService;
import com.pinguela.rentexpres.service.impl.VehicleStatusServiceImpl;

public class VehicleStatusServiceTest {
    public static void main(String[] args) {
        VehicleStatusService service = new VehicleStatusServiceImpl();

        try {
            VehicleStatusDTO status = service.findById(1, "en");
            System.out.println("Vehicle status found: " + status);

            List<VehicleStatusDTO> statuses = service.findAll("en");
            System.out.println("Total vehicle statuses: " + (statuses != null ? statuses.size() : 0));
            if (statuses != null) {
                for (VehicleStatusDTO dto : statuses) {
                    System.out.println(dto);
                }
            }
        } catch (RentexpresException e) {
            e.printStackTrace();
        }
    }
}