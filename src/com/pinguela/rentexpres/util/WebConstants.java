package com.pinguela.rentexpres.util;

public final class WebConstants {

    private WebConstants() {
        // Utility class
    }

    public static final String SESSION_USER = "currentUser";
    public static final String SESSION_RESERVATION_DRAFT = "reservationDraft";

    public static final String REQUEST_ERRORS = "error";
    public static final String REQUEST_VEHICLE = "vehicle";
    public static final String REQUEST_VEHICLE_IMAGES = "vehicleImages";
    public static final String REQUEST_RESERVATION = "reservation";

    public static final String PARAM_VEHICLE_ID = "vehicleId";

    public static final String URL_LOGIN = "/login";
    public static final String URL_PRIVATE_VEHICLE_LIST = "/private/vehicles";
    public static final String URL_PRIVATE_RESERVATION_START = "/private/reservations/start";
    public static final String URL_PRIVATE_RESERVATION_FORM = "/private/reservations/new";

    public static final String VIEW_PRIVATE_VEHICLE_DETAIL = "/private/vehicle/vehicle-detail.jsp";
    public static final String VIEW_PRIVATE_RESERVATION_START = "/private/reservation/reservation-start.jsp";

    public static final String MESSAGE_ERROR_VEHICLE_REQUIRED = "error.vehicle.required";
    public static final String MESSAGE_ERROR_VEHICLE_INVALID = "error.vehicle.invalid";
    public static final String MESSAGE_ERROR_VEHICLE_NOT_FOUND = "error.vehicle.notFound";
    public static final String MESSAGE_ERROR_RESERVATION_DRAFT = "error.reservation.draftMissing";
}
