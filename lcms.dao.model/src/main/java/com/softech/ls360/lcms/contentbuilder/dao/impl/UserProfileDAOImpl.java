package com.softech.ls360.lcms.contentbuilder.dao.impl;


import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.softech.ls360.lcms.contentbuilder.dao.GenericDAOImpl;
import com.softech.ls360.lcms.contentbuilder.dao.UserProfileDAO;
import com.softech.ls360.lcms.contentbuilder.model.UserProfile;

@Repository
public class UserProfileDAOImpl extends GenericDAOImpl<UserProfile >implements UserProfileDAO{

	@Override
	@Transactional
	public UserProfile getUserProfile(long id) {
		
		return super.getById(id);
	}

	@Override
	@Transactional
	public UserProfile updateUserProfile(UserProfile userProfile) {
		
		return super.save(userProfile);
	}
}