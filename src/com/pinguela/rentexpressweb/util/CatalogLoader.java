package com.pinguela.rentexpressweb.util;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.HeadquartersDTO;
import com.pinguela.rentexpres.model.VehicleCategoryDTO;
import com.pinguela.rentexpres.service.HeadquartersService;
import com.pinguela.rentexpres.service.VehicleCategoryService;

public final class CatalogLoader {

    private CatalogLoader() {
        // Utility class
    }

    public static List<HeadquartersDTO> loadHeadquarters(HttpServletRequest request, HeadquartersService service)
            throws ServletException {
        try {
            List<HeadquartersDTO> headquarters = service.findAll();
            request.setAttribute(WebConstants.REQUEST_HEADQUARTERS, headquarters);
            return headquarters;
        } catch (DataException e) {
            throw new ServletException("Unable to load headquarters", e);
        }
    }

    public static List<VehicleCategoryDTO> loadCategories(HttpServletRequest request, VehicleCategoryService service,
            String locale) throws ServletException {
        try {
            List<VehicleCategoryDTO> categories = service.findAll(locale);
            request.setAttribute(WebConstants.REQUEST_VEHICLE_CATEGORIES, categories);
            return categories;
        } catch (RentexpresException e) {
            throw new ServletException("Unable to load vehicle categories", e);
        }
    }
}
