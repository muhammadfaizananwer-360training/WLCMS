package com.softech.ls360.lcms.contentbuilder.security;

import org.springframework.security.core.GrantedAuthority;


public enum  LS360Authority implements GrantedAuthority  {
	ROLE_CONTENT_OWNER, ROLE_ADMIN, ROLE_USER;

	    public String getAuthority() {
	        return name();
	    }
}

