package com.pinguela.rentexpres.model;

public class VehicleCategoryLanguageDTO extends ValueObject {

        /**
         *
         */
        private static final long serialVersionUID = 1L;

        private Integer categoryId; // vehicle_category_language.category_id
        private Integer languageId; // vehicle_category_language.language_id

        /** Maximum length: 80 characters. */
        private String translatedName; // vehicle_category_language.translated_name

        private VehicleCategoryDTO category;
        private LanguageDTO language;

        public VehicleCategoryLanguageDTO() {
                super();
        }

        public Integer getCategoryId() {
                return categoryId;
        }

        public void setCategoryId(Integer categoryId) {
                this.categoryId = categoryId;
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

        public VehicleCategoryDTO getCategory() {
                return category;
        }

        public void setCategory(VehicleCategoryDTO category) {
                this.category = category;
        }

        public LanguageDTO getLanguage() {
                return language;
        }

        public void setLanguage(LanguageDTO language) {
                this.language = language;
        }
}
