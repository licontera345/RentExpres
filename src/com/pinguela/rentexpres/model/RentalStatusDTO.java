package com.pinguela.rentexpres.model;

import java.util.List;

public class RentalStatusDTO extends ValueObject {

	/**
	 * 
	 */
        private static final long serialVersionUID = 1L;
        private Integer rentalStatusId; // rental_status.rental_status_id

        /** Maximum length: 50 characters. */
        private String statusName; // rental_status.status_name

        private List<RentalStatusLanguageDTO> translations;

	public RentalStatusDTO() {
		super();
	}

	public Integer getRentalStatusId() {
		return rentalStatusId;
	}

	public void setRentalStatusId(Integer rentalStatusId) {
		this.rentalStatusId = rentalStatusId;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

        public List<RentalStatusLanguageDTO> getTranslations() {
            return translations;
        }

        public void setTranslations(List<RentalStatusLanguageDTO> translations) {
            this.translations = translations;
        }

}
