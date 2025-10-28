package com.pinguela.rentexpres.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.dao.VehicleStatusDAO;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.VehicleStatusDTO;

public class VehicleStatusDAOImpl implements VehicleStatusDAO {

    private static final Logger logger = LogManager.getLogger(VehicleStatusDAOImpl.class);

    private static final String BASE_SELECT = "SELECT vs.vehicle_status_id AS vehicle_status_id, "
            + "       vs.status_name AS status_name "
            + "FROM vehicle_status vs";

    private static final String I18N_SELECT = "SELECT vs.vehicle_status_id AS vehicle_status_id, "
            + "       COALESCE(vsl.translated_name, vs.status_name) AS status_name "
            + "FROM vehicle_status vs "
            + "LEFT JOIN language l ON l.iso_code = ? "
            + "LEFT JOIN vehicle_status_language vsl "
            + "       ON vsl.vehicle_status_id = vs.vehicle_status_id AND vsl.language_id = l.language_id";

    @Override
    public VehicleStatusDTO findById(Connection connection, Integer id, String isoCode) throws DataException {
        final String method = "findById";
        if (id == null) {
            logger.warn("[{}] called with null id", method);
            return null;
        }
        String normalizedIso = normalizeIso(isoCode);
        StringBuilder sql = new StringBuilder(normalizedIso != null ? I18N_SELECT : BASE_SELECT);
        sql.append(" WHERE vs.vehicle_status_id = ?");
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
            logger.warn("[{}] No vehicle status found id {}", method, id);
        } catch (SQLException e) {
            logger.error("[{}] Error finding vehicle status id {}", method, id, e);
            throw new DataException(e);
        }
        return null;
    }

    @Override
    public List<VehicleStatusDTO> findAll(Connection connection, String isoCode) throws DataException {
        final String method = "findAll";
        String normalizedIso = normalizeIso(isoCode);
        StringBuilder sql = new StringBuilder(normalizedIso != null ? I18N_SELECT : BASE_SELECT);
        sql.append(" ORDER BY vs.vehicle_status_id");
        List<VehicleStatusDTO> results = new ArrayList<VehicleStatusDTO>();
        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            if (normalizedIso != null) {
                ps.setString(1, normalizedIso);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(load(rs));
                }
            }
            logger.info("[{}] {} vehicle statuses found", method, Integer.valueOf(results.size()));
        } catch (SQLException e) {
            logger.error("[{}] Error listing vehicle statuses", method, e);
            throw new DataException(e);
        }
        return results;
    }

    private VehicleStatusDTO load(ResultSet rs) throws SQLException {
        VehicleStatusDTO dto = new VehicleStatusDTO();
        dto.setVehicleStatusId(Integer.valueOf(rs.getInt("vehicle_status_id")));
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
