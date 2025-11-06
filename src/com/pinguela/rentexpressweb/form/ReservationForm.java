package com.pinguela.rentexpressweb.form;

import java.io.Serializable;

public class ReservationForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private String categoryId;
    private String pickupHeadquartersId;
    private String returnHeadquartersId;
    private String startDate;
    private String endDate;

    public ReservationForm() {
        super();
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getPickupHeadquartersId() {
        return pickupHeadquartersId;
    }

    public void setPickupHeadquartersId(String pickupHeadquartersId) {
        this.pickupHeadquartersId = pickupHeadquartersId;
    }

    public String getReturnHeadquartersId() {
        return returnHeadquartersId;
    }

    public void setReturnHeadquartersId(String returnHeadquartersId) {
        this.returnHeadquartersId = returnHeadquartersId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
