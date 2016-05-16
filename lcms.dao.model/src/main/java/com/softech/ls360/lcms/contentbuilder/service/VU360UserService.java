package com.softech.ls360.lcms.contentbuilder.service;

import java.util.List;

import com.softech.ls360.lcms.contentbuilder.model.VU360User;

public interface VU360UserService extends org.springframework.security.core.userdetails.UserDetailsService {
	public VU360User getVU360User(String userName);
	public List<VU360User> getVU360User(String userEmail, String firstName, String lastName);
	public Boolean VU360UserAlreadyExist(String userName);
	public Boolean isLMSRoleExist(String lmsRole);
	public VU360User changeVU360UserPassword(VU360User vu360User, String newPasswrod) ;
	public void setAuthorBitInActiveDirectory(String userName);
	public boolean updateUserFirstTimeLogin(long userId, boolean value);
	//public UserDetails getVu360UserDetail();
	//public LdapUserDetailsImpl loadUserByUsername(String username);
			
		
}

