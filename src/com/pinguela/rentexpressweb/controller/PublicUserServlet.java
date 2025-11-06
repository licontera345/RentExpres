package com.pinguela.rentexpressweb.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.AddressDTO;
import com.pinguela.rentexpres.model.CityDTO;
import com.pinguela.rentexpres.model.ProvinceDTO;
import com.pinguela.rentexpres.model.UserDTO;
import com.pinguela.rentexpres.service.AddressService;
import com.pinguela.rentexpres.service.CityService;
import com.pinguela.rentexpres.service.ProvinceService;
import com.pinguela.rentexpres.service.UserService;
import com.pinguela.rentexpres.service.impl.AddressServiceImpl;
import com.pinguela.rentexpres.service.impl.CityServiceImpl;
import com.pinguela.rentexpres.service.impl.ProvinceServiceImpl;
import com.pinguela.rentexpres.service.impl.UserServiceImpl;
import com.pinguela.rentexpressweb.constants.AppConstants;
import com.pinguela.rentexpressweb.util.MessageResolver;
import com.pinguela.rentexpressweb.util.Views;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/public/users/register")
public class PublicUserServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LogManager.getLogger(PublicUserServlet.class);
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private final ProvinceService provinceService;
    private final CityService cityService;
    private final AddressService addressService;
    private final UserService userService;

    public PublicUserServlet() {
        this(new ProvinceServiceImpl(), new CityServiceImpl(), new AddressServiceImpl(), new UserServiceImpl());
    }

    PublicUserServlet(ProvinceService provinceService, CityService cityService,
                      AddressService addressService, UserService userService) {
        this.provinceService = provinceService;
        this.cityService = cityService;
        this.addressService = addressService;
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        configure(request, response);
        ensureFormData(request);
        loadReferenceData(request);
        forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        configure(request, response);

        Map<String, String> errors = new LinkedHashMap<>();
        Map<String, String> formData = captureFormData(request);

        validateForm(request, formData, errors);

        if (!errors.isEmpty()) {
            request.setAttribute("formData", formData);
            request.setAttribute("errors", errors);
            loadReferenceData(request);
            forward(request, response);
            return;
        }

        if (!registerUser(request, formData, errors)) {
            request.setAttribute("formData", formData);
            request.setAttribute("errors", errors);
            loadReferenceData(request);
            forward(request, response);
            return;
        }

        request.setAttribute("formData", new HashMap<String, String>());
        request.setAttribute("errors", new HashMap<String, String>());
        request.setAttribute("flashSuccess",
                MessageResolver.getMessage(request, "register.user.flash.success"));
        loadReferenceData(request);
        forward(request, response);
    }

    private void configure(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
    }

    private void ensureFormData(HttpServletRequest request) {
        if (!(request.getAttribute("formData") instanceof Map)) {
            request.setAttribute("formData", new HashMap<String, String>());
        }
        if (!(request.getAttribute("errors") instanceof Map)) {
            request.setAttribute("errors", new HashMap<String, String>());
        }
    }

    private Map<String, String> captureFormData(HttpServletRequest request) {
        Map<String, String> formData = new HashMap<>();
        formData.put("firstName", param(request, "firstName"));
        formData.put("lastName1", param(request, "lastName1"));
        formData.put("lastName2", param(request, "lastName2"));
        formData.put("email", param(request, "email"));
        formData.put("password", param(request, "password"));
        formData.put("phone", param(request, "phone"));
        formData.put("street", param(request, "street"));
        formData.put("postalCode", param(request, "postalCode"));
        formData.put("provinceId", param(request, "provinceId"));
        formData.put("cityId", param(request, "cityId"));
        return formData;
    }

    private void validateForm(HttpServletRequest request, Map<String, String> formData, Map<String, String> errors) {

        requireValue(request, formData, "firstName", "register.user.error.firstName", errors);
        requireValue(request, formData, "lastName1", "register.user.error.lastName1", errors);
        requireValue(request, formData, "street", "register.user.error.street", errors);
        requireValue(request, formData, "postalCode", "register.user.error.postalCode", errors);

        String email = formData.get("email");
        if (email == null || email.isEmpty()) {
            errors.put("email", MessageResolver.getMessage(request, "register.user.error.email"));
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            errors.put("email", MessageResolver.getMessage(request, "register.user.error.email.format"));
        }

        String password = formData.get("password");
        if (password == null || password.isEmpty()) {
            errors.put("password", MessageResolver.getMessage(request, "register.user.error.password"));
        } else if (password.length() < 6) {
            errors.put("password", MessageResolver.getMessage(request, "register.user.error.password.length"));
        }

        if (isEmpty(formData.get("provinceId"))) {
            errors.put("provinceId", MessageResolver.getMessage(request, "register.user.error.province"));
        }
        if (isEmpty(formData.get("cityId"))) {
            errors.put("cityId", MessageResolver.getMessage(request, "register.user.error.city"));
        }
    }

    private boolean registerUser(HttpServletRequest request, Map<String, String> formData, Map<String, String> errors) {
        Integer cityId = parseInteger(formData.get("cityId"));
        if (cityId == null) {
            errors.put("cityId", MessageResolver.getMessage(request, "register.user.error.city"));
            return false;
        }

        AddressDTO address = new AddressDTO();
        address.setCityId(cityId);
        address.setProvinceId(parseInteger(formData.get("provinceId")));
        address.setStreet(formData.get("street"));
        address.setNumber(formData.get("postalCode"));

        try {
            if (!addressService.create(address)) {
                LOGGER.error("No se pudo crear la dirección para el registro público");
                setGeneralError(request, errors);
                return false;
            }
        } catch (RentexpresException ex) {
            LOGGER.error("Error creando dirección", ex);
            setGeneralError(request, errors);
            return false;
        }

        UserDTO user = new UserDTO();
        user.setFirstName(formData.get("firstName"));
        user.setLastName1(formData.get("lastName1"));
        user.setLastName2(formData.get("lastName2"));
        user.setEmail(formData.get("email"));
        user.setPassword(formData.get("password"));
        user.setPhone(formData.get("phone"));
        user.setUsername(formData.get("email"));
        user.setRoleId(AppConstants.ROLE_CLIENT);
        user.setAddressId(address.getId());
        user.setActiveStatus(Boolean.TRUE);

        try {
            if (userService.create(user)) {
                return true;
            }
            LOGGER.warn("El servicio de usuarios no pudo completar el registro público");
            setGeneralError(request, errors);
        } catch (RentexpresException ex) {
            LOGGER.error("Error registrando usuario público", ex);
            setGeneralError(request, errors);
        }
        return false;
    }

    private void loadReferenceData(HttpServletRequest request) {
        List<ProvinceDTO> provinces = new ArrayList<>();
        List<CityDTO> cities = new ArrayList<>();

        try {
            List<ProvinceDTO> provinceList = provinceService.findAll();
            if (provinceList != null) {
                provinces = provinceList;
            }
        } catch (RentexpresException ex) {
            LOGGER.error("Error cargando provincias", ex);
        }

        try {
            List<CityDTO> cityList = cityService.findAll();
            if (cityList != null) {
                cities = cityList;
            }
        } catch (RentexpresException ex) {
            LOGGER.error("Error cargando ciudades", ex);
        }

        request.setAttribute("provinces", provinces);
        request.setAttribute("cities", cities);
    }

    private void forward(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(Views.PUBLIC_REGISTER_USER).forward(request, response);
    }

    private void requireValue(HttpServletRequest request, Map<String, String> formData, String key,
                              String messageKey, Map<String, String> errors) {
        String value = formData.get(key);
        if (isEmpty(value)) {
            errors.put(key, MessageResolver.getMessage(request, messageKey));
        }
    }

    private String param(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return (value != null) ? value.trim() : null;
    }

    private Integer parseInteger(String value) {
        try {
            return (value == null || value.isEmpty()) ? null : Integer.valueOf(value);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    private void setGeneralError(HttpServletRequest request, Map<String, String> errors) {
        String message = MessageResolver.getMessage(request, "register.user.error.general");
        errors.put("generalError", message);
        request.setAttribute("errorGeneral", message);
    }
}
