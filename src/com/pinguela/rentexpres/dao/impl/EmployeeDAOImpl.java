package com.pinguela.rentexpres.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jasypt.util.password.StrongPasswordEncryptor;

import com.pinguela.rentexpres.dao.EmployeeDAO;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.EmployeeCriteria;
import com.pinguela.rentexpres.model.EmployeeDTO;
import com.pinguela.rentexpres.model.Results;

public class EmployeeDAOImpl implements EmployeeDAO {

        private static final Logger logger = LogManager.getLogger(EmployeeDAOImpl.class);

        private static final StrongPasswordEncryptor ENCRYPTOR = new StrongPasswordEncryptor();

        private static final String BASE_SELECT = String.join(" ",
                        "SELECT e.employee_id, e.employee_name, e.password, e.role_id, e.headquarters_id,",
                        "       e.first_name, e.last_name1, e.last_name2, e.email, e.phone,",
                        "       e.active_status, e.created_at, e.updated_at",
                        "FROM employee e");

        private static final Map<String, String> ORDER_BY_COLUMNS = buildOrderColumns();

        private static Map<String, String> buildOrderColumns() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("employee_id", "e.employee_id");
                map.put("employee_name", "e.employee_name");
                map.put("first_name", "e.first_name");
                map.put("last_name1", "e.last_name1");
                map.put("email", "e.email");
                map.put("phone", "e.phone");
                map.put("created_at", "e.created_at");
                map.put("updated_at", "e.updated_at");
                return map;
        }

        @Override
        public EmployeeDTO findById(Connection connection, Integer id) throws DataException {
                final String method = "findById";
                if (id == null) {
                        logger.warn("[{}] called with null id", method);
                        return null;
                }

                String sql = BASE_SELECT + " WHERE e.employee_id = ? AND e.active_status = 1";

                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                        ps.setInt(1, id.intValue());
                        try (ResultSet rs = ps.executeQuery()) {
                                if (rs.next()) {
                                        logger.info("[{}] Employee found with id {}", method, id);
                                        return toEmployeeDTO(rs);
                                }
                        }
                        logger.warn("[{}] No employee found with id {}", method, id);
                } catch (SQLException e) {
                        logger.error("[{}] Error finding employee id {}", method, id, e);
                        throw new DataException("Error finding employee by id", e);
                }
                return null;
        }

        @Override
        public boolean create(Connection connection, EmployeeDTO employee) throws DataException {
                final String method = "create";
                if (employee == null) {
                        logger.warn("[{}] called with null employee", method);
                        return false;
                }
                if (employee.getPassword() == null || employee.getPassword().trim().isEmpty()) {
                        logger.warn("[{}] Missing password for employee {}", method, employee.getEmployeeName());
                        return false;
                }

                String sql = "INSERT INTO employee (employee_name, password, role_id, headquarters_id, first_name, last_name1,"
                                + " last_name2, email, phone, active_status, created_at)"
                                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 1, NOW())";

                String hashed = ENCRYPTOR.encryptPassword(employee.getPassword());

                try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                        int idx = 1;
                        ps.setString(idx++, employee.getEmployeeName());
                        ps.setString(idx++, hashed);
                        setInteger(ps, idx++, employee.getRoleId());
                        setInteger(ps, idx++, employee.getHeadquartersId());
                        ps.setString(idx++, employee.getFirstName());
                        ps.setString(idx++, employee.getLastName1());
                        ps.setString(idx++, employee.getLastName2());
                        ps.setString(idx++, employee.getEmail());
                        ps.setString(idx++, employee.getPhone());

                        if (ps.executeUpdate() > 0) {
                                try (ResultSet keys = ps.getGeneratedKeys()) {
                                        if (keys.next()) {
                                                employee.setEmployeeId(Integer.valueOf(keys.getInt(1)));
                                        }
                                }
                                logger.info("[{}] Created employee {} with id {}", method, employee.getEmployeeName(),
                                                employee.getEmployeeId());
                                return true;
                        }
                } catch (SQLException e) {
                        logger.error("[{}] Error creating employee {}", method, employee.getEmployeeName(), e);
                        throw new DataException("Error creating employee", e);
                }
                return false;
        }

        @Override
        public boolean update(Connection connection, EmployeeDTO employee) throws DataException {
                final String method = "update";
                if (employee == null || employee.getEmployeeId() == null) {
                        logger.warn("[{}] called with null employee or id", method);
                        return false;
                }

                StringBuilder sql = new StringBuilder();
                sql.append("UPDATE employee SET employee_name = ?, role_id = ?, headquarters_id = ?, first_name = ?, ");
                sql.append("last_name1 = ?, last_name2 = ?, email = ?, phone = ?, updated_at = NOW()");

                List<Object> params = new ArrayList<Object>();
                params.add(employee.getEmployeeName());
                params.add(employee.getRoleId());
                params.add(employee.getHeadquartersId());
                params.add(employee.getFirstName());
                params.add(employee.getLastName1());
                params.add(employee.getLastName2());
                params.add(employee.getEmail());
                params.add(employee.getPhone());

                if (employee.getPassword() != null && !employee.getPassword().trim().isEmpty()) {
                                sql.append(", password = ?");
                                params.add(ENCRYPTOR.encryptPassword(employee.getPassword()));
                }

                if (employee.getActiveStatus() != null) {
                        sql.append(", active_status = ?");
                        params.add(Boolean.valueOf(employee.getActiveStatus().booleanValue()));
                }

                sql.append(" WHERE employee_id = ?");
                params.add(employee.getEmployeeId());

                try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
                        int updated = ps.executeUpdate();
                        if (updated > 0) {
                                logger.info("[{}] Updated employee id {}", method, employee.getEmployeeId());
                                return true;
                        }
                        logger.warn("[{}] No rows updated for employee id {}", method, employee.getEmployeeId());
                } catch (SQLException e) {
                        logger.error("[{}] Error updating employee id {}", method, employee.getEmployeeId(), e);
                        throw new DataException("Error updating employee", e);
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
                String sql = "UPDATE employee SET active_status = 0, updated_at = NOW() WHERE employee_id = ?";

                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                        ps.setInt(1, id.intValue());
                        int rows = ps.executeUpdate();
                        if (rows > 0) {
                                logger.info("[{}] Employee {} marked as inactive", method, id);
                                return true;
                        }
                        logger.warn("[{}] No employee deactivated for id {}", method, id);
                } catch (SQLException e) {
                        logger.error("[{}] Error deactivating employee {}", method, id, e);
                        throw new DataException("Error deactivating employee", e);
                }
                return false;
        }

        @Override
        public List<EmployeeDTO> findAll(Connection connection) throws DataException {
                final String method = "findAll";
                List<EmployeeDTO> employees = new ArrayList<EmployeeDTO>();
                String sql = BASE_SELECT + " WHERE e.active_status = 1 ORDER BY e.employee_id";

                try (PreparedStatement ps = connection.prepareStatement(sql);
                                ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                                employees.add(toEmployeeDTO(rs));
                        }
                        logger.info("[{}] Retrieved {} active employees", method, Integer.valueOf(employees.size()));
                } catch (SQLException e) {
                        logger.error("[{}] Error retrieving all employees", method, e);
                        throw new DataException("Error retrieving all employees", e);
                }
                return employees;
        }

        @Override
        public EmployeeDTO authenticate(Connection connection, String login, String clearPassword) throws DataException {
                final String method = "authenticate";
                if (login == null || clearPassword == null) {
                        logger.warn("[{}] called with null credentials", method);
                        return null;
                }

                String trimmedLogin = login.trim();
                boolean isEmail = trimmedLogin.contains("@");

                String sql = BASE_SELECT + " WHERE e.active_status = 1 AND "
                                + (isEmail ? "LOWER(e.email) = LOWER(?)" : "e.employee_name = ?");

                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                        ps.setString(1, trimmedLogin);
                        try (ResultSet rs = ps.executeQuery()) {
                                if (rs.next()) {
                                        String hashed = rs.getString("password");
                                        if (hashed != null && ENCRYPTOR.checkPassword(clearPassword, hashed)) {
                                                logger.info("[{}] Employee authenticated: {}", method, trimmedLogin);
                                                return toEmployeeDTO(rs);
                                        }
                                        logger.warn("[{}] Invalid password for {}", method, trimmedLogin);
                                } else {
                                        logger.warn("[{}] No employee found for login {}", method, trimmedLogin);
                                }
                        }
                } catch (SQLException e) {
                        logger.error("[{}] Error authenticating {}", method, trimmedLogin, e);
                        throw new DataException("Error authenticating employee", e);
                }
                return null;
        }

        @Override
        public Results<EmployeeDTO> findByCriteria(Connection connection, EmployeeCriteria criteria) throws DataException {
                final String method = "findByCriteria";
                if (criteria == null) {
                        criteria = new EmployeeCriteria();
                }
                criteria.normalize();

                Results<EmployeeDTO> results = new Results<EmployeeDTO>();
                List<Object> filters = new ArrayList<Object>();

                StringBuilder from = new StringBuilder(BASE_SELECT.substring(BASE_SELECT.indexOf("FROM")));
                StringBuilder where = new StringBuilder(" WHERE 1=1");

                if (criteria.getEmployeeId() != null) {
                        where.append(" AND e.employee_id = ?");
                        filters.add(criteria.getEmployeeId());
                }
                if (criteria.getEmployeeName() != null) {
                        where.append(" AND e.employee_name LIKE ?");
                        filters.add(like(criteria.getEmployeeName()));
                }
                if (criteria.getRoleId() != null) {
                        where.append(" AND e.role_id = ?");
                        filters.add(criteria.getRoleId());
                }
                if (criteria.getHeadquartersId() != null) {
                        where.append(" AND e.headquarters_id = ?");
                        filters.add(criteria.getHeadquartersId());
                }
                if (criteria.getFirstName() != null) {
                        where.append(" AND e.first_name LIKE ?");
                        filters.add(like(criteria.getFirstName()));
                }
                if (criteria.getLastName1() != null) {
                        where.append(" AND e.last_name1 LIKE ?");
                        filters.add(like(criteria.getLastName1()));
                }
                if (criteria.getLastName2() != null) {
                        where.append(" AND e.last_name2 LIKE ?");
                        filters.add(like(criteria.getLastName2()));
                }
                if (criteria.getEmail() != null) {
                        where.append(" AND e.email LIKE ?");
                        filters.add(like(criteria.getEmail()));
                }
                if (criteria.getPhone() != null) {
                        where.append(" AND e.phone LIKE ?");
                        filters.add(like(criteria.getPhone()));
                }
                if (criteria.getCreatedAtFrom() != null) {
                        where.append(" AND e.created_at >= ?");
                        filters.add(criteria.getCreatedAtFrom());
                }
                if (criteria.getCreatedAtTo() != null) {
                        where.append(" AND e.created_at <= ?");
                        filters.add(criteria.getCreatedAtTo());
                }
                if (criteria.getUpdatedAtFrom() != null) {
                        where.append(" AND e.updated_at >= ?");
                        filters.add(criteria.getUpdatedAtFrom());
                }
                if (criteria.getUpdatedAtTo() != null) {
                        where.append(" AND e.updated_at <= ?");
                        filters.add(criteria.getUpdatedAtTo());
                }

                if (criteria.getActiveStatus() == null) {
                        where.append(" AND e.active_status = 1");
                } else {
                        where.append(" AND e.active_status = ?");
                        filters.add(Boolean.valueOf(criteria.getActiveStatus().booleanValue()));
                }

                String countSql = "SELECT COUNT(1) " + from.toString() + where.toString();

                int page = criteria.getSafePage();
                int pageSize = criteria.getSafePageSize();
                int total;

                try (PreparedStatement ps = connection.prepareStatement(countSql)) {
                        bind(ps, filters);
                        try (ResultSet rs = ps.executeQuery()) {
                                total = rs.next() ? rs.getInt(1) : 0;
                        }
                } catch (SQLException e) {
                        logger.error("[{}] Error executing count", method, e);
                        throw new DataException("Error executing employee count", e);
                }

                if (total == 0) {
                        results.setItems(Collections.<EmployeeDTO>emptyList());
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

                List<EmployeeDTO> items = new ArrayList<EmployeeDTO>();
                try (PreparedStatement ps = connection.prepareStatement(selectSql)) {
                        int idx = bind(ps, filters);
                        ps.setInt(idx++, pageSize);
                        ps.setInt(idx, offset);
                        try (ResultSet rs = ps.executeQuery()) {
                                while (rs.next()) {
                                        items.add(toEmployeeDTO(rs));
                                }
                        }
                } catch (SQLException e) {
                        logger.error("[{}] Error executing paginated query", method, e);
                        throw new DataException("Error searching employees", e);
                }

                results.setItems(items);
                results.setPage(page);
                results.setPageSize(pageSize);
                results.setTotal(total);
                results.normalize();
                return results;
        }

        @Override
        public boolean activate(Connection connection, Integer employeeId) throws DataException {
                final String method = "activate";
                if (employeeId == null) {
                        logger.warn("[{}] called with null id", method);
                        return false;
                }
                String sql = "UPDATE employee SET active_status = 1, updated_at = NOW() WHERE employee_id = ?";

                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                        ps.setInt(1, employeeId.intValue());
                        int rows = ps.executeUpdate();
                        if (rows > 0) {
                                logger.info("[{}] Employee {} reactivated", method, employeeId);
                                return true;
                        }
                        logger.warn("[{}] No employee reactivated for id {}", method, employeeId);
                } catch (SQLException e) {
                        logger.error("[{}] Error activating employee {}", method, employeeId, e);
                        throw new DataException("Error activating employee", e);
                }
                return false;
        }

        private static String like(String value) {
                return value == null ? null : "%" + value + "%";
        }

        private static void setInteger(PreparedStatement ps, int index, Integer value) throws SQLException {
                if (value == null) {
                        ps.setNull(index, java.sql.Types.INTEGER);
                } else {
                        ps.setInt(index, value.intValue());
                }
        }

        private int bind(PreparedStatement ps, List<Object> values) throws SQLException {
                int idx = 1;
                for (Object value : values) {
                        if (value instanceof String) {
                                ps.setString(idx++, (String) value);
                        } else if (value instanceof Integer) {
                                ps.setInt(idx++, ((Integer) value).intValue());
                        } else if (value instanceof Boolean) {
                                ps.setInt(idx++, ((Boolean) value).booleanValue() ? 1 : 0);
                        } else if (value instanceof LocalDateTime) {
                                LocalDateTime dt = (LocalDateTime) value;
                                ps.setTimestamp(idx++, dt == null ? null : Timestamp.valueOf(dt));
                        } else {
                                ps.setObject(idx++, value);
                        }
                }
                return idx;
        }

        private String resolveOrderColumn(String requested) {
                String key = requested == null ? null : requested;
                String column = ORDER_BY_COLUMNS.get(key);
                return column != null ? column : "e.employee_id";
        }

        private EmployeeDTO toEmployeeDTO(ResultSet rs) throws SQLException {
                EmployeeDTO dto = new EmployeeDTO();
                dto.setEmployeeId(Integer.valueOf(rs.getInt("employee_id")));
                dto.setEmployeeName(rs.getString("employee_name"));
                dto.setRoleId(Integer.valueOf(rs.getInt("role_id")));
                dto.setHeadquartersId(Integer.valueOf(rs.getInt("headquarters_id")));
                dto.setFirstName(rs.getString("first_name"));
                dto.setLastName1(rs.getString("last_name1"));
                dto.setLastName2(rs.getString("last_name2"));
                dto.setEmail(rs.getString("email"));
                dto.setPhone(rs.getString("phone"));
                dto.setActiveStatus(Boolean.valueOf(rs.getInt("active_status") == 1));
                Timestamp created = rs.getTimestamp("created_at");
                dto.setCreatedAt(created == null ? null : created.toLocalDateTime());
                Timestamp updated = rs.getTimestamp("updated_at");
                dto.setUpdatedAt(updated == null ? null : updated.toLocalDateTime());
                dto.setPassword(null);
                return dto;
        }
}
