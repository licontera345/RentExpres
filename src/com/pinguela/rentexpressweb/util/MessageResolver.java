package com.pinguela.rentexpressweb.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpressweb.constants.AppConstants;

public final class MessageResolver {

    private static final Logger LOGGER = LogManager.getLogger(MessageResolver.class);
    private static final String BASE_BUNDLE_PATH = "/WEB-INF/i18n/messages";
    private static final String PROPERTIES_SUFFIX = ".properties";

    private MessageResolver() {
        // Utility class
    }

    public static String getMessage(HttpServletRequest request, String key) {
        if (key == null) {
            return "";
        }
        Properties properties = loadBundle(request);
        String value = properties.getProperty(key);
        return value != null ? value : key;
    }

    private static Properties loadBundle(HttpServletRequest request) {
        Properties properties = new Properties();
        ServletContext context = request.getServletContext();
        if (context == null) {
            LOGGER.warn("ServletContext not available when resolving messages");
            return properties;
        }

        Locale locale = resolveLocale(request);
        loadProperties(context, properties, BASE_BUNDLE_PATH + PROPERTIES_SUFFIX);

        if (locale != null && locale.getLanguage() != null && !locale.getLanguage().isEmpty()) {
            String languagePath = BASE_BUNDLE_PATH + "_" + locale.getLanguage() + PROPERTIES_SUFFIX;
            loadProperties(context, properties, languagePath);
        }
        return properties;
    }

    private static void loadProperties(ServletContext context, Properties properties, String path) {
        try (InputStream stream = context.getResourceAsStream(path)) {
            if (stream != null) {
                Properties loaded = new Properties();
                loaded.load(stream);
                properties.putAll(loaded);
            } else {
                LOGGER.debug("Resource bundle path {} not found", path);
            }
        } catch (IOException ex) {
            LOGGER.error("Error loading resource bundle from {}", path, ex);
        }
    }

    private static Locale resolveLocale(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String language = AppConstants.DEFAULT_LANGUAGE;
        if (session != null) {
            Object attribute = session.getAttribute(AppConstants.ATTR_LANGUAGE);
            if (attribute instanceof String && !((String) attribute).trim().isEmpty()) {
                language = ((String) attribute).trim();
            }
        }
        return Locale.forLanguageTag(language);
    }
}
