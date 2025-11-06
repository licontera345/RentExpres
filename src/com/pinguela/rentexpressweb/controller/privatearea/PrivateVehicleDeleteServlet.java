package com.pinguela.rentexpressweb.controller.privatearea;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.service.VehicleService;
import com.pinguela.rentexpres.service.VehicleManagementService;
import com.pinguela.rentexpres.service.impl.VehicleServiceImpl;
import com.pinguela.rentexpres.service.impl.VehicleManagementServiceImpl;
import com.pinguela.rentexpres.util.WebConstants;

@WebServlet("/private/vehicles/delete")
public class PrivateVehicleDeleteServlet extends BasePrivateServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(PrivateVehicleDeleteServlet.class);

    private transient VehicleService vehicleService;
    private transient VehicleManagementService vehicleManagementService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.vehicleService = new VehicleServiceImpl();
        this.vehicleManagementService = new VehicleManagementServiceImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        applyNoCache(response);
        HttpSession session = requireAuthenticatedSession(request, response);
        if (session == null) {
            return;
        }

        Map<String, String> errors = new LinkedHashMap<String, String>();
        Integer vehicleId = parseInteger(request.getParameter(WebConstants.PARAM_VEHICLE_ID));
        if (vehicleId == null) {
            errors.put(WebConstants.PARAM_VEHICLE_ID, WebConstants.MESSAGE_ERROR_VEHICLE_INVALID);
            session.setAttribute(WebConstants.SESSION_FLASH_ERRORS, errors);
            response.sendRedirect(request.getContextPath() + WebConstants.URL_PRIVATE_VEHICLE_LIST);
            return;
        }

        try {
            if (!vehicleService.delete(vehicleId)) {
                errors.put("global", WebConstants.MESSAGE_ERROR_VEHICLE_DELETE);
                session.setAttribute(WebConstants.SESSION_FLASH_ERRORS, errors);
            } else {
                removeVehicleImages(vehicleId);
                session.setAttribute(WebConstants.SESSION_FLASH_SUCCESS, WebConstants.MESSAGE_SUCCESS_VEHICLE_DELETED);
            }
        } catch (RentexpresException e) {
            logger.error("Error deleting vehicle {}", vehicleId, e);
            throw new ServletException("Unable to delete vehicle", e);
        }

        response.sendRedirect(request.getContextPath() + WebConstants.URL_PRIVATE_VEHICLE_LIST);
    }

    private void removeVehicleImages(Integer vehicleId) {
        try {
            vehicleManagementService.synchronizeVehicleImages(vehicleId, Collections.<String>emptySet(),
                    Collections.<File>emptyList());
        } catch (RentexpresException e) {
            logger.warn("Error removing images for deleted vehicle {}", vehicleId, e);
        }
    }
}
