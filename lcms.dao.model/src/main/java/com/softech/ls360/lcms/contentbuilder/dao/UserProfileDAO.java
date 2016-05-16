package com.softech.ls360.lcms.contentbuilder.dao;


import org.springframework.transaction.annotation.Transactional;

import com.softech.ls360.lcms.contentbuilder.model.UserProfile;

public interface UserProfileDAO extends GenericDAO<UserProfile>{
	
	public UserProfile getUserProfile(long id);
	@Transactional
	public UserProfile updateUserProfile(UserProfile userProfile);
}
