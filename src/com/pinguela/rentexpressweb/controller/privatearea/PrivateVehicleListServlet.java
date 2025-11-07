package com.pinguela.rentexpressweb.controller.privatearea;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.model.VehicleCriteria;
import com.pinguela.rentexpres.model.VehicleDTO;
import com.pinguela.rentexpres.model.VehicleReferenceData;
import com.pinguela.rentexpres.service.VehicleManagementService;
import com.pinguela.rentexpres.service.VehicleService;
import com.pinguela.rentexpres.service.impl.VehicleManagementServiceImpl;
import com.pinguela.rentexpres.service.impl.VehicleServiceImpl;
import com.pinguela.rentexpres.util.WebConstants;
import com.pinguela.rentexpressweb.util.CatalogLoader;

@WebServlet(urlPatterns = WebConstants.URL_PRIVATE_VEHICLE_LIST)
public class PrivateVehicleListServlet extends BasePrivateServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(PrivateVehicleListServlet.class);

    private transient VehicleService vehicleService;
    private transient VehicleManagementService vehicleManagementService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.vehicleService = new VehicleServiceImpl();
        this.vehicleManagementService = new VehicleManagementServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        applyNoCache(response);
        HttpSession session = requireAuthenticatedSession(request, response);
        if (session == null) {
            return;
        }

        Map<String, String> errors = new LinkedHashMap<String, String>();
        VehicleCriteria criteria = buildCriteria(request);
        Results<VehicleDTO> results = null;

        try {
            results = vehicleService.findBy(criteria, criteria.getSafePage(), criteria.getSafePageSize());
        } catch (RentexpresException e) {
            logger.error("Error retrieving vehicle list", e);
            throw new ServletException("Unable to retrieve vehicles", e);
        }

        request.setAttribute(WebConstants.REQUEST_VEHICLE_RESULTS, results);
        request.setAttribute(WebConstants.REQUEST_VEHICLE_CRITERIA, criteria);

        VehicleReferenceData referenceData = null;
        try {
            referenceData = vehicleManagementService.loadReferenceData(resolveLocale(session));
        } catch (RentexpresException e) {
            logger.error("Error loading vehicle reference data", e);
            throw new ServletException("Unable to load vehicle reference data", e);
        }
        request.setAttribute("vehicleStatuses", referenceData.getVehicleStatuses());
        request.setAttribute("vehicleCategories", referenceData.getVehicleCategories());
        request.setAttribute("headquarters", referenceData.getHeadquarters());
        request.setAttribute("vehicleStatusMap", referenceData.getVehicleStatusMap());
        request.setAttribute("vehicleCategoryMap", referenceData.getVehicleCategoryMap());
        request.setAttribute(WebConstants.REQUEST_PAGE_SIZES, referenceData.getPageSizes());

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
                        formImages = vehicleManagementService.loadVehicleImageNames(vehicleId);
                    }
                } catch (RentexpresException e) {
                    logger.error("Error loading vehicle {} for edition", vehicleId, e);
                    throw new ServletException("Unable to load vehicle", e);
                }
            }
        } else if (WebConstants.ACTION_CREATE.equalsIgnoreCase(action)) {
            vehicleForm = new VehicleDTO();
        }

        handleFlashMessages(session, request);

        if (vehicleForm != null) {
            request.setAttribute(WebConstants.REQUEST_VEHICLE_FORM, vehicleForm);
            request.setAttribute(WebConstants.REQUEST_VEHICLE_FORM_IMAGES, formImages);
        }

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
}
