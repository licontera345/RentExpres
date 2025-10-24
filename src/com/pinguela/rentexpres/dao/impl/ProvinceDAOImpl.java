package com.pinguela.rentexpres.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.dao.ProvinceDAO;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.ProvinceDTO;

public class ProvinceDAOImpl implements ProvinceDAO {

    private static final Logger logger = LogManager.getLogger(ProvinceDAOImpl.class);

    private static final String BASE_SELECT = String.join(" ",
            "SELECT p.province_id AS province_id,",
            "       p.province_name AS province_name",
            "FROM province p");

    @Override
    public ProvinceDTO findById(Connection connection, Integer id) throws DataException {
        final String method = "findById";
        if (id == null) {
            logger.warn("[{}] called with null id", method);
            return null;
        }
        String sql = BASE_SELECT + " WHERE p.province_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id.intValue());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    logger.info("[{}] Province found with id: {}", method, id);
                    return loadProvince(rs);
                }
            }
            logger.warn("[{}] No province found with id: {}", method, id);
        } catch (SQLException e) {
            logger.error("[{}] Error finding province by id: {}", method, id, e);
            throw new DataException(e);
        }
        return null;
    }

    @Override
    public boolean create(Connection connection, ProvinceDTO province) throws DataException {
        final String method = "create";
        if (province == null) {
            logger.warn("[{}] called with null province", method);
            return false;
        }
        String sql = "INSERT INTO province (province_name) VALUES (?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, province.getProvinceName());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        province.setProvinceId(Integer.valueOf(keys.getInt(1)));
                    }
                }
                logger.info("[{}] Province created successfully: {}", method, province.getProvinceName());
                return true;
            }
            logger.warn("[{}] No province created", method);
            return false;
        } catch (SQLException e) {
            logger.error("[{}] Error creating province: {}", method, province.getProvinceName(), e);
            throw new DataException(e);
        }
    }

    @Override
    public boolean update(Connection connection, ProvinceDTO province) throws DataException {
        final String method = "update";
        if (province == null || province.getProvinceId() == null) {
            logger.warn("[{}] called with null province or id", method);
            return false;
        }
        String sql = "UPDATE province SET province_name = ? WHERE province_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, province.getProvinceName());
            ps.setInt(2, province.getProvinceId().intValue());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                logger.info("[{}] Province updated successfully, id: {}", method, province.getProvinceId());
                return true;
            }
            logger.warn("[{}] No province updated for id: {}", method, province.getProvinceId());
            return false;
        } catch (SQLException e) {
            logger.error("[{}] Error updating province id {}", method, province.getProvinceId(), e);
            throw new DataException(e);
        }
    }

    @Override
    public boolean delete(Connection connection, Integer id) throws DataException {
        final String method = "delete";
        if (id == null) {
            logger.warn("[{}] called with null id", method);
            return false;
        }
        String sql = "DELETE FROM province WHERE province_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id.intValue());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                logger.info("[{}] Province deleted, id: {}", method, id);
                return true;
            }
            logger.warn("[{}] No province deleted for id: {}", method, id);
            return false;
        } catch (SQLException e) {
            logger.error("[{}] Error deleting province id {}", method, id, e);
            throw new DataException(e);
        }
    }

    @Override
    public List<ProvinceDTO> findAll(Connection connection) throws DataException {
        final String method = "findAll";
        List<ProvinceDTO> provinces = new ArrayList<ProvinceDTO>();
        String sql = BASE_SELECT + " ORDER BY p.province_name";
        try (PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                provinces.add(loadProvince(rs));
            }
            logger.info("[{}] {} provinces found.", method, Integer.valueOf(provinces.size()));
        } catch (SQLException e) {
            logger.error("[{}] Error fetching all provinces", method, e);
            throw new DataException(e);
        }
        return provinces;
    }

    private ProvinceDTO loadProvince(ResultSet rs) throws SQLException {
        ProvinceDTO dto = new ProvinceDTO();
        dto.setProvinceId(Integer.valueOf(rs.getInt("province_id")));
        dto.setProvinceName(rs.getString("province_name"));
        return dto;
    }
}
