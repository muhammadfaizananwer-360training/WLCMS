package com.softech.ls360.lcms.contentbuilder.model;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;


/**
 * @author Muhamamd Saleem
 */

@Entity 

@NamedNativeQueries({
	@NamedNativeQuery(
        name="LMSUserRole.getLMSUserRoleByUserId",
		query=" SELECT ROLE_ID,ROLENAME,ROLE_TYPE,USER_ID FROM VU360USER_ROLE UR   "
					+ " INNER JOIN LMSROLE LR ON LR.ID = UR.ROLE_ID "
					+ " WHERE UR.USER_ID=:VU360USER_ID",
		resultClass=com.softech.ls360.lcms.contentbuilder.model.LMSUserRole.class
	)
})
//resultSetMapping="getLMSUserRoleByUserId"
/* WHERE UR.USER_ID=:VU360USER_ID
@SqlResultSetMappings({ 
	 @SqlResultSetMapping(
	    name="getLMSUserRoleByUserId", 
		classes = {
		    @ConstructorResult(targetClass = LMSUserRole.class,
			    columns={
		    		@ColumnResult(name = "ROLE_ID", type=BigInteger.class ),
		            @ColumnResult(name = "ROLENAME" ),
		        	@ColumnResult(name = "ROLE_TYPE")
				}
			)
		}
	) 
})
*/

public class LMSUserRole  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="ROLE_ID")
	private BigInteger roleId;
	
	@Column(name="ROLENAME")
	private String roleName = null;
	
	@Column(name="ROLE_TYPE")
	private String roleType = null;
	
	/**/
	@Column(name="USER_ID")
	private long VU360USER_ID;
	

	public LMSUserRole() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LMSUserRole(BigInteger roleId, String roleName, String roleType,
			long uSER_ID) {
		super();
		this.roleId = roleId;
		this.roleName = roleName;
		this.roleType = roleType;
		this.VU360USER_ID = uSER_ID;
	}

	@Override
	public String toString() {
		return "LMSUserRole [roleId=" + roleId + ", roleName=" + roleName
				+ ", roleType=" + roleType + ", USER_ID=" + VU360USER_ID + "]";
	}

	public BigInteger getRoleId() {
		return roleId;
	}
	public void setRoleId(BigInteger roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getRoleType() {
		return roleType;
	}
	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}
	public long getUSER_ID() {
		return this.VU360USER_ID;
	}
	public void setUSER_ID(long uSER_ID) {
		VU360USER_ID = uSER_ID;
	}
}
