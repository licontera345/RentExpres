package com.pinguela.rentexpressweb.controller.privatearea;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import com.pinguela.rentexpres.model.VehicleDTO;
import com.pinguela.rentexpres.service.FileService;
import com.pinguela.rentexpres.service.VehicleService;
import com.pinguela.rentexpres.service.impl.FileServiceImpl;
import com.pinguela.rentexpres.service.impl.VehicleServiceImpl;
import com.pinguela.rentexpres.util.WebConstants;

@WebServlet("/private/vehicles/detail")
public class PrivateVehicleDetailServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(PrivateVehicleDetailServlet.class);

    private transient VehicleService vehicleService;
    private transient FileService fileService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.vehicleService = new VehicleServiceImpl();
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
                List<File> images = fileService.getImagesByVehicleId(vehicle.getVehicleId());
                List<String> imageNames = new ArrayList<String>();
                if (images != null) {
                    for (File img : images) {
                        if (img != null) {
                            imageNames.add(img.getName());
                        }
                    }
                }
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

    private void applyNoCache(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
    }
}
