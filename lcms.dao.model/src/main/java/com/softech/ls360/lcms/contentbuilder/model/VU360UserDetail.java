package com.softech.ls360.lcms.contentbuilder.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.softech.ls360.lcms.contentbuilder.utils.TypeConvertor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@SuppressWarnings("deprecation")
public class VU360UserDetail extends
		org.springframework.security.core.userdetails.User {	
		//org.springframework.security.ldap.userdetails.LdapUserDetailsImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private final String userGUID;
	private final long vu360UserID;
	private final long contentOwnerId;
	private final long authorId;
	private final String userDisplayName;
	private final String emailAddress;
	private String userProfileImageURL;
	private boolean firstTimeLogin;
	private List<LMSUserRole> LMSUserRole = new ArrayList<LMSUserRole>();
	private Map<Integer, Boolean> featurePermissionMap = new HashMap<Integer, Boolean>();
	static List<GrantedAuthority> lst = new ArrayList<GrantedAuthority>();

	
	static {
		lst.add(new SimpleGrantedAuthority("ROLE_USER"));
	}

	// public VU360UserDetail(String userName, String password, String userGUID,
	// long userId, long cOId, long authorId) { //user.isEnabled()
	public VU360UserDetail(Object[] userFromDB) { // user.isEnabled()
		// super(userFromDB[1].toString(), userFromDB[2].toString(), true, true, true, true,new GrantedAuthority[]{ new GrantedAuthorityImpl("ROLE_USER") });
		 super(userFromDB[1].toString(), userFromDB[2].toString(),true, true, true, true, lst);
		this.userGUID = userFromDB[3].toString();
		this.vu360UserID = TypeConvertor.AnyToLong(userFromDB[0]);
		this.contentOwnerId = TypeConvertor.AnyToLong(userFromDB[4]);
		this.authorId = TypeConvertor.AnyToLong(userFromDB[5]);
		this.userDisplayName = userFromDB[7].toString() + " "
				+ userFromDB[8].toString();
		this.emailAddress = userFromDB[9].toString();
		this.firstTimeLogin = (Boolean)userFromDB[13];
	}

	public boolean isFirstTimeLogin() {
		return firstTimeLogin;
	}

	public void setFirstTimeLogin(boolean firstTimeLogin) {
		this.firstTimeLogin = firstTimeLogin;
	}

	public String getUserGUID() {
		return userGUID;
	}

	public long getVu360UserID() {
		return this.vu360UserID;
	}

	public long getContentOwnerId() {
		return contentOwnerId;
	}

	public long getAuthorId() {
		return authorId;
	}

	public String getUserDisplayName() {
		return userDisplayName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}
	
	public static List<GrantedAuthority> getLst() {
		return lst;
	}

	public static void setLst(List<GrantedAuthority> lst) {
		VU360UserDetail.lst = lst;
	}
	public List<LMSUserRole> getLMSUserRole() {
		return LMSUserRole;
	}

	public void setLMSUserRole(List<LMSUserRole> lMSUserRole) {
		LMSUserRole = lMSUserRole;
	}

	public String getUserProfileURL() {
		return userProfileImageURL;
	}

	public void setUserProfileURL(String userProfileURL) {
		this.userProfileImageURL = userProfileURL;
	}
	
	public void setFeaturePermissionMap(Map<Integer, Boolean> featurePermissionMap) {
		this.featurePermissionMap = featurePermissionMap;
	}

	public Boolean hasFeaturePermission(Integer permissionId){
		
		if(this.featurePermissionMap.containsKey(permissionId)){
			return this.featurePermissionMap.get(permissionId);
		}
		return false;
	}
	
	public int getFeaturePermissionCount(){
		
		 return this.featurePermissionMap.size();
	}
}
