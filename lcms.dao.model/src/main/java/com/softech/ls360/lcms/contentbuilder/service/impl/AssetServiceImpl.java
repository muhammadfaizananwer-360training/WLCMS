package com.softech.ls360.lcms.contentbuilder.service.impl;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;

import com.softech.ls360.lcms.contentbuilder.dao.AssetDAO;
import com.softech.ls360.lcms.contentbuilder.model.AssetDTO;
import com.softech.ls360.lcms.contentbuilder.model.AssetGroup;
import com.softech.ls360.lcms.contentbuilder.model.SupportMaterial;
import com.softech.ls360.lcms.contentbuilder.service.IAssetService;
import com.softech.ls360.lcms.contentbuilder.upload.FileUploader;
import com.softech.ls360.lcms.contentbuilder.upload.FTPFileUploader.FileDetail;
import com.softech.ls360.lcms.contentbuilder.utils.ObjectWrapper;



public class AssetServiceImpl implements IAssetService{
	
	@Autowired
	private AssetDAO assetDAO;
	
	@Autowired
	FileUploader fileUploader;
	
	@Autowired
	@Qualifier("fms")
	FileUploader fmsFileUploader;
	
	public AssetGroup getAssetGroupByCourseId (long courseId) throws Exception{
		return assetDAO.getAssetGroupByCourseId(courseId);
	}

	public boolean insertAssetAssetGroupRelationship (long assetId, long assetGroupId, long userId) throws Exception{
		return assetDAO.insertAssetAssetGroupRelationship(assetId, assetGroupId, userId);
	}
	
	public AssetDAO getAssetDAO() {
		return assetDAO;
	}

	public void setAssetDAO(AssetDAO assetDAO) {
		this.assetDAO = assetDAO;
	}

	@Override
	public String getAssetLocation(int assetId) throws Exception {
		// TODO Auto-generated method stub
		return this.assetDAO.getAssetLocation(assetId);
	}

	@Override	
	public boolean insertSupportMaterial (SupportMaterial sm) {
		return assetDAO.insertSupportMaterial(sm);
	}
	
	@Override
	public List<SupportMaterial> getSupportMaterialListByContObject(int conObjectId){
		return assetDAO.getSupportMaterialListByContObject(conObjectId);
	}
	
	@Override
	public List<SupportMaterial> getSupportMaterialListByCourse (int courseId){
		return assetDAO.getSupportMaterialListByCourse(courseId);
	}
	
	@Override
	public List<SupportMaterial> getSupportMaterialDetailListByContObject(int conObjectId){
		return assetDAO.getSupportMaterialDetailListByContObject(conObjectId);
	}
	
	@Override
	public SupportMaterial getSupportMaterialDetail(int lessonId){
		return assetDAO.getSupportMaterialDetail(lessonId);	
	}
	
	@Override
	public int getSpprtMtrSequanceMax(int lessonId, int courseId){
		return assetDAO.getSpprtMtrSequanceMax(lessonId, courseId);	
	}
	
	@Override
	public int getSpprtMtrIdMax(int lessonId){
		return assetDAO.getSpprtMtrIdMax(lessonId);	
	}
	
	@Override
	public boolean deleteSupportMaterial(String supMaterialId){
		return assetDAO.deleteSupportMaterial(supMaterialId);
	}
	
	@Override
	public boolean setSpprtMtrDisplayOrder(int lessonId, int itemId, int direction){
		List<SupportMaterial> lstSuppMaterial = assetDAO.getSupportMaterialListByContObject(lessonId);
		SupportMaterial smActual = null;
		SupportMaterial smAdjoin = null;
		for (int i=0;i<lstSuppMaterial.size();i++) {
			if(lstSuppMaterial.get(i).getId() == itemId){
				if(direction==1){//up
					smActual = lstSuppMaterial.get(i);
					smActual.setSequence( smActual.getSequence()-1 );
					smAdjoin = lstSuppMaterial.get( i - 1 );
					smAdjoin.setSequence( smAdjoin.getSequence() + 1);
				}else{
					smActual = lstSuppMaterial.get(i);
					smActual.setSequence( smActual.getSequence() + 1 );
					smAdjoin = lstSuppMaterial.get( i + 1 );
					smAdjoin.setSequence( smAdjoin.getSequence() - 1);
				}
				break;
			}
		}

		if(smActual!=null) {
			assetDAO.setSpprtMtrDisplayOrder(smActual.getId(), smActual.getSequence());
			assetDAO.setSpprtMtrDisplayOrder(smAdjoin.getId(),smAdjoin.getSequence());
			return true;
		}
		return false;
	}
	
	@Override
	public boolean setSpprtMtrDisplayOrderOnAddDlt(int courseId, int contObjectId, int direction){
		assetDAO.getSupportMaterialListGrtrThenContObject(courseId, contObjectId, direction);
		
		return true;
	}

	@Override
	public Long addAsset(String requestId, String userName,final long ownerId, long authorId,String assetName, final String fileRelativePath, String assetType,String assetKeywords, String assetDescription,ObjectWrapper<Long> assetVersionWrapper) throws Exception {
		FileUploader uploader = assetType.equalsIgnoreCase("VSC")? fmsFileUploader: fileUploader;
		
		if(!uploader.tempFileExists(fileRelativePath)) {
			throw new FileNotFoundException("file not found");
		}
		
		final long assetId = assetDAO.addAsset(userName,ownerId, authorId
				, assetName
				, FilenameUtils.getName(fileRelativePath)
				, assetType,assetKeywords, assetDescription, assetVersionWrapper);
		final long assetVersionId= assetVersionWrapper.getValue();
		
		
		//update location on FMS if asset type is VSC
		if(assetType.equalsIgnoreCase("VSC")) {
			FileDetail newDetail = uploader.confirmFile(requestId,fileRelativePath
					, ownerId + "/" + assetId + "/" + assetVersionId + "/" + FilenameUtils.getName(fileRelativePath).replace(' ', '_'));
			assetDAO.updateVSCAssetFileDetail(authorId,assetId, "/" + newDetail.getFilePath(),newDetail.getSizeInBytes());
		} else {
			
			FileDetail newDetail = uploader.confirmFile(requestId,fileRelativePath
					, assetId + "/" + assetVersionId + "/" + FilenameUtils.getName(fileRelativePath).replace(' ', '_'));
			assetDAO.updateAssetLocation(assetVersionWrapper.getValue(), "/" + newDetail.getFilePath());
			assetDAO.updateVSCAssetFileDetail(authorId,assetId, "/" + newDetail.getFilePath(),newDetail.getSizeInBytes());
		}
		
		return assetId;
	}
	
	
	@Override
	public AssetDTO updateAsset(String requestId,final long ownerId, long authorId,long assetId, final String fileRelativePath) throws Exception {
		AssetDTO assetDetail = assetDAO.getAssetDetails(assetId);
		FileUploader uploader = assetDetail.getAssetType().equalsIgnoreCase("VSC")? fmsFileUploader: fileUploader;
		
		if(!uploader.tempFileExists(fileRelativePath)) {
			throw new FileNotFoundException("temporary file not found");
		} 
		
		if(assetDetail.getAssetType().equalsIgnoreCase("VSC")) {
			uploader.deleteFile(StringUtils.trimLeadingCharacter(StringUtils.trimLeadingCharacter(assetDetail.getVideoLocation(),'/'),'\\'));
			
			FileDetail newDetail = uploader.confirmFile(requestId,fileRelativePath
						, ownerId + "/" + assetId + "/" + assetDetail.getVersionId() + "/" + FilenameUtils.getName(fileRelativePath).replace(' ', '_'));
			assetDAO.updateVSCAssetFileDetail(authorId,assetId, "/" + newDetail.getFilePath(),newDetail.getSizeInBytes());
			return assetDAO.getAssetDetails(assetId);
		} else {
			throw new Exception("updateAsset is not implemented for other than VSC assets");
		}
	}
	
	@Override
	public List<AssetDTO> searchAssets(long contentOwnerId,String text, Map<String, Object> options) {
		return assetDAO.searchAssets(contentOwnerId,text, options);
	}
	
	@Override
	public boolean deleteAsset(long assetId, long userId) throws Exception{
		AssetDTO assetDetail = assetDAO.getAssetDetails(assetId);
		FileUploader uploader = assetDetail.getAssetType().equalsIgnoreCase("VSC")? fmsFileUploader: fileUploader;
		
		if(assetDetail.getAssetType().equalsIgnoreCase("VSC")) {
			uploader.deleteFile(StringUtils.trimLeadingCharacter(StringUtils.trimLeadingCharacter(assetDetail.getVideoLocation(),'/'),'\\'));
			return assetDAO.deleteAsset(assetId, userId);
		} else {
			throw new Exception("deleteAsset is not implemented for other than VSC assets");
		}
	}
	
	@Override
	public long getFMSUsedSpaceInBytes(long ownerId) {
		return assetDAO.getFMSUsedSpaceInBytes(ownerId);
	}

    @Override
    public String getAssetLocationByVersonId(long versionId) {
        return assetDAO.getAssetLocationByVersonId(versionId);
    }
}
