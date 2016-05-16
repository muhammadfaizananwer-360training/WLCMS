package com.softech.ls360.lcms.contentbuilder.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.softech.ls360.lcms.contentbuilder.dao.LMSUserRoleDAO;
import com.softech.ls360.lcms.contentbuilder.dao.SPCallingParams;
import com.softech.ls360.lcms.contentbuilder.dao.VU360UserDAO;
import com.softech.ls360.lcms.contentbuilder.model.LMSUserRole;
import com.softech.ls360.lcms.contentbuilder.model.VU360User;
import com.softech.ls360.lcms.contentbuilder.model.VU360UserDetail;
import com.softech.ls360.lcms.contentbuilder.service.IActiveDirectoryService;
import com.softech.ls360.lcms.contentbuilder.service.IAssetService;
import com.softech.ls360.lcms.contentbuilder.service.VU360UserService;
import com.softech.ls360.lcms.contentbuilder.utils.LCMSProperties;
import com.softech.ls360.lcms.contentbuilder.utils.LCMS_Util;

//http://forum.spring.io/forum/spring-projects/security/98705-spring-security-3-with-hibernate-cannot-login

public class VU360UserServiceImpl implements VU360UserService {

	private static Logger logger = LoggerFactory.getLogger(VU360UserServiceImpl.class);
	
	private VU360UserDAO vu360UserDAO;
	
	@Autowired
	private LMSUserRoleDAO lmsUserRoleDAO;
	
	@Autowired
	private IAssetService asssetService;
	
	private PasswordEncoder passwordEncoder;
	
	private SaltSource saltSource;
	
	private IActiveDirectoryService activeDirectoryService;

	public VU360UserDAO getVu360UserDAO() {
		return vu360UserDAO;
	}

	public void setVu360UserDAO(VU360UserDAO vu360UserDAO) {
		this.vu360UserDAO = vu360UserDAO;
	}

	public LMSUserRoleDAO getLmsUserRoleDAO() {
		return lmsUserRoleDAO;
	}

	public void setLmsUserRoleDAO(LMSUserRoleDAO lmsUserRoleDAO) {
		this.lmsUserRoleDAO = lmsUserRoleDAO;
	}

	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public SaltSource getSaltSource() {
		return saltSource;
	}

	public void setSaltSource(SaltSource saltSource) {
		this.saltSource = saltSource;
	}

	

	@Transactional
	public VU360User getVU360User(String userName) {

		SPCallingParams sparam1 = LCMS_Util.createSPObject("USERNAME", userName, String.class, ParameterMode.IN);
		Object[] userResultFromDB = vu360UserDAO.callStoredProc("LCMS_GET_VU360USER_BY_USERNAME", sparam1).toArray();
		VU360User vU360User=null;

		if (userResultFromDB.length > 0) {
			Object[] usr = (Object[]) userResultFromDB[0]; 
			vU360User = new VU360User();
			
			vU360User.setId((Long.parseLong(usr[0].toString())));
			vU360User.setUsername((String) usr[1]);
			vU360User.setPassword((String) usr[2]);
			vU360User.setUserGUID((String) usr[3]);
			vU360User.setContentId((Long.parseLong(usr[4].toString() )));
			vU360User.setAuthorId((Long.parseLong(usr[5].toString())));
			vU360User.setMiddleName((String) usr[6]);
			vU360User.setFirstName((String) usr[7]);
			vU360User.setLastName((String) usr[8]);
			vU360User.setEmailAddress( (String) usr[9]);
			vU360User.setCreatedDate( (Timestamp) usr[10]);
			vU360User.setLastUpdatedDate( (Timestamp) usr[11]);
		}
		
		return vU360User;
	}

	@Transactional
	public VU360User changeVU360UserPassword(VU360User vu360User, String plainPassword_) {

		Object salt = saltSource.getSalt(vu360User);
		@SuppressWarnings("deprecation")
		String encodedPassword = passwordEncoder.encodePassword( plainPassword_ , salt);
		
		vu360User.setPassword(encodedPassword);
		vu360User.setPassWordChanged(true);
		SPCallingParams sparam1 = LCMS_Util.createSPObject("ID", String.valueOf(vu360User.getId()) , BigDecimal.class, ParameterMode.IN);
		SPCallingParams sparam2 = LCMS_Util.createSPObject("PASSWORD_2", String.valueOf(encodedPassword) , String.class, ParameterMode.IN);
		
		StoredProcedureQuery qr1 = vu360UserDAO.createQueryParameters("UPDATE_VU360USER_PASSWORD", sparam1, sparam2 );		
		qr1.execute();
		logger.info("User updated successfully with (PlainPwd:"+plainPassword_ + "UserId"+sparam1+"  :  encodedPwd:"+sparam2 );

		logger.info("Is AD integrated  ("+activeDirectoryService.isADIntegrationEnabled());
		
		if (activeDirectoryService.isADIntegrationEnabled()){//if success and AD is enabled
			vu360User.setPassword(plainPassword_);// we require plain password in AD
			activeDirectoryService.updateUser(vu360User); //edit user to AD
			vu360User.setPassword(encodedPassword);// reset the password to encrypted
		}	
		
		return vu360User;
		
	}
	
	@Transactional
	public List<VU360User> getVU360User(String userEmail, String firstName, String lastName)
	{
		SPCallingParams sparam1 = LCMS_Util.createSPObject("EMAIL", userEmail, String.class, ParameterMode.IN);
		SPCallingParams sparam2 = LCMS_Util.createSPObject("FIRSTNAME", firstName, String.class, ParameterMode.IN);
		SPCallingParams sparam3 = LCMS_Util.createSPObject("LASTNAME", lastName, String.class, ParameterMode.IN);
		List ls = vu360UserDAO.callStoredProc("LCMS_GET_VU360USER_BY_EMAIL_FIRSTNAME_LASTNAME", sparam1, sparam2, sparam3);
		List<VU360User> userList = null; 
		if(ls!=null && ls.size()>0)
		{	

			userList = new ArrayList<VU360User>();
			Object[] userResultFromDB = ls.toArray();
			
			for (int i = 0; i < userResultFromDB.length; i++) {
				Object[] usr = (Object[]) userResultFromDB[i];
				VU360User vU360User = new VU360User();
				vU360User.setId((Long.parseLong(usr[0].toString())));
				vU360User.setUsername((String) usr[1]);
				vU360User.setPassword((String) usr[2]);
				vU360User.setUserGUID((String) usr[3]);
				vU360User.setContentId((Long.parseLong(usr[4].toString() )));
				vU360User.setAuthorId((Long.parseLong(usr[5].toString())));
				vU360User.setMiddleName((String) usr[6]);
				vU360User.setFirstName((String) usr[7]);
				vU360User.setLastName((String) usr[8]);
				vU360User.setEmailAddress( (String) usr[9]);
				vU360User.setCreatedDate( (Timestamp) usr[10]);
				vU360User.setLastUpdatedDate( (Timestamp) usr[11]);
				userList.add(vU360User);
			}
		
		}
		
		return userList;
		
	}
	@Transactional
	@Override
	public User loadUserByUsername(String username)
			throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		System.out.println("username:" + username);

		SPCallingParams sparam1 = LCMS_Util.createSPObject("USERNAME",
				username, String.class, ParameterMode.IN);

		Object[] userResultFromDB = vu360UserDAO.callStoredProc(
				"LCMS_GET_VU360USER_BY_USERNAME", sparam1).toArray();

		if (userResultFromDB.length > 0) {
			System.out.println("user found:");
			// super.loadUserByUsername( userNameUtils.getPrincipleName(
			// username ) );
			VU360UserDetail user = new VU360UserDetail(	(Object[]) userResultFromDB[0]);
			
			Object[] userFromDB = (Object[]) userResultFromDB[0];
			
			if(userFromDB[12] != null){
				try {
					user.setUserProfileURL(LCMSProperties.getLCMSProperty("code.lcms.assets.URL") + asssetService.getAssetLocation((Integer)userFromDB[12]));
				} catch (Exception e) {
					logger.error("EXception at loadUserByUsername::" + e.getMessage());
				}
			}
			
			if (user != null){
				List<LMSUserRole> lmsUserRole = lmsUserRoleDAO.getLMSUserRoleByUserId(BigInteger.valueOf(user.getVu360UserID()));
				user.setLMSUserRole(lmsUserRole);
				user.setFeaturePermissionMap(this.getFeaturePermissions(username));
			}
			
			//isLMSRoleExist("ROLE_LEARNER");
						
			return user;
		}
		
		else {
			System.out.println("user not found:");
			throw new UsernameNotFoundException("user not found");
		}

	}

	private Map<Integer, Boolean> getFeaturePermissions(String userName){
		
		SPCallingParams sparam1 = LCMS_Util.createSPObject("USERNAME",
				userName, String.class, ParameterMode.IN);

		Object[] featurePermissionResultFromDB = vu360UserDAO.callStoredProc(
				"GET_USERAUTHENTICATION", sparam1).toArray();

		Map<Integer, Boolean> permissionMap = new HashMap<Integer, Boolean>();
		if (featurePermissionResultFromDB.length > 0) {
			
			for(Object permssionId: featurePermissionResultFromDB){
				permissionMap.put(((BigDecimal)permssionId).intValue() , true); 
			}
		}
		return permissionMap;
	}
	@Transactional
	@Override
	public Boolean VU360UserAlreadyExist(String userName) {

		SPCallingParams sparam1 = LCMS_Util.createSPObject("USERNAME",userName, String.class, ParameterMode.IN);
		Object[] userResultFromDB = vu360UserDAO.callStoredProc("LCMS_WEB_CHECKUSEREXIST", sparam1).toArray();
		
		if(userResultFromDB!=null && userResultFromDB.length>0)
			return true;
		return false;
		
	}
	
	public Boolean isLMSRoleExist(String lmsRole)
	{
		//VU360UserDetail user = (VU360UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		VU360UserDetail user  = (VU360UserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<LMSUserRole> lmsUserRoles = ((VU360UserDetail)user ).getLMSUserRole();
		if(lmsUserRoles != null){
			for (LMSUserRole lmsUserRole : lmsUserRoles) {
				if (lmsUserRole.getRoleType().equalsIgnoreCase(lmsRole))
					return true;
			}
		}
				
		return false;
		/**/
		//return true;
		
	}

	public IActiveDirectoryService getActiveDirectoryService() {
		return activeDirectoryService;
	}

	public void setActiveDirectoryService(
			IActiveDirectoryService activeDirectoryService) {
		this.activeDirectoryService = activeDirectoryService;
	}

	
	public void setAuthorBitInActiveDirectory(String userName){
		activeDirectoryService.addAttribute(userName);
	}
	
	@Override
	public boolean updateUserFirstTimeLogin(long userId, boolean value){
		return this.vu360UserDAO.updateUserFirstTimeLogin(userId, value);
	}
}
