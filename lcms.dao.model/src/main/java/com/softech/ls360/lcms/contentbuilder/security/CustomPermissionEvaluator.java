package com.softech.ls360.lcms.contentbuilder.security;

import java.io.Serializable;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import com.softech.ls360.lcms.contentbuilder.model.VU360UserDetail;

public class CustomPermissionEvaluator implements PermissionEvaluator{
	
	@Override
	public boolean hasPermission(Authentication authentication,
			Object targetDomainObject, Object permission) {
		
		VU360UserDetail user = (VU360UserDetail) authentication.getPrincipal();
		return user.hasFeaturePermission((Integer) permission);
	}

	@Override
	public boolean hasPermission(Authentication authentication,
			Serializable targetId, String targetType, Object permission) {
		return false;
	}

}
