package com.softech.ls360.lcms.contentbuilder.security;

import java.util.Collection;

//import javax.persistence.ParameterMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

import com.softech.ls360.lcms.contentbuilder.dao.SPCallingParams;
import com.softech.ls360.lcms.contentbuilder.dao.VU360UserDAO;
import com.softech.ls360.lcms.contentbuilder.model.VU360UserDetail;
import com.softech.ls360.lcms.contentbuilder.utils.LCMS_Util;

class CustomUserDetailsContextMapper implements UserDetailsContextMapper {

	@Autowired
	private VU360UserDAO vu360UserDAO;
	

	   @Override
	   public UserDetails mapUserFromContext(DirContextOperations ctx, String username, 
			   Collection<? extends GrantedAuthority> authorities) {
		   System.out.println("username:"+username);
			 
			
			SPCallingParams sparam1 =  null ;//LCMS_Util.createSPObject("USERNAME", username , String.class, ParameterMode.IN);
			
			Object[] userResultFromDB  =  vu360UserDAO.callStoredProc("LCMS_GET_VU360USER_BY_USERNAME",  sparam1).toArray() ; 
			
			if (userResultFromDB.length>0) {
				System.out.println("user found:");			
				//super.loadUserByUsername( userNameUtils.getPrincipleName( username ) );
				VU360UserDetail user = new VU360UserDetail((Object[]) userResultFromDB[0]);				
				return user;
			}
			
			else { 
	        	System.out.println("user not found:");
	          throw new UsernameNotFoundException("user not found"); 
	        }
	   }

	   @Override
	   public void mapUserToContext(UserDetails arg0, DirContextAdapter arg1) {
	      
		   System.out.println("CustomUserDetailsContextMapper::mapUserToContext " + arg0.getUsername());
		   
		   throw new IllegalStateException("Only retrieving data from AD is currently supported");    
	   }

	

	}