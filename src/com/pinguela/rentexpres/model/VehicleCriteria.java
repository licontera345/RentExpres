package com.pinguela.rentexpres.model;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class VehicleCriteria extends CriteriaBase {

        private static final long serialVersionUID = 1L;

        private static final Set<String> ORDERABLE = new HashSet<String>(
                        Arrays.asList("vehicle_id", "brand", "model", "manufacture_year", "daily_price", "current_mileage",
                                        "created_at", "updated_at"));

        private Integer vehicleId;
        private String brand;
        private String model;
        private Integer categoryId;
        private Integer vehicleStatusId;
        private Integer manufactureYearFrom;
        private Integer manufactureYearTo;
        private BigDecimal dailyPriceMin;
        private BigDecimal dailyPriceMax;
        private Integer currentHeadquartersId;

        public VehicleCriteria() {
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
                this.brand = trimToNull(brand);
        }

        public String getModel() {
                return model;
        }

        public void setModel(String model) {
                this.model = trimToNull(model);
        }

        public Integer getCategoryId() {
                return categoryId;
        }

        public void setCategoryId(Integer categoryId) {
                this.categoryId = categoryId;
        }

        public Integer getVehicleStatusId() {
                return vehicleStatusId;
        }

        public void setVehicleStatusId(Integer vehicleStatusId) {
                this.vehicleStatusId = vehicleStatusId;
        }

        public Integer getManufactureYearFrom() {
                return manufactureYearFrom;
        }

        public void setManufactureYearFrom(Integer manufactureYearFrom) {
                this.manufactureYearFrom = manufactureYearFrom;
        }

        public Integer getManufactureYearTo() {
                return manufactureYearTo;
        }

        public void setManufactureYearTo(Integer manufactureYearTo) {
                this.manufactureYearTo = manufactureYearTo;
        }

        public BigDecimal getDailyPriceMin() {
                return dailyPriceMin;
        }

        public void setDailyPriceMin(BigDecimal dailyPriceMin) {
                this.dailyPriceMin = dailyPriceMin;
        }

        public BigDecimal getDailyPriceMax() {
                return dailyPriceMax;
        }

        public void setDailyPriceMax(BigDecimal dailyPriceMax) {
                this.dailyPriceMax = dailyPriceMax;
        }

        public Integer getCurrentHeadquartersId() {
                return currentHeadquartersId;
        }

        public void setCurrentHeadquartersId(Integer currentHeadquartersId) {
                this.currentHeadquartersId = currentHeadquartersId;
        }

        public boolean isOrderable(String column) {
                return column != null && ORDERABLE.contains(column);
        }

        public String getSafeOrderBy() {
                String column = getOrderBy();
                return isOrderable(column) ? column : "vehicle_id";
        }

        @Override
        public void normalize() {
                super.normalize();
                if (manufactureYearFrom != null && manufactureYearTo != null
                                && manufactureYearFrom.intValue() > manufactureYearTo.intValue()) {
                        Integer tmp = manufactureYearFrom;
                        manufactureYearFrom = manufactureYearTo;
                        manufactureYearTo = tmp;
                }
                if (dailyPriceMin != null && dailyPriceMax != null && dailyPriceMin.compareTo(dailyPriceMax) > 0) {
                        BigDecimal tmp = dailyPriceMin;
                        dailyPriceMin = dailyPriceMax;
                        dailyPriceMax = tmp;
                }
        }
}
