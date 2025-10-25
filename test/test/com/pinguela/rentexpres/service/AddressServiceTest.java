package test.com.pinguela.rentexpres.service;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.AddressDTO;
import com.pinguela.rentexpres.service.AddressService;
import com.pinguela.rentexpres.service.impl.AddressServiceImpl;

public class AddressServiceTest {
    public static void main(String[] args) {
        AddressService service = new AddressServiceImpl();

        try {
            AddressDTO address = new AddressDTO();
            address.setCityId(1);
            address.setStreet("Main Street");
            address.setNumber("123");

            boolean created = service.create(address);
            System.out.println("Create result: " + created);
            if (created) {
                System.out.println("Address created with ID: " + address.getId());
            }

            AddressDTO found = service.findById(address.getId());
            System.out.println("Address found: " + found);

            if (found != null) {
                found.setStreet("Updated Street");
                boolean updated = service.update(found);
                System.out.println("Update result: " + updated);
                AddressDTO updatedAddress = service.findById(found.getId());
                System.out.println("Address after update: " + updatedAddress);
            }

            boolean deleted = service.delete(address);
            System.out.println("Delete result: " + deleted);

        } catch (RentexpresException e) {
            e.printStackTrace();
        }
    }
}