package com.pinguela.rentexpres.model;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Criteria class to dynamically filter employees. Each non-null field will
 * generate a WHERE condition in DAO queries.
 */
public class EmployeeCriteria extends CriteriaBase {

        private static final long serialVersionUID = 1L;

        private static final Set<String> ORDERABLE = new HashSet<String>(
                        Arrays.asList("employee_id", "employee_name", "first_name", "last_name1", "email", "phone",
                                        "created_at", "updated_at"));

        private Integer Id; // employee.employee_id
        private String employeeName; // employee.employee_name (username/login)
        private Integer roleId; // FK → role.role_id
        private Integer headquartersId; // FK → headquarters.headquarters_id

        private String firstName; // employee.first_name
        private String lastName1; // employee.last_name1
        private String lastName2; // employee.last_name2
        private String email; // employee.email
        private String phone; // employee.phone

        private Boolean activeStatus; // employee.active_status (tinyint(1))

        private LocalDateTime createdAtFrom;
        private LocalDateTime createdAtTo;
        private LocalDateTime updatedAtFrom;
        private LocalDateTime updatedAtTo;

        public EmployeeCriteria() {
                super();
        }

        public Integer getEmployeeId() {
                return Id;
        }

        public void setEmployeeId(Integer Id) {
                this.Id = Id;
        }

        public String getEmployeeName() {
                return employeeName;
        }

        public void setEmployeeName(String employeeName) {
                this.employeeName = trimToNull(employeeName);
        }

        public Integer getRoleId() {
                return roleId;
        }

        public void setRoleId(Integer roleId) {
                this.roleId = roleId;
        }

        public Integer getHeadquartersId() {
                return headquartersId;
        }

        public void setHeadquartersId(Integer headquartersId) {
                this.headquartersId = headquartersId;
        }

        public String getFirstName() {
                return firstName;
        }

        public void setFirstName(String firstName) {
                this.firstName = trimToNull(firstName);
        }

        public String getLastName1() {
                return lastName1;
        }

        public void setLastName1(String lastName1) {
                this.lastName1 = trimToNull(lastName1);
        }

        public String getLastName2() {
                return lastName2;
        }

        public void setLastName2(String lastName2) {
                this.lastName2 = trimToNull(lastName2);
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = trimToNull(email);
        }

        public String getPhone() {
                return phone;
        }

        public void setPhone(String phone) {
                this.phone = trimToNull(phone);
        }

        public Boolean getActiveStatus() {
                return activeStatus;
        }

        public void setActiveStatus(Boolean activeStatus) {
                this.activeStatus = activeStatus;
        }

        @Override
        public Integer getPageNumber() {
                return super.getPageNumber();
        }

        @Override
        public void setPageNumber(Integer pageNumber) {
                super.setPageNumber(pageNumber);
        }

        @Override
        public Integer getPageSize() {
                return super.getPageSize();
        }

        @Override
        public void setPageSize(Integer pageSize) {
                super.setPageSize(pageSize);
        }

        public LocalDateTime getCreatedAtFrom() {
                return createdAtFrom;
        }

        public void setCreatedAtFrom(LocalDateTime createdAtFrom) {
                this.createdAtFrom = createdAtFrom;
        }

        public LocalDateTime getCreatedAtTo() {
                return createdAtTo;
        }

        public void setCreatedAtTo(LocalDateTime createdAtTo) {
                this.createdAtTo = createdAtTo;
        }

        public LocalDateTime getUpdatedAtFrom() {
                return updatedAtFrom;
        }

        public void setUpdatedAtFrom(LocalDateTime updatedAtFrom) {
                this.updatedAtFrom = updatedAtFrom;
        }

        public LocalDateTime getUpdatedAtTo() {
                return updatedAtTo;
        }

        public void setUpdatedAtTo(LocalDateTime updatedAtTo) {
                this.updatedAtTo = updatedAtTo;
        }

        public boolean isOrderable(String column) {
                return column != null && ORDERABLE.contains(column);
        }

        public String getSafeOrderBy() {
                String column = getOrderBy();
                return isOrderable(column) ? column : "employee_id";
        }

        @Override
        public void normalize() {
                super.normalize();
                if (createdAtFrom != null && createdAtTo != null && createdAtFrom.isAfter(createdAtTo)) {
                        LocalDateTime tmp = createdAtFrom;
                        createdAtFrom = createdAtTo;
                        createdAtTo = tmp;
                }
                if (updatedAtFrom != null && updatedAtTo != null && updatedAtFrom.isAfter(updatedAtTo)) {
                        LocalDateTime tmp = updatedAtFrom;
                        updatedAtFrom = updatedAtTo;
                        updatedAtTo = tmp;
                }
        }
}
