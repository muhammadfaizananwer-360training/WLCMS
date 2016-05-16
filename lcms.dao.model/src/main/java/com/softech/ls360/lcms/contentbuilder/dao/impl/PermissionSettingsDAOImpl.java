package com.softech.ls360.lcms.contentbuilder.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.softech.ls360.lcms.contentbuilder.dao.GenericDAOImpl;
import com.softech.ls360.lcms.contentbuilder.dao.PermissionSettingsDAO;
import com.softech.ls360.lcms.contentbuilder.dao.SPCallingParams;
import com.softech.ls360.lcms.contentbuilder.model.PermissionSettings;
import com.softech.ls360.lcms.contentbuilder.utils.LCMS_Util;

public class PermissionSettingsDAOImpl extends GenericDAOImpl<PermissionSettings> implements PermissionSettingsDAO{

	private static Logger logger = LoggerFactory
			.getLogger(PermissionSettingsDAOImpl.class);
	@Transactional
	@Override
	public List<PermissionSettings> searchContentOwnersWithAuthorGruop(String firstOrLastName) {
		
		List<PermissionSettings> permissionSettingsList = new ArrayList<PermissionSettings>();
		SPCallingParams sparam1 = LCMS_Util.createSPObject("NAME", String.valueOf(firstOrLastName) , String.class, ParameterMode.IN);

		Object[] courseRow;		
		Object[] courseRows = callStoredProc("[GET_WLCMS_PERMISSION]", sparam1).toArray();
		
		if(courseRows != null && courseRows.length > 0){

			for (Object currentRow : courseRows) {
				PermissionSettings permissionSettings = new PermissionSettings();
				courseRow = (Object[]) currentRow;
				
				permissionSettings.setUserName(courseRow[1].toString());
				permissionSettings.setFirstName(courseRow[2].toString());
				permissionSettings.setLastName(courseRow[3].toString());
				permissionSettings.setAuthorGroupId(((BigDecimal)courseRow[4]).intValue());
				permissionSettings.setUserPermissionFeatureId(((BigDecimal)courseRow[5]).intValue());
				permissionSettings.setUserPermission(((BigDecimal)courseRow[6]).intValue());
				permissionSettings.setNpsRatingFeatureId(((BigDecimal)courseRow[7]).intValue());
				permissionSettings.setNpsRatingPermission(((BigDecimal)courseRow[8]).intValue());
				permissionSettings.setRoyaltySettingFeatureId(((BigDecimal)courseRow[9]).intValue());
				permissionSettings.setRoyaltySettingPermission(((BigDecimal)courseRow[10]).intValue());
				permissionSettings.setBulkCourseImportPermssionId((((BigDecimal)courseRow[11]).intValue()));
				permissionSettings.setBulkCourseImportPermission((((BigDecimal)courseRow[12]).intValue()));
				permissionSettings.setViewWLCMSReportPermssionId((((BigDecimal)courseRow[13]).intValue()));
				permissionSettings.setViewWLCMSReportPermssion((((BigDecimal)courseRow[14]).intValue()));
				
				permissionSettingsList.add(permissionSettings);
			}
		}
		return permissionSettingsList;
	}

	@Transactional
	@Override
	public boolean updatePermissionSettings(int authorGroupId, int featureId,
			int permissionId, long lastUpdatedUserId) {

		try{
		SPCallingParams sparam1 = LCMS_Util.createSPObject("ID", String.valueOf(authorGroupId), Integer.class, ParameterMode.IN);		
		SPCallingParams sparam2 = LCMS_Util.createSPObject("FeatureId", String.valueOf(featureId), Integer.class, ParameterMode.IN);
		SPCallingParams sparam3 = LCMS_Util.createSPObject("EnableTF", String.valueOf(permissionId), Integer.class, ParameterMode.IN);
		SPCallingParams sparam4 = LCMS_Util.createSPObject("UNID", String.valueOf(0), Integer.class, ParameterMode.IN);
		SPCallingParams sparam5 = LCMS_Util.createSPObject("LastUpdateUser", String.valueOf(lastUpdatedUserId) , Long.class, ParameterMode.IN);
		
		StoredProcedureQuery qr = createQueryParameters("UPDATE_LCMSPERMISSION", sparam1, sparam2, sparam3, sparam4, sparam5);
		
		return qr.execute();
		}catch (Throwable e) {
			logger.error(e.getMessage());
		}
		
		return true;
	}
}
