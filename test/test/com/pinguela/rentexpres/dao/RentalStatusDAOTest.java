
package test.com.pinguela.rentexpres.dao;

import java.sql.Connection;
import java.util.List;

import com.pinguela.rentexpres.dao.impl.RentalStatusDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.RentalStatusDTO;
import com.pinguela.rentexpres.util.JDBCUtils;

public class RentalStatusDAOTest {

        public static void main(String[] args) {
                Connection connection = null;
                RentalStatusDAOImpl dao = new RentalStatusDAOImpl();

                try {
                        connection = JDBCUtils.getConnection();
                        System.out.println("Connection established successfully.");

                        RentalStatusDTO found = dao.findById(connection, 1, null);
                        if (found != null) {
                                System.out.println("Status found: " + found.getStatusName());
                        } else {
                                System.out.println("No status found with ID: " + 1);
                        }
                        List<RentalStatusDTO> all = dao.findAll(connection, null);
                        System.out.println("Rental status list (" + all.size() + "):");
                        for (RentalStatusDTO s : all) {
                                System.out.println("  ID: " + s.getRentalStatusId() + " | Name: " + s.getStatusName());
                        }

                } catch (DataException e) {
                        e.printStackTrace();
                } catch (Exception e) {
                        e.printStackTrace();
                } finally {
                        JDBCUtils.close(connection);
                }
        }
}
