package com.pinguela.rentexpressweb.controller.privatearea;

import java.io.IOException;
import java.util.LinkedHashMap;
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
import com.pinguela.rentexpres.model.ReservationDTO;
import com.pinguela.rentexpres.model.UserDTO;
import com.pinguela.rentexpres.model.VehicleDTO;
import com.pinguela.rentexpres.service.VehicleService;
import com.pinguela.rentexpres.service.impl.VehicleServiceImpl;
import com.pinguela.rentexpres.util.WebConstants;

@WebServlet(WebConstants.URL_PRIVATE_RESERVATION_START)
public class PrivateReservationServlet extends BasePrivateServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(PrivateReservationServlet.class);

    private transient VehicleService vehicleService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.vehicleService = new VehicleServiceImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
                logger.warn("Invalid vehicle id received for reservation start: {}", vehicleIdParam);
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
                logger.error("Error retrieving vehicle {} for reservation start", vehicleId, e);
                throw new ServletException("Unable to start reservation", e);
            }
        }

        if (!errors.isEmpty()) {
            request.setAttribute(WebConstants.REQUEST_ERRORS, errors);
            if (vehicle != null) {
                request.setAttribute(WebConstants.REQUEST_VEHICLE, vehicle);
            }
            RequestDispatcher dispatcher = request.getRequestDispatcher(WebConstants.VIEW_PRIVATE_VEHICLE_DETAIL);
            dispatcher.forward(request, response);
            return;
        }

        UserDTO user = (UserDTO) session.getAttribute(WebConstants.SESSION_USER);
        ReservationDTO reservationDraft = new ReservationDTO();
        reservationDraft.setVehicleId(vehicle.getVehicleId());
        reservationDraft.setUserId(user.getUserId());
        reservationDraft.setVehicle(vehicle);
        reservationDraft.setUser(user);

        session.setAttribute(WebConstants.SESSION_RESERVATION_DRAFT, reservationDraft);

        request.setAttribute(WebConstants.REQUEST_VEHICLE, vehicle);
        request.setAttribute(WebConstants.REQUEST_RESERVATION, reservationDraft);

        RequestDispatcher dispatcher = request.getRequestDispatcher(WebConstants.VIEW_PRIVATE_RESERVATION_START);
        dispatcher.forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        applyNoCache(response);

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(WebConstants.SESSION_USER) == null) {
            response.sendRedirect(request.getContextPath() + WebConstants.URL_LOGIN);
            return;
        }

        ReservationDTO reservationDraft = (ReservationDTO) session.getAttribute(WebConstants.SESSION_RESERVATION_DRAFT);
        if (reservationDraft == null) {
            Map<String, String> errors = new LinkedHashMap<String, String>();
            errors.put(WebConstants.PARAM_VEHICLE_ID, WebConstants.MESSAGE_ERROR_RESERVATION_DRAFT);
            request.setAttribute(WebConstants.REQUEST_ERRORS, errors);
            RequestDispatcher dispatcher = request.getRequestDispatcher(WebConstants.VIEW_PRIVATE_RESERVATION_START);
            dispatcher.forward(request, response);
            return;
        }

        request.setAttribute(WebConstants.REQUEST_RESERVATION, reservationDraft);
        request.setAttribute(WebConstants.REQUEST_VEHICLE, reservationDraft.getVehicle());

        RequestDispatcher dispatcher = request.getRequestDispatcher(WebConstants.VIEW_PRIVATE_RESERVATION_START);
        dispatcher.forward(request, response);
    }
}
