package com.pinguela.rentexpres.model;

import java.time.LocalDateTime;

public class HeadquartersDTO extends ValueObject {

	/**
	 * 
	 */
        private static final long serialVersionUID = 1L;

        private Integer headquartersId; // headquarters.headquarters_id

        /** Maximum length: 80 characters. */
        private String name; // headquarters.name

        /** Maximum length: 30 characters. */
        private String phone; // headquarters.phone

        /** Maximum length: 120 characters. */
        private String email; // headquarters.email

        private Integer addressId; // headquarters.address_id
        private LocalDateTime createdAt; // headquarters.created_at
        /** Nullable audit column. */
        private LocalDateTime updatedAt; // headquarters.updated_at

        private AddressDTO address;
        private CityDTO city;
        private ProvinceDTO province;

	public HeadquartersDTO() {
		super();
	}

        public Integer getAddressId() {
            return addressId;
        }

        public void setAddressId(Integer addressId) {
            this.addressId = addressId;
        }

        public Integer getHeadquartersId() {
            return headquartersId;
        }

        public void setHeadquartersId(Integer headquartersId) {
            this.headquartersId = headquartersId;
        }

        public Integer getId() {
            return headquartersId;
        }

        public void setId(Integer id) {
            this.headquartersId = id;
        }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

        public AddressDTO getAddress() {
            return address;
        }

        public void setAddress(AddressDTO address) {
            this.address = address;
        }

	public CityDTO getCity() {
		return city;
	}

	public void setCity(CityDTO city) {
		this.city = city;
	}

	public ProvinceDTO getProvince() {
		return province;
	}

	public void setProvince(ProvinceDTO province) {
		this.province = province;
	}

}
