package com.pinguela.rentexpressweb.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public final class SessionManager {

    private SessionManager() {
        // Utility class
    }

    public static HttpSession getSession(HttpServletRequest request) {
        return request.getSession(true);
    }

    public static HttpSession getExistingSession(HttpServletRequest request) {
        return request.getSession(false);
    }

    public static void setAttribute(HttpServletRequest request, String name, Object value) {
        HttpSession session = getSession(request);
        session.setAttribute(name, value);
    }

    public static Object getAttribute(HttpServletRequest request, String name) {
        HttpSession session = getExistingSession(request);
        if (session == null) {
            return null;
        }
        return session.getAttribute(name);
    }

    public static void removeAttribute(HttpServletRequest request, String name) {
        HttpSession session = getExistingSession(request);
        if (session != null) {
            session.removeAttribute(name);
        }
    }
}
