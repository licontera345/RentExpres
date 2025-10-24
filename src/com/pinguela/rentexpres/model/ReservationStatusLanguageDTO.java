package com.pinguela.rentexpres.model;

public class ReservationStatusLanguageDTO extends ValueObject {

        /**
         *
         */
        private static final long serialVersionUID = 1L;

        private Integer reservationStatusId; // reservation_status_language.reservation_status_id
        private Integer languageId; // reservation_status_language.language_id

        /** Maximum length: 80 characters. */
        private String translatedName; // reservation_status_language.translated_name

        private ReservationStatusDTO reservationStatus;
        private LanguageDTO language;

        public ReservationStatusLanguageDTO() {
                super();
        }

        public Integer getReservationStatusId() {
                return reservationStatusId;
        }

        public void setReservationStatusId(Integer reservationStatusId) {
                this.reservationStatusId = reservationStatusId;
        }

        public Integer getLanguageId() {
                return languageId;
        }

        public void setLanguageId(Integer languageId) {
                this.languageId = languageId;
        }

        public String getTranslatedName() {
                return translatedName;
        }

        public void setTranslatedName(String translatedName) {
                this.translatedName = translatedName;
        }

        public ReservationStatusDTO getReservationStatus() {
                return reservationStatus;
        }

        public void setReservationStatus(ReservationStatusDTO reservationStatus) {
                this.reservationStatus = reservationStatus;
        }

        public LanguageDTO getLanguage() {
                return language;
        }

        public void setLanguage(LanguageDTO language) {
                this.language = language;
        }
}
