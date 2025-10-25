package test.com.pinguela.rentexpres.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.pinguela.rentexpres.dao.UserDAO;
import com.pinguela.rentexpres.dao.impl.UserDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.model.UserCriteria;
import com.pinguela.rentexpres.model.UserDTO;
import com.pinguela.rentexpres.util.JDBCUtils;

public class UserDAOTest {
    public static void main(String[] args) {
        Connection connection = null;
        UserDAO dao = new UserDAOImpl();
        try {
            connection = JDBCUtils.getConnection();

            UserDTO user = new UserDTO();
            user.setUsername("john.doe");
            user.setFirstName("John");
            user.setLastName1("Doe");
            user.setLastName2("Smith");
            user.setBirthDate(LocalDate.of(1990, 1, 1));
            user.setEmail("john.doe@example.com");
            user.setPhone("123456789");
            user.setPassword("securePassword123");
            user.setRoleId(2);
            user.setAddressId(1);
            user.setActiveStatus(Boolean.TRUE);

            boolean created = dao.create(connection, user);
            System.out.println("Create result: " + created);
            if (created) {
                System.out.println("New user ID: " + user.getUserId());
            }

            UserDTO found = dao.findById(connection, user.getUserId());
            System.out.println("User found: " + found);

            if (found != null) {
                found.setPhone("987654321");
                boolean updated = dao.update(connection, found);
                System.out.println("Update result: " + updated);
                UserDTO updatedUser = dao.findById(connection, found.getUserId());
                System.out.println("User after update: " + updatedUser);
            }

            List<UserDTO> list = dao.findAll(connection);
            System.out.println("Total users: " + list.size());
            for (UserDTO dto : list) {
                System.out.println(dto);
            }

            UserCriteria criteria = new UserCriteria();
            criteria.setUsername("john.doe");
            criteria.setFirstName("John");
            criteria.setPageNumber(1);
            criteria.setPageSize(10);
            Results<UserDTO> results = dao.findByCriteria(connection, criteria);
            System.out.println("Results with criteria - Total records: " + results.getTotalRecords());
            if (results.getResults() != null) {
                for (UserDTO dto : results.getResults()) {
                    System.out.println(dto);
                }
            }

            UserDTO authenticated = dao.authenticate(connection, "john.doe", "securePassword123");
            System.out.println("Authenticated user: " + authenticated);

            boolean deleted = dao.delete(connection, user.getUserId());
            System.out.println("Delete result: " + deleted);

        } catch (DataException | SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(connection);
        }
    }
}