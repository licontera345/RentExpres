package com.pinguela.rentexpressweb.controller.privatearea;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

@WebServlet(urlPatterns = WebConstants.URL_PRIVATE_VEHICLE_LIST)
public class PrivateVehicleListServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(PrivateVehicleListServlet.class);

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        applyNoCache(response);
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(WebConstants.SESSION_USER) == null) {
            response.sendRedirect(request.getContextPath() + WebConstants.URL_LOGIN);
            return;
        }

        Map<String, String> errors = new LinkedHashMap<String, String>();
        VehicleCriteria criteria = buildCriteria(request);
        Results<VehicleDTO> results = null;

        try {
            results = vehicleService.findByCriteria(criteria);
        } catch (RentexpresException e) {
            logger.error("Error retrieving vehicle list", e);
            throw new ServletException("Unable to retrieve vehicles", e);
        }

        request.setAttribute(WebConstants.REQUEST_VEHICLE_RESULTS, results);
        request.setAttribute(WebConstants.REQUEST_VEHICLE_CRITERIA, criteria);
        request.setAttribute(WebConstants.REQUEST_PAGE_SIZES, PAGE_SIZES);

        String action = request.getParameter(WebConstants.PARAM_ACTION);
        VehicleDTO vehicleForm = null;
        List<String> formImages = Collections.emptyList();
        if (WebConstants.ACTION_EDIT.equalsIgnoreCase(action)) {
            Integer vehicleId = parseInteger(request.getParameter(WebConstants.PARAM_VEHICLE_ID));
            if (vehicleId == null) {
                errors.put(WebConstants.PARAM_VEHICLE_ID, WebConstants.MESSAGE_ERROR_VEHICLE_INVALID);
            } else {
                try {
                    vehicleForm = vehicleService.findById(vehicleId);
                    if (vehicleForm == null) {
                        errors.put(WebConstants.PARAM_VEHICLE_ID, WebConstants.MESSAGE_ERROR_VEHICLE_NOT_FOUND);
                    } else {
                        formImages = fileService.getImagesByVehicleId(vehicleId).stream().map(file -> file.getName())
                                .collect(Collectors.toList());
                    }
                } catch (RentexpresException e) {
                    logger.error("Error loading vehicle {} for edition", vehicleId, e);
                    throw new ServletException("Unable to load vehicle", e);
                }
            }
        } else if (WebConstants.ACTION_CREATE.equalsIgnoreCase(action)) {
            vehicleForm = new VehicleDTO();
        }

        populateReferenceData(request, session);

        if (vehicleForm != null) {
            request.setAttribute(WebConstants.REQUEST_VEHICLE_FORM, vehicleForm);
            request.setAttribute(WebConstants.REQUEST_VEHICLE_FORM_IMAGES, formImages);
        }

        handleFlashMessages(session, request);

        if (!errors.isEmpty()) {
            request.setAttribute(WebConstants.REQUEST_ERRORS, errors);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(WebConstants.VIEW_PRIVATE_VEHICLE_MANAGEMENT);
        dispatcher.forward(request, response);
    }

    private VehicleCriteria buildCriteria(HttpServletRequest request) {
        VehicleCriteria criteria = new VehicleCriteria();
        criteria.setBrand(request.getParameter("brand"));
        criteria.setModel(request.getParameter("model"));
        criteria.setCategoryId(parseInteger(request.getParameter("categoryId")));
        criteria.setVehicleStatusId(parseInteger(request.getParameter("vehicleStatusId")));
        criteria.setCurrentHeadquartersId(parseInteger(request.getParameter("headquartersId")));
        criteria.setManufactureYearFrom(parseInteger(request.getParameter("manufactureYearFrom")));
        criteria.setManufactureYearTo(parseInteger(request.getParameter("manufactureYearTo")));
        criteria.setDailyPriceMin(parseBigDecimal(request.getParameter("dailyPriceMin")));
        criteria.setDailyPriceMax(parseBigDecimal(request.getParameter("dailyPriceMax")));
        criteria.setPageNumber(parseInteger(request.getParameter("page")));
        criteria.setPageSize(parseInteger(request.getParameter("pageSize")));
        criteria.setOrderBy(request.getParameter("orderBy"));
        criteria.setOrderDir(request.getParameter("orderDir"));
        return criteria;
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

    private void populateReferenceData(HttpServletRequest request, HttpSession session) throws ServletException {
        String locale = resolveLocale(session);
        try {
            List<VehicleStatusDTO> statuses = vehicleStatusService.findAll(locale);
            List<VehicleCategoryDTO> categories = CatalogLoader.loadCategories(request, vehicleCategoryService, locale);
            List<com.pinguela.rentexpres.model.HeadquartersDTO> headquarters = CatalogLoader
                    .loadHeadquarters(request, headquartersService);

            request.setAttribute("vehicleStatuses", statuses);
            request.setAttribute("vehicleStatusMap", buildStatusMap(statuses));
            request.setAttribute("vehicleCategoryMap", buildCategoryMap(categories));
        } catch (RentexpresException e) {
            logger.error("Error loading reference data for vehicle management", e);
            throw new ServletException("Unable to load vehicle reference data", e);
        }
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

    private void handleFlashMessages(HttpSession session, HttpServletRequest request) {
        Object success = session.getAttribute(WebConstants.SESSION_FLASH_SUCCESS);
        if (success != null) {
            request.setAttribute(WebConstants.SESSION_FLASH_SUCCESS, success);
            session.removeAttribute(WebConstants.SESSION_FLASH_SUCCESS);
        }
        @SuppressWarnings("unchecked")
        Map<String, String> flashErrors = (Map<String, String>) session.getAttribute(WebConstants.SESSION_FLASH_ERRORS);
        if (flashErrors != null && !flashErrors.isEmpty()) {
            request.setAttribute(WebConstants.REQUEST_ERRORS, new LinkedHashMap<String, String>(flashErrors));
            session.removeAttribute(WebConstants.SESSION_FLASH_ERRORS);
        }
    }

    private void applyNoCache(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
    }
}
