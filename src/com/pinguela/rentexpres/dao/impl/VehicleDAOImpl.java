package com.pinguela.rentexpres.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.dao.VehicleDAO;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.model.VehicleCriteria;
import com.pinguela.rentexpres.model.VehicleDTO;

public class VehicleDAOImpl implements VehicleDAO {

        private static final Logger logger = LogManager.getLogger(VehicleDAOImpl.class);

        private static final String BASE_SELECT = "SELECT v.vehicle_id, v.brand, v.model, v.manufacture_year, v.daily_price, "
                        + "       v.license_plate, v.vin_number, v.current_mileage, v.vehicle_status_id, "
                        + "       v.category_id, v.current_headquarters_id, v.created_at, v.updated_at "
                        + "FROM vehicle v";

        private static final Map<String, String> ORDER_BY_COLUMNS = buildOrderColumns();

        private static Map<String, String> buildOrderColumns() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("vehicle_id", "v.vehicle_id");
                map.put("brand", "v.brand");
                map.put("model", "v.model");
                map.put("manufacture_year", "v.manufacture_year");
                map.put("daily_price", "v.daily_price");
                map.put("current_mileage", "v.current_mileage");
                map.put("created_at", "v.created_at");
                map.put("updated_at", "v.updated_at");
                return map;
        }

        @Override
        public VehicleDTO findById(Connection connection, Integer id) throws DataException {
                final String method = "findById";
                if (id == null) {
                        logger.warn("[{}] called with null id", method);
                        return null;
                }
                String sql = BASE_SELECT + " WHERE v.vehicle_id = ?";
                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                        ps.setInt(1, id.intValue());
                        try (ResultSet rs = ps.executeQuery()) {
                                if (rs.next()) {
                                        logger.info("[{}] Vehicle found with id {}", method, id);
                                        return toVehicleDTO(rs);
                                }
                        }
                        logger.warn("[{}] No vehicle found with id {}", method, id);
                } catch (SQLException e) {
                        logger.error("[{}] Error finding vehicle id {}", method, id, e);
                        throw new DataException("Error finding vehicle by id", e);
                }
                return null;
        }

        @Override
        public List<VehicleDTO> findAll(Connection connection) throws DataException {
                final String method = "findAll";
                List<VehicleDTO> vehicles = new ArrayList<VehicleDTO>();
                String sql = BASE_SELECT + " ORDER BY v.vehicle_id";
                try (PreparedStatement ps = connection.prepareStatement(sql);
                                ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                                vehicles.add(toVehicleDTO(rs));
                        }
                        logger.info("[{}] Retrieved {} vehicles", method, Integer.valueOf(vehicles.size()));
                } catch (SQLException e) {
                        logger.error("[{}] Error retrieving all vehicles", method, e);
                        throw new DataException("Error retrieving all vehicles", e);
                }
                return vehicles;
        }

        @Override
        public boolean create(Connection connection, VehicleDTO vehicle) throws DataException {
                final String method = "create";
                if (vehicle == null) {
                        logger.warn("[{}] called with null vehicle", method);
                        return false;
                }

                String sql = "INSERT INTO vehicle (brand, model, manufacture_year, daily_price, license_plate, vin_number,"
                                + " current_mileage, vehicle_status_id, category_id, current_headquarters_id, created_at)"
                                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";

                try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                        bindWrite(ps, vehicle, false);
                        if (ps.executeUpdate() > 0) {
                                try (ResultSet keys = ps.getGeneratedKeys()) {
                                        if (keys.next()) {
                                                vehicle.setVehicleId(Integer.valueOf(keys.getInt(1)));
                                        }
                                }
                                logger.info("[{}] Created vehicle id {}", method, vehicle.getVehicleId());
                                return true;
                        }
                } catch (SQLException e) {
                        logger.error("[{}] Error creating vehicle", method, e);
                        throw new DataException("Error creating vehicle", e);
                }
                return false;
        }

        @Override
        public boolean update(Connection connection, VehicleDTO vehicle) throws DataException {
                final String method = "update";
                if (vehicle == null || vehicle.getVehicleId() == null) {
                        logger.warn("[{}] called with null vehicle or id", method);
                        return false;
                }
                String sql = "UPDATE vehicle SET brand = ?, model = ?, manufacture_year = ?, daily_price = ?,"
                                + " license_plate = ?, vin_number = ?, current_mileage = ?, vehicle_status_id = ?,"
                                + " category_id = ?, current_headquarters_id = ?, updated_at = NOW() WHERE vehicle_id = ?";

                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                        bindWrite(ps, vehicle, true);
                        int updated = ps.executeUpdate();
                        if (updated > 0) {
                                logger.info("[{}] Updated vehicle id {}", method, vehicle.getVehicleId());
                                return true;
                        }
                        logger.warn("[{}] No vehicle updated for id {}", method, vehicle.getVehicleId());
                } catch (SQLException e) {
                        logger.error("[{}] Error updating vehicle id {}", method, vehicle.getVehicleId(), e);
                        throw new DataException("Error updating vehicle", e);
                }
                return false;
        }

        @Override
        public boolean delete(Connection connection, Integer id) throws DataException {
                final String method = "delete";
                if (id == null) {
                        logger.warn("[{}] called with null id", method);
                        return false;
                }
                String sql = "DELETE FROM vehicle WHERE vehicle_id = ?";
                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                        ps.setInt(1, id.intValue());
                        int rows = ps.executeUpdate();
                        if (rows > 0) {
                                logger.info("[{}] Vehicle {} deleted", method, id);
                                return true;
                        }
                        logger.warn("[{}] No vehicle deleted for id {}", method, id);
                } catch (SQLException e) {
                        logger.error("[{}] Error deleting vehicle id {}", method, id, e);
                        throw new DataException("Error deleting vehicle", e);
                }
                return false;
        }

        @Override
        public Results<VehicleDTO> findByCriteria(Connection connection, VehicleCriteria criteria) throws DataException {
                final String method = "findByCriteria";
                if (criteria == null) {
                        criteria = new VehicleCriteria();
                }
                criteria.normalize();

                Results<VehicleDTO> results = new Results<VehicleDTO>();
                List<Object> filters = new ArrayList<Object>();

                StringBuilder from = new StringBuilder(BASE_SELECT.substring(BASE_SELECT.indexOf("FROM")));
                StringBuilder where = new StringBuilder(" WHERE 1=1");

                if (criteria.getVehicleId() != null) {
                        where.append(" AND v.vehicle_id = ?");
                        filters.add(criteria.getVehicleId());
                }
                if (criteria.getBrand() != null) {
                        where.append(" AND v.brand LIKE ?");
                        filters.add(like(criteria.getBrand()));
                }
                if (criteria.getModel() != null) {
                        where.append(" AND v.model LIKE ?");
                        filters.add(like(criteria.getModel()));
                }
                if (criteria.getCategoryId() != null) {
                        where.append(" AND v.category_id = ?");
                        filters.add(criteria.getCategoryId());
                }
                if (criteria.getVehicleStatusId() != null) {
                        where.append(" AND v.vehicle_status_id = ?");
                        filters.add(criteria.getVehicleStatusId());
                }
                if (criteria.getManufactureYearFrom() != null) {
                        where.append(" AND v.manufacture_year >= ?");
                        filters.add(criteria.getManufactureYearFrom());
                }
                if (criteria.getManufactureYearTo() != null) {
                        where.append(" AND v.manufacture_year <= ?");
                        filters.add(criteria.getManufactureYearTo());
                }
                if (criteria.getDailyPriceMin() != null) {
                        where.append(" AND v.daily_price >= ?");
                        filters.add(criteria.getDailyPriceMin());
                }
                if (criteria.getDailyPriceMax() != null) {
                        where.append(" AND v.daily_price <= ?");
                        filters.add(criteria.getDailyPriceMax());
                }
                if (criteria.getCurrentHeadquartersId() != null) {
                        where.append(" AND v.current_headquarters_id = ?");
                        filters.add(criteria.getCurrentHeadquartersId());
                }

                String countSql = "SELECT COUNT(1) " + from.toString() + where.toString();

                int page = criteria.getSafePage();
                int pageSize = criteria.getSafePageSize();
                int total;

                try (PreparedStatement ps = connection.prepareStatement(countSql)) {
                        bindFilters(ps, filters);
                        try (ResultSet rs = ps.executeQuery()) {
                                total = rs.next() ? rs.getInt(1) : 0;
                        }
                } catch (SQLException e) {
                        logger.error("[{}] Error executing vehicle count", method, e);
                        throw new DataException("Error executing vehicle count", e);
                }

                if (total == 0) {
                        results.setItems(Collections.<VehicleDTO>emptyList());
                        results.setPage(page);
                        results.setPageSize(pageSize);
                        results.setTotal(total);
                        results.normalize();
                        return results;
                }

                int totalPages = (total + pageSize - 1) / pageSize;
                if (page > totalPages) {
                        page = totalPages;
                }
                int offset = (page - 1) * pageSize;

                String orderColumn = resolveOrderColumn(criteria.getOrderBy());
                String direction = criteria.getSafeOrderDir();

                String selectSql = BASE_SELECT + where.toString() + " ORDER BY " + orderColumn + " " + direction
                                + " LIMIT ? OFFSET ?";

                List<VehicleDTO> items = new ArrayList<VehicleDTO>();
                try (PreparedStatement ps = connection.prepareStatement(selectSql)) {
                        int idx = bindFilters(ps, filters);
                        ps.setInt(idx++, pageSize);
                        ps.setInt(idx, offset);
                        try (ResultSet rs = ps.executeQuery()) {
                                while (rs.next()) {
                                        items.add(toVehicleDTO(rs));
                                }
                        }
                } catch (SQLException e) {
                        logger.error("[{}] Error executing vehicle search", method, e);
                        throw new DataException("Error executing vehicle search", e);
                }

                results.setItems(items);
                results.setPage(page);
                results.setPageSize(pageSize);
                results.setTotal(total);
                results.normalize();
                return results;
        }

        @Override
        public void updateStatus(Connection connection, Integer vehicleId, Integer statusId) throws DataException {
                final String method = "updateStatus";
                if (vehicleId == null || statusId == null) {
                        logger.warn("[{}] called with null parameters", method);
                        return;
                }
                String sql = "UPDATE vehicle SET vehicle_status_id = ?, updated_at = NOW() WHERE vehicle_id = ?";
                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                        ps.setInt(1, statusId.intValue());
                        ps.setInt(2, vehicleId.intValue());
                        ps.executeUpdate();
                } catch (SQLException e) {
                        logger.error("[{}] Error updating vehicle {} status", method, vehicleId, e);
                        throw new DataException("Error updating vehicle status", e);
                }
        }

        private static String like(String value) {
                return value == null ? null : "%" + value + "%";
        }

        private void bindWrite(PreparedStatement ps, VehicleDTO vehicle, boolean isUpdate) throws SQLException {
                int idx = 1;
                ps.setString(idx++, vehicle.getBrand());
                ps.setString(idx++, vehicle.getModel());
                ps.setInt(idx++, vehicle.getManufactureYear());
                ps.setBigDecimal(idx++, vehicle.getDailyPrice());
                ps.setString(idx++, vehicle.getLicensePlate());
                ps.setString(idx++, vehicle.getVinNumber());
                ps.setInt(idx++, vehicle.getCurrentMileage());
                ps.setInt(idx++, vehicle.getVehicleStatusId());
                ps.setInt(idx++, vehicle.getCategoryId());
                ps.setInt(idx++, vehicle.getCurrentHeadquartersId());
                if (isUpdate) {
                        ps.setInt(idx, vehicle.getVehicleId());
                }
        }

        private int bindFilters(PreparedStatement ps, List<Object> filters) throws SQLException {
                int idx = 1;
                for (Object value : filters) {
                        if (value instanceof String) {
                                ps.setString(idx++, (String) value);
                        } else if (value instanceof Integer) {
                                ps.setInt(idx++, ((Integer) value).intValue());
                        } else if (value instanceof BigDecimal) {
                                ps.setBigDecimal(idx++, (BigDecimal) value);
                        } else {
                                ps.setObject(idx++, value);
                        }
                }
                return idx;
        }

        private String resolveOrderColumn(String requested) {
                String key = requested == null ? null : requested;
                String column = ORDER_BY_COLUMNS.get(key);
                return column != null ? column : "v.vehicle_id";
        }

        private VehicleDTO toVehicleDTO(ResultSet rs) throws SQLException {
                VehicleDTO dto = new VehicleDTO();
                dto.setVehicleId(Integer.valueOf(rs.getInt("vehicle_id")));
                dto.setBrand(rs.getString("brand"));
                dto.setModel(rs.getString("model"));
                dto.setManufactureYear(Integer.valueOf(rs.getInt("manufacture_year")));
                dto.setDailyPrice(rs.getBigDecimal("daily_price"));
                dto.setLicensePlate(rs.getString("license_plate"));
                dto.setVinNumber(rs.getString("vin_number"));
                dto.setCurrentMileage(Integer.valueOf(rs.getInt("current_mileage")));
                dto.setVehicleStatusId(Integer.valueOf(rs.getInt("vehicle_status_id")));
                dto.setCategoryId(Integer.valueOf(rs.getInt("category_id")));
                dto.setCurrentHeadquartersId(Integer.valueOf(rs.getInt("current_headquarters_id")));
                Timestamp created = rs.getTimestamp("created_at");
                dto.setCreatedAt(created == null ? null : created.toLocalDateTime());
                Timestamp updated = rs.getTimestamp("updated_at");
                dto.setUpdatedAt(updated == null ? null : updated.toLocalDateTime());
                return dto;
        }
}
