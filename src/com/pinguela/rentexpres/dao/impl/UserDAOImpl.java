package com.pinguela.rentexpres.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.jasypt.util.password.StrongPasswordEncryptor;

import com.pinguela.rentexpres.dao.UserDAO;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.model.UserCriteria;
import com.pinguela.rentexpres.model.UserDTO;

public class UserDAOImpl implements UserDAO {

        private static final Logger logger = LogManager.getLogger(UserDAOImpl.class);

        private static final StrongPasswordEncryptor ENCRYPTOR = new StrongPasswordEncryptor();

        private static final String BASE_SELECT = String.join(" ",
                        "SELECT u.user_id, u.username, u.first_name, u.last_name1, u.last_name2,",
                        "       u.birth_date, u.email, u.password, u.role_id, u.phone, u.address_id,",
                        "       u.active_status, u.created_at, u.updated_at",
                        "FROM user u");

        private static final Map<String, String> ORDER_BY_COLUMNS = buildOrderColumns();

        private static Map<String, String> buildOrderColumns() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("user_id", "u.user_id");
                map.put("username", "u.username");
                map.put("first_name", "u.first_name");
                map.put("last_name1", "u.last_name1");
                map.put("email", "u.email");
                map.put("phone", "u.phone");
                map.put("created_at", "u.created_at");
                map.put("updated_at", "u.updated_at");
                return map;
        }

        @Override
        public UserDTO authenticate(Connection connection, String login, String password) throws DataException {
                final String method = "authenticate";
                if (login == null || password == null) {
                        logger.warn("[{}] called with null credentials", method);
                        return null;
                }

                String sql = BASE_SELECT + " WHERE (LOWER(u.username) = LOWER(?) OR LOWER(u.email) = LOWER(?))"
                                + " AND u.active_status = 1";

                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                        ps.setString(1, login.trim());
                        ps.setString(2, login.trim());
                        try (ResultSet rs = ps.executeQuery()) {
                                if (rs.next()) {
                                        String encryptedPassword = rs.getString("password");
                                        if (encryptedPassword != null) {
                                                try {
                                                        if (ENCRYPTOR.checkPassword(password, encryptedPassword)) {
                                                                logger.info("[{}] Successful authentication for login: {}", method,
                                                                                login);
                                                                return toUserDTO(rs, true);
                                                        }
                                                        logger.warn("[{}] Invalid password for login: {}", method, login);
                                                } catch (EncryptionOperationNotPossibleException e) {
                                                        logger.error("[{}] Unable to verify password for login: {}", method,
                                                                        login, e);
                                                }
                                        } else {
                                                logger.warn("[{}] Stored password is null for login: {}", method, login);
                                        }
                                } else {
                                        logger.warn("[{}] No user found for login: {}", method, login);
                                }
                        }
                } catch (SQLException e) {
                        logger.error("[{}] Error authenticating login: {}", method, login, e);
                        throw new DataException("Error authenticating user", e);
                }
                return null;
        }

        @Override
        public UserDTO findById(Connection connection, Integer id) throws DataException {
                final String method = "findById";
                if (id == null) {
                        logger.warn("[{}] called with null id", method);
                        return null;
                }

                String sql = BASE_SELECT + " WHERE u.user_id = ? AND u.active_status = 1";

                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                        ps.setInt(1, id.intValue());
                        try (ResultSet rs = ps.executeQuery()) {
                                if (rs.next()) {
                                        logger.info("[{}] User found with id {}", method, id);
                                        return toUserDTO(rs, false);
                                }
                        }
                        logger.warn("[{}] No user found with id {}", method, id);
                } catch (SQLException e) {
                        logger.error("[{}] Error finding user id {}", method, id, e);
                        throw new DataException("Error finding user by id", e);
                }
                return null;
        }

        @Override
        public List<UserDTO> findAll(Connection connection) throws DataException {
                        final String method = "findAll";
                        List<UserDTO> users = new ArrayList<UserDTO>();

                        String sql = BASE_SELECT + " WHERE u.active_status = 1 ORDER BY u.user_id";

                        try (PreparedStatement ps = connection.prepareStatement(sql);
                                        ResultSet rs = ps.executeQuery()) {
                                while (rs.next()) {
                                        users.add(toUserDTO(rs, false));
                                }
                                logger.info("[{}] Retrieved {} active users", method, Integer.valueOf(users.size()));
                        } catch (SQLException e) {
                                logger.error("[{}] Error retrieving all users", method, e);
                                throw new DataException("Error retrieving all users", e);
                        }
                        return users;
        }

        @Override
        public boolean create(Connection connection, UserDTO user) throws DataException {
                final String method = "create";
                if (user == null) {
                        logger.warn("[{}] called with null user", method);
                        return false;
                }
                if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                        logger.warn("[{}] Missing password for user {}", method, user.getUsername());
                        return false;
                }

                String sql = "INSERT INTO user (username, first_name, last_name1, last_name2, birth_date, email, password, phone,"
                                + " role_id, address_id, active_status, created_at)"
                                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1, NOW())";

                String encryptedPassword = ENCRYPTOR.encryptPassword(user.getPassword());

                try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                        int idx = 1;
                        ps.setString(idx++, user.getUsername());
                        ps.setString(idx++, user.getFirstName());
                        ps.setString(idx++, user.getLastName1());
                        ps.setString(idx++, user.getLastName2());
                        setLocalDate(ps, idx++, user.getBirthDate());
                        ps.setString(idx++, user.getEmail());
                        ps.setString(idx++, encryptedPassword);
                        ps.setString(idx++, user.getPhone());
                        setInteger(ps, idx++, user.getRoleId());
                        setInteger(ps, idx++, user.getAddressId());

                        if (ps.executeUpdate() > 0) {
                                try (ResultSet keys = ps.getGeneratedKeys()) {
                                        if (keys.next()) {
                                                user.setUserId(Integer.valueOf(keys.getInt(1)));
                                        }
                                }
                                logger.info("[{}] Created user {} with id {}", method, user.getUsername(), user.getUserId());
                                return true;
                        }
                } catch (SQLException e) {
                        logger.error("[{}] Error creating user {}", method, user.getUsername(), e);
                        throw new DataException("Error creating user", e);
                }
                return false;
        }

        @Override
        public boolean update(Connection connection, UserDTO user) throws DataException {
                final String method = "update";
                if (user == null || user.getUserId() == null) {
                        logger.warn("[{}] called with null user or id", method);
                        return false;
                }

                List<Object> params = new ArrayList<Object>();
                StringBuilder sql = new StringBuilder();
                sql.append("UPDATE user SET username = ?, first_name = ?, last_name1 = ?, last_name2 = ?, birth_date = ?, ");
                sql.append("email = ?, phone = ?, role_id = ?, address_id = ?, updated_at = NOW()");

                params.add(user.getUsername());
                params.add(user.getFirstName());
                params.add(user.getLastName1());
                params.add(user.getLastName2());
                params.add(user.getBirthDate());
                params.add(user.getEmail());
                params.add(user.getPhone());
                params.add(user.getRoleId());
                params.add(user.getAddressId());

                if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
                        sql.append(", password = ?");
                        params.add(ENCRYPTOR.encryptPassword(user.getPassword()));
                }

                if (user.getActiveStatus() != null) {
                        sql.append(", active_status = ?");
                        params.add(Boolean.valueOf(user.getActiveStatus().booleanValue()));
                }

                sql.append(" WHERE user_id = ?");
                params.add(user.getUserId());

                try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
                        int idx = 1;
                        for (Object value : params) {
                                idx = bindValue(ps, idx, value);
                        }
                        int updated = ps.executeUpdate();
                        if (updated > 0) {
                                logger.info("[{}] Updated user id {}", method, user.getUserId());
                                return true;
                        }
                        logger.warn("[{}] No rows updated for user id {}", method, user.getUserId());
                } catch (SQLException e) {
                        logger.error("[{}] Error updating user id {}", method, user.getUserId(), e);
                        throw new DataException("Error updating user", e);
                }
                return false;
        }

        @Override
        public boolean delete(Connection connection, Integer userId) throws DataException {
                final String method = "delete";
                if (userId == null) {
                        logger.warn("[{}] called with null id", method);
                        return false;
                }
                String sql = "UPDATE user SET active_status = 0, updated_at = NOW() WHERE user_id = ?";

                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                        ps.setInt(1, userId.intValue());
                        int rows = ps.executeUpdate();
                        if (rows > 0) {
                                logger.info("[{}] User {} marked as inactive", method, userId);
                                return true;
                        }
                        logger.warn("[{}] No user deactivated for id {}", method, userId);
                } catch (SQLException e) {
                        logger.error("[{}] Error deactivating user {}", method, userId, e);
                        throw new DataException("Error deactivating user", e);
                }
                return false;
        }

        @Override
        public Results<UserDTO> findByCriteria(Connection connection, UserCriteria criteria) throws DataException {
                final String method = "findByCriteria";
                if (criteria == null) {
                        criteria = new UserCriteria();
                }
                criteria.normalize();

                Results<UserDTO> results = new Results<UserDTO>();
                List<Object> filters = new ArrayList<Object>();

                StringBuilder from = new StringBuilder(BASE_SELECT.substring(BASE_SELECT.indexOf("FROM")));
                StringBuilder where = new StringBuilder(" WHERE 1=1");

                if (criteria.getUserId() != null) {
                        where.append(" AND u.user_id = ?");
                        filters.add(criteria.getUserId());
                }
                if (criteria.getRoleId() != null) {
                        where.append(" AND u.role_id = ?");
                        filters.add(criteria.getRoleId());
                }
                if (criteria.getAddressId() != null) {
                        where.append(" AND u.address_id = ?");
                        filters.add(criteria.getAddressId());
                }
                if (criteria.getUsername() != null) {
                        where.append(" AND u.username LIKE ?");
                        filters.add(like(criteria.getUsername()));
                }
                if (criteria.getFirstName() != null) {
                        where.append(" AND u.first_name LIKE ?");
                        filters.add(like(criteria.getFirstName()));
                }
                if (criteria.getLastName1() != null) {
                        where.append(" AND u.last_name1 LIKE ?");
                        filters.add(like(criteria.getLastName1()));
                }
                if (criteria.getLastName2() != null) {
                        where.append(" AND u.last_name2 LIKE ?");
                        filters.add(like(criteria.getLastName2()));
                }
                if (criteria.getEmail() != null) {
                        where.append(" AND u.email LIKE ?");
                        filters.add(like(criteria.getEmail()));
                }
                if (criteria.getPhone() != null) {
                        where.append(" AND u.phone LIKE ?");
                        filters.add(like(criteria.getPhone()));
                }
                if (criteria.getBirthDateFrom() != null) {
                        where.append(" AND u.birth_date >= ?");
                        filters.add(criteria.getBirthDateFrom());
                }
                if (criteria.getBirthDateTo() != null) {
                        where.append(" AND u.birth_date <= ?");
                        filters.add(criteria.getBirthDateTo());
                }
                if (criteria.getCreatedAtFrom() != null) {
                        where.append(" AND u.created_at >= ?");
                        filters.add(criteria.getCreatedAtFrom());
                }
                if (criteria.getCreatedAtTo() != null) {
                        where.append(" AND u.created_at <= ?");
                        filters.add(criteria.getCreatedAtTo());
                }
                if (criteria.getUpdatedAtFrom() != null) {
                        where.append(" AND u.updated_at >= ?");
                        filters.add(criteria.getUpdatedAtFrom());
                }
                if (criteria.getUpdatedAtTo() != null) {
                        where.append(" AND u.updated_at <= ?");
                        filters.add(criteria.getUpdatedAtTo());
                }

                if (criteria.getActiveStatus() == null) {
                        where.append(" AND u.active_status = 1");
                } else {
                        where.append(" AND u.active_status = ?");
                        filters.add(Boolean.valueOf(criteria.getActiveStatus().booleanValue()));
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
                        logger.error("[{}] Error executing count", method, e);
                        throw new DataException("Error executing user count", e);
                }

                if (total == 0) {
                        results.setItems(Collections.<UserDTO>emptyList());
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

                List<UserDTO> items = new ArrayList<UserDTO>();
                try (PreparedStatement ps = connection.prepareStatement(selectSql)) {
                        int idx = bindFilters(ps, filters);
                        ps.setInt(idx++, pageSize);
                        ps.setInt(idx, offset);
                        try (ResultSet rs = ps.executeQuery()) {
                                while (rs.next()) {
                                        items.add(toUserDTO(rs, false));
                                }
                        }
                } catch (SQLException e) {
                        logger.error("[{}] Error executing paginated query", method, e);
                        throw new DataException("Error searching users", e);
                }

                results.setItems(items);
                results.setPage(page);
                results.setPageSize(pageSize);
                results.setTotal(total);
                results.normalize();
                return results;
        }

        @Override
        public boolean activate(Connection connection, Integer userId) throws DataException {
                final String method = "activate";
                if (userId == null) {
                        logger.warn("[{}] called with null id", method);
                        return false;
                }
                String sql = "UPDATE user SET active_status = 1, updated_at = NOW() WHERE user_id = ?";

                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                        ps.setInt(1, userId.intValue());
                        int rows = ps.executeUpdate();
                        if (rows > 0) {
                                logger.info("[{}] User {} reactivated", method, userId);
                                return true;
                        }
                        logger.warn("[{}] No user reactivated for id {}", method, userId);
                } catch (SQLException e) {
                        logger.error("[{}] Error activating user {}", method, userId, e);
                        throw new DataException("Error activating user", e);
                }
                return false;
        }

        private static void setLocalDate(PreparedStatement ps, int index, LocalDate value) throws SQLException {
                if (value == null) {
                        ps.setNull(index, java.sql.Types.DATE);
                } else {
                        ps.setDate(index, java.sql.Date.valueOf(value));
                }
        }

        private static void setInteger(PreparedStatement ps, int index, Integer value) throws SQLException {
                if (value == null) {
                        ps.setNull(index, java.sql.Types.INTEGER);
                } else {
                        ps.setInt(index, value.intValue());
                }
        }

        private static String like(String value) {
                return value == null ? null : "%" + value + "%";
        }

        private static int bindValue(PreparedStatement ps, int index, Object value) throws SQLException {
                if (value instanceof String) {
                        ps.setString(index++, (String) value);
                } else if (value instanceof Integer) {
                        ps.setInt(index++, ((Integer) value).intValue());
                } else if (value instanceof Boolean) {
                        ps.setInt(index++, ((Boolean) value).booleanValue() ? 1 : 0);
                } else if (value instanceof LocalDate) {
                        setLocalDate(ps, index++, (LocalDate) value);
                } else if (value instanceof LocalDateTime) {
                        LocalDateTime dt = (LocalDateTime) value;
                        ps.setTimestamp(index++, dt == null ? null : Timestamp.valueOf(dt));
                } else {
                        ps.setObject(index++, value);
                }
                return index;
        }

        private int bindFilters(PreparedStatement ps, List<Object> filters) throws SQLException {
                int idx = 1;
                for (Object value : filters) {
                        idx = bindValue(ps, idx, value);
                }
                return idx;
        }

        private String resolveOrderColumn(String requested) {
                String key = requested == null ? null : requested;
                String column = ORDER_BY_COLUMNS.get(key);
                return column != null ? column : "u.user_id";
        }

        private UserDTO toUserDTO(ResultSet rs, boolean authenticated) throws SQLException {
                UserDTO dto = new UserDTO();
                dto.setUserId(Integer.valueOf(rs.getInt("user_id")));
                dto.setUsername(rs.getString("username"));
                dto.setFirstName(rs.getString("first_name"));
                dto.setLastName1(rs.getString("last_name1"));
                dto.setLastName2(rs.getString("last_name2"));
                java.sql.Date birth = rs.getDate("birth_date");
                dto.setBirthDate(birth == null ? null : birth.toLocalDate());
                dto.setEmail(rs.getString("email"));
                dto.setPhone(rs.getString("phone"));
                dto.setRoleId(Integer.valueOf(rs.getInt("role_id")));
                dto.setAddressId(Integer.valueOf(rs.getInt("address_id")));
                dto.setActiveStatus(Boolean.valueOf(rs.getInt("active_status") == 1));
                Timestamp created = rs.getTimestamp("created_at");
                dto.setCreatedAt(created == null ? null : created.toLocalDateTime());
                Timestamp updated = rs.getTimestamp("updated_at");
                dto.setUpdatedAt(updated == null ? null : updated.toLocalDateTime());
                dto.setPassword(null); // never expose hashed password
                return dto;
        }
}