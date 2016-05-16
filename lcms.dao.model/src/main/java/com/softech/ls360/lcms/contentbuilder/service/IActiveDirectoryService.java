package com.softech.ls360.lcms.contentbuilder.service;

import com.softech.ls360.lcms.contentbuilder.model.ActiveDirectoryUser;
import com.softech.ls360.lcms.contentbuilder.model.VU360User;


public interface IActiveDirectoryService {
	public boolean isADIntegrationEnabled();
	public ActiveDirectoryUser updateUser(VU360User updatedUser);
	public ActiveDirectoryUser addUser(VU360User user);
	public boolean authenticateADUser(String user, String password);
	public boolean findADUser(String username);
	public void addAttribute(String userToUpdate);
}


