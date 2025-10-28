package com.pinguela.rentexpres.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.dao.RentalStatusDAO;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.RentalStatusDTO;

public class RentalStatusDAOImpl implements RentalStatusDAO {

    private static final Logger logger = LogManager.getLogger(RentalStatusDAOImpl.class);

    private static final String BASE_SELECT = "SELECT rs.rental_status_id AS rental_status_id, "
            + "       rs.status_name AS status_name "
            + "FROM rental_status rs";

    private static final String I18N_SELECT = "SELECT rs.rental_status_id AS rental_status_id, "
            + "       COALESCE(rsl.translated_name, rs.status_name) AS status_name "
            + "FROM rental_status rs "
            + "LEFT JOIN language l ON l.iso_code = ? "
            + "LEFT JOIN rental_status_language rsl "
            + "       ON rsl.rental_status_id = rs.rental_status_id AND rsl.language_id = l.language_id";

    @Override
    public RentalStatusDTO findById(Connection connection, Integer id, String isoCode) throws DataException {
        final String method = "findById";
        if (id == null) {
            logger.warn("[{}] called with null id", method);
            return null;
        }
        String normalizedIso = normalizeIso(isoCode);
        StringBuilder sql = new StringBuilder(normalizedIso != null ? I18N_SELECT : BASE_SELECT);
        sql.append(" WHERE rs.rental_status_id = ?");
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
            logger.warn("[{}] No rental status found id {}", method, id);
        } catch (SQLException e) {
            logger.error("[{}] Error finding rental status id {}", method, id, e);
            throw new DataException(e);
        }
        return null;
    }

    @Override
    public List<RentalStatusDTO> findAll(Connection connection, String isoCode) throws DataException {
        final String method = "findAll";
        String normalizedIso = normalizeIso(isoCode);
        StringBuilder sql = new StringBuilder(normalizedIso != null ? I18N_SELECT : BASE_SELECT);
        sql.append(" ORDER BY rs.rental_status_id");
        List<RentalStatusDTO> results = new ArrayList<RentalStatusDTO>();
        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            if (normalizedIso != null) {
                ps.setString(1, normalizedIso);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(load(rs));
                }
            }
            logger.info("[{}] {} rental statuses found", method, Integer.valueOf(results.size()));
        } catch (SQLException e) {
            logger.error("[{}] Error listing rental statuses", method, e);
            throw new DataException(e);
        }
        return results;
    }

    private RentalStatusDTO load(ResultSet rs) throws SQLException {
        RentalStatusDTO dto = new RentalStatusDTO();
        dto.setRentalStatusId(Integer.valueOf(rs.getInt("rental_status_id")));
        dto.setStatusName(rs.getString("status_name"));
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
