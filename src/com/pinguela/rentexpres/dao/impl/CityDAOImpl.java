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

import com.pinguela.rentexpres.dao.CityDAO;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.CityDTO;

public class CityDAOImpl implements CityDAO {

    private static final Logger logger = LogManager.getLogger(CityDAOImpl.class);

    private static final String BASE_SELECT = "SELECT c.city_id AS city_id, "
            + "       c.city_name AS city_name, "
            + "       c.province_id AS province_id "
            + "FROM city c";

    @Override
    public CityDTO findById(Connection connection, Integer id) throws DataException {
        final String method = "findById";
        if (id == null) {
            logger.warn("[{}] called with null id", method);
            return null;
        }
        String sql = BASE_SELECT + " WHERE c.city_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id.intValue());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    logger.info("[{}] City found with id: {}", method, id);
                    return loadCity(rs);
                }
            }
            logger.warn("[{}] No city found with id: {}", method, id);
        } catch (SQLException e) {
            logger.error("[{}] Error finding city by id: {}", method, id, e);
            throw new DataException(e);
        }
        return null;
    }

    @Override
    public List<CityDTO> findAll(Connection connection) throws DataException {
        final String method = "findAll";
        List<CityDTO> cities = new ArrayList<CityDTO>();
        String sql = BASE_SELECT + " ORDER BY c.city_name";
        try (PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                cities.add(loadCity(rs));
            }
            logger.info("[{}] {} cities found.", method, Integer.valueOf(cities.size()));
        } catch (SQLException e) {
            logger.error("[{}] Error listing cities", method, e);
            throw new DataException(e);
        }
        return cities;
    }

    @Override
    public List<CityDTO> findByProvinceId(Connection connection, Integer provinceId) throws DataException {
        final String method = "findByProvinceId";
        if (provinceId == null) {
            logger.warn("[{}] called with null province id", method);
            return new ArrayList<CityDTO>();
        }
        List<CityDTO> cities = new ArrayList<CityDTO>();
        String sql = BASE_SELECT + " WHERE c.province_id = ? ORDER BY c.city_name";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, provinceId.intValue());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    cities.add(loadCity(rs));
                }
            }
            logger.info("[{}] {} cities found for province id: {}", method, Integer.valueOf(cities.size()), provinceId);
        } catch (SQLException e) {
            logger.error("[{}] Error finding cities by province id: {}", method, provinceId, e);
            throw new DataException(e);
        }
        return cities;
    }

    @Override
    public boolean create(Connection connection, CityDTO city) throws DataException {
        final String method = "create";
        if (city == null) {
            logger.warn("[{}] called with null city", method);
            return false;
        }
        String sql = "INSERT INTO city (city_name, province_id) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setCityParameters(ps, city, false);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        city.setCityId(Integer.valueOf(keys.getInt(1)));
                    }
                }
                logger.info("[{}] City created successfully with id: {}", method, city.getCityId());
                return true;
            }
            logger.warn("[{}] No city created", method);
            return false;
        } catch (SQLException e) {
            logger.error("[{}] Error creating city", method, e);
            throw new DataException(e);
        }
    }

    @Override
    public boolean update(Connection connection, CityDTO city) throws DataException {
        final String method = "update";
        if (city == null || city.getCityId() == null) {
            logger.warn("[{}] called with null city or id", method);
            return false;
        }
        String sql = "UPDATE city SET city_name = ?, province_id = ? WHERE city_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            setCityParameters(ps, city, true);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                logger.info("[{}] City updated successfully (id: {})", method, city.getCityId());
                return true;
            }
            logger.warn("[{}] No city updated for id: {}", method, city.getCityId());
            return false;
        } catch (SQLException e) {
            logger.error("[{}] Error updating city id {}", method, city.getCityId(), e);
            throw new DataException(e);
        }
    }

    @Override
    public boolean delete(Connection connection, CityDTO city) throws DataException {
        final String method = "delete";
        if (city == null || city.getCityId() == null) {
            logger.warn("[{}] called with null city or id", method);
            return false;
        }
        String sql = "DELETE FROM city WHERE city_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, city.getCityId().intValue());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                logger.info("[{}] City deleted successfully (id: {})", method, city.getCityId());
                return true;
            }
            logger.warn("[{}] No city deleted for id: {}", method, city.getCityId());
            return false;
        } catch (SQLException e) {
            logger.error("[{}] Error deleting city id {}", method, city.getCityId(), e);
            throw new DataException(e);
        }
    }

    private CityDTO loadCity(ResultSet rs) throws SQLException {
        CityDTO dto = new CityDTO();
        dto.setCityId(Integer.valueOf(rs.getInt("city_id")));
        dto.setCityName(rs.getString("city_name"));
        dto.setProvinceId(Integer.valueOf(rs.getInt("province_id")));
        return dto;
    }

    private void setCityParameters(PreparedStatement ps, CityDTO city, boolean isUpdate) throws SQLException {
        ps.setString(1, city.getCityName());
        ps.setInt(2, city.getProvinceId().intValue());
        if (isUpdate) {
            ps.setInt(3, city.getCityId().intValue());
        }
    }
}
