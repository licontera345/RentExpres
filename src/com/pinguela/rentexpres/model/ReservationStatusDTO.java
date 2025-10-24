package com.pinguela.rentexpres.model;

import java.util.List;

public class ReservationStatusDTO extends ValueObject {

	/**
	 * 
	 */
        private static final long serialVersionUID = 1L;
        private Integer reservationStatusId; // reservation_status.reservation_status_id

        /** Maximum length: 50 characters. */
        private String statusName; // reservation_status.status_name

        private List<ReservationStatusLanguageDTO> translations;

	public ReservationStatusDTO() {
		super();
	}

	public Integer getReservationStatusId() {
		return reservationStatusId;
	}

	public void setReservationStatusId(Integer reservationStatusId) {
		this.reservationStatusId = reservationStatusId;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

        public List<ReservationStatusLanguageDTO> getTranslations() {
            return translations;
        }

        public void setTranslations(List<ReservationStatusLanguageDTO> translations) {
            this.translations = translations;
        }

}
