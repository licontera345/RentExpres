package com.pinguela.rentexpres.model;

public class AddressDTO extends ValueObject {

	/**
	 * 
	 */
        private static final long serialVersionUID = 1L;

        private Integer addressId; // address.address_id

        /** Maximum length: 255 characters. */
        private String street; // address.street

        /** Maximum length: 10 characters. */
        private String number; // address.number

        private Integer cityId; // address.city_id

        private String cityName; // derived from city.city_name

        private Integer provinceId; // derived from city.province_id

        private String provinceName; // derived from province.province_name

        private CityDTO city;

	public AddressDTO() {
		super();
	}

        public Integer getProvinceId() {
                return provinceId;
        }

        public void setProvinceId(Integer provinceId) {
                this.provinceId = provinceId;
        }

        public Integer getCityId() {
                return cityId;
        }

        public void setCityId(Integer cityId) {
                this.cityId = cityId;
        }

        public String getCityName() {
                return cityName;
        }

        public void setCityName(String cityName) {
                this.cityName = cityName;
        }

        public String getProvinceName() {
                return provinceName;
        }

        public void setProvinceName(String provinceName) {
                this.provinceName = provinceName;
        }

        public Integer getAddressId() {
                return addressId;
        }

        public void setAddressId(Integer addressId) {
                this.addressId = addressId;
        }

        public Integer getId() {
                return addressId;
        }

        public void setId(Integer id) {
                this.addressId = id;
        }

        public String getStreet() {
                return street;
        }

        public void setStreet(String street) {
                this.street = street;
        }

        public String getNumber() {
                return number;
        }

        public void setNumber(String number) {
                this.number = number;
        }

        public CityDTO getCity() {
                return city;
        }

        public void setCity(CityDTO city) {
                this.city = city;
        }

}
