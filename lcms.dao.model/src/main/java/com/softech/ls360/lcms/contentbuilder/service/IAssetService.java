package com.softech.ls360.lcms.contentbuilder.service;

import com.softech.ls360.lcms.contentbuilder.model.AssetDTO;
import com.softech.ls360.lcms.contentbuilder.model.AssetGroup;
import com.softech.ls360.lcms.contentbuilder.model.SupportMaterial;
import com.softech.ls360.lcms.contentbuilder.utils.ObjectWrapper;

import java.util.List;
import java.util.Map;


public interface IAssetService {
	public AssetGroup getAssetGroupByCourseId (long courseId) throws Exception;
	public boolean insertAssetAssetGroupRelationship (long assetId, long assetGroupId, long userId) throws Exception;
	public String getAssetLocation(int assetId) throws Exception;
	public boolean insertSupportMaterial(SupportMaterial sm);
	public List<SupportMaterial> getSupportMaterialListByContObject(int contObjectId);
	public List<SupportMaterial> getSupportMaterialDetailListByContObject(int contObjectId);
	public List<SupportMaterial> getSupportMaterialListByCourse (int courseId);
	public SupportMaterial getSupportMaterialDetail(int lessonId);
	public int getSpprtMtrSequanceMax(int lessonId, int courseId);
	public int getSpprtMtrIdMax(int lessonId);
	public boolean deleteSupportMaterial(String supMaterialId);
	boolean setSpprtMtrDisplayOrder(int lessonId, int itemId, int direction);
	public boolean setSpprtMtrDisplayOrderOnAddDlt(int courseId, int contObjectId, int direction);
	public Long addAsset(String requestId,String userName, long ownerId, long authorId, String assetName, String fileName, String assetType,String assetKeywords, String assetDescription,ObjectWrapper<Long> assetVersionWrapper) throws Exception;
	List<AssetDTO> searchAssets(long contentOwnerId, String text, Map<String, Object> options);
	AssetDTO updateAsset(String requestId, long ownerId, long authorId, long assetId, String fileRelativePath) throws Exception;
	boolean deleteAsset(long assetId, long userId) throws Exception;
	long getFMSUsedSpaceInBytes(long ownerId);
        public String getAssetLocationByVersonId(long versionId);
}

