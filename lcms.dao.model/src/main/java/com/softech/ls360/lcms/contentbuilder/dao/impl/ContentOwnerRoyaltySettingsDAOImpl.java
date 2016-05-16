package com.softech.ls360.lcms.contentbuilder.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ParameterMode;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.softech.ls360.lcms.contentbuilder.dao.ContentOwnerRoyaltySettingsDAO;
import com.softech.ls360.lcms.contentbuilder.dao.GenericDAOImpl;
import com.softech.ls360.lcms.contentbuilder.dao.SPCallingParams;
import com.softech.ls360.lcms.contentbuilder.model.ContentOwnerRoyaltySettings;
import com.softech.ls360.lcms.contentbuilder.utils.LCMS_Util;

public class ContentOwnerRoyaltySettingsDAOImpl extends GenericDAOImpl<ContentOwnerRoyaltySettings>implements ContentOwnerRoyaltySettingsDAO{

	@Transactional
	@Override
	public List<ContentOwnerRoyaltySettings> searchContentOwners(
			String firstOrLastName) {
		
		
		List<ContentOwnerRoyaltySettings> royaltySettingsList = new ArrayList<ContentOwnerRoyaltySettings>();
		SPCallingParams sparam1 = LCMS_Util.createSPObject("NAME", String.valueOf(firstOrLastName) , String.class, ParameterMode.IN);

		Object[] courseRow;		
		Object[] courseRows = callStoredProc("[SEARCH_CONTENTOWNER_ROYALTY_SETTINGS]", sparam1).toArray();
		
		if(courseRows != null && courseRows.length > 0){

			for (Object currentRow : courseRows) {
				ContentOwnerRoyaltySettings royaltySettings = new ContentOwnerRoyaltySettings();
				courseRow = (Object[]) currentRow;
				
				royaltySettings.setUserName(courseRow[0].toString());
				royaltySettings.setFirstName(courseRow[1].toString());
				royaltySettings.setLastName(courseRow[2].toString());
				royaltySettings.setContentOwnerId((Integer)courseRow[3]);
				royaltySettings.setOnlineRoyaltyPercentage(courseRow[4].toString());
				royaltySettings.setClassroomRoyaltyPercentage(courseRow[5].toString());
				royaltySettings.setWebinarRoyaltyPercentage(courseRow[6].toString());

				royaltySettingsList.add(royaltySettings);
			}
		}
		return royaltySettingsList;
	}
	
	@Transactional
	@Override
	public ContentOwnerRoyaltySettings getContentOwnerById(Integer cId) {
		
		
		Query query = entityManager.createNativeQuery("SELECT C.ONLINE_ROYALTY_PERC, C.CLASSROOM_ROYALTY_PERC, C.WEBINAR_ROYALTY_PERC FROM CONTENTOWNER C WHERE C.ID=:cID");
		query.setParameter("cID",cId);
		Object contentObject = query.getSingleResult();
		Object[] contentRow = (Object[])contentObject;
		
		ContentOwnerRoyaltySettings settings = new ContentOwnerRoyaltySettings();
		settings.setOnlineRoyaltyPercentage(contentRow[0].toString());
		settings.setClassroomRoyaltyPercentage(contentRow[1].toString());
		settings.setWebinarRoyaltyPercentage(contentRow[2].toString());
		
		return settings;
	}

	@Transactional
	@Override
	public boolean updateRoyaltSettings(int contentOwnerId,
			float onlineRoyaltyPercentage, float classroomRoyaltyPercentage,
			float webinarRoyaltyPercentage) {
		
		Query query = entityManager.createNativeQuery("UPDATE CONTENTOWNER SET ONLINE_ROYALTY_PERC =:ONLINE_ROYALTY_PERC, CLASSROOM_ROYALTY_PERC =:CLASSROOM_ROYALTY_PERC, WEBINAR_ROYALTY_PERC= :WEBINAR_ROYALTY_PERC" +
				" WHERE ID =:ID");
		query.setParameter("ONLINE_ROYALTY_PERC", onlineRoyaltyPercentage);
		query.setParameter("CLASSROOM_ROYALTY_PERC", classroomRoyaltyPercentage);
		query.setParameter("WEBINAR_ROYALTY_PERC", webinarRoyaltyPercentage);
		query.setParameter("ID", contentOwnerId);
		int rowAffected = query.executeUpdate();
		if(rowAffected>0)
			return true;
		else
			return false;
	}
	
	@Transactional
	@Override
	public ContentOwnerRoyaltySettings getRoyaltySettingsByUserName(String userName){
		
		SPCallingParams sparam1 = LCMS_Util.createSPObject("NAME", String.valueOf(userName) , String.class, ParameterMode.IN);
		Object[] courseRow;		
		Object[] courseRows = callStoredProc("[GET_AUTHORCONTENTOWNER]", sparam1).toArray();
		
		ContentOwnerRoyaltySettings royaltySettings = new ContentOwnerRoyaltySettings();
		
		if(courseRows != null && courseRows.length > 0){

			for (Object currentRow : courseRows) {
				courseRow = (Object[]) currentRow;
				
				royaltySettings.setUserName(courseRow[1].toString());
				royaltySettings.setContentOwnerId((Integer)courseRow[3]);
				
				royaltySettings.setOnlineRoyaltyPercentage((courseRow[7] == null) ? null : courseRow[7].toString());
				royaltySettings.setClassroomRoyaltyPercentage((courseRow[8] == null) ? null : courseRow[8].toString());
				royaltySettings.setWebinarRoyaltyPercentage((courseRow[9] == null) ? null : courseRow[9].toString());
			}
		}
		return royaltySettings;
	}
}
