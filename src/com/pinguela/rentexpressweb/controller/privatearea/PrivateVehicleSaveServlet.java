package com.pinguela.rentexpressweb.controller.privatearea;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.model.VehicleCategoryDTO;
import com.pinguela.rentexpres.model.VehicleCriteria;
import com.pinguela.rentexpres.model.VehicleDTO;
import com.pinguela.rentexpres.model.VehicleStatusDTO;
import com.pinguela.rentexpres.service.FileService;
import com.pinguela.rentexpres.service.HeadquartersService;
import com.pinguela.rentexpres.service.VehicleCategoryService;
import com.pinguela.rentexpres.service.VehicleService;
import com.pinguela.rentexpres.service.VehicleStatusService;
import com.pinguela.rentexpres.service.impl.FileServiceImpl;
import com.pinguela.rentexpres.service.impl.HeadquartersServiceImpl;
import com.pinguela.rentexpres.service.impl.VehicleCategoryServiceImpl;
import com.pinguela.rentexpres.service.impl.VehicleServiceImpl;
import com.pinguela.rentexpres.service.impl.VehicleStatusServiceImpl;
import com.pinguela.rentexpres.util.WebConstants;
import com.pinguela.rentexpressweb.util.CatalogLoader;

@WebServlet("/private/vehicles/save")
@MultipartConfig
public class PrivateVehicleSaveServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(PrivateVehicleSaveServlet.class);
    private static final List<Integer> PAGE_SIZES = Collections.unmodifiableList(Arrays.asList(10, 20, 25, 50, 100));

    private transient VehicleService vehicleService;
    private transient VehicleStatusService vehicleStatusService;
    private transient VehicleCategoryService vehicleCategoryService;
    private transient HeadquartersService headquartersService;
    private transient FileService fileService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.vehicleService = new VehicleServiceImpl();
        this.vehicleStatusService = new VehicleStatusServiceImpl();
        this.vehicleCategoryService = new VehicleCategoryServiceImpl();
        this.headquartersService = new HeadquartersServiceImpl();
        this.fileService = new FileServiceImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        applyNoCache(response);
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(WebConstants.SESSION_USER) == null) {
            response.sendRedirect(request.getContextPath() + WebConstants.URL_LOGIN);
            return;
        }

        request.setCharacterEncoding("UTF-8");
        Map<String, String> errors = new LinkedHashMap<String, String>();

        Integer vehicleId = parseInteger(request.getParameter("vehicleId"));
        String brand = trim(request.getParameter("brand"));
        String model = trim(request.getParameter("model"));
        Integer manufactureYear = parseInteger(request.getParameter("manufactureYear"));
        BigDecimal dailyPrice = parseBigDecimal(request.getParameter("dailyPrice"));
        String licensePlate = trim(request.getParameter("licensePlate"));
        String vinNumber = trim(request.getParameter("vinNumber"));
        Integer currentMileage = parseInteger(request.getParameter("currentMileage"));
        Integer statusId = parseInteger(request.getParameter("vehicleStatusId"));
        Integer categoryId = parseInteger(request.getParameter("categoryId"));
        Integer headquartersId = parseInteger(request.getParameter("headquartersId"));

        validateRequired(brand, "brand", errors, "error.vehicle.brand.required");
        validateRequired(model, "model", errors, "error.vehicle.model.required");
        validateRequired(licensePlate, "licensePlate", errors, "error.vehicle.licensePlate.required");
        validateRequired(vinNumber, "vinNumber", errors, "error.vehicle.vin.required");

        if (manufactureYear == null || manufactureYear.intValue() < 1950) {
            errors.put("manufactureYear", "error.vehicle.year.invalid");
        }
        if (dailyPrice == null || dailyPrice.signum() <= 0) {
            errors.put("dailyPrice", "error.vehicle.dailyPrice.invalid");
        }
        if (currentMileage == null || currentMileage.intValue() < 0) {
            errors.put("currentMileage", "error.vehicle.mileage.invalid");
        }
        if (statusId == null) {
            errors.put("vehicleStatusId", "error.vehicle.status.required");
        }
        if (categoryId == null) {
            errors.put("categoryId", "error.vehicle.category.required");
        }
        if (headquartersId == null) {
            errors.put("headquartersId", "error.vehicle.headquarters.required");
        }

        VehicleDTO vehicle = new VehicleDTO();
        vehicle.setVehicleId(vehicleId);
        vehicle.setBrand(brand);
        vehicle.setModel(model);
        vehicle.setManufactureYear(manufactureYear);
        vehicle.setDailyPrice(dailyPrice);
        vehicle.setLicensePlate(licensePlate);
        vehicle.setVinNumber(vinNumber);
        vehicle.setCurrentMileage(currentMileage);
        vehicle.setVehicleStatusId(statusId);
        vehicle.setCategoryId(categoryId);
        vehicle.setCurrentHeadquartersId(headquartersId);

        boolean createOperation = vehicleId == null;

        if (!errors.isEmpty()) {
            logger.debug("Validation errors detected while saving vehicle: {}", errors);
            forwardWithErrors(request, response, session, vehicle, errors);
            return;
        }

        try {
            if (createOperation) {
                if (!vehicleService.create(vehicle)) {
                    errors.put("global", "error.vehicle.createFailed");
                    forwardWithErrors(request, response, session, vehicle, errors);
                    return;
                }
            } else {
                if (!vehicleService.update(vehicle)) {
                    errors.put("global", "error.vehicle.updateFailed");
                    forwardWithErrors(request, response, session, vehicle, errors);
                    return;
                }
            }
        } catch (RentexpresException e) {
            logger.error("Error saving vehicle", e);
            throw new ServletException("Unable to save vehicle", e);
        }

        List<File> filesToSync = new ArrayList<File>();
        List<File> tempFiles = new ArrayList<File>();
        try {
            if (!createOperation) {
                List<File> currentImages = fileService.getImagesByVehicleId(vehicle.getVehicleId());
                Set<String> keepImages = extractExistingSelection(request);
                if (!currentImages.isEmpty()) {
                    for (File file : currentImages) {
                        if (keepImages.contains(file.getName())) {
                            filesToSync.add(file);
                        }
                    }
                }
            }

            for (Part part : request.getParts()) {
                if (!"images".equals(part.getName()) || part.getSize() <= 0) {
                    continue;
                }
                File temp = File.createTempFile("vehicle-image-", getExtension(part));
                try (InputStream in = part.getInputStream()) {
                    Files.copy(in, temp.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                }
                filesToSync.add(temp);
                tempFiles.add(temp);
            }

            if (!filesToSync.isEmpty() || !createOperation) {
                fileService.uploadImagesByVehicleId(filesToSync, vehicle.getVehicleId());
            }
        } catch (RentexpresException e) {
            logger.error("Error synchronising vehicle images", e);
            throw new ServletException("Unable to process vehicle images", e);
        } finally {
            for (File temp : tempFiles) {
                try {
                    Files.deleteIfExists(temp.toPath());
                } catch (IOException ex) {
                    logger.warn("Unable to delete temporary file {}", temp.getAbsolutePath(), ex);
                }
            }
        }

        String successKey = createOperation ? WebConstants.MESSAGE_SUCCESS_VEHICLE_CREATED
                : WebConstants.MESSAGE_SUCCESS_VEHICLE_UPDATED;
        session.setAttribute(WebConstants.SESSION_FLASH_SUCCESS, successKey);
        response.sendRedirect(request.getContextPath() + WebConstants.URL_PRIVATE_VEHICLE_LIST);
    }

    private void forwardWithErrors(HttpServletRequest request, HttpServletResponse response, HttpSession session,
            VehicleDTO vehicle, Map<String, String> errors) throws ServletException, IOException {
        request.setAttribute(WebConstants.REQUEST_VEHICLE_FORM, vehicle);
        request.setAttribute(WebConstants.REQUEST_VEHICLE_FORM_IMAGES, loadExistingImageNames(vehicle));
        request.setAttribute(WebConstants.REQUEST_ERRORS, errors);
        populateReferenceData(request, session);
        populateResultsSnapshot(request);
        RequestDispatcher dispatcher = request.getRequestDispatcher(WebConstants.VIEW_PRIVATE_VEHICLE_MANAGEMENT);
        dispatcher.forward(request, response);
    }

    private List<String> loadExistingImageNames(VehicleDTO vehicle) {
        if (vehicle == null || vehicle.getVehicleId() == null) {
            return new ArrayList<String>();
        }
        try {
            List<File> current = fileService.getImagesByVehicleId(vehicle.getVehicleId());
            return current.stream().map(File::getName).collect(Collectors.toList());
        } catch (RentexpresException e) {
            logger.warn("Error loading existing images for vehicle {}", vehicle.getVehicleId(), e);
            return new ArrayList<String>();
        }
    }

    private void populateResultsSnapshot(HttpServletRequest request) {
        VehicleCriteria criteria = new VehicleCriteria();
        criteria.setPageNumber(Integer.valueOf(1));
        criteria.setPageSize(Integer.valueOf(10));
        try {
            Results<VehicleDTO> results = vehicleService.findByCriteria(criteria);
            request.setAttribute(WebConstants.REQUEST_VEHICLE_RESULTS, results);
        } catch (RentexpresException e) {
            logger.warn("Error loading vehicle list snapshot", e);
            Results<VehicleDTO> fallback = new Results<VehicleDTO>();
            fallback.setItems(Collections.<VehicleDTO>emptyList());
            fallback.setPage(1);
            fallback.setPageSize(10);
            fallback.setTotal(0);
            fallback.normalize();
            request.setAttribute(WebConstants.REQUEST_VEHICLE_RESULTS, fallback);
        }
        request.setAttribute(WebConstants.REQUEST_VEHICLE_CRITERIA, criteria);
    }

    private void populateReferenceData(HttpServletRequest request, HttpSession session) throws ServletException {
        String locale = resolveLocale(session);
        try {
            List<VehicleStatusDTO> statuses = vehicleStatusService.findAll(locale);
            List<VehicleCategoryDTO> categories = CatalogLoader.loadCategories(request, vehicleCategoryService, locale);
            CatalogLoader.loadHeadquarters(request, headquartersService);

            request.setAttribute("vehicleStatuses", statuses);
            request.setAttribute("vehicleStatusMap", buildStatusMap(statuses));
            request.setAttribute("vehicleCategoryMap", buildCategoryMap(categories));
        } catch (RentexpresException e) {
            logger.error("Error loading reference data for vehicle form", e);
            throw new ServletException("Unable to load vehicle reference data", e);
        }
        request.setAttribute(WebConstants.REQUEST_PAGE_SIZES, PAGE_SIZES);
    }

    private Map<Integer, String> buildStatusMap(List<VehicleStatusDTO> statuses) {
        Map<Integer, String> map = new LinkedHashMap<Integer, String>();
        if (statuses != null) {
            for (VehicleStatusDTO status : statuses) {
                map.put(status.getVehicleStatusId(), status.getName());
            }
        }
        return map;
    }

    private Map<Integer, String> buildCategoryMap(List<VehicleCategoryDTO> categories) {
        Map<Integer, String> map = new LinkedHashMap<Integer, String>();
        if (categories != null) {
            for (VehicleCategoryDTO category : categories) {
                map.put(category.getCategoryId(), category.getCategoryName());
            }
        }
        return map;
    }

    private Set<String> extractExistingSelection(HttpServletRequest request) {
        String[] selected = request.getParameterValues("existingImage");
        if (selected == null || selected.length == 0) {
            return Collections.emptySet();
        }
        Set<String> set = new LinkedHashSet<String>();
        for (String value : selected) {
            if (value != null && !value.trim().isEmpty()) {
                set.add(value.trim());
            }
        }
        return set;
    }

    private Integer parseInteger(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.valueOf(value.trim());
        } catch (NumberFormatException ex) {
            logger.debug("Invalid integer value received: {}", value, ex);
            return null;
        }
    }

    private BigDecimal parseBigDecimal(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException ex) {
            logger.debug("Invalid decimal value received: {}", value, ex);
            return null;
        }
    }

    private void validateRequired(String value, String field, Map<String, String> errors, String messageKey) {
        if (value == null || value.trim().isEmpty()) {
            errors.put(field, messageKey);
        }
    }

    private String trim(String value) {
        if (value == null) {
            return null;
        }
        String cleaned = value.trim();
        return cleaned.isEmpty() ? null : cleaned;
    }

    private String resolveLocale(HttpSession session) {
        Object localeAttr = session.getAttribute("locale");
        if (localeAttr instanceof String) {
            String value = ((String) localeAttr).trim();
            if (!value.isEmpty()) {
                return value;
            }
        }
        return Locale.getDefault().getLanguage();
    }

    private String getExtension(Part part) {
        String submitted = part.getSubmittedFileName();
        if (submitted == null) {
            return ".tmp";
        }
        int idx = submitted.lastIndexOf('.');
        return idx >= 0 ? submitted.substring(idx) : ".tmp";
    }

    private void applyNoCache(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
    }
}
