package com.pinguela.rentexpres.model;

public class VehicleStatusLanguageDTO extends ValueObject {

        /**
         *
         */
        private static final long serialVersionUID = 1L;

        private Integer vehicleStatusId; // vehicle_status_language.vehicle_status_id
        private Integer languageId; // vehicle_status_language.language_id

        /** Maximum length: 80 characters. */
        private String translatedName; // vehicle_status_language.translated_name

        private VehicleStatusDTO vehicleStatus;
        private LanguageDTO language;

        public VehicleStatusLanguageDTO() {
                super();
        }

        public Integer getVehicleStatusId() {
                return vehicleStatusId;
        }

        public void setVehicleStatusId(Integer vehicleStatusId) {
                this.vehicleStatusId = vehicleStatusId;
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

        public VehicleStatusDTO getVehicleStatus() {
                return vehicleStatus;
        }

        public void setVehicleStatus(VehicleStatusDTO vehicleStatus) {
                this.vehicleStatus = vehicleStatus;
        }

        public LanguageDTO getLanguage() {
                return language;
        }

        public void setLanguage(LanguageDTO language) {
                this.language = language;
        }
}
