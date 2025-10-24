package com.pinguela.rentexpres.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
public class UserDTO extends ValueObject {

	/**
	 * 
	 */
        private static final long serialVersionUID = 1L;
        private Integer userId; // user.user_id

        /** Maximum length: 255 characters. */
        private String username; // user.username

        /** Maximum length: 255 characters. */
        private String firstName; // user.first_name

        /** Maximum length: 45 characters. */
        private String lastName1; // user.last_name1

        /** Maximum length: 45 characters. */
        private String lastName2; // user.last_name2

        private LocalDate birthDate; // user.birth_date

        /** Maximum length: 255 characters. */
        private String email; // user.email

        /** Maximum length: 255 characters. */
        private String password; // user.password

        private Integer roleId; // user.role_id

        /** Maximum length: 20 characters. */
        private String phone; // user.phone

        private Integer addressId; // user.address_id
        private Boolean activeStatus; // user.active_status (tinyint 1/0)
        private LocalDateTime createdAt; // user.created_at
        private LocalDateTime updatedAt; // user.updated_at

        private RoleDTO role;
        private AddressDTO address;

	public UserDTO() {
		super();
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName1() {
		return lastName1;
	}

	public void setLastName1(String lastName1) {
		this.lastName1 = lastName1;
	}

	public String getLastName2() {
		return lastName2;
	}

	public void setLastName2(String lastName2) {
		this.lastName2 = lastName2;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Boolean getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Boolean activeStatus) {
		this.activeStatus = activeStatus;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

        public RoleDTO getRole() {
            return role;
        }

        public void setRole(RoleDTO role) {
            this.role = role;
        }

        public AddressDTO getAddress() {
            return address;
        }

        public void setAddress(AddressDTO address) {
            this.address = address;
        }

}
