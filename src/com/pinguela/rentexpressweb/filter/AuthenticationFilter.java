package com.pinguela.rentexpressweb.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.model.RoleDTO;
import com.pinguela.rentexpres.model.UserDTO;
import com.pinguela.rentexpres.util.WebConstants;

@WebFilter(urlPatterns = "/private/*", dispatcherTypes = { DispatcherType.REQUEST, DispatcherType.FORWARD })
public class AuthenticationFilter implements Filter {

    private static final Logger logger = LogManager.getLogger(AuthenticationFilter.class);
    private static final Set<String> ALLOWED_ROLES = Collections.unmodifiableSet(
            new HashSet<String>(Arrays.asList(WebConstants.ROLE_ADMIN, WebConstants.ROLE_EMPLOYEE)));

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Nothing to initialise
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);
        if (session == null) {
            logger.info("Access to {} denied: session does not exist", httpRequest.getRequestURI());
            redirectToLogin(httpRequest, httpResponse);
            return;
        }

        Object userAttr = session.getAttribute(WebConstants.SESSION_USER);
        if (!(userAttr instanceof UserDTO)) {
            logger.info("Access to {} denied: user not found in session", httpRequest.getRequestURI());
            redirectToLogin(httpRequest, httpResponse);
            return;
        }

        if (!hasRequiredRole(session, (UserDTO) userAttr)) {
            logger.warn("Access to {} denied: user lacks required role", httpRequest.getRequestURI());
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        applyNoCache(httpResponse);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Nothing to destroy
    }

    private boolean hasRequiredRole(HttpSession session, UserDTO user) {
        String sessionRole = getSessionRole(session);
        if (sessionRole != null && ALLOWED_ROLES.contains(sessionRole)) {
            return true;
        }

        RoleDTO role = user.getRole();
        if (role != null) {
            String roleName = normalise(role.getRoleName());
            if (roleName != null && ALLOWED_ROLES.contains(roleName)) {
                session.setAttribute(WebConstants.SESSION_ROLE, roleName);
                return true;
            }
        }

        Integer roleId = user.getRoleId();
        if (roleId != null) {
            switch (roleId.intValue()) {
            case 1:
                session.setAttribute(WebConstants.SESSION_ROLE, WebConstants.ROLE_ADMIN);
                return true;
            case 2:
                session.setAttribute(WebConstants.SESSION_ROLE, WebConstants.ROLE_EMPLOYEE);
                return true;
            default:
                break;
            }
        }
        return false;
    }

    private String getSessionRole(HttpSession session) {
        Object roleAttr = session.getAttribute(WebConstants.SESSION_ROLE);
        if (roleAttr instanceof String) {
            return normalise((String) roleAttr);
        }
        return null;
    }

    private String normalise(String value) {
        if (value == null) {
            return null;
        }
        return value.trim().toUpperCase(Locale.ROOT);
    }

    private void redirectToLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + WebConstants.URL_LOGIN);
    }

    private void applyNoCache(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
    }
}
