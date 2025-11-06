package com.pinguela.rentexpres.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class VehicleReferenceData implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<VehicleStatusDTO> vehicleStatuses;
    private Map<Integer, String> vehicleStatusMap = new LinkedHashMap<Integer, String>();
    private List<VehicleCategoryDTO> vehicleCategories;
    private Map<Integer, String> vehicleCategoryMap = new LinkedHashMap<Integer, String>();
    private List<HeadquartersDTO> headquarters;
    private List<Integer> pageSizes = Collections.emptyList();

    public List<VehicleStatusDTO> getVehicleStatuses() {
        return vehicleStatuses;
    }

    public void setVehicleStatuses(List<VehicleStatusDTO> vehicleStatuses) {
        this.vehicleStatuses = vehicleStatuses;
    }

    public Map<Integer, String> getVehicleStatusMap() {
        return vehicleStatusMap;
    }

    public void setVehicleStatusMap(Map<Integer, String> vehicleStatusMap) {
        if (vehicleStatusMap == null) {
            this.vehicleStatusMap = new LinkedHashMap<Integer, String>();
        } else {
            this.vehicleStatusMap = vehicleStatusMap;
        }
    }

    public List<VehicleCategoryDTO> getVehicleCategories() {
        return vehicleCategories;
    }

    public void setVehicleCategories(List<VehicleCategoryDTO> vehicleCategories) {
        this.vehicleCategories = vehicleCategories;
    }

    public Map<Integer, String> getVehicleCategoryMap() {
        return vehicleCategoryMap;
    }

    public void setVehicleCategoryMap(Map<Integer, String> vehicleCategoryMap) {
        if (vehicleCategoryMap == null) {
            this.vehicleCategoryMap = new LinkedHashMap<Integer, String>();
        } else {
            this.vehicleCategoryMap = vehicleCategoryMap;
        }
    }

    public List<HeadquartersDTO> getHeadquarters() {
        return headquarters;
    }

    public void setHeadquarters(List<HeadquartersDTO> headquarters) {
        this.headquarters = headquarters;
    }

    public List<Integer> getPageSizes() {
        return pageSizes;
    }

    public void setPageSizes(List<Integer> pageSizes) {
        if (pageSizes == null) {
            this.pageSizes = Collections.emptyList();
        } else {
            this.pageSizes = pageSizes;
        }
    }
}
