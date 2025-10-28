package com.pinguela.rentexpres.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.dao.RoleDAO;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.RoleDTO;

public class RoleDAOImpl implements RoleDAO {

    private static final Logger logger = LogManager.getLogger(RoleDAOImpl.class);

    private static final String BASE_SELECT = "SELECT r.role_id AS role_id, "
            + "       r.role_name AS role_name "
            + "FROM role r";

    @Override
    public RoleDTO findById(Connection connection, Integer id) throws DataException {
        final String method = "findById";
        if (id == null) {
            logger.warn("[{}] called with null id", method);
            return null;
        }
        String sql = BASE_SELECT + " WHERE r.role_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id.intValue());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    logger.info("[{}] Role found with id: {}", method, id);
                    return loadRole(rs);
                }
            }
            logger.warn("[{}] No role found with id: {}", method, id);
        } catch (SQLException e) {
            logger.error("[{}] Error finding role by id: {}", method, id, e);
            throw new DataException(e);
        }
        return null;
    }

    @Override
    public List<RoleDTO> findAll(Connection connection) throws DataException {
        final String method = "findAll";
        List<RoleDTO> roles = new ArrayList<RoleDTO>();
        String sql = BASE_SELECT + " ORDER BY r.role_id";
        try (PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                roles.add(loadRole(rs));
            }
            logger.info("[{}] Total roles found: {}", method, Integer.valueOf(roles.size()));
        } catch (SQLException e) {
            logger.error("[{}] Error retrieving all roles", method, e);
            throw new DataException(e);
        }
        return roles;
    }

    private RoleDTO loadRole(ResultSet rs) throws SQLException {
        RoleDTO dto = new RoleDTO();
        dto.setRoleId(Integer.valueOf(rs.getInt("role_id")));
        dto.setRoleName(rs.getString("role_name"));
        return dto;
    }
}
