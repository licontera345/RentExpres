package test.com.pinguela.rentexpres.dao;

import java.sql.Connection;

import com.pinguela.rentexpres.dao.impl.AddressDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.AddressDTO;
import com.pinguela.rentexpres.util.JDBCUtils;

public class AddressDAOTest {

    public static void main(String[] args) {
        AddressDAOImpl dao = new AddressDAOImpl();
        Connection connection = null;

        try {
            connection = JDBCUtils.getConnection();

            AddressDTO newAddress = new AddressDTO();
            newAddress.setCityId(1);
            newAddress.setStreet("Main Street");
            newAddress.setNumber("123");
            dao.create(connection, newAddress);
            System.out.println("Address created successfully.");

            AddressDTO found = dao.findById(connection, newAddress.getId());
            System.out.println("Address found: " + (found != null ? found.getStreet() : null));

            if (found != null) {
                found.setStreet("Updated Street");
                dao.update(connection, found);
                System.out.println("Address updated successfully.");
            }

            dao.delete(connection, found, found.getId());
            System.out.println("Address deleted successfully.");

        } catch (DataException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(connection);
        }
    }
}