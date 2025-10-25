package test.com.pinguela.rentexpres.service;

import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.EmployeeCriteria;
import com.pinguela.rentexpres.model.EmployeeDTO;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.service.EmployeeService;
import com.pinguela.rentexpres.service.impl.EmployeeServiceImpl;

public class EmployeeServiceTest {
    public static void main(String[] args) {
        EmployeeService service = new EmployeeServiceImpl();

        try {
            EmployeeDTO employee = new EmployeeDTO();
            employee.setEmployeeName("employee.test");
            employee.setPassword("abc123.");
            employee.setRoleId(2);
            employee.setHeadquartersId(1);
            employee.setFirstName("John");
            employee.setLastName1("Doe");
            employee.setLastName2("Smith");
            employee.setEmail("john.doe@rentexpres.com");
            employee.setPhone("600123456");

            boolean created = service.create(employee);
            System.out.println("Create result: " + created);
            if (created) {
                System.out.println("Employee created with ID: " + employee.getId());
            }

            EmployeeDTO found = service.findById(employee.getId());
            System.out.println("Employee found: " + found);

            if (found != null) {
                found.setFirstName("John Updated");
                found.setPhone("699888777");
                if (found.getPassword() == null || found.getPassword().isEmpty()) {
                    found.setPassword("abc123.");
                }
                boolean updated = service.update(found);
                System.out.println("Update result: " + updated);
                EmployeeDTO updatedEmployee = service.findById(found.getId());
                System.out.println("Employee after update: " + updatedEmployee);
            }

            List<EmployeeDTO> employees = service.findAll();
            System.out.println("Total employees: " + (employees != null ? employees.size() : 0));
            if (employees != null) {
                for (EmployeeDTO dto : employees) {
                    System.out.println(dto);
                }
            }

            EmployeeDTO authenticated = service.autenticar(employee.getEmployeeName(), "abc123.");
            System.out.println("Authenticated employee: " + authenticated);

            EmployeeCriteria criteria = new EmployeeCriteria();
            criteria.setEmployeeName("employee.test");
            criteria.setFirstName("John");
            criteria.setRoleId(2);
            criteria.setPageNumber(1);
            criteria.setPageSize(10);
            Results<EmployeeDTO> results = service.findByCriteria(criteria);
            System.out.println("Results by criteria - Total records: " + (results != null ? results.getTotalRecords() : 0));
            if (results != null && results.getResults() != null) {
                for (EmployeeDTO dto : results.getResults()) {
                    System.out.println(dto);
                }
            }

            boolean deleted = service.delete(employee, employee.getId());
            System.out.println("Delete result: " + deleted);

        } catch (RentexpresException e) {
            e.printStackTrace();
        }
    }
}