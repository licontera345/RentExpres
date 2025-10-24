package com.pinguela.rentexpres.model;

import java.util.List;

public class VehicleCategoryDTO extends ValueObject {

	/**
	 * 
	 */
        private static final long serialVersionUID = 1L;
        private Integer categoryId; // vehicle_category.category_id

        /** Maximum length: 50 characters. */
        private String categoryName; // vehicle_category.category_name

        private List<VehicleCategoryLanguageDTO> translations;

	public VehicleCategoryDTO() {
		super();
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

        public List<VehicleCategoryLanguageDTO> getTranslations() {
            return translations;
        }

        public void setTranslations(List<VehicleCategoryLanguageDTO> translations) {
            this.translations = translations;
        }

}
