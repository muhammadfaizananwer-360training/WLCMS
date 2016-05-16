package com.softech.ls360.lcms.contentbuilder.service;

import java.util.List;
import com.softech.ls360.lcms.contentbuilder.model.ContentOwnerRoyaltySettings;

public interface IContentOwnerRoyaltySettingsService {

	List<ContentOwnerRoyaltySettings> searchContentOwners(String firstOrLastName);
	boolean updateRoyaltSettings(int contentOwnerId, float onlineRoyaltyPercentage, float classroomRoyaltyPercentage, float webinarRoyaltyPercentage);
	public ContentOwnerRoyaltySettings getContentOwnerById(Integer cId);
	public ContentOwnerRoyaltySettings getRoyaltySettingsByUserName(String userName);
}
