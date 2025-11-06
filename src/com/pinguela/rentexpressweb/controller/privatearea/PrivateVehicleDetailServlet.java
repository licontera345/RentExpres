package com.pinguela.rentexpressweb.controller.privatearea;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.VehicleDTO;
import com.pinguela.rentexpres.service.VehicleService;
import com.pinguela.rentexpres.service.VehicleManagementService;
import com.pinguela.rentexpres.service.impl.VehicleServiceImpl;
import com.pinguela.rentexpres.service.impl.VehicleManagementServiceImpl;
import com.pinguela.rentexpres.util.WebConstants;

@WebServlet("/private/vehicles/detail")
public class PrivateVehicleDetailServlet extends BasePrivateServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(PrivateVehicleDetailServlet.class);

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
        String vehicleIdParam = request.getParameter(WebConstants.PARAM_VEHICLE_ID);
        Integer vehicleId = null;

        if (vehicleIdParam == null || vehicleIdParam.trim().isEmpty()) {
            errors.put(WebConstants.PARAM_VEHICLE_ID, WebConstants.MESSAGE_ERROR_VEHICLE_REQUIRED);
        } else {
            try {
                vehicleId = Integer.valueOf(vehicleIdParam);
            } catch (NumberFormatException ex) {
                logger.warn("Invalid vehicle id received: {}", vehicleIdParam);
                errors.put(WebConstants.PARAM_VEHICLE_ID, WebConstants.MESSAGE_ERROR_VEHICLE_INVALID);
            }
        }

        VehicleDTO vehicle = null;
        if (errors.isEmpty()) {
            try {
                vehicle = vehicleService.findById(vehicleId);
                if (vehicle == null) {
                    errors.put(WebConstants.PARAM_VEHICLE_ID, WebConstants.MESSAGE_ERROR_VEHICLE_NOT_FOUND);
                }
            } catch (RentexpresException e) {
                logger.error("Error loading vehicle detail for id {}", vehicleId, e);
                throw new ServletException("Unable to load vehicle detail", e);
            }
        }

        if (vehicle != null) {
            request.setAttribute(WebConstants.REQUEST_VEHICLE, vehicle);
            try {
                List<String> imageNames = vehicleManagementService.loadVehicleImageNames(vehicle.getVehicleId());
                request.setAttribute(WebConstants.REQUEST_VEHICLE_IMAGES, imageNames);
            } catch (RentexpresException e) {
                logger.error("Error loading vehicle images for id {}", vehicle.getVehicleId(), e);
                throw new ServletException("Unable to load vehicle images", e);
            }
        }

        if (!errors.isEmpty()) {
            request.setAttribute(WebConstants.REQUEST_ERRORS, errors);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(WebConstants.VIEW_PRIVATE_VEHICLE_DETAIL);
        dispatcher.forward(request, response);
    }
}
