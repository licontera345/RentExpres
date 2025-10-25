
package test.com.pinguela.rentexpres.dao;

import java.sql.Connection;
import java.util.List;

import com.pinguela.rentexpres.dao.VehicleCategoryDAO;
import com.pinguela.rentexpres.dao.impl.VehicleCategoryDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.VehicleCategoryDTO;
import com.pinguela.rentexpres.util.JDBCUtils;

public class VehicleCategoryDAOTest {
    public static void main(String[] args) {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();

            VehicleCategoryDAO categoryDAO = new VehicleCategoryDAOImpl();

            VehicleCategoryDTO category = categoryDAO.findById(connection, 1, "en");
            if (category != null) {
                System.out.println("Category found: " + category.getCategoryName());
            } else {
                System.out.println("No category found with ID 1.");
            }

            List<VehicleCategoryDTO> categories = categoryDAO.findAll(connection, "en");
            System.out.println("Total categories: " + categories.size());
            for (VehicleCategoryDTO dto : categories) {
                System.out.println(dto.getCategoryId() + " - " + dto.getCategoryName());
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
