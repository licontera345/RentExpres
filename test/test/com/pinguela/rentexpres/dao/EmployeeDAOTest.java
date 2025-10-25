package test.com.pinguela.rentexpres.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import com.pinguela.rentexpres.dao.impl.EmployeeDAOImpl;
import com.pinguela.rentexpres.dao.impl.HeadquartersDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.EmployeeDTO;
import com.pinguela.rentexpres.model.HeadquartersDTO;
import com.pinguela.rentexpres.util.JDBCUtils;

public class EmployeeDAOTest {

        public static void main(String[] args) {
                Connection connection = null;
                EmployeeDAOImpl employeeDAO = new EmployeeDAOImpl();
                HeadquartersDAOImpl hqDAO = new HeadquartersDAOImpl();

                try {
                        connection = JDBCUtils.getConnection();
                        System.out.println("Connection established successfully.");

                        try (PreparedStatement ps = connection.prepareStatement(
                                        "INSERT IGNORE INTO province (province_id, province_name) VALUES (1, 'Madrid')")) {
                                ps.executeUpdate();
                        }
                        try (PreparedStatement ps = connection.prepareStatement(
                                        "INSERT IGNORE INTO city (city_id, city_name, province_id) VALUES (1, 'Madrid', 1)")) {
                                ps.executeUpdate();
                        }

                        try (PreparedStatement ps = connection.prepareStatement(
                                        "INSERT IGNORE INTO address (address_id, city_id, street, number) VALUES (1, 1, 'Main Avenue', '100')")) {
                                ps.executeUpdate();
                        }

                        try (PreparedStatement ps = connection
                                        .prepareStatement("INSERT IGNORE INTO role (role_id, role_name) VALUES (2, 'EMPLOYEE')")) {
                                ps.executeUpdate();
                        }

                        HeadquartersDTO hq = new HeadquartersDTO();
                        hq.setName("Madrid HQ");
                        hq.setPhone("910000000");
                        hq.setEmail("hq@rentexpres.com");
                        hq.setAddressId(1);
                        hqDAO.create(connection, hq);

                        EmployeeDTO emp = new EmployeeDTO();
                        emp.setEmployeeName("employee.test");
                        if (emp.getPassword() == null || emp.getPassword().isEmpty()) {
                            emp.setPassword("abc123.");
                        }
                        emp.setRoleId(2);
                        emp.setHeadquartersId(hq.getId());
                        emp.setFirstName("Charles");
                        emp.setLastName1("Perez");
                        emp.setLastName2("Lopez");
                        emp.setPhone("600123456");
                        emp.setEmail("charles.perez@rentexpres.com");

                        employeeDAO.create(connection, emp);
                        System.out.println("Employee created successfully with ID: " + emp.getId());

                        EmployeeDTO found = employeeDAO.findById(connection, emp.getId());
                        if (found != null) {
                                System.out.println("Employee found: " + found.getFirstName() + " " + found.getLastName1());
                        }

                        found.setFirstName("Charles Updated");
                        found.setPhone("699888777");
                        if (found.getPassword() == null || found.getPassword().isEmpty()) {
                            found.setPassword("abc123.");
                        }
                        employeeDAO.update(connection, found);
                        System.out.println("Employee updated successfully.");

                        List<EmployeeDTO> all = employeeDAO.findAll(connection);
                        System.out.println("Employee list (" + all.size() + "):");
                        for (EmployeeDTO e : all) {
                                System.out.println("  " + e.getEmployeeName() + " - " + e.getEmail());
                        }

                        employeeDAO.delete(connection, found.getId());
                        System.out.println("Employee deleted successfully.");

                } catch (DataException e) {
                        e.printStackTrace();
                } catch (Exception e) {
                        e.printStackTrace();
                } finally {
                        JDBCUtils.close(connection);
                }
        }
}