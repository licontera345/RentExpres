package com.pinguela.rentexpres.service;

import java.io.File;
import java.util.List;
import java.util.Set;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.model.VehicleDTO;
import com.pinguela.rentexpres.model.VehicleReferenceData;

public interface VehicleManagementService {

    VehicleReferenceData loadReferenceData(String locale) throws RentexpresException;

    List<String> loadVehicleImageNames(Integer vehicleId) throws RentexpresException;

    void synchronizeVehicleImages(Integer vehicleId, Set<String> keepImageNames, List<File> newImages)
            throws RentexpresException;

    Results<VehicleDTO> loadVehicleSnapshot() throws RentexpresException;
}
