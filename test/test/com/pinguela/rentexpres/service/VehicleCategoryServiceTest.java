package test.com.pinguela.rentexpres.service;

import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.VehicleCategoryDTO;
import com.pinguela.rentexpres.service.VehicleCategoryService;
import com.pinguela.rentexpres.service.impl.VehicleCategoryServiceImpl;

public class VehicleCategoryServiceTest {
    public static void main(String[] args) {
        VehicleCategoryService service = new VehicleCategoryServiceImpl();

        try {
            VehicleCategoryDTO category = service.findById(1, "en");
            System.out.println("Vehicle category found: " + category);

            List<VehicleCategoryDTO> categories = service.findAll("en");
            System.out.println("Total vehicle categories: " + (categories != null ? categories.size() : 0));
            if (categories != null) {
                for (VehicleCategoryDTO dto : categories) {
                    System.out.println(dto);
                }
            }
        } catch (RentexpresException e) {
            e.printStackTrace();
        }
    }
}