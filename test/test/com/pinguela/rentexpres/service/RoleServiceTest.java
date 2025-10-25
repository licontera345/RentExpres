package test.com.pinguela.rentexpres.service;

import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.RoleDTO;
import com.pinguela.rentexpres.service.RoleService;
import com.pinguela.rentexpres.service.impl.RoleServiceImpl;

public class RoleServiceTest {
    public static void main(String[] args) {
        RoleService service = new RoleServiceImpl();

        try {
            RoleDTO role = service.findById(1);
            System.out.println("Role found: " + role);

            List<RoleDTO> roles = service.findAll();
            System.out.println("Total roles: " + (roles != null ? roles.size() : 0));
            if (roles != null) {
                for (RoleDTO dto : roles) {
                    System.out.println(dto);
                }
            }
        } catch (RentexpresException e) {
            e.printStackTrace();
        }
    }
}