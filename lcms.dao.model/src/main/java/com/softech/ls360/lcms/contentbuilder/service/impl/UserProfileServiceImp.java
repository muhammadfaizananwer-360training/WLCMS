package com.softech.ls360.lcms.contentbuilder.service.impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softech.ls360.lcms.contentbuilder.dao.UserProfileDAO;
import com.softech.ls360.lcms.contentbuilder.model.SlideAsset;
import com.softech.ls360.lcms.contentbuilder.model.UserProfile;
import com.softech.ls360.lcms.contentbuilder.service.ISlideService;
import com.softech.ls360.lcms.contentbuilder.service.IUserProfile;
import com.softech.ls360.lcms.contentbuilder.utils.LCMSProperties;

@Service
public class UserProfileServiceImp implements IUserProfile{

	@Autowired
	UserProfileDAO userProfileDAO;
	
	@Autowired
	ISlideService slideService;
	
	@Override
	public UserProfile getUserProfile(long id) {
		
		return userProfileDAO.getUserProfile(id);
	}

	@Override
	public void updateUserProfile(UserProfile userProfile) {
		
		 userProfileDAO.updateUserProfile(userProfile);
	}
	
	@Override
	public SlideAsset getAssetSlide(UserProfile userProfile) throws Exception {
		
		List<SlideAsset> lstAssets = slideService.getVisualAssetBySlideId((long)userProfile.getProfileImageAssetId(), "1");
		SlideAsset asset = null;
	
		if(lstAssets.get(0) != null){
			String locationPath = LCMSProperties
					.getLCMSProperty("code.lcms.assets.URL");
			asset = (SlideAsset) lstAssets.get(0);
			asset.setStreamingServerPath(locationPath
					+ asset.getLocation());
			asset.setLocation(locationPath + asset.getLocation());
		}
		return asset;
	}
}