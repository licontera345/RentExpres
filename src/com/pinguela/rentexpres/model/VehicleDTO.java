package com.pinguela.rentexpres.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
public class VehicleDTO extends ValueObject {

	/**
	 * 
	 */
        private static final long serialVersionUID = 1L;
        private Integer vehicleId; // vehicle.vehicle_id

        /** Maximum length: 255 characters. */
        private String brand; // vehicle.brand

        /** Maximum length: 255 characters. */
        private String model; // vehicle.model

        private Integer manufactureYear; // vehicle.manufacture_year
        private BigDecimal dailyPrice; // vehicle.daily_price

        /** Maximum length: 255 characters. */
        private String licensePlate; // vehicle.license_plate

        /** Maximum length: 17 characters. */
        private String vinNumber; // vehicle.vin_number

        private Integer currentMileage; // vehicle.current_mileage
        private Integer vehicleStatusId; // vehicle.vehicle_status_id
        private Integer categoryId; // vehicle.category_id
        private Integer currentHeadquartersId; // vehicle.current_headquarters_id
        private LocalDateTime createdAt; // vehicle.created_at
        /** Nullable audit column. */
        private LocalDateTime updatedAt; // vehicle.updated_at

        private VehicleStatusDTO vehicleStatus;
        private VehicleCategoryDTO vehicleCategory;
        private HeadquartersDTO currentHeadquarters;

	public VehicleDTO() {
		super();
	}

	public Integer getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(Integer vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Integer getManufactureYear() {
		return manufactureYear;
	}

	public void setManufactureYear(Integer manufactureYear) {
		this.manufactureYear = manufactureYear;
	}

	public BigDecimal getDailyPrice() {
		return dailyPrice;
	}

	public void setDailyPrice(BigDecimal dailyPrice) {
		this.dailyPrice = dailyPrice;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public String getVinNumber() {
		return vinNumber;
	}

	public void setVinNumber(String vinNumber) {
		this.vinNumber = vinNumber;
	}

	public Integer getCurrentMileage() {
		return currentMileage;
	}

	public void setCurrentMileage(Integer currentMileage) {
		this.currentMileage = currentMileage;
	}

	public Integer getVehicleStatusId() {
		return vehicleStatusId;
	}

	public void setVehicleStatusId(Integer vehicleStatusId) {
		this.vehicleStatusId = vehicleStatusId;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getCurrentHeadquartersId() {
		return currentHeadquartersId;
	}

	public void setCurrentHeadquartersId(Integer currentHeadquartersId) {
		this.currentHeadquartersId = currentHeadquartersId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

        public VehicleStatusDTO getVehicleStatus() {
            return vehicleStatus;
        }

        public void setVehicleStatus(VehicleStatusDTO vehicleStatus) {
            this.vehicleStatus = vehicleStatus;
        }

        public VehicleCategoryDTO getVehicleCategory() {
            return vehicleCategory;
        }

        public void setVehicleCategory(VehicleCategoryDTO vehicleCategory) {
            this.vehicleCategory = vehicleCategory;
        }

        public HeadquartersDTO getCurrentHeadquarters() {
            return currentHeadquarters;
        }

        public void setCurrentHeadquarters(HeadquartersDTO currentHeadquarters) {
            this.currentHeadquarters = currentHeadquarters;
        }


}
