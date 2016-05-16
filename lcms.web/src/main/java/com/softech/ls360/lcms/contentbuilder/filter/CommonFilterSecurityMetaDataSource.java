package com.softech.ls360.lcms.contentbuilder.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

public class CommonFilterSecurityMetaDataSource implements FilterInvocationSecurityMetadataSource{
	
	

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
		List<ConfigAttribute> attributes = new ArrayList<ConfigAttribute>();
		if(attributes.isEmpty())
        {
                attributes.add(new SecurityConfig("IS_NOT_AUTHENTICATED_FULLY"));
        }
        return attributes;
		
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return true;
	}
	
	
	
	
	
	
	
	

}
