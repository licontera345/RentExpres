package com.pinguela.rentexpres.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.dao.VehicleCategoryDAO;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.VehicleCategoryDTO;

public class VehicleCategoryDAOImpl implements VehicleCategoryDAO {

    private static final Logger logger = LogManager.getLogger(VehicleCategoryDAOImpl.class);

    private static final String BASE_SELECT = "SELECT vc.category_id AS category_id, "
            + "       vc.category_name AS category_name "
            + "FROM vehicle_category vc";

    private static final String I18N_SELECT = "SELECT vc.category_id AS category_id, "
            + "       COALESCE(vcl.translated_name, vc.category_name) AS category_name "
            + "FROM vehicle_category vc "
            + "LEFT JOIN language l ON l.iso_code = ? "
            + "LEFT JOIN vehicle_category_language vcl "
            + "       ON vcl.category_id = vc.category_id AND vcl.language_id = l.language_id";

    @Override
    public VehicleCategoryDTO findById(Connection connection, Integer id, String isoCode) throws DataException {
        final String method = "findById";
        if (id == null) {
            logger.warn("[{}] called with null id", method);
            return null;
        }
        String normalizedIso = normalizeIso(isoCode);
        StringBuilder sql = new StringBuilder(normalizedIso != null ? I18N_SELECT : BASE_SELECT);
        sql.append(" WHERE vc.category_id = ?");
        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int idx = 1;
            if (normalizedIso != null) {
                ps.setString(idx++, normalizedIso);
            }
            ps.setInt(idx, id.intValue());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return load(rs);
                }
            }
            logger.warn("[{}] No vehicle category found id {}", method, id);
        } catch (SQLException e) {
            logger.error("[{}] Error finding vehicle category id {}", method, id, e);
            throw new DataException(e);
        }
        return null;
    }

    @Override
    public List<VehicleCategoryDTO> findAll(Connection connection, String isoCode) throws DataException {
        final String method = "findAll";
        String normalizedIso = normalizeIso(isoCode);
        StringBuilder sql = new StringBuilder(normalizedIso != null ? I18N_SELECT : BASE_SELECT);
        sql.append(" ORDER BY vc.category_id");
        List<VehicleCategoryDTO> results = new ArrayList<VehicleCategoryDTO>();
        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            if (normalizedIso != null) {
                ps.setString(1, normalizedIso);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(load(rs));
                }
            }
            logger.info("[{}] {} vehicle categories found", method, Integer.valueOf(results.size()));
        } catch (SQLException e) {
            logger.error("[{}] Error listing vehicle categories", method, e);
            throw new DataException(e);
        }
        return results;
    }

    private VehicleCategoryDTO load(ResultSet rs) throws SQLException {
        VehicleCategoryDTO dto = new VehicleCategoryDTO();
        dto.setCategoryId(Integer.valueOf(rs.getInt("category_id")));
        dto.setCategoryName(rs.getString("category_name"));
        return dto;
    }

    private String normalizeIso(String iso) {
        if (iso == null) {
            return null;
        }
        String trimmed = iso.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        int dash = trimmed.indexOf('-');
        String base = dash > 0 ? trimmed.substring(0, dash) : trimmed;
        return base.toLowerCase();
    }
}
