package com.softech.ls360.lcms.contentbuilder.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import com.softech.ls360.lcms.contentbuilder.dao.PermissionSettingsDAO;
import com.softech.ls360.lcms.contentbuilder.model.PermissionSettings;
import com.softech.ls360.lcms.contentbuilder.service.IPermissionSettingsService;

public class PermissionSettingsServiceImpl implements IPermissionSettingsService{
	
	@Autowired
	PermissionSettingsDAO permissionSettingsDAO;

	@Override
	@PreAuthorize("hasPermission(#firstOrLastName, T(com.softech.ls360.lcms.contentbuilder.utils.UserFeature).userPermission)")
	public List<PermissionSettings> searchContentOwnersWithAuthorGruop(
			String firstOrLastName) {
		
		return permissionSettingsDAO.searchContentOwnersWithAuthorGruop(firstOrLastName);
	}

	@Override
	@PreAuthorize("hasPermission(#firstOrLastName, T(com.softech.ls360.lcms.contentbuilder.utils.UserFeature).userPermission)")
	public boolean updatePermissionSettings(int authorGroupId, int featureId,
			int permissionId, long lastUpdatedUserId) {
	
		return permissionSettingsDAO.updatePermissionSettings(authorGroupId, featureId, permissionId, lastUpdatedUserId);
	}

	public PermissionSettingsDAO getPermissionSettingsDAO() {
		return permissionSettingsDAO;
	}

	public void setPermissionSettingsDAO(PermissionSettingsDAO permissionSettingsDAO) {
		this.permissionSettingsDAO = permissionSettingsDAO;
	}
}
