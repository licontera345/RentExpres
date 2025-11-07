package com.pinguela.rentexpressweb.controller.privatearea;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.Results;
import java.util.Collections;

import com.pinguela.rentexpres.model.VehicleCriteria;
import com.pinguela.rentexpres.model.VehicleDTO;
import com.pinguela.rentexpres.model.VehicleReferenceData;
import com.pinguela.rentexpres.service.VehicleManagementService;
import com.pinguela.rentexpres.service.VehicleService;
import com.pinguela.rentexpres.service.impl.VehicleManagementServiceImpl;
import com.pinguela.rentexpres.service.impl.VehicleServiceImpl;
import com.pinguela.rentexpres.util.WebConstants;

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

        VehicleCriteria criteria = buildCriteria(request);
        Results<VehicleDTO> results;
        try {
            results = vehicleService.findBy(criteria, criteria.getSafePage(), criteria.getSafePageSize());
        } catch (RentexpresException e) {
            logger.error("Error retrieving vehicle list", e);
            throw new ServletException("Unable to retrieve vehicles", e);
        }

        if (results == null) {
            results = new Results<VehicleDTO>();
            results.setItems(Collections.<VehicleDTO>emptyList());
            results.setPage(criteria.getSafePage());
            results.setPageSize(criteria.getSafePageSize());
            results.setTotal(0);
            results.normalize();
        } else {
            results.normalize();
        }

        try {
            String locale = resolveLocale(session);
            VehicleReferenceData referenceData = vehicleManagementService.loadReferenceData(locale);
            request.setAttribute("vehicleStatuses", referenceData.getVehicleStatuses());
            request.setAttribute("vehicleCategories", referenceData.getVehicleCategories());
            request.setAttribute("headquarters", referenceData.getHeadquarters());
            request.setAttribute("pageSizes", referenceData.getPageSizes());
        } catch (RentexpresException e) {
            logger.error("Error loading vehicle reference data", e);
            throw new ServletException("Unable to load vehicle reference data", e);
        }

        request.setAttribute("vehicles", results.getItems());
        request.setAttribute("total", Integer.valueOf(results.getTotal()));
        request.setAttribute("totalPages", Integer.valueOf(results.getTotalPages()));
        request.setAttribute("currentPage", Integer.valueOf(results.getPage()));
        request.setAttribute("hasPrev", Boolean.valueOf(results.isHasPrev()));
        request.setAttribute("hasNext", Boolean.valueOf(results.isHasNext()));
        request.setAttribute("fromRow", Integer.valueOf(results.getFromRow()));
        request.setAttribute("toRow", Integer.valueOf(results.getToRow()));
        request.setAttribute("criteria", criteria);

        request.getRequestDispatcher(WebConstants.VIEW_PRIVATE_VEHICLE_MANAGEMENT).forward(request, response);
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
        Integer page = parseInteger(request.getParameter("page"));
        if (page == null || page.intValue() < 1) {
            page = Integer.valueOf(1);
        }
        criteria.setPageNumber(page);
        Integer pageSize = parseInteger(request.getParameter("pageSize"));
        if (pageSize == null) {
            pageSize = Integer.valueOf(20);
        }
        criteria.setPageSize(pageSize);
        criteria.setOrderBy(request.getParameter("orderBy"));
        criteria.setOrderDir(request.getParameter("orderDir"));
        criteria.normalize();
        return criteria;
    }
}
