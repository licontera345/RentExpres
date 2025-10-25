package test.com.pinguela.rentexpres.service;

import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.CityDTO;
import com.pinguela.rentexpres.service.CityService;
import com.pinguela.rentexpres.service.impl.CityServiceImpl;

public class CityServiceTest {
    public static void main(String[] args) {
        CityService service = new CityServiceImpl();

        try {
            CityDTO city = new CityDTO();
            city.setCityName("Test City");
            city.setProvinceId(1);
            boolean created = service.create(city);
            System.out.println("Create result: " + created);
            if (created) {
                System.out.println("City created with ID: " + city.getId());
            }

            CityDTO found = service.findById(city.getId());
            System.out.println("City found: " + found);

            if (found != null) {
                found.setCityName("Updated City");
                boolean updated = service.update(found);
                System.out.println("Update result: " + updated);
                CityDTO updatedCity = service.findById(found.getId());
                System.out.println("City after update: " + updatedCity);
            }

            List<CityDTO> cities = service.findAll();
            System.out.println("Total cities: " + (cities != null ? cities.size() : 0));
            if (cities != null) {
                for (CityDTO dto : cities) {
                    System.out.println(dto);
                }
            }

            List<CityDTO> citiesByProvince = service.findByProvinceId(1);
            System.out.println("Cities by province: " + (citiesByProvince != null ? citiesByProvince.size() : 0));

            boolean deleted = service.delete(city);
            System.out.println("Delete result: " + deleted);

        } catch (RentexpresException e) {
            e.printStackTrace();
        }
    }
}