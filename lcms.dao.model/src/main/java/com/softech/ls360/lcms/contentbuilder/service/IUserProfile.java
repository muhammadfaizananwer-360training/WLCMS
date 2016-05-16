package com.softech.ls360.lcms.contentbuilder.service;

import com.softech.ls360.lcms.contentbuilder.model.SlideAsset;
import com.softech.ls360.lcms.contentbuilder.model.UserProfile;

public interface IUserProfile {

	UserProfile getUserProfile(long id);
	void updateUserProfile(UserProfile userProfile);
	public SlideAsset getAssetSlide(UserProfile userProfile) throws Exception;
}