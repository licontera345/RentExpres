package com.pinguela.rentexpres.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.HeadquartersDTO;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.model.VehicleCategoryDTO;
import com.pinguela.rentexpres.model.VehicleCriteria;
import com.pinguela.rentexpres.model.VehicleDTO;
import com.pinguela.rentexpres.model.VehicleReferenceData;
import com.pinguela.rentexpres.model.VehicleStatusDTO;
import com.pinguela.rentexpres.service.FileService;
import com.pinguela.rentexpres.service.HeadquartersService;
import com.pinguela.rentexpres.service.VehicleCategoryService;
import com.pinguela.rentexpres.service.VehicleManagementService;
import com.pinguela.rentexpres.service.VehicleService;
import com.pinguela.rentexpres.service.VehicleStatusService;

public class VehicleManagementServiceImpl implements VehicleManagementService {
    private static final List<Integer> PAGE_SIZES = Collections
            .unmodifiableList(Arrays.asList(Integer.valueOf(10), Integer.valueOf(20), Integer.valueOf(25),
                    Integer.valueOf(50), Integer.valueOf(100)));

    private final VehicleService vehicleService;
    private final VehicleStatusService vehicleStatusService;
    private final VehicleCategoryService vehicleCategoryService;
    private final HeadquartersService headquartersService;
    private final FileService fileService;

    public VehicleManagementServiceImpl() {
        this.vehicleService = new VehicleServiceImpl();
        this.vehicleStatusService = new VehicleStatusServiceImpl();
        this.vehicleCategoryService = new VehicleCategoryServiceImpl();
        this.headquartersService = new HeadquartersServiceImpl();
        this.fileService = new FileServiceImpl();
    }

    @Override
    public VehicleReferenceData loadReferenceData(String locale) throws RentexpresException {
        VehicleReferenceData data = new VehicleReferenceData();
        List<VehicleStatusDTO> statuses = vehicleStatusService.findAll(locale);
        data.setVehicleStatuses(statuses);
        data.setVehicleStatusMap(buildStatusMap(statuses));

        List<VehicleCategoryDTO> categories = vehicleCategoryService.findAll(locale);
        data.setVehicleCategories(categories);
        data.setVehicleCategoryMap(buildCategoryMap(categories));

        List<HeadquartersDTO> headquarters = headquartersService.findAll();
        data.setHeadquarters(headquarters);
        data.setPageSizes(PAGE_SIZES);
        return data;
    }

    @Override
    public List<String> loadVehicleImageNames(Integer vehicleId) throws RentexpresException {
        if (vehicleId == null) {
            return Collections.emptyList();
        }
        List<File> files = fileService.getImagesByVehicleId(vehicleId);
        List<String> names = new ArrayList<String>();
        if (files != null) {
            for (File file : files) {
                if (file != null) {
                    names.add(file.getName());
                }
            }
        }
        return names;
    }

    @Override
    public void synchronizeVehicleImages(Integer vehicleId, Set<String> keepImageNames, List<File> newImages)
            throws RentexpresException {
        if (vehicleId == null) {
            return;
        }
        List<File> filesToSync = new ArrayList<File>();
        boolean hadExisting = false;
        List<File> currentImages = fileService.getImagesByVehicleId(vehicleId);
        if (currentImages != null && !currentImages.isEmpty()) {
            hadExisting = true;
            if (keepImageNames != null && !keepImageNames.isEmpty()) {
                for (File file : currentImages) {
                    if (file != null && keepImageNames.contains(file.getName())) {
                        filesToSync.add(file);
                    }
                }
            }
        }
        if (newImages != null && !newImages.isEmpty()) {
            filesToSync.addAll(newImages);
        }
        if (!filesToSync.isEmpty() || hadExisting) {
            fileService.uploadImagesByVehicleId(filesToSync, vehicleId);
        }
    }

    @Override
    public Results<VehicleDTO> loadVehicleSnapshot() throws RentexpresException {
        VehicleCriteria criteria = new VehicleCriteria();
        criteria.setPageNumber(Integer.valueOf(1));
        criteria.setPageSize(PAGE_SIZES.get(0));
        Results<VehicleDTO> results = vehicleService.findByCriteria(criteria);
        if (results != null) {
            results.normalize();
        }
        return results;
    }

    private Map<Integer, String> buildStatusMap(List<VehicleStatusDTO> statuses) {
        Map<Integer, String> map = new LinkedHashMap<Integer, String>();
        if (statuses != null) {
            for (VehicleStatusDTO status : statuses) {
                if (status != null) {
                    map.put(status.getVehicleStatusId(), status.getName());
                }
            }
        }
        return map;
    }

    private Map<Integer, String> buildCategoryMap(List<VehicleCategoryDTO> categories) {
        Map<Integer, String> map = new LinkedHashMap<Integer, String>();
        if (categories != null) {
            for (VehicleCategoryDTO category : categories) {
                if (category != null) {
                    map.put(category.getCategoryId(), category.getName());
                }
            }
        }
        return map;
    }
}
