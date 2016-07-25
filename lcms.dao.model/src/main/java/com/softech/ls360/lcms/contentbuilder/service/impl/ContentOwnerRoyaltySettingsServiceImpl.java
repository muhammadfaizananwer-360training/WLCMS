package com.softech.ls360.lcms.contentbuilder.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import com.softech.ls360.lcms.contentbuilder.dao.ContentOwnerRoyaltySettingsDAO;
import com.softech.ls360.lcms.contentbuilder.model.ContentOwnerRoyaltySettings;
import com.softech.ls360.lcms.contentbuilder.service.IContentOwnerRoyaltySettingsService;

public class ContentOwnerRoyaltySettingsServiceImpl implements IContentOwnerRoyaltySettingsService{

	@Autowired
	ContentOwnerRoyaltySettingsDAO royaltySettingsDAO;
	
	@Override
	@PreAuthorize("hasPermission(#firstOrLastName, @userFeature.contentOwnerRoyaltySettings)")
	public List<ContentOwnerRoyaltySettings> searchContentOwners(
			String firstOrLastName) {
		
		return royaltySettingsDAO.searchContentOwners(firstOrLastName);
	}

	@Override
	@PreAuthorize("hasPermission(#firstOrLastName, @userFeature.contentOwnerRoyaltySettings)")
	public boolean updateRoyaltSettings(int contentOwnerId,
			float onlineRoyaltyPercentage, float classroomRoyaltyPercentage,
			float webinarRoyaltyPercentage) {
		
		return royaltySettingsDAO.updateRoyaltSettings(contentOwnerId, onlineRoyaltyPercentage, classroomRoyaltyPercentage, webinarRoyaltyPercentage);
	}

	public ContentOwnerRoyaltySettingsDAO getRoyaltySettingsDAO() {
		return royaltySettingsDAO;
	}

	public void setRoyaltySettingsDAO(
			ContentOwnerRoyaltySettingsDAO royaltySettingsDAO) {
		this.royaltySettingsDAO = royaltySettingsDAO;
	}

	@Override
	//@PreAuthorize("hasPermission(#firstOrLastName, T(com.softech.ls360.lcms.contentbuilder.utils.UserFeature).contentOwnerRoyaltySettings)")
	public ContentOwnerRoyaltySettings getContentOwnerById(Integer cId) {
		
		return royaltySettingsDAO.getContentOwnerById(cId);
	}
	
	@Override
	//@PreAuthorize("hasPermission(#firstOrLastName, T(com.softech.ls360.lcms.contentbuilder.utils.UserFeature).contentOwnerRoyaltySettings)")
	public ContentOwnerRoyaltySettings getRoyaltySettingsByUserName(String userName){
		return royaltySettingsDAO.getRoyaltySettingsByUserName(userName);
	}
}
