package com.pinguela.rentexpressweb.controller.privatearea;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.EmployeeCriteria;
import com.pinguela.rentexpres.model.EmployeeDTO;
import com.pinguela.rentexpres.model.HeadquartersDTO;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.model.RoleDTO;
import com.pinguela.rentexpres.service.EmployeeService;
import com.pinguela.rentexpres.service.HeadquartersService;
import com.pinguela.rentexpres.service.RoleService;
import com.pinguela.rentexpres.service.impl.EmployeeServiceImpl;
import com.pinguela.rentexpres.service.impl.HeadquartersServiceImpl;
import com.pinguela.rentexpres.service.impl.RoleServiceImpl;
import com.pinguela.rentexpres.util.WebConstants;

@WebServlet(urlPatterns = WebConstants.URL_PRIVATE_EMPLOYEE_LIST)
public class PrivateEmployeeServlet extends BasePrivateServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LogManager.getLogger(PrivateEmployeeServlet.class);

    private static final String PARAM_EMPLOYEE_NAME = "employeeName";
    private static final String PARAM_FIRST_NAME = "firstName";
    private static final String PARAM_LAST_NAME1 = "lastName1";
    private static final String PARAM_LAST_NAME2 = "lastName2";
    private static final String PARAM_EMAIL = "email";
    private static final String PARAM_PHONE = "phone";
    private static final String PARAM_ROLE_ID = "roleId";
    private static final String PARAM_HEADQUARTERS_ID = "headquartersId";
    private static final String PARAM_ACTIVE_STATUS = "activeStatus";
    private static final String PARAM_PAGE = "page";
    private static final String PARAM_PAGE_SIZE = "pageSize";
    private static final String PARAM_ORDER_BY = "orderBy";
    private static final String PARAM_ORDER_DIR = "orderDir";

    private static final String ATTR_CRITERIA = WebConstants.REQUEST_EMPLOYEE_CRITERIA;
    private static final String ATTR_EMPLOYEES = WebConstants.REQUEST_EMPLOYEES;
    private static final String ATTR_TOTAL = "total";
    private static final String ATTR_TOTAL_PAGES = "totalPages";
    private static final String ATTR_CURRENT_PAGE = "currentPage";
    private static final String ATTR_HAS_PREV = "hasPrev";
    private static final String ATTR_HAS_NEXT = "hasNext";
    private static final String ATTR_FROM_ROW = "fromRow";
    private static final String ATTR_TO_ROW = "toRow";
    private static final String ATTR_ACTIVE_FILTER = "activeFilter";

    private static final List<Integer> PAGE_SIZES = Collections.unmodifiableList(Arrays.asList(10, 20, 25, 50, 100));

    private transient EmployeeService employeeService;
    private transient HeadquartersService headquartersService;
    private transient RoleService roleService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.employeeService = new EmployeeServiceImpl();
        this.headquartersService = new HeadquartersServiceImpl();
        this.roleService = new RoleServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        applyNoCache(response);

        HttpSession session = requireAuthenticatedSession(request, response);
        if (session == null) {
            return;
        }

        handleFlashMessages(session, request);

        EmployeeCriteria criteria = buildCriteria(request);

        Results<EmployeeDTO> results;
        try {
            results = employeeService.findBy(criteria, criteria.getSafePage(), criteria.getSafePageSize());
        } catch (RentexpresException e) {
            LOGGER.error("Error retrieving employee list", e);
            throw new ServletException("Unable to retrieve employee list", e);
        }

        if (results == null) {
            results = new Results<EmployeeDTO>();
            results.setItems(Collections.<EmployeeDTO>emptyList());
            results.setPage(criteria.getSafePage());
            results.setPageSize(criteria.getSafePageSize());
            results.setTotal(0);
        }

        results.normalize();

        request.setAttribute(ATTR_EMPLOYEES, results.getItems());
        request.setAttribute(ATTR_TOTAL, Integer.valueOf(results.getTotal()));
        request.setAttribute(ATTR_TOTAL_PAGES, Integer.valueOf(results.getTotalPages()));
        request.setAttribute(ATTR_CURRENT_PAGE, Integer.valueOf(results.getPage()));
        request.setAttribute(ATTR_HAS_PREV, Boolean.valueOf(results.isHasPrev()));
        request.setAttribute(ATTR_HAS_NEXT, Boolean.valueOf(results.isHasNext()));
        request.setAttribute(ATTR_FROM_ROW, Integer.valueOf(results.getFromRow()));
        request.setAttribute(ATTR_TO_ROW, Integer.valueOf(results.getToRow()));
        request.setAttribute(ATTR_CRITERIA, criteria);
        request.setAttribute(ATTR_ACTIVE_FILTER, resolveActiveFilter(criteria.getActiveStatus()));
        request.setAttribute(WebConstants.REQUEST_PAGE_SIZES, PAGE_SIZES);

        loadReferenceData(request);

        RequestDispatcher dispatcher = request.getRequestDispatcher(WebConstants.VIEW_PRIVATE_EMPLOYEE_MANAGEMENT);
        dispatcher.forward(request, response);
    }

    private EmployeeCriteria buildCriteria(HttpServletRequest request) {
        EmployeeCriteria criteria = new EmployeeCriteria();
        criteria.setEmployeeName(clean(request.getParameter(PARAM_EMPLOYEE_NAME)));
        criteria.setFirstName(clean(request.getParameter(PARAM_FIRST_NAME)));
        criteria.setLastName1(clean(request.getParameter(PARAM_LAST_NAME1)));
        criteria.setLastName2(clean(request.getParameter(PARAM_LAST_NAME2)));
        criteria.setEmail(clean(request.getParameter(PARAM_EMAIL)));
        criteria.setPhone(clean(request.getParameter(PARAM_PHONE)));
        criteria.setRoleId(parseInteger(request.getParameter(PARAM_ROLE_ID)));
        criteria.setHeadquartersId(parseInteger(request.getParameter(PARAM_HEADQUARTERS_ID)));
        criteria.setOrderBy(clean(request.getParameter(PARAM_ORDER_BY)));
        criteria.setOrderDir(clean(request.getParameter(PARAM_ORDER_DIR)));

        String activeParam = request.getParameter(PARAM_ACTIVE_STATUS);
        if (activeParam != null) {
            String value = activeParam.trim();
            if (!value.isEmpty()) {
                if ("active".equalsIgnoreCase(value) || "1".equals(value)) {
                    criteria.setActiveStatus(Boolean.TRUE);
                } else if ("inactive".equalsIgnoreCase(value) || "0".equals(value)) {
                    criteria.setActiveStatus(Boolean.FALSE);
                }
            }
        }

        Integer page = parseInteger(request.getParameter(PARAM_PAGE));
        if (page == null || page.intValue() < 1) {
            page = Integer.valueOf(1);
        }
        criteria.setPageNumber(page);

        Integer pageSize = parseInteger(request.getParameter(PARAM_PAGE_SIZE));
        if (pageSize == null) {
            pageSize = Integer.valueOf(20);
        }
        criteria.setPageSize(pageSize);

        criteria.normalize();
        return criteria;
    }

    private void loadReferenceData(HttpServletRequest request) throws ServletException {
        try {
            List<HeadquartersDTO> headquarters = headquartersService.findAll();
            request.setAttribute(WebConstants.REQUEST_HEADQUARTERS, headquarters);
        } catch (DataException e) {
            LOGGER.error("Error loading headquarters list", e);
            throw new ServletException("Unable to load headquarters", e);
        }

        try {
            List<RoleDTO> roles = roleService.findAll();
            request.setAttribute(WebConstants.REQUEST_ROLES, roles);
        } catch (RentexpresException e) {
            LOGGER.error("Error loading role list", e);
            throw new ServletException("Unable to load roles", e);
        }
    }

    private String clean(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String resolveActiveFilter(Boolean activeStatus) {
        if (activeStatus == null) {
            return "";
        }
        return Boolean.TRUE.equals(activeStatus) ? "active" : "inactive";
    }
}
