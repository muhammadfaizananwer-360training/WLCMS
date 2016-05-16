package com.softech.ls360.lcms.contentbuilder.dao;

import java.util.List;
import java.util.Map;

import com.softech.ls360.lcms.contentbuilder.model.AssetDTO;
import com.softech.ls360.lcms.contentbuilder.model.AssetGroup;
import com.softech.ls360.lcms.contentbuilder.model.SupportMaterial;
import com.softech.ls360.lcms.contentbuilder.utils.ObjectWrapper;

public interface AssetDAO {
	
	public AssetGroup getAssetGroupByCourseId (long id) throws Exception ;
	public boolean insertAssetAssetGroupRelationship (long assetId, long assetGroupId, long userId) throws Exception;
	public String getAssetLocation(int assetId) throws Exception;
	public boolean insertSupportMaterial(SupportMaterial sm) ;
	public List<SupportMaterial> getSupportMaterialListByContObject(int contObjectId);
	public List<SupportMaterial>  getSupportMaterialDetailListByContObject(int contObjectId);	
	public List<SupportMaterial> getSupportMaterialListByCourse (int courseId);
	public SupportMaterial getSupportMaterialDetail(int lessonId);
	public int getSpprtMtrSequanceMax(int lessonId, int courseId);
	public int getSpprtMtrIdMax(int lessonId);
	public boolean deleteSupportMaterial(String supMaterialId);
	boolean setSpprtMtrDisplayOrder(int varSMId, int sequence);
	public List<SupportMaterial> getSupportMaterialListGrtrThenContObject (int courseId, int contObjectId, int direction);
	public Long addAsset(String userName, long ownerId, long authorId, String assetName, String fileName, String assetType,String assetKeywords, String assetDescription, ObjectWrapper<Long> assetVebrsion);
	boolean updateAssetLocation(long assetVersionId, String location);
	boolean updateVSCAssetLocation(long assetId, String location);
	List<AssetDTO> searchAssets(long contentOwnerId, String text, Map<String, Object> options);
	boolean updateVSCAssetFileDetail(long userId, long assetId, String location, Long sizeInBytes);
	AssetDTO getAssetDetails(long assetId);
	boolean deleteAsset(long assetId, long userId);
	public long getFMSUsedSpaceInBytes(long ownerId);
        public String getAssetLocationByVersonId(long versionId);
}
