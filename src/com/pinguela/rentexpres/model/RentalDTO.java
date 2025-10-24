package com.pinguela.rentexpres.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RentalDTO extends ValueObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer rentalId; // rental.rental_id
	private LocalDateTime startDateEffective; // rental.start_date_effective
	private LocalDateTime endDateEffective; // rental.end_date_effective
	private Integer initialKm; // rental.initial_km
	private Integer finalKm; // rental.final_km
	private Integer rentalStatusId; // FK → rental_status.rental_status_id
	private BigDecimal totalCost; // rental.total_cost
	private Integer reservationId; // FK → reservation.reservation_id
	private Integer pickupHeadquartersId; // FK → headquarters.headquarters_id
	private Integer returnHeadquartersId; // FK → headquarters.headquarters_id
	private LocalDateTime createdAt; // rental.created_at
        /** Nullable audit column. */
        private LocalDateTime updatedAt; // rental.updated_at

	// Optional relational objects (for joins or criteria)
	private RentalStatusDTO rentalStatus;
	private ReservationDTO reservation;
	private HeadquartersDTO pickupHeadquarters;
	private HeadquartersDTO returnHeadquarters;
        private Integer vehicleId; // derived via reservation.vehicle_id

        /** Maximum length: 255 characters. */
        private String licensePlate; // derived from vehicle.license_plate

        /** Maximum length: 255 characters. */
        private String brand; // derived from vehicle.brand

        /** Maximum length: 255 characters. */
        private String model; // derived from vehicle.model

        /** Maximum length: 50 characters. */
        private String rentalStatusName; // derived from rental_status.status_name
        private Integer userId; // derived via reservation.user_id
        /** Maximum length: 255 characters. */
        private String userFirstName; // derived from user.first_name

        /** Maximum length: 45 characters. */
        private String userLastName1; // derived from user.last_name1

        /** Maximum length: 20 characters. */
        private String phone; // derived from user.phone

        /** Maximum length: 80 characters. */
        private String pickupHeadquartersName; // derived from headquarters.name

	// super();
	public RentalDTO() {
		super();
	}

	public Integer getRentalId() {
		return rentalId;
	}

	public void setRentalId(Integer rentalId) {
		this.rentalId = rentalId;
	}

	public LocalDateTime getStartDateEffective() {
		return startDateEffective;
	}

	public void setStartDateEffective(LocalDateTime startDateEffective) {
		this.startDateEffective = startDateEffective;
	}

	public LocalDateTime getEndDateEffective() {
		return endDateEffective;
	}

	public void setEndDateEffective(LocalDateTime endDateEffective) {
		this.endDateEffective = endDateEffective;
	}

	public Integer getInitialKm() {
		return initialKm;
	}

	public void setInitialKm(Integer initialKm) {
		this.initialKm = initialKm;
	}

	public Integer getFinalKm() {
		return finalKm;
	}

	public void setFinalKm(Integer finalKm) {
		this.finalKm = finalKm;
	}

	public Integer getRentalStatusId() {
		return rentalStatusId;
	}

	public void setRentalStatusId(Integer rentalStatusId) {
		this.rentalStatusId = rentalStatusId;
	}

	public BigDecimal getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(BigDecimal totalCost) {
		this.totalCost = totalCost;
	}

	public Integer getReservationId() {
		return reservationId;
	}

	public void setReservationId(Integer reservationId) {
		this.reservationId = reservationId;
	}

	public Integer getPickupHeadquartersId() {
		return pickupHeadquartersId;
	}

	public void setPickupHeadquartersId(Integer pickupHeadquartersId) {
		this.pickupHeadquartersId = pickupHeadquartersId;
	}

	public Integer getReturnHeadquartersId() {
		return returnHeadquartersId;
	}

	public void setReturnHeadquartersId(Integer returnHeadquartersId) {
		this.returnHeadquartersId = returnHeadquartersId;
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

	public RentalStatusDTO getRentalStatus() {
		return rentalStatus;
	}

	public void setRentalStatus(RentalStatusDTO rentalStatus) {
		this.rentalStatus = rentalStatus;
	}

	public ReservationDTO getReservation() {
		return reservation;
	}

	public void setReservation(ReservationDTO reservation) {
		this.reservation = reservation;
	}

	public HeadquartersDTO getPickupHeadquarters() {
		return pickupHeadquarters;
	}

	public void setPickupHeadquarters(HeadquartersDTO pickupHeadquarters) {
		this.pickupHeadquarters = pickupHeadquarters;
	}

	public HeadquartersDTO getReturnHeadquarters() {
		return returnHeadquarters;
	}

	public void setReturnHeadquarters(HeadquartersDTO returnHeadquarters) {
		this.returnHeadquarters = returnHeadquarters;
	}

	public Integer getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(Integer vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
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

	public String getRentalStatusName() {
		return rentalStatusName;
	}

	public void setRentalStatusName(String rentalStatusName) {
		this.rentalStatusName = rentalStatusName;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserFirstName() {
		return userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public String getUserLastName1() {
		return userLastName1;
	}

	public void setUserLastName1(String userLastName1) {
		this.userLastName1 = userLastName1;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPickupHeadquartersName() {
		return pickupHeadquartersName;
	}

	public void setPickupHeadquartersName(String pickupHeadquartersName) {
		this.pickupHeadquartersName = pickupHeadquartersName;
	}



	
}
