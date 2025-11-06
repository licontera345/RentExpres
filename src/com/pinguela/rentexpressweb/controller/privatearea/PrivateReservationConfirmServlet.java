package com.pinguela.rentexpressweb.controller.privatearea;

import java.io.IOException;
import java.util.LinkedHashMap;
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
import com.pinguela.rentexpres.model.ReservationDTO;
import com.pinguela.rentexpres.service.ReservationService;
import com.pinguela.rentexpres.service.impl.ReservationServiceImpl;
import com.pinguela.rentexpres.util.WebConstants;
import com.pinguela.rentexpressweb.util.SessionManager;
import com.pinguela.rentexpressweb.view.ReservationSummaryView;

@WebServlet(urlPatterns = WebConstants.URL_PRIVATE_RESERVATION_CONFIRM)
public class PrivateReservationConfirmServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(PrivateReservationConfirmServlet.class);

    private transient ReservationService reservationService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.reservationService = new ReservationServiceImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        applyNoCache(response);

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(WebConstants.SESSION_USER) == null) {
            response.sendRedirect(request.getContextPath() + WebConstants.URL_LOGIN);
            return;
        }

        ReservationSummaryView summary = (ReservationSummaryView) session
                .getAttribute(WebConstants.SESSION_RESERVATION_SUMMARY);
        if (summary == null) {
            response.sendRedirect(request.getContextPath() + WebConstants.URL_PUBLIC_RESERVATION_FORM);
            return;
        }

        ReservationDTO reservationDraft = (ReservationDTO) session.getAttribute(WebConstants.SESSION_RESERVATION_DRAFT);
        if (reservationDraft == null) {
            Map<String, String> errors = new LinkedHashMap<String, String>();
            errors.put("global", WebConstants.MESSAGE_ERROR_RESERVATION_DRAFT);
            request.setAttribute(WebConstants.REQUEST_ERRORS, errors);
            RequestDispatcher dispatcher = request.getRequestDispatcher(WebConstants.VIEW_PRIVATE_RESERVATION_START);
            dispatcher.forward(request, response);
            return;
        }

        if (reservationDraft.getVehicleId() == null || reservationDraft.getUserId() == null) {
            Map<String, String> errors = new LinkedHashMap<String, String>();
            errors.put("global", WebConstants.MESSAGE_ERROR_RESERVATION_DRAFT);
            request.setAttribute(WebConstants.REQUEST_ERRORS, errors);
            RequestDispatcher dispatcher = request.getRequestDispatcher(WebConstants.VIEW_PRIVATE_RESERVATION_START);
            dispatcher.forward(request, response);
            return;
        }

        ReservationDTO reservation = new ReservationDTO();
        reservation.setVehicleId(reservationDraft.getVehicleId());
        reservation.setUserId(reservationDraft.getUserId());
        reservation.setEmployeeId(Integer.valueOf(WebConstants.DEFAULT_RESERVATION_EMPLOYEE_ID));
        reservation.setReservationStatusId(Integer.valueOf(WebConstants.DEFAULT_RESERVATION_STATUS_PENDING));
        reservation.setPickupHeadquartersId(summary.getPickupHeadquarters().getHeadquartersId());
        reservation.setReturnHeadquartersId(summary.getReturnHeadquarters().getHeadquartersId());
        reservation.setStartDate(summary.getPickupDateTime());
        reservation.setEndDate(summary.getReturnDateTime());

        try {
            if (!reservationService.create(reservation)) {
                Map<String, String> errors = new LinkedHashMap<String, String>();
                errors.put("global", "reservation.confirmation.error.createFailed");
                request.setAttribute(WebConstants.REQUEST_ERRORS, errors);
                request.setAttribute(WebConstants.REQUEST_RESERVATION_SUMMARY, summary);
                RequestDispatcher dispatcher = request
                        .getRequestDispatcher(WebConstants.VIEW_PUBLIC_RESERVATION_SUMMARY);
                dispatcher.forward(request, response);
                return;
            }
        } catch (RentexpresException e) {
            logger.error("Error creating reservation", e);
            throw new ServletException("Unable to create reservation", e);
        }

        SessionManager.removeAttribute(request, WebConstants.SESSION_RESERVATION_SUMMARY);
        SessionManager.removeAttribute(request, WebConstants.SESSION_RESERVATION_FORM);
        session.removeAttribute(WebConstants.SESSION_RESERVATION_DRAFT);

        request.setAttribute(WebConstants.REQUEST_RESERVATION_SUMMARY, summary);

        RequestDispatcher dispatcher = request.getRequestDispatcher(WebConstants.VIEW_PUBLIC_RESERVATION_CONFIRMATION);
        dispatcher.forward(request, response);
    }

    private void applyNoCache(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
    }
}
