package com.pinguela.rentexpressweb.view;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.pinguela.rentexpres.model.HeadquartersDTO;
import com.pinguela.rentexpres.model.VehicleCategoryDTO;

public class ReservationSummaryView implements Serializable {

    private static final long serialVersionUID = 1L;

    private VehicleCategoryDTO category;
    private HeadquartersDTO pickupHeadquarters;
    private HeadquartersDTO returnHeadquarters;
    private LocalDateTime pickupDateTime;
    private LocalDateTime returnDateTime;
    private String pickupDateFormatted;
    private String returnDateFormatted;

    public ReservationSummaryView() {
        super();
    }

    public VehicleCategoryDTO getCategory() {
        return category;
    }

    public void setCategory(VehicleCategoryDTO category) {
        this.category = category;
    }

    public String getCategoryName() {
        if (category == null) {
            return null;
        }
        return category.getCategoryName();
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

    public String getPickupHeadquartersName() {
        if (pickupHeadquarters == null) {
            return null;
        }
        return pickupHeadquarters.getName();
    }

    public String getReturnHeadquartersName() {
        if (returnHeadquarters == null) {
            return null;
        }
        return returnHeadquarters.getName();
    }

    public LocalDateTime getPickupDateTime() {
        return pickupDateTime;
    }

    public void setPickupDateTime(LocalDateTime pickupDateTime) {
        this.pickupDateTime = pickupDateTime;
    }

    public LocalDateTime getReturnDateTime() {
        return returnDateTime;
    }

    public void setReturnDateTime(LocalDateTime returnDateTime) {
        this.returnDateTime = returnDateTime;
    }

    public String getPickupDateFormatted() {
        return pickupDateFormatted;
    }

    public void setPickupDateFormatted(String pickupDateFormatted) {
        this.pickupDateFormatted = pickupDateFormatted;
    }

    public String getReturnDateFormatted() {
        return returnDateFormatted;
    }

    public void setReturnDateFormatted(String returnDateFormatted) {
        this.returnDateFormatted = returnDateFormatted;
    }
}
