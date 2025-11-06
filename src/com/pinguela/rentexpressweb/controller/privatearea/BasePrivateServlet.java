package com.pinguela.rentexpressweb.controller.privatearea;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.util.WebConstants;

public abstract class BasePrivateServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void applyNoCache(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
    }

    protected HttpSession requireAuthenticatedSession(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(WebConstants.SESSION_USER) == null) {
            response.sendRedirect(request.getContextPath() + WebConstants.URL_LOGIN);
            return null;
        }
        return session;
    }

    protected Integer parseInteger(String value) {
        if (value == null) {
            return null;
        }
        String cleaned = value.trim();
        if (cleaned.isEmpty()) {
            return null;
        }
        try {
            return Integer.valueOf(cleaned);
        } catch (NumberFormatException ex) {
            getLogger().debug("Invalid integer value received: {}", value, ex);
            return null;
        }
    }

    protected BigDecimal parseBigDecimal(String value) {
        if (value == null) {
            return null;
        }
        String cleaned = value.trim();
        if (cleaned.isEmpty()) {
            return null;
        }
        try {
            return new BigDecimal(cleaned);
        } catch (NumberFormatException ex) {
            getLogger().debug("Invalid decimal value received: {}", value, ex);
            return null;
        }
    }

    protected String resolveLocale(HttpSession session) {
        if (session == null) {
            return Locale.getDefault().getLanguage();
        }
        Object localeAttr = session.getAttribute("locale");
        if (localeAttr instanceof String) {
            String value = ((String) localeAttr).trim();
            if (!value.isEmpty()) {
                return value;
            }
        }
        return Locale.getDefault().getLanguage();
    }

    protected void handleFlashMessages(HttpSession session, HttpServletRequest request) {
        if (session == null) {
            return;
        }
        Object success = session.getAttribute(WebConstants.SESSION_FLASH_SUCCESS);
        if (success != null) {
            request.setAttribute(WebConstants.SESSION_FLASH_SUCCESS, success);
            session.removeAttribute(WebConstants.SESSION_FLASH_SUCCESS);
        }
        @SuppressWarnings("unchecked")
        Map<String, String> flashErrors = (Map<String, String>) session.getAttribute(WebConstants.SESSION_FLASH_ERRORS);
        if (flashErrors != null && !flashErrors.isEmpty()) {
            request.setAttribute(WebConstants.REQUEST_ERRORS, new LinkedHashMap<String, String>(flashErrors));
            session.removeAttribute(WebConstants.SESSION_FLASH_ERRORS);
        }
    }

    protected Logger getLogger() {
        return LogManager.getLogger(getClass());
    }
}
