package com.pinguela.rentexpres.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.dao.HeadquartersDAO;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.AddressDTO;
import com.pinguela.rentexpres.model.CityDTO;
import com.pinguela.rentexpres.model.HeadquartersDTO;
import com.pinguela.rentexpres.model.ProvinceDTO;

public class HeadquartersDAOImpl implements HeadquartersDAO {

    private static final Logger logger = LogManager.getLogger(HeadquartersDAOImpl.class);

    private static final String BASE_SELECT = String.join(" ",
            "SELECT h.headquarters_id AS headquarters_id,",
            "       h.name AS name,",
            "       h.phone AS phone,",
            "       h.email AS email,",
            "       h.address_id AS address_id,",
            "       h.created_at AS created_at,",
            "       h.updated_at AS updated_at,",
            "       a.address_id AS address_address_id,",
            "       a.city_id AS address_city_id,",
            "       a.street AS address_street,",
            "       a.number AS address_number,",
            "       c.city_id AS city_city_id,",
            "       c.city_name AS city_city_name,",
            "       c.province_id AS city_province_id,",
            "       p.province_id AS province_province_id,",
            "       p.province_name AS province_province_name",
            "FROM headquarters h",
            "INNER JOIN address a ON a.address_id = h.address_id",
            "INNER JOIN city c ON c.city_id = a.city_id",
            "INNER JOIN province p ON p.province_id = c.province_id");

    @Override
    public HeadquartersDTO findById(Connection connection, Integer id) throws DataException {
        final String method = "findById";
        if (id == null) {
            logger.warn("[{}] called with null id", method);
            return null;
        }
        String sql = BASE_SELECT + " WHERE h.headquarters_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id.intValue());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    logger.info("[{}] Headquarters found with id: {}", method, id);
                    return toHeadquarters(rs);
                }
            }
            logger.warn("[{}] No headquarters found with id: {}", method, id);
        } catch (SQLException e) {
            logger.error("[{}] Error finding headquarters by id: {}", method, id, e);
            throw new DataException(e);
        }
        return null;
    }

    @Override
    public boolean create(Connection connection, HeadquartersDTO headquarters) throws DataException {
        final String method = "create";
        if (headquarters == null) {
            logger.warn("[{}] called with null headquarters", method);
            return false;
        }
        String sql = "INSERT INTO headquarters (name, phone, email, address_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setParameters(ps, headquarters, false);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        headquarters.setHeadquartersId(Integer.valueOf(keys.getInt(1)));
                    }
                }
                logger.info("[{}] Headquarters created successfully: {}", method, headquarters.getName());
                return true;
            }
            logger.warn("[{}] No headquarters created", method);
            return false;
        } catch (SQLException e) {
            logger.error("[{}] Error creating headquarters", method, e);
            throw new DataException(e);
        }
    }

    @Override
    public boolean update(Connection connection, HeadquartersDTO headquarters) throws DataException {
        final String method = "update";
        if (headquarters == null || headquarters.getHeadquartersId() == null) {
            logger.warn("[{}] called with null headquarters or id", method);
            return false;
        }
        String sql = "UPDATE headquarters SET name = ?, phone = ?, email = ?, address_id = ? WHERE headquarters_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            setParameters(ps, headquarters, true);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                logger.info("[{}] Headquarters updated successfully, id: {}", method, headquarters.getHeadquartersId());
                return true;
            }
            logger.warn("[{}] No headquarters updated for id: {}", method, headquarters.getHeadquartersId());
            return false;
        } catch (SQLException e) {
            logger.error("[{}] Error updating headquarters id {}", method, headquarters.getHeadquartersId(), e);
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
        String sql = "DELETE FROM headquarters WHERE headquarters_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id.intValue());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                logger.info("[{}] Headquarters deleted, id: {}", method, id);
                return true;
            }
            logger.warn("[{}] No headquarters deleted for id: {}", method, id);
            return false;
        } catch (SQLException e) {
            logger.error("[{}] Error deleting headquarters id {}", method, id, e);
            throw new DataException(e);
        }
    }

    @Override
    public List<HeadquartersDTO> findAll(Connection connection) throws DataException {
        final String method = "findAll";
        List<HeadquartersDTO> list = new ArrayList<HeadquartersDTO>();
        String sql = BASE_SELECT + " ORDER BY h.headquarters_id";
        try (PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(toHeadquarters(rs));
            }
            logger.info("[{}] {} headquarters found.", method, Integer.valueOf(list.size()));
        } catch (SQLException e) {
            logger.error("[{}] Error fetching all headquarters", method, e);
            throw new DataException(e);
        }
        return list;
    }

    private HeadquartersDTO toHeadquarters(ResultSet rs) throws SQLException {
        HeadquartersDTO dto = new HeadquartersDTO();
        dto.setHeadquartersId(Integer.valueOf(rs.getInt("headquarters_id")));
        dto.setName(rs.getString("name"));
        dto.setPhone(rs.getString("phone"));
        dto.setEmail(rs.getString("email"));
        dto.setAddressId(Integer.valueOf(rs.getInt("address_id")));

        Timestamp created = rs.getTimestamp("created_at");
        if (created != null) {
            dto.setCreatedAt(created.toLocalDateTime());
        }
        Timestamp updated = rs.getTimestamp("updated_at");
        if (updated != null) {
            dto.setUpdatedAt(updated.toLocalDateTime());
        }

        CityDTO city = new CityDTO();
        city.setCityId(Integer.valueOf(rs.getInt("city_city_id")));
        city.setCityName(rs.getString("city_city_name"));
        city.setProvinceId(Integer.valueOf(rs.getInt("city_province_id")));
        dto.setCity(city);

        ProvinceDTO province = new ProvinceDTO();
        province.setProvinceId(Integer.valueOf(rs.getInt("province_province_id")));
        province.setProvinceName(rs.getString("province_province_name"));
        dto.setProvince(province);

        AddressDTO address = new AddressDTO();
        address.setAddressId(Integer.valueOf(rs.getInt("address_address_id")));
        address.setCityId(Integer.valueOf(rs.getInt("address_city_id")));
        address.setStreet(rs.getString("address_street"));
        address.setNumber(rs.getString("address_number"));
        address.setCityName(rs.getString("city_city_name"));
        address.setProvinceId(Integer.valueOf(rs.getInt("province_province_id")));
        address.setProvinceName(rs.getString("province_province_name"));
        address.setCity(city);
        dto.setAddress(address);

        return dto;
    }

    private void setParameters(PreparedStatement ps, HeadquartersDTO headquarters, boolean isUpdate) throws SQLException {
        ps.setString(1, headquarters.getName());
        ps.setString(2, headquarters.getPhone());
        ps.setString(3, headquarters.getEmail());
        ps.setInt(4, headquarters.getAddressId().intValue());
        if (isUpdate) {
            ps.setInt(5, headquarters.getHeadquartersId().intValue());
        }
    }
}
