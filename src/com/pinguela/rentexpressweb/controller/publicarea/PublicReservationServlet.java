package com.pinguela.rentexpressweb.controller.publicarea;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.HeadquartersDTO;
import com.pinguela.rentexpres.model.VehicleCategoryDTO;
import com.pinguela.rentexpres.service.HeadquartersService;
import com.pinguela.rentexpres.service.VehicleCategoryService;
import com.pinguela.rentexpres.service.impl.HeadquartersServiceImpl;
import com.pinguela.rentexpres.service.impl.VehicleCategoryServiceImpl;
import com.pinguela.rentexpres.util.WebConstants;
import com.pinguela.rentexpressweb.form.ReservationForm;
import com.pinguela.rentexpressweb.util.CatalogLoader;
import com.pinguela.rentexpressweb.util.SessionManager;
import com.pinguela.rentexpressweb.view.ReservationSummaryView;

@WebServlet(urlPatterns = WebConstants.URL_PUBLIC_RESERVATION_FORM)
public class PublicReservationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(PublicReservationServlet.class);
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private transient VehicleCategoryService vehicleCategoryService;
    private transient HeadquartersService headquartersService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.vehicleCategoryService = new VehicleCategoryServiceImpl();
        this.headquartersService = new HeadquartersServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ReservationForm form = (ReservationForm) SessionManager.getAttribute(request, WebConstants.SESSION_RESERVATION_FORM);
        if (form == null) {
            form = new ReservationForm();
        }
        request.setAttribute(WebConstants.REQUEST_RESERVATION_FORM, form);

        loadReferenceData(request);

        RequestDispatcher dispatcher = request.getRequestDispatcher(WebConstants.VIEW_PUBLIC_RESERVATION_FORM);
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        ReservationForm form = buildFormFromRequest(request);
        Map<String, String> errors = new LinkedHashMap<String, String>();

        LocalDateTime pickupDateTime = parseDateTime(form.getStartDate(), WebConstants.PARAM_START_DATE,
                "reservation.form.error.startDate.required", errors);
        LocalDateTime returnDateTime = parseDateTime(form.getEndDate(), WebConstants.PARAM_END_DATE,
                "reservation.form.error.endDate.required", errors);

        if (pickupDateTime != null && pickupDateTime.isBefore(LocalDateTime.now())) {
            errors.put(WebConstants.PARAM_START_DATE, "reservation.form.error.startDate.past");
        }
        if (pickupDateTime != null && returnDateTime != null && !returnDateTime.isAfter(pickupDateTime)) {
            errors.put(WebConstants.PARAM_END_DATE, "reservation.form.error.dateRange");
        }

        Integer pickupHeadquartersId = parseInteger(form.getPickupHeadquartersId(), WebConstants.PARAM_PICKUP_HEADQUARTERS,
                "reservation.form.error.pickup.required", errors);
        Integer returnHeadquartersId = parseInteger(form.getReturnHeadquartersId(), WebConstants.PARAM_RETURN_HEADQUARTERS,
                "reservation.form.error.return.required", errors);
        Integer categoryId = parseInteger(form.getCategoryId(), WebConstants.PARAM_CATEGORY_ID,
                "reservation.form.error.category.required", errors);

        if (pickupHeadquartersId != null && returnHeadquartersId != null
                && pickupHeadquartersId.equals(returnHeadquartersId)) {
            errors.put(WebConstants.PARAM_RETURN_HEADQUARTERS, "reservation.form.error.headquarters.same");
        }

        String locale = resolveLocale(request);
        HeadquartersDTO pickupHeadquarters = null;
        HeadquartersDTO returnHeadquarters = null;
        VehicleCategoryDTO category = null;

        if (errors.isEmpty()) {
            try {
                pickupHeadquarters = headquartersService.findById(pickupHeadquartersId);
                if (pickupHeadquarters == null) {
                    errors.put(WebConstants.PARAM_PICKUP_HEADQUARTERS, "reservation.form.error.pickup.notFound");
                }
            } catch (DataException e) {
                logger.error("Error retrieving pickup headquarters {}", pickupHeadquartersId, e);
                throw new ServletException("Unable to load pickup headquarters", e);
            }

            if (errors.isEmpty()) {
                try {
                    returnHeadquarters = headquartersService.findById(returnHeadquartersId);
                    if (returnHeadquarters == null) {
                        errors.put(WebConstants.PARAM_RETURN_HEADQUARTERS, "reservation.form.error.return.notFound");
                    }
                } catch (DataException e) {
                    logger.error("Error retrieving return headquarters {}", returnHeadquartersId, e);
                    throw new ServletException("Unable to load return headquarters", e);
                }
            }

            if (errors.isEmpty()) {
                try {
                    category = vehicleCategoryService.findById(categoryId, locale);
                    if (category == null) {
                        errors.put(WebConstants.PARAM_CATEGORY_ID, "reservation.form.error.category.notFound");
                    }
                } catch (RentexpresException e) {
                    logger.error("Error retrieving vehicle category {}", categoryId, e);
                    throw new ServletException("Unable to load vehicle category", e);
                }
            }
        }

        if (!errors.isEmpty()) {
            request.setAttribute(WebConstants.REQUEST_ERRORS, errors);
            request.setAttribute(WebConstants.REQUEST_RESERVATION_FORM, form);
            SessionManager.setAttribute(request, WebConstants.SESSION_RESERVATION_FORM, form);
            loadReferenceData(request);
            RequestDispatcher dispatcher = request.getRequestDispatcher(WebConstants.VIEW_PUBLIC_RESERVATION_FORM);
            dispatcher.forward(request, response);
            return;
        }

        ReservationSummaryView summary = new ReservationSummaryView();
        summary.setCategory(category);
        summary.setPickupHeadquarters(pickupHeadquarters);
        summary.setReturnHeadquarters(returnHeadquarters);
        summary.setPickupDateTime(pickupDateTime);
        summary.setReturnDateTime(returnDateTime);
        summary.setPickupDateFormatted(DISPLAY_FORMATTER.format(pickupDateTime));
        summary.setReturnDateFormatted(DISPLAY_FORMATTER.format(returnDateTime));

        SessionManager.setAttribute(request, WebConstants.SESSION_RESERVATION_FORM, form);
        SessionManager.setAttribute(request, WebConstants.SESSION_RESERVATION_SUMMARY, summary);

        request.setAttribute(WebConstants.REQUEST_RESERVATION_FORM, form);
        request.setAttribute(WebConstants.REQUEST_RESERVATION_SUMMARY, summary);

        RequestDispatcher dispatcher = request.getRequestDispatcher(WebConstants.VIEW_PUBLIC_RESERVATION_SUMMARY);
        dispatcher.forward(request, response);
    }

    private ReservationForm buildFormFromRequest(HttpServletRequest request) {
        ReservationForm form = new ReservationForm();
        form.setCategoryId(clean(request.getParameter(WebConstants.PARAM_CATEGORY_ID)));
        form.setPickupHeadquartersId(clean(request.getParameter(WebConstants.PARAM_PICKUP_HEADQUARTERS)));
        form.setReturnHeadquartersId(clean(request.getParameter(WebConstants.PARAM_RETURN_HEADQUARTERS)));
        form.setStartDate(clean(request.getParameter(WebConstants.PARAM_START_DATE)));
        form.setEndDate(clean(request.getParameter(WebConstants.PARAM_END_DATE)));
        return form;
    }

    private LocalDateTime parseDateTime(String value, String field, String requiredKey, Map<String, String> errors) {
        if (value == null || value.trim().isEmpty()) {
            errors.put(field, requiredKey);
            return null;
        }
        try {
            return LocalDateTime.parse(value.trim(), INPUT_FORMATTER);
        } catch (DateTimeParseException ex) {
            logger.debug("Invalid date received for {}: {}", field, value, ex);
            errors.put(field, "reservation.form.error.datetime.format");
            return null;
        }
    }

    private Integer parseInteger(String value, String field, String requiredKey, Map<String, String> errors) {
        if (value == null || value.trim().isEmpty()) {
            errors.put(field, requiredKey);
            return null;
        }
        try {
            return Integer.valueOf(value.trim());
        } catch (NumberFormatException ex) {
            logger.debug("Invalid numeric value received for {}: {}", field, value, ex);
            errors.put(field, "reservation.form.error.numeric");
            return null;
        }
    }

    private void loadReferenceData(HttpServletRequest request) throws ServletException {
        String locale = resolveLocale(request);
        CatalogLoader.loadCategories(request, vehicleCategoryService, locale);
        CatalogLoader.loadHeadquarters(request, headquartersService);
    }

    private String resolveLocale(HttpServletRequest request) {
        Object localeAttr = SessionManager.getAttribute(request, "locale");
        if (localeAttr instanceof String) {
            String value = ((String) localeAttr).trim();
            if (!value.isEmpty()) {
                return value;
            }
        }
        return Locale.getDefault().getLanguage();
    }

    private String clean(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
