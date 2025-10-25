
package test.com.pinguela.rentexpres.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.pinguela.rentexpres.dao.RoleDAO;
import com.pinguela.rentexpres.dao.impl.RoleDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.RoleDTO;
import com.pinguela.rentexpres.util.JDBCUtils;

public class RoleDAOTest {
    public static void main(String[] args) {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();

            RoleDAO dao = new RoleDAOImpl();

            RoleDTO role = dao.findById(connection, 1);
            System.out.println("Role found with ID 1: " + role);

            List<RoleDTO> roles = dao.findAll(connection);
            System.out.println("Total roles found: " + roles.size());
            for (RoleDTO dto : roles) {
                System.out.println(dto);
            }
        } catch (DataException | SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(connection);
        }
    }
}
