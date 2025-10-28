package com.pinguela.rentexpres.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.dao.RentalDAO;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.RentalCriteria;
import com.pinguela.rentexpres.model.RentalDTO;
import com.pinguela.rentexpres.model.Results;

public class RentalDAOImpl implements RentalDAO {

        private static final Logger logger = LogManager.getLogger(RentalDAOImpl.class);

        private static final String BASE_SELECT = "SELECT r.rental_id, r.reservation_id, r.start_date_effective, r.end_date_effective, "
                        + "       r.initial_km, r.final_km, r.rental_status_id, r.total_cost, "
                        + "       r.pickup_headquarters_id, r.return_headquarters_id, r.created_at, r.updated_at, "
                        + "       res.vehicle_id, res.user_id, "
                        + "       v.license_plate, v.brand, v.model, "
                        + "       u.first_name AS user_first_name, u.last_name1 AS user_last_name1, u.phone AS user_phone, "
                        + "       s.status_name AS rental_status_name, "
                        + "       h1.name AS pickup_headquarters_name, h2.name AS return_headquarters_name "
                        + "FROM rental r "
                        + "INNER JOIN rental_status s ON r.rental_status_id = s.rental_status_id "
                        + "INNER JOIN reservation res ON r.reservation_id = res.reservation_id "
                        + "INNER JOIN vehicle v ON res.vehicle_id = v.vehicle_id "
                        + "INNER JOIN user u ON res.user_id = u.user_id "
                        + "INNER JOIN headquarters h1 ON r.pickup_headquarters_id = h1.headquarters_id "
                        + "INNER JOIN headquarters h2 ON r.return_headquarters_id = h2.headquarters_id";

        private static final Map<String, String> ORDER_BY_COLUMNS = buildOrderColumns();

        private static Map<String, String> buildOrderColumns() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("rental_id", "r.rental_id");
                map.put("start_date_effective", "r.start_date_effective");
                map.put("end_date_effective", "r.end_date_effective");
                map.put("total_cost", "r.total_cost");
                map.put("created_at", "r.created_at");
                map.put("updated_at", "r.updated_at");
                return map;
        }

        @Override
        public RentalDTO findById(Connection connection, Integer id) throws DataException {
                final String method = "findById";
                if (id == null) {
                        logger.warn("[{}] called with null id", method);
                        return null;
                }

                String sql = BASE_SELECT + " WHERE r.rental_id = ?";
                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                        ps.setInt(1, id.intValue());
                        try (ResultSet rs = ps.executeQuery()) {
                                if (rs.next()) {
                                        logger.info("[{}] Rental found with id {}", method, id);
                                        return toRentalDTO(rs);
                                }
                        }
                        logger.warn("[{}] No rental found with id {}", method, id);
                } catch (SQLException e) {
                        logger.error("[{}] Error finding rental id {}", method, id, e);
                        throw new DataException("Error finding rental by id", e);
                }
                return null;
        }

        @Override
        public List<RentalDTO> findAll(Connection connection) throws DataException {
                final String method = "findAll";
                List<RentalDTO> rentals = new ArrayList<RentalDTO>();
                String sql = BASE_SELECT + " ORDER BY r.start_date_effective DESC";
                try (PreparedStatement ps = connection.prepareStatement(sql);
                                ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                                rentals.add(toRentalDTO(rs));
                        }
                        logger.info("[{}] Retrieved {} rentals", method, Integer.valueOf(rentals.size()));
                } catch (SQLException e) {
                        logger.error("[{}] Error retrieving rentals", method, e);
                        throw new DataException("Error retrieving rentals", e);
                }
                return rentals;
        }

        @Override
        public boolean create(Connection connection, RentalDTO rental) throws DataException {
                final String method = "create";
                if (rental == null) {
                        logger.warn("[{}] called with null rental", method);
                        return false;
                }

                String sql = "INSERT INTO rental (reservation_id, start_date_effective, end_date_effective, initial_km, final_km,"
                                + " rental_status_id, total_cost, pickup_headquarters_id, return_headquarters_id, created_at)"
                                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";

                try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                        bindWrite(ps, rental, false);
                        if (ps.executeUpdate() > 0) {
                                try (ResultSet keys = ps.getGeneratedKeys()) {
                                        if (keys.next()) {
                                                rental.setRentalId(Integer.valueOf(keys.getInt(1)));
                                        }
                                }
                                logger.info("[{}] Created rental id {}", method, rental.getRentalId());
                                return true;
                        }
                } catch (SQLException e) {
                        logger.error("[{}] Error creating rental", method, e);
                        throw new DataException("Error creating rental", e);
                }
                return false;
        }

        @Override
        public boolean update(Connection connection, RentalDTO rental) throws DataException {
                final String method = "update";
                if (rental == null || rental.getRentalId() == null) {
                        logger.warn("[{}] called with null rental or id", method);
                        return false;
                }

                String sql = "UPDATE rental SET start_date_effective = ?, end_date_effective = ?, initial_km = ?, final_km = ?,"
                                + " rental_status_id = ?, total_cost = ?, pickup_headquarters_id = ?, return_headquarters_id = ?,"
                                + " updated_at = NOW() WHERE rental_id = ?";

                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                        bindWrite(ps, rental, true);
                        int updated = ps.executeUpdate();
                        if (updated > 0) {
                                logger.info("[{}] Updated rental id {}", method, rental.getRentalId());
                                return true;
                        }
                        logger.warn("[{}] No rental updated for id {}", method, rental.getRentalId());
                } catch (SQLException e) {
                        logger.error("[{}] Error updating rental id {}", method, rental.getRentalId(), e);
                        throw new DataException("Error updating rental", e);
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
                String sql = "DELETE FROM rental WHERE rental_id = ?";
                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                        ps.setInt(1, id.intValue());
                        int rows = ps.executeUpdate();
                        if (rows > 0) {
                                logger.info("[{}] Rental {} deleted", method, id);
                                return true;
                        }
                        logger.warn("[{}] No rental deleted for id {}", method, id);
                } catch (SQLException e) {
                        logger.error("[{}] Error deleting rental id {}", method, id, e);
                        throw new DataException("Error deleting rental", e);
                }
                return false;
        }

        @Override
        public Results<RentalDTO> findByCriteria(Connection connection, RentalCriteria criteria) throws DataException {
                final String method = "findByCriteria";
                if (criteria == null) {
                        criteria = new RentalCriteria();
                }
                criteria.normalize();

                Results<RentalDTO> results = new Results<RentalDTO>();
                List<Object> filters = new ArrayList<Object>();

                StringBuilder from = new StringBuilder(BASE_SELECT.substring(BASE_SELECT.indexOf("FROM")));
                StringBuilder where = new StringBuilder(" WHERE 1=1");

                addEqualsFilter(where, filters, " AND r.rental_id = ?", criteria.getRentalId());
                addEqualsFilter(where, filters, " AND r.rental_status_id = ?", criteria.getRentalStatusId());
                addEqualsFilter(where, filters, " AND r.reservation_id = ?", criteria.getReservationId());
                addEqualsFilter(where, filters, " AND res.user_id = ?", criteria.getUserId());
                addEqualsFilter(where, filters, " AND res.employee_id = ?", criteria.getEmployeeId());
                addEqualsFilter(where, filters, " AND res.vehicle_id = ?", criteria.getVehicleId());
                addEqualsFilter(where, filters, " AND r.pickup_headquarters_id = ?", criteria.getPickupHeadquartersId());
                addEqualsFilter(where, filters, " AND r.return_headquarters_id = ?", criteria.getReturnHeadquartersId());

                addRangeFilter(where, filters, " AND r.initial_km >= ?", " AND r.initial_km <= ?", criteria.getInitialKmMin(),
                                criteria.getInitialKmMax());
                addRangeFilter(where, filters, " AND r.final_km >= ?", " AND r.final_km <= ?", criteria.getFinalKmMin(),
                                criteria.getFinalKmMax());
                addRangeFilter(where, filters, " AND r.total_cost >= ?", " AND r.total_cost <= ?",
                                criteria.getTotalCostMin(), criteria.getTotalCostMax());

                addRangeFilter(where, filters, " AND r.start_date_effective >= ?", " AND r.start_date_effective <= ?",
                                criteria.getStartDateEffectiveFrom(), criteria.getStartDateEffectiveTo());
                addRangeFilter(where, filters, " AND r.end_date_effective >= ?", " AND r.end_date_effective <= ?",
                                criteria.getEndDateEffectiveFrom(), criteria.getEndDateEffectiveTo());
                addRangeFilter(where, filters, " AND r.created_at >= ?", " AND r.created_at <= ?",
                                criteria.getCreatedAtFrom(), criteria.getCreatedAtTo());
                addRangeFilter(where, filters, " AND r.updated_at >= ?", " AND r.updated_at <= ?",
                                criteria.getUpdatedAtFrom(), criteria.getUpdatedAtTo());

                addEqualsFilter(where, filters, " AND r.start_date_effective = ?", criteria.getStartDateEffective());
                addEqualsFilter(where, filters, " AND r.end_date_effective = ?", criteria.getEndDateEffective());
                addEqualsFilter(where, filters, " AND r.initial_km = ?", criteria.getInitialKm());
                addEqualsFilter(where, filters, " AND r.final_km = ?", criteria.getFinalKm());
                addEqualsFilter(where, filters, " AND r.total_cost = ?", criteria.getTotalCost());

                addLikeFilter(where, filters, " AND u.first_name LIKE ?", criteria.getUserFirstName());
                addLikeFilter(where, filters, " AND u.last_name1 LIKE ?", criteria.getUserLastName1());
                addLikeFilter(where, filters, " AND u.phone LIKE ?", criteria.getPhone());
                addLikeFilter(where, filters, " AND v.license_plate LIKE ?", criteria.getLicensePlate());
                addLikeFilter(where, filters, " AND v.brand LIKE ?", criteria.getBrand());
                addLikeFilter(where, filters, " AND v.model LIKE ?", criteria.getModel());

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
                        logger.error("[{}] Error executing rental count", method, e);
                        throw new DataException("Error executing rental count", e);
                }

                if (total == 0) {
                        results.setItems(Collections.<RentalDTO>emptyList());
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

                List<RentalDTO> items = new ArrayList<RentalDTO>();
                try (PreparedStatement ps = connection.prepareStatement(selectSql)) {
                        int idx = bindFilters(ps, filters);
                        ps.setInt(idx++, pageSize);
                        ps.setInt(idx, offset);
                        try (ResultSet rs = ps.executeQuery()) {
                                while (rs.next()) {
                                        items.add(toRentalDTO(rs));
                                }
                        }
                } catch (SQLException e) {
                        logger.error("[{}] Error executing rental search", method, e);
                        throw new DataException("Error executing rental search", e);
                }

                results.setItems(items);
                results.setPage(page);
                results.setPageSize(pageSize);
                results.setTotal(total);
                results.normalize();
                return results;
        }

        @Override
        public boolean existsByReservation(Connection connection, Integer reservationId) throws DataException {
                final String method = "existsByReservation";
                if (reservationId == null) {
                        logger.warn("[{}] called with null reservation id", method);
                        return false;
                }
                String sql = "SELECT 1 FROM rental WHERE reservation_id = ? LIMIT 1";
                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                        ps.setInt(1, reservationId.intValue());
                        try (ResultSet rs = ps.executeQuery()) {
                                return rs.next();
                        }
                } catch (SQLException e) {
                        logger.error("[{}] Error checking rentals for reservation {}", method, reservationId, e);
                        throw new DataException("Error checking rentals by reservation", e);
                }
        }

        private void bindWrite(PreparedStatement ps, RentalDTO rental, boolean isUpdate) throws SQLException {
                int idx = 1;
                if (!isUpdate) {
                        ps.setInt(idx++, rental.getReservationId());
                }
                setTimestamp(ps, idx++, rental.getStartDateEffective());
                setTimestamp(ps, idx++, rental.getEndDateEffective());
                ps.setInt(idx++, rental.getInitialKm());
                ps.setInt(idx++, rental.getFinalKm());
                ps.setInt(idx++, rental.getRentalStatusId());
                BigDecimal cost = rental.getTotalCost();
                ps.setBigDecimal(idx++, cost == null ? BigDecimal.ZERO : cost);
                ps.setInt(idx++, rental.getPickupHeadquartersId());
                ps.setInt(idx++, rental.getReturnHeadquartersId());
                if (isUpdate) {
                        ps.setInt(idx, rental.getRentalId());
                }
        }

        private void addEqualsFilter(StringBuilder sql, List<Object> filters, String clause, Object value) {
                if (value != null) {
                        sql.append(clause);
                        filters.add(value);
                }
        }

        private void addRangeFilter(StringBuilder sql, List<Object> filters, String minClause, String maxClause, Object min,
                        Object max) {
                if (min != null) {
                        sql.append(minClause);
                        filters.add(min);
                }
                if (max != null) {
                        sql.append(maxClause);
                        filters.add(max);
                }
        }

        private void addLikeFilter(StringBuilder sql, List<Object> filters, String clause, String value) {
                if (value != null && !value.isEmpty()) {
                        sql.append(clause);
                        filters.add("%" + value + "%");
                }
        }

        private int bindFilters(PreparedStatement ps, List<Object> filters) throws SQLException {
                int idx = 1;
                for (Object value : filters) {
                        if (value instanceof Integer) {
                                ps.setInt(idx++, ((Integer) value).intValue());
                        } else if (value instanceof BigDecimal) {
                                ps.setBigDecimal(idx++, (BigDecimal) value);
                        } else if (value instanceof LocalDateTime) {
                                setTimestamp(ps, idx++, (LocalDateTime) value);
                        } else {
                                ps.setObject(idx++, value);
                        }
                }
                return idx;
        }

        private String resolveOrderColumn(String requested) {
                String key = requested == null ? null : requested;
                String column = ORDER_BY_COLUMNS.get(key);
                return column != null ? column : "r.rental_id";
        }

        private static void setTimestamp(PreparedStatement ps, int index, LocalDateTime value) throws SQLException {
                if (value == null) {
                        ps.setNull(index, Types.TIMESTAMP);
                } else {
                        ps.setTimestamp(index, Timestamp.valueOf(value));
                }
        }

        private RentalDTO toRentalDTO(ResultSet rs) throws SQLException {
                RentalDTO dto = new RentalDTO();
                dto.setRentalId(Integer.valueOf(rs.getInt("rental_id")));
                dto.setReservationId(Integer.valueOf(rs.getInt("reservation_id")));
                Timestamp start = rs.getTimestamp("start_date_effective");
                dto.setStartDateEffective(start == null ? null : start.toLocalDateTime());
                Timestamp end = rs.getTimestamp("end_date_effective");
                dto.setEndDateEffective(end == null ? null : end.toLocalDateTime());
                dto.setInitialKm(Integer.valueOf(rs.getInt("initial_km")));
                dto.setFinalKm(Integer.valueOf(rs.getInt("final_km")));
                dto.setRentalStatusId(Integer.valueOf(rs.getInt("rental_status_id")));
                dto.setTotalCost(rs.getBigDecimal("total_cost"));
                dto.setPickupHeadquartersId(Integer.valueOf(rs.getInt("pickup_headquarters_id")));
                dto.setReturnHeadquartersId(Integer.valueOf(rs.getInt("return_headquarters_id")));
                Timestamp created = rs.getTimestamp("created_at");
                dto.setCreatedAt(created == null ? null : created.toLocalDateTime());
                Timestamp updated = rs.getTimestamp("updated_at");
                dto.setUpdatedAt(updated == null ? null : updated.toLocalDateTime());
                dto.setVehicleId(Integer.valueOf(rs.getInt("vehicle_id")));
                dto.setUserId(Integer.valueOf(rs.getInt("user_id")));
                dto.setLicensePlate(rs.getString("license_plate"));
                dto.setBrand(rs.getString("brand"));
                dto.setModel(rs.getString("model"));
                dto.setUserFirstName(rs.getString("user_first_name"));
                dto.setUserLastName1(rs.getString("user_last_name1"));
                dto.setPhone(rs.getString("user_phone"));
                dto.setRentalStatusName(rs.getString("rental_status_name"));
                dto.setPickupHeadquartersName(rs.getString("pickup_headquarters_name"));
                return dto;
        }
}
