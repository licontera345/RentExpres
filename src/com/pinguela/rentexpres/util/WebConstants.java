package com.pinguela.rentexpres.util;

public final class WebConstants {

    private WebConstants() {
        // Utility class
    }

    public static final String SESSION_USER = "currentUser";
    public static final String SESSION_RESERVATION_DRAFT = "reservationDraft";
    public static final String SESSION_ROLE = "role";

    public static final String REQUEST_ERRORS = "error";
    public static final String REQUEST_VEHICLE = "vehicle";
    public static final String REQUEST_VEHICLE_IMAGES = "vehicleImages";
    public static final String REQUEST_VEHICLE_RESULTS = "vehicleResults";
    public static final String REQUEST_VEHICLE_CRITERIA = "vehicleCriteria";
    public static final String REQUEST_VEHICLE_FORM = "vehicleForm";
    public static final String REQUEST_VEHICLE_FORM_IMAGES = "vehicleFormImages";
    public static final String REQUEST_VEHICLE_CATEGORIES = "vehicleCategories";
    public static final String REQUEST_HEADQUARTERS = "headquarters";
    public static final String REQUEST_ROLES = "roles";
    public static final String REQUEST_EMPLOYEES = "employees";
    public static final String REQUEST_EMPLOYEE_CRITERIA = "employeeCriteria";
    public static final String REQUEST_RESERVATION = "reservation";
    public static final String REQUEST_RESERVATION_FORM = "reservationForm";
    public static final String REQUEST_RESERVATION_SUMMARY = "reservationSummary";
    public static final String REQUEST_PAGE_SIZES = "pageSizes";

    public static final String SESSION_FLASH_SUCCESS = "flashSuccess";
    public static final String SESSION_FLASH_ERRORS = "flashErrors";
    public static final String SESSION_RESERVATION_FORM = "reservationFormSession";
    public static final String SESSION_RESERVATION_SUMMARY = "reservationSummarySession";

    public static final String PARAM_VEHICLE_ID = "vehicleId";
    public static final String PARAM_ACTION = "action";
    public static final String PARAM_CATEGORY_ID = "categoryId";
    public static final String PARAM_START_DATE = "startDate";
    public static final String PARAM_END_DATE = "endDate";
    public static final String PARAM_PICKUP_HEADQUARTERS = "pickupHeadquartersId";
    public static final String PARAM_RETURN_HEADQUARTERS = "returnHeadquartersId";
    public static final String ACTION_CREATE = "create";
    public static final String ACTION_EDIT = "edit";

    public static final String URL_LOGIN = "/login";
    public static final String URL_PRIVATE_VEHICLE_LIST = "/private/vehicles";
    public static final String URL_PRIVATE_EMPLOYEE_LIST = "/private/employees";
    public static final String URL_PRIVATE_RESERVATION_START = "/private/reservations/start";
    public static final String URL_PRIVATE_RESERVATION_FORM = "/private/reservations/new";
    public static final String URL_PRIVATE_RESERVATION_CONFIRM = "/private/reservations/confirm";
    public static final String URL_PUBLIC_RESERVATION_FORM = "/public/reservations";
    public static final String URL_PUBLIC_RESERVATION_CONFIRM = "/public/reservations/confirm";

    public static final String VIEW_PRIVATE_VEHICLE_DETAIL = "/private/vehicle/vehicle-detail.jsp";
    public static final String VIEW_PRIVATE_VEHICLE_MANAGEMENT = "/private/vehicle/vehicle-management.jsp";
    public static final String VIEW_PRIVATE_EMPLOYEE_MANAGEMENT = "/private/employee/employee-management.jsp";
    public static final String VIEW_PRIVATE_RESERVATION_START = "/private/reservation/reservation-start.jsp";
    public static final String VIEW_PUBLIC_RESERVATION_FORM = "/public/reservation/reservation-form.jsp";
    public static final String VIEW_PUBLIC_RESERVATION_SUMMARY = "/public/reservation/reservation-summary.jsp";
    public static final String VIEW_PUBLIC_RESERVATION_CONFIRMATION = "/public/reservation/reservation-confirmation.jsp";

    public static final String MESSAGE_ERROR_VEHICLE_REQUIRED = "error.vehicle.required";
    public static final String MESSAGE_ERROR_VEHICLE_INVALID = "error.vehicle.invalid";
    public static final String MESSAGE_ERROR_VEHICLE_NOT_FOUND = "error.vehicle.notFound";
    public static final String MESSAGE_ERROR_RESERVATION_DRAFT = "error.reservation.draftMissing";
    public static final String MESSAGE_ERROR_RESERVATION_SUMMARY = "error.reservation.summaryMissing";

    public static final String MESSAGE_SUCCESS_VEHICLE_CREATED = "success.vehicle.created";
    public static final String MESSAGE_SUCCESS_VEHICLE_UPDATED = "success.vehicle.updated";
    public static final String MESSAGE_SUCCESS_VEHICLE_DELETED = "success.vehicle.deleted";
    public static final String MESSAGE_ERROR_VEHICLE_DELETE = "error.vehicle.delete";

    public static final int DEFAULT_RESERVATION_STATUS_PENDING = 1;
    public static final int DEFAULT_RESERVATION_EMPLOYEE_ID = 1;

    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_EMPLOYEE = "EMPLOYEE";
    public static final String ROLE_CLIENT = "CLIENT";
}
