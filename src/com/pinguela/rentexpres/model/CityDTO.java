package com.pinguela.rentexpres.model;

public class CityDTO extends ValueObject {

	/**
	 * 
	 */
        private static final long serialVersionUID = 1L;

        private Integer cityId; // city.city_id

        /** Maximum length: 255 characters. */
        private String cityName; // city.city_name

        private Integer provinceId; // city.province_id

	private ProvinceDTO province;

	public CityDTO() {
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

        public Integer getId() {
            return cityId;
        }

        public void setId(Integer id) {
            this.cityId = id;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

	public ProvinceDTO getProvince() {
		return province;
	}

	public void setProvince(ProvinceDTO province) {
		this.province = province;
	}

}
