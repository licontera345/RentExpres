package test.com.pinguela.rentexpres.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.model.VehicleCriteria;
import com.pinguela.rentexpres.model.VehicleDTO;
import com.pinguela.rentexpres.service.VehicleService;
import com.pinguela.rentexpres.service.impl.VehicleServiceImpl;

public class VehicleServiceTest {
    public static void main(String[] args) {
        VehicleService service = new VehicleServiceImpl();

        try {
            VehicleDTO vehicle = new VehicleDTO();
            vehicle.setBrand("Toyota");
            vehicle.setModel("Corolla");
            vehicle.setManufactureYear(2015);
            vehicle.setDailyPrice(new BigDecimal("50.0"));
            vehicle.setLicensePlate("AGC-123");
            vehicle.setVinNumber("XYZ987654321");
            vehicle.setCurrentMileage(120000);
            vehicle.setCategoryId(1);
            vehicle.setVehicleStatusId(1);
            vehicle.setCurrentHeadquartersId(1);
            vehicle.setCreatedAt(LocalDateTime.now());
            vehicle.setUpdatedAt(LocalDateTime.now());

            boolean created = service.create(vehicle);
            System.out.println("Create result: " + created);
            if (created) {
                System.out.println("Vehicle created with ID: " + vehicle.getVehicleId());
            }

            VehicleDTO found = service.findById(vehicle.getVehicleId());
            System.out.println("Vehicle found: " + found);

            if (found != null) {
                found.setCurrentMileage(125000);
                boolean updated = service.update(found);
                System.out.println("Update result: " + updated);
                VehicleDTO updatedVehicle = service.findById(found.getVehicleId());
                System.out.println("Vehicle after update: " + updatedVehicle);
            }

            List<VehicleDTO> vehicles = service.findAll();
            System.out.println("Total vehicles: " + (vehicles != null ? vehicles.size() : 0));
            if (vehicles != null) {
                for (VehicleDTO dto : vehicles) {
                    System.out.println(dto);
                }
            }

            VehicleCriteria criteria = new VehicleCriteria();
            criteria.setBrand("Toyota");
            criteria.setPageNumber(1);
            criteria.setPageSize(10);
            Results<VehicleDTO> results = service.findByCriteria(criteria);
            System.out.println("Results findByCriteria - Total records: " + results.getTotalRecords());
            if (results.getResults() != null) {
                for (VehicleDTO dto : results.getResults()) {
                    System.out.println(dto);
                }
            }

            boolean deleted = service.delete(vehicle.getVehicleId());
            System.out.println("Delete result: " + deleted);

        } catch (RentexpresException e) {
            e.printStackTrace();
        }
    }
}