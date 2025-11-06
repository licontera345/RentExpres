package com.pinguela.rentexpressweb.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import jakarta.servlet.http.HttpServletRequest;

public final class MessageResolver {

    private static final String BUNDLE_BASE_NAME = "i18n.messages";

    private MessageResolver() {
        // Utility class
    }

    public static String getMessage(HttpServletRequest request, String key) {
        if (key == null) {
            return "";
        }

        Locale locale = request != null ? request.getLocale() : Locale.getDefault();
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_BASE_NAME, locale);
            if (bundle.containsKey(key)) {
                return bundle.getString(key);
            }
        } catch (MissingResourceException ex) {
            // Fallback below
        }
        return key;
    }
}
