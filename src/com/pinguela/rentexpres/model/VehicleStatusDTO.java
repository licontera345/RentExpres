package com.pinguela.rentexpres.model;

import java.util.List;

public class VehicleStatusDTO extends ValueObject {

	/**
	 * 
	 */
        private static final long serialVersionUID = 1L;
        private Integer vehicleStatusId; // vehicle_status.vehicle_status_id

        /** Maximum length: 50 characters. */
        private String statusName; // vehicle_status.status_name

        private List<VehicleStatusLanguageDTO> translations;

	public VehicleStatusDTO() {
		super();
	}

	public Integer getVehicleStatusId() {
		return vehicleStatusId;
	}

	public void setVehicleStatusId(Integer vehicleStatusId) {
		this.vehicleStatusId = vehicleStatusId;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

        public List<VehicleStatusLanguageDTO> getTranslations() {
            return translations;
        }

        public void setTranslations(List<VehicleStatusLanguageDTO> translations) {
            this.translations = translations;
        }

}
