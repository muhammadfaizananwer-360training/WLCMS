package com.softech.ls360.lcms.contentbuilder.dao;

import java.util.List;

import com.softech.ls360.lcms.contentbuilder.model.PermissionSettings;

public interface PermissionSettingsDAO {

	List<PermissionSettings> searchContentOwnersWithAuthorGruop(String firstOrLastName);
	boolean updatePermissionSettings(int authorGroupId, int featureId, int permissionId, long lastUpdatedUserId);
}
