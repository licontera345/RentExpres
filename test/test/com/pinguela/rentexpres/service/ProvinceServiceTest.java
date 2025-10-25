package test.com.pinguela.rentexpres.service;

import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.ProvinceDTO;
import com.pinguela.rentexpres.service.ProvinceService;
import com.pinguela.rentexpres.service.impl.ProvinceServiceImpl;

public class ProvinceServiceTest {
    public static void main(String[] args) {
        ProvinceService service = new ProvinceServiceImpl();

        try {
            ProvinceDTO province = new ProvinceDTO();
            province.setProvinceName("Test Province");
            boolean created = service.create(province);
            System.out.println("Create result: " + created);
            if (created) {
                System.out.println("Province created with ID: " + province.getProvinceId());
            }

            ProvinceDTO found = service.findById(province.getProvinceId());
            System.out.println("Province found: " + found);

            if (found != null) {
                found.setProvinceName("Updated Province");
                boolean updated = service.update(found);
                System.out.println("Update result: " + updated);
                ProvinceDTO updatedProvince = service.findById(found.getProvinceId());
                System.out.println("Province after update: " + updatedProvince);
            }

            List<ProvinceDTO> provinces = service.findAll();
            System.out.println("Total provinces: " + (provinces != null ? provinces.size() : 0));
            if (provinces != null) {
                for (ProvinceDTO dto : provinces) {
                    System.out.println(dto);
                }
            }

            boolean deleted = service.delete(province.getProvinceId());
            System.out.println("Delete result: " + deleted);

        } catch (RentexpresException e) {
            e.printStackTrace();
        }
    }
}