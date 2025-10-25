package test.com.pinguela.rentexpres.service;

import java.time.LocalDate;
import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.model.UserCriteria;
import com.pinguela.rentexpres.model.UserDTO;
import com.pinguela.rentexpres.service.UserService;
import com.pinguela.rentexpres.service.impl.UserServiceImpl;

public class UserServiceTest {
	public static void main(String[] args) {
		UserService service = new UserServiceImpl();

		try {
			UserDTO user = new UserDTO();
			user.setUsername("john.doe");
			user.setFirstName("John");
			user.setLastName1("Doe");
			user.setLastName2("Smith");
			user.setEmail("john.doe@example.com");
			user.setPhone("555000111");
			user.setPassword("secret");
			user.setBirthDate(LocalDate.of(1990, 1, 15));
			user.setRoleId(2);
			user.setAddressId(1);

			boolean created = service.create(user);
			System.out.println("Create result: " + created);
			if (created) {
				System.out.println("User created with ID: " + user.getUserId());
			}

			UserDTO found = service.findById(user.getUserId());
			System.out.println("User found: " + found);

			if (found != null) {
				found.setEmail("john.updated@example.com");
				found.setPhone("555000222");
				boolean updated = service.update(found);
				System.out.println("Update result: " + updated);
				UserDTO updatedUser = service.findById(found.getUserId());
				System.out.println("User after update: " + updatedUser);
			}

			List<UserDTO> users = service.findAll();
			System.out.println("Total users: " + (users != null ? users.size() : 0));
			if (users != null) {
				for (UserDTO dto : users) {
					System.out.println(dto);
				}
			}

			UserDTO authenticated = service.authenticate(user.getUsername(), "secret");
			System.out.println("Authenticated user: " + authenticated);

			UserCriteria criteria = new UserCriteria();
			criteria.setUsername("john.doe");
			criteria.setFirstName("John");
			criteria.setLastName1("Doe");
			criteria.setEmail("john.updated@example.com");
			criteria.setPhone("555000222");
			criteria.setPageNumber(1);
			criteria.setPageSize(10);

			Results<UserDTO> results = service.findByCriteria(criteria);
			System.out.println(
					"Results findByCriteria - Total records: " + (results != null ? results.getTotalRecords() : 0));
			if (results != null && results.getResults() != null) {
				for (UserDTO dto : results.getResults()) {
					System.out.println(dto);
				}
			}

			boolean deleted = service.delete(user.getUserId());
			System.out.println("Delete result: " + deleted);

		} catch (RentexpresException e) {
			e.printStackTrace();
		}
	}
}