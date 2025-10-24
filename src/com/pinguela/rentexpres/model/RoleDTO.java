package com.pinguela.rentexpres.model;

public class RoleDTO extends ValueObject {

        /**
	 * 
	 */
        private static final long serialVersionUID = 1L;
        private Integer roleId;     // role.role_id

        /** Maximum length: 50 characters. */
        private String roleName;    // role.role_name

        public RoleDTO() {
        	super();
        }

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
        
        
}
