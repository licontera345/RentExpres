package com.pinguela.rentexpres.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.dao.AddressDAO;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.AddressDTO;

public class AddressDAOImpl implements AddressDAO {

        private static final Logger logger = LogManager.getLogger(AddressDAOImpl.class);

        private static final String BASE_SELECT = "SELECT a.address_id, a.city_id, a.street, a.number, "
                        + "       c.city_name, c.province_id, p.province_name "
                        + "FROM address a "
                        + "INNER JOIN city c ON a.city_id = c.city_id "
                        + "INNER JOIN province p ON c.province_id = p.province_id";

        @Override
        public AddressDTO findById(Connection connection, Integer id) throws DataException {
                final String method = "findById";
                if (id == null) {
                        logger.warn("[{}] called with null id", method);
                        return null;
                }
                String sql = BASE_SELECT + " WHERE a.address_id = ?";
                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                        ps.setInt(1, id.intValue());
                        try (ResultSet rs = ps.executeQuery()) {
                                if (rs.next()) {
                                        logger.info("[{}] Address found id {}", method, id);
                                        return toAddressDTO(rs);
                                }
                        }
                        logger.warn("[{}] No address found id {}", method, id);
                } catch (SQLException e) {
                        logger.error("[{}] Error finding address id {}", method, id, e);
                        throw new DataException("Error finding address by id", e);
                }
                return null;
        }

        @Override
        public boolean create(Connection connection, AddressDTO address) throws DataException {
                final String method = "create";
                if (address == null) {
                        logger.warn("[{}] called with null address", method);
                        return false;
                }
                String sql = "INSERT INTO address (city_id, street, number) VALUES (?, ?, ?)";
                try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                        ps.setInt(1, address.getCityId());
                        ps.setString(2, address.getStreet());
                        ps.setString(3, address.getNumber());
                        if (ps.executeUpdate() > 0) {
                                try (ResultSet keys = ps.getGeneratedKeys()) {
                                        if (keys.next()) {
                                                address.setAddressId(Integer.valueOf(keys.getInt(1)));
                                        }
                                }
                                logger.info("[{}] Address created id {}", method, address.getAddressId());
                                return true;
                        }
                } catch (SQLException e) {
                        logger.error("[{}] Error creating address", method, e);
                        throw new DataException("Error creating address", e);
                }
                return false;
        }

        @Override
        public boolean update(Connection connection, AddressDTO address) throws DataException {
                final String method = "update";
                if (address == null || address.getAddressId() == null) {
                        logger.warn("[{}] called with null address or id", method);
                        return false;
                }
                String sql = "UPDATE address SET city_id = ?, street = ?, number = ? WHERE address_id = ?";
                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                        ps.setInt(1, address.getCityId());
                        ps.setString(2, address.getStreet());
                        ps.setString(3, address.getNumber());
                        ps.setInt(4, address.getAddressId());
                        int updated = ps.executeUpdate();
                        if (updated > 0) {
                                logger.info("[{}] Address updated id {}", method, address.getAddressId());
                                return true;
                        }
                        logger.warn("[{}] No address updated id {}", method, address.getAddressId());
                } catch (SQLException e) {
                        logger.error("[{}] Error updating address id {}", method, address.getAddressId(), e);
                        throw new DataException("Error updating address", e);
                }
                return false;
        }

        @Override
        public boolean delete(Connection connection, AddressDTO address, Integer id) throws DataException {
                final String method = "delete";
                Integer targetId = id != null ? id : address != null ? address.getAddressId() : null;
                if (targetId == null) {
                        logger.warn("[{}] called with null id", method);
                        return false;
                }
                String sql = "DELETE FROM address WHERE address_id = ?";
                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                        ps.setInt(1, targetId.intValue());
                        int rows = ps.executeUpdate();
                        if (rows > 0) {
                                logger.info("[{}] Address deleted id {}", method, targetId);
                                return true;
                        }
                        logger.warn("[{}] No address deleted id {}", method, targetId);
                } catch (SQLException e) {
                        logger.error("[{}] Error deleting address id {}", method, targetId, e);
                        throw new DataException("Error deleting address", e);
                }
                return false;
        }

        private AddressDTO toAddressDTO(ResultSet rs) throws SQLException {
                AddressDTO dto = new AddressDTO();
                dto.setAddressId(Integer.valueOf(rs.getInt("address_id")));
                dto.setCityId(Integer.valueOf(rs.getInt("city_id")));
                dto.setStreet(rs.getString("street"));
                dto.setNumber(rs.getString("number"));
                dto.setCityName(rs.getString("city_name"));
                dto.setProvinceId(Integer.valueOf(rs.getInt("province_id")));
                dto.setProvinceName(rs.getString("province_name"));
                return dto;
        }
}
