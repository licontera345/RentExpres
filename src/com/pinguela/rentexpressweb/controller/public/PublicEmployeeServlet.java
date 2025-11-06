package com.pinguela.rentexpressweb.controller.public;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.EmployeeDTO;
import com.pinguela.rentexpres.model.HeadquartersDTO;
import com.pinguela.rentexpres.model.RoleDTO;
import com.pinguela.rentexpres.service.EmployeeService;
import com.pinguela.rentexpres.service.HeadquartersService;
import com.pinguela.rentexpres.service.RoleService;
import com.pinguela.rentexpres.service.impl.EmployeeServiceImpl;
import com.pinguela.rentexpres.service.impl.HeadquartersServiceImpl;
import com.pinguela.rentexpres.service.impl.RoleServiceImpl;
import com.pinguela.rentexpressweb.constants.AppConstants;
import com.pinguela.rentexpressweb.util.MessageResolver;
import com.pinguela.rentexpressweb.util.Views;

@WebServlet("/public/employees/register")
public class PublicEmployeeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LogManager.getLogger(PublicEmployeeServlet.class);
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private static final int MIN_PASSWORD_LENGTH = 6;

    private final EmployeeService employeeService;
    private final HeadquartersService headquartersService;
    private final RoleService roleService;

    public PublicEmployeeServlet() {
        this(new EmployeeServiceImpl(), new HeadquartersServiceImpl(), new RoleServiceImpl());
    }

    PublicEmployeeServlet(EmployeeService employeeService, HeadquartersService headquartersService,
            RoleService roleService) {
        this.employeeService = employeeService;
        this.headquartersService = headquartersService;
        this.roleService = roleService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        configure(request, response);
        ensureFormData(request);
        loadReferenceData(request);
        forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        configure(request, response);

        Map<String, String> errors = new LinkedHashMap<String, String>();
        Map<String, String> formData = captureFormData(request);

        validateForm(request, formData, errors);

        if (!errors.isEmpty()) {
            clearSensitiveFields(formData);
            request.setAttribute(AppConstants.ATTR_FORM_DATA, formData);
            request.setAttribute(AppConstants.ATTR_ERRORS, errors);
            loadReferenceData(request);
            forward(request, response);
            return;
        }

        if (!registerEmployee(request, formData, errors)) {
            clearSensitiveFields(formData);
            request.setAttribute(AppConstants.ATTR_FORM_DATA, formData);
            request.setAttribute(AppConstants.ATTR_ERRORS, errors);
            loadReferenceData(request);
            forward(request, response);
            return;
        }

        request.setAttribute(AppConstants.ATTR_FORM_DATA, new HashMap<String, String>());
        request.setAttribute(AppConstants.ATTR_ERRORS, new HashMap<String, String>());
        request.setAttribute(AppConstants.ATTR_FLASH_SUCCESS,
                MessageResolver.getMessage(request, "register.employee.flash.success"));
        loadReferenceData(request);
        forward(request, response);
    }

    private void configure(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
    }

    private void ensureFormData(HttpServletRequest request) {
        Object formAttr = request.getAttribute(AppConstants.ATTR_FORM_DATA);
        if (!(formAttr instanceof Map)) {
            request.setAttribute(AppConstants.ATTR_FORM_DATA, new HashMap<String, String>());
        }
        Object errorsAttr = request.getAttribute(AppConstants.ATTR_ERRORS);
        if (!(errorsAttr instanceof Map)) {
            request.setAttribute(AppConstants.ATTR_ERRORS, new HashMap<String, String>());
        }
    }

    private Map<String, String> captureFormData(HttpServletRequest request) {
        Map<String, String> formData = new LinkedHashMap<String, String>();
        formData.put("employeeName", param(request, "employeeName"));
        formData.put("firstName", param(request, "firstName"));
        formData.put("lastName1", param(request, "lastName1"));
        formData.put("lastName2", param(request, "lastName2"));
        formData.put("email", param(request, "email"));
        formData.put("phone", param(request, "phone"));
        formData.put("password", param(request, "password"));
        formData.put("roleId", param(request, "roleId"));
        formData.put("headquartersId", param(request, "headquartersId"));
        return formData;
    }

    private void validateForm(HttpServletRequest request, Map<String, String> formData, Map<String, String> errors) {
        requireValue(request, formData, "employeeName", "register.employee.error.employeeName", errors);
        requireValue(request, formData, "firstName", "register.employee.error.firstName", errors);
        requireValue(request, formData, "lastName1", "register.employee.error.lastName1", errors);

        String email = formData.get("email");
        if (email == null || email.isEmpty()) {
            errors.put("email", MessageResolver.getMessage(request, "register.employee.error.email"));
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            errors.put("email", MessageResolver.getMessage(request, "register.employee.error.email.format"));
        }

        String password = formData.get("password");
        if (password == null || password.isEmpty()) {
            errors.put("password", MessageResolver.getMessage(request, "register.employee.error.password"));
        } else if (password.length() < MIN_PASSWORD_LENGTH) {
            errors.put("password", MessageResolver.getMessage(request, "register.employee.error.password.length"));
        }

        if (isEmpty(formData.get("headquartersId"))) {
            errors.put("headquartersId", MessageResolver.getMessage(request, "register.employee.error.headquarters"));
        }

        if (isEmpty(formData.get("roleId"))) {
            errors.put("roleId", MessageResolver.getMessage(request, "register.employee.error.role"));
        }
    }

    private boolean registerEmployee(HttpServletRequest request, Map<String, String> formData, Map<String, String> errors) {
        Integer headquartersId = parseInteger(formData.get("headquartersId"));
        if (headquartersId == null) {
            errors.put("headquartersId", MessageResolver.getMessage(request, "register.employee.error.headquarters"));
            return false;
        }

        Integer roleId = parseInteger(formData.get("roleId"));
        if (roleId == null) {
            errors.put("roleId", MessageResolver.getMessage(request, "register.employee.error.role"));
            return false;
        }

        EmployeeDTO employee = new EmployeeDTO();
        employee.setEmployeeName(formData.get("employeeName"));
        employee.setFirstName(formData.get("firstName"));
        employee.setLastName1(formData.get("lastName1"));
        employee.setLastName2(formData.get("lastName2"));
        employee.setEmail(formData.get("email"));
        employee.setPhone(formData.get("phone"));
        employee.setPassword(formData.get("password"));
        employee.setHeadquartersId(headquartersId);
        employee.setRoleId(roleId);
        employee.setActiveStatus(Boolean.TRUE);

        try {
            boolean created = employeeService.create(employee);
            if (!created) {
                LOGGER.warn("Employee service returned false when creating {}", employee.getEmployeeName());
                setGeneralError(request, errors);
                return false;
            }
            return true;
        } catch (RentexpresException ex) {
            LOGGER.error("Error creating employee {}", employee.getEmployeeName(), ex);
            setGeneralError(request, errors);
            return false;
        }
    }

    private void loadReferenceData(HttpServletRequest request) {
        List<HeadquartersDTO> headquarters = new ArrayList<HeadquartersDTO>();
        List<RoleDTO> roles = new ArrayList<RoleDTO>();

        try {
            List<HeadquartersDTO> list = headquartersService.findAll();
            if (list != null) {
                headquarters = list;
            }
        } catch (RentexpresException ex) {
            LOGGER.error("Error loading headquarters list", ex);
        }

        try {
            List<RoleDTO> list = roleService.findAll();
            if (list != null) {
                roles = list;
            }
        } catch (RentexpresException ex) {
            LOGGER.error("Error loading roles list", ex);
        }

        request.setAttribute("headquarters", headquarters);
        request.setAttribute("roles", roles);
    }

    private void forward(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(Views.PUBLIC_REGISTER_EMPLOYEE).forward(request, response);
    }

    private void requireValue(HttpServletRequest request, Map<String, String> formData, String key, String messageKey,
            Map<String, String> errors) {
        String value = formData.get(key);
        if (isEmpty(value)) {
            errors.put(key, MessageResolver.getMessage(request, messageKey));
        }
    }

    private String param(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return value != null ? value.trim() : null;
    }

    private Integer parseInteger(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.valueOf(value.trim());
        } catch (NumberFormatException ex) {
            LOGGER.debug("Invalid integer value received: {}", value, ex);
            return null;
        }
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void setGeneralError(HttpServletRequest request, Map<String, String> errors) {
        String message = MessageResolver.getMessage(request, "register.employee.error.general");
        errors.put("generalError", message);
        request.setAttribute(AppConstants.ATTR_FLASH_ERROR, message);
    }

    private void clearSensitiveFields(Map<String, String> formData) {
        if (formData != null) {
            formData.put("password", "");
        }
    }
}
