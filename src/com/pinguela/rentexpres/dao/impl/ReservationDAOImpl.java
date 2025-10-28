package com.pinguela.rentexpres.dao.impl;

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

import com.pinguela.rentexpres.dao.ReservationDAO;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.ReservationCriteria;
import com.pinguela.rentexpres.model.ReservationDTO;
import com.pinguela.rentexpres.model.Results;

public class ReservationDAOImpl implements ReservationDAO {

        private static final Logger logger = LogManager.getLogger(ReservationDAOImpl.class);

        private static final String BASE_SELECT = "SELECT r.reservation_id, r.vehicle_id, r.user_id, r.employee_id, "
                        + "       r.reservation_status_id, r.pickup_headquarters_id, r.return_headquarters_id, "
                        + "       r.start_date, r.end_date, r.created_at, r.updated_at "
                        + "FROM reservation r";

        private static final Map<String, String> ORDER_BY_COLUMNS = buildOrderColumns();

        private static Map<String, String> buildOrderColumns() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("reservation_id", "r.reservation_id");
                map.put("start_date", "r.start_date");
                map.put("end_date", "r.end_date");
                map.put("created_at", "r.created_at");
                map.put("updated_at", "r.updated_at");
                return map;
        }

        @Override
        public ReservationDTO findById(Connection connection, Integer id) throws DataException {
                final String method = "findById";
                if (id == null) {
                        logger.warn("[{}] called with null id", method);
                        return null;
                }

                String sql = BASE_SELECT + " WHERE r.reservation_id = ?";
                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                        ps.setInt(1, id.intValue());
                        try (ResultSet rs = ps.executeQuery()) {
                                if (rs.next()) {
                                        logger.info("[{}] Reservation found with id {}", method, id);
                                        return toReservationDTO(rs);
                                }
                        }
                        logger.warn("[{}] No reservation found with id {}", method, id);
                } catch (SQLException e) {
                        logger.error("[{}] Error finding reservation id {}", method, id, e);
                        throw new DataException("Error finding reservation by id", e);
                }
                return null;
        }

        @Override
        public List<ReservationDTO> findAll(Connection connection) throws DataException {
                final String method = "findAll";
                List<ReservationDTO> reservations = new ArrayList<ReservationDTO>();
                String sql = BASE_SELECT + " ORDER BY r.start_date DESC";
                try (PreparedStatement ps = connection.prepareStatement(sql);
                                ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                                reservations.add(toReservationDTO(rs));
                        }
                        logger.info("[{}] Retrieved {} reservations", method, Integer.valueOf(reservations.size()));
                } catch (SQLException e) {
                        logger.error("[{}] Error retrieving reservations", method, e);
                        throw new DataException("Error retrieving reservations", e);
                }
                return reservations;
        }

        @Override
        public boolean create(Connection connection, ReservationDTO reservation) throws DataException {
                final String method = "create";
                if (reservation == null) {
                        logger.warn("[{}] called with null reservation", method);
                        return false;
                }

                String sql = "INSERT INTO reservation (vehicle_id, user_id, employee_id, reservation_status_id,"
                                + " pickup_headquarters_id, return_headquarters_id, start_date, end_date, created_at)"
                                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW())";

                try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                        bindWrite(ps, reservation, false);
                        if (ps.executeUpdate() > 0) {
                                try (ResultSet keys = ps.getGeneratedKeys()) {
                                        if (keys.next()) {
                                                reservation.setReservationId(Integer.valueOf(keys.getInt(1)));
                                        }
                                }
                                logger.info("[{}] Created reservation id {}", method, reservation.getReservationId());
                                return true;
                        }
                } catch (SQLException e) {
                        logger.error("[{}] Error creating reservation", method, e);
                        throw new DataException("Error creating reservation", e);
                }
                return false;
        }

        @Override
        public boolean update(Connection connection, ReservationDTO reservation) throws DataException {
                final String method = "update";
                if (reservation == null || reservation.getReservationId() == null) {
                        logger.warn("[{}] called with null reservation or id", method);
                        return false;
                }

                String sql = "UPDATE reservation SET vehicle_id = ?, user_id = ?, employee_id = ?, reservation_status_id = ?,"
                                + " pickup_headquarters_id = ?, return_headquarters_id = ?, start_date = ?, end_date = ?,"
                                + " updated_at = NOW() WHERE reservation_id = ?";

                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                        bindWrite(ps, reservation, true);
                        int updated = ps.executeUpdate();
                        if (updated > 0) {
                                logger.info("[{}] Updated reservation id {}", method, reservation.getReservationId());
                                return true;
                        }
                        logger.warn("[{}] No reservation updated for id {}", method, reservation.getReservationId());
                } catch (SQLException e) {
                        logger.error("[{}] Error updating reservation id {}", method, reservation.getReservationId(), e);
                        throw new DataException("Error updating reservation", e);
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
                String sql = "DELETE FROM reservation WHERE reservation_id = ?";
                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                        ps.setInt(1, id.intValue());
                        int rows = ps.executeUpdate();
                        if (rows > 0) {
                                logger.info("[{}] Reservation {} deleted", method, id);
                                return true;
                        }
                        logger.warn("[{}] No reservation deleted for id {}", method, id);
                } catch (SQLException e) {
                        logger.error("[{}] Error deleting reservation id {}", method, id, e);
                        throw new DataException("Error deleting reservation", e);
                }
                return false;
        }

        @Override
        public Results<ReservationDTO> findByCriteria(Connection connection, ReservationCriteria criteria) throws DataException {
                final String method = "findByCriteria";
                if (criteria == null) {
                        criteria = new ReservationCriteria();
                }
                criteria.normalize();

                Results<ReservationDTO> results = new Results<ReservationDTO>();
                List<Object> filters = new ArrayList<Object>();

                StringBuilder from = new StringBuilder(BASE_SELECT.substring(BASE_SELECT.indexOf("FROM")));
                StringBuilder where = new StringBuilder(" WHERE 1=1");

                if (criteria.getReservationId() != null) {
                        where.append(" AND r.reservation_id = ?");
                        filters.add(criteria.getReservationId());
                }
                if (criteria.getVehicleId() != null) {
                        where.append(" AND r.vehicle_id = ?");
                        filters.add(criteria.getVehicleId());
                }
                if (criteria.getUserId() != null) {
                        where.append(" AND r.user_id = ?");
                        filters.add(criteria.getUserId());
                }
                if (criteria.getEmployeeId() != null) {
                        where.append(" AND r.employee_id = ?");
                        filters.add(criteria.getEmployeeId());
                }
                if (criteria.getReservationStatusId() != null) {
                        where.append(" AND r.reservation_status_id = ?");
                        filters.add(criteria.getReservationStatusId());
                }
                if (criteria.getPickupHeadquartersId() != null) {
                        where.append(" AND r.pickup_headquarters_id = ?");
                        filters.add(criteria.getPickupHeadquartersId());
                }
                if (criteria.getReturnHeadquartersId() != null) {
                        where.append(" AND r.return_headquarters_id = ?");
                        filters.add(criteria.getReturnHeadquartersId());
                }
                if (criteria.getStartDateFrom() != null) {
                        where.append(" AND r.start_date >= ?");
                        filters.add(criteria.getStartDateFrom());
                }
                if (criteria.getStartDateTo() != null) {
                        where.append(" AND r.start_date <= ?");
                        filters.add(criteria.getStartDateTo());
                }
                if (criteria.getEndDateFrom() != null) {
                        where.append(" AND r.end_date >= ?");
                        filters.add(criteria.getEndDateFrom());
                }
                if (criteria.getEndDateTo() != null) {
                        where.append(" AND r.end_date <= ?");
                        filters.add(criteria.getEndDateTo());
                }
                if (criteria.getCreatedAtFrom() != null) {
                        where.append(" AND r.created_at >= ?");
                        filters.add(criteria.getCreatedAtFrom());
                }
                if (criteria.getCreatedAtTo() != null) {
                        where.append(" AND r.created_at <= ?");
                        filters.add(criteria.getCreatedAtTo());
                }
                if (criteria.getUpdatedAtFrom() != null) {
                        where.append(" AND r.updated_at >= ?");
                        filters.add(criteria.getUpdatedAtFrom());
                }
                if (criteria.getUpdatedAtTo() != null) {
                        where.append(" AND r.updated_at <= ?");
                        filters.add(criteria.getUpdatedAtTo());
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
                        logger.error("[{}] Error executing reservation count", method, e);
                        throw new DataException("Error executing reservation count", e);
                }

                if (total == 0) {
                        results.setItems(Collections.<ReservationDTO>emptyList());
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

                List<ReservationDTO> items = new ArrayList<ReservationDTO>();
                try (PreparedStatement ps = connection.prepareStatement(selectSql)) {
                        int idx = bindFilters(ps, filters);
                        ps.setInt(idx++, pageSize);
                        ps.setInt(idx, offset);
                        try (ResultSet rs = ps.executeQuery()) {
                                while (rs.next()) {
                                        items.add(toReservationDTO(rs));
                                }
                        }
                } catch (SQLException e) {
                        logger.error("[{}] Error executing reservation search", method, e);
                        throw new DataException("Error executing reservation search", e);
                }

                results.setItems(items);
                results.setPage(page);
                results.setPageSize(pageSize);
                results.setTotal(total);
                results.normalize();
                return results;
        }

        @Override
        public void updateStatus(Connection connection, Integer reservationId, Integer statusId) throws DataException {
                final String method = "updateStatus";
                if (reservationId == null || statusId == null) {
                        logger.warn("[{}] called with null parameters", method);
                        return;
                }
                String sql = "UPDATE reservation SET reservation_status_id = ?, updated_at = NOW() WHERE reservation_id = ?";
                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                        ps.setInt(1, statusId.intValue());
                        ps.setInt(2, reservationId.intValue());
                        ps.executeUpdate();
                } catch (SQLException e) {
                        logger.error("[{}] Error updating reservation {} status", method, reservationId, e);
                        throw new DataException("Error updating reservation status", e);
                }
        }

        private void bindWrite(PreparedStatement ps, ReservationDTO reservation, boolean isUpdate) throws SQLException {
                int idx = 1;
                ps.setInt(idx++, reservation.getVehicleId());
                ps.setInt(idx++, reservation.getUserId());
                if (reservation.getEmployeeId() == null) {
                        ps.setNull(idx++, Types.INTEGER);
                } else {
                        ps.setInt(idx++, reservation.getEmployeeId().intValue());
                }
                ps.setInt(idx++, reservation.getReservationStatusId());
                ps.setInt(idx++, reservation.getPickupHeadquartersId());
                ps.setInt(idx++, reservation.getReturnHeadquartersId());
                setTimestamp(ps, idx++, reservation.getStartDate());
                setTimestamp(ps, idx++, reservation.getEndDate());
                if (isUpdate) {
                        ps.setInt(idx, reservation.getReservationId());
                }
        }

        private int bindFilters(PreparedStatement ps, List<Object> filters) throws SQLException {
                int idx = 1;
                for (Object value : filters) {
                        if (value instanceof Integer) {
                                ps.setInt(idx++, ((Integer) value).intValue());
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
                return column != null ? column : "r.reservation_id";
        }

        private static void setTimestamp(PreparedStatement ps, int index, LocalDateTime value) throws SQLException {
                if (value == null) {
                        ps.setNull(index, Types.TIMESTAMP);
                } else {
                        ps.setTimestamp(index, Timestamp.valueOf(value));
                }
        }

        private ReservationDTO toReservationDTO(ResultSet rs) throws SQLException {
                ReservationDTO dto = new ReservationDTO();
                dto.setReservationId(Integer.valueOf(rs.getInt("reservation_id")));
                dto.setVehicleId(Integer.valueOf(rs.getInt("vehicle_id")));
                dto.setUserId(Integer.valueOf(rs.getInt("user_id")));
                int employee = rs.getInt("employee_id");
                dto.setEmployeeId(rs.wasNull() ? null : Integer.valueOf(employee));
                dto.setReservationStatusId(Integer.valueOf(rs.getInt("reservation_status_id")));
                dto.setPickupHeadquartersId(Integer.valueOf(rs.getInt("pickup_headquarters_id")));
                dto.setReturnHeadquartersId(Integer.valueOf(rs.getInt("return_headquarters_id")));
                Timestamp start = rs.getTimestamp("start_date");
                dto.setStartDate(start == null ? null : start.toLocalDateTime());
                Timestamp end = rs.getTimestamp("end_date");
                dto.setEndDate(end == null ? null : end.toLocalDateTime());
                Timestamp created = rs.getTimestamp("created_at");
                dto.setCreatedAt(created == null ? null : created.toLocalDateTime());
                Timestamp updated = rs.getTimestamp("updated_at");
                dto.setUpdatedAt(updated == null ? null : updated.toLocalDateTime());
                return dto;
        }
}
