package com.pinguela.rentexpres.model;

public class RentalStatusLanguageDTO extends ValueObject {

        /**
         *
         */
        private static final long serialVersionUID = 1L;

        private Integer rentalStatusId; // rental_status_language.rental_status_id
        private Integer languageId; // rental_status_language.language_id

        /** Maximum length: 80 characters. */
        private String translatedName; // rental_status_language.translated_name

        private RentalStatusDTO rentalStatus;
        private LanguageDTO language;

        public RentalStatusLanguageDTO() {
                super();
        }

        public Integer getRentalStatusId() {
                return rentalStatusId;
        }

        public void setRentalStatusId(Integer rentalStatusId) {
                this.rentalStatusId = rentalStatusId;
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

        public RentalStatusDTO getRentalStatus() {
                return rentalStatus;
        }

        public void setRentalStatus(RentalStatusDTO rentalStatus) {
                this.rentalStatus = rentalStatus;
        }

        public LanguageDTO getLanguage() {
                return language;
        }

        public void setLanguage(LanguageDTO language) {
                this.language = language;
        }
}
