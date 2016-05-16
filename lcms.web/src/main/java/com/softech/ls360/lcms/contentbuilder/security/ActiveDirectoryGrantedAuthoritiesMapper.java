package com.softech.ls360.lcms.contentbuilder.security;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import com.softech.ls360.lcms.contentbuilder.model.VU360UserDetail;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;

import com.softech.ls360.lcms.contentbuilder.service.VU360UserService;

/**
 * Maps groups defined in LDAP to roles for a specific user.
 */
public class ActiveDirectoryGrantedAuthoritiesMapper implements LdapAuthoritiesPopulator  {

    // Constants for group defined in LDAP
   // private static final String ROLE_CONTENT_OWNER = "LS360-ContentOwner";
    /*private static final String ROLE_CONTENT_OWNER = "Administrator";
    private static final String ROLE_USER = "Normal User";*/
    
    @Autowired
    VU360UserService vu360Service;

    public ActiveDirectoryGrantedAuthoritiesMapper() {
    }
   
    @Override
	public Collection<? extends GrantedAuthority> getGrantedAuthorities(
			DirContextOperations userData, String username) {
		
		Set<LS360Authority> roles = EnumSet.noneOf(LS360Authority.class);
				
		VU360UserDetail  user = (VU360UserDetail) vu360Service.loadUserByUsername(username);
		if (user != null)	
		{
			roles.add(LS360Authority.ROLE_CONTENT_OWNER);
			roles.add(LS360Authority.ROLE_ADMIN);
			roles.add(LS360Authority.ROLE_USER);
		}	
	
		return roles;
	}
}