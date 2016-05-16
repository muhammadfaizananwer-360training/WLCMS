package com.softech.ls360.lcms.contentbuilder.dao;

 
import com.softech.ls360.lcms.contentbuilder.model.VU360User;

public interface VU360UserDAO extends GenericDAO<VU360User> {
	
	boolean updateUserFirstTimeLogin(long userId, boolean value);
}


