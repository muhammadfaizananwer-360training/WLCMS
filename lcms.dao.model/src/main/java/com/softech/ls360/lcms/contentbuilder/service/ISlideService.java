package com.softech.ls360.lcms.contentbuilder.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.softech.ls360.lcms.contentbuilder.model.Slide;
import com.softech.ls360.lcms.contentbuilder.model.SlideAsset;

public interface ISlideService {
	public Slide getSlide (long slideId) throws Exception;	
	public Slide addSlide (Slide objSlide) throws SQLException;	
	public Slide updateSlide (Slide objSlide) throws Exception;	
	public List<SlideAsset> getSlideTextAgainstSlideId(long varSlideId)	throws SQLException; 
	public List<SlideAsset> getSlideCloseCaptionAgainstSlideId(long varSlideId)	throws SQLException;
	public List<SlideAsset> getVisualAssetBySlideId(long varSlideId, String assetType) throws Exception; 
	public SlideAsset updateSlideText (SlideAsset objSlideAsset) throws Exception;	
	public SlideAsset updateSlideCloseCaption (SlideAsset objSlideAsset) throws Exception;
	public List<SlideAsset> getSlideAssetSearch(String criteria, int ContentOwnerID,	int assetype) throws SQLException; 
	public SlideAsset insertSelectedAsset (SlideAsset slideAsset) throws SQLException;
	public boolean deleteSlide (String slideId, String courseId) throws SQLException;
	public Slide updateSelectedSlideTemplate (Slide objSlide) throws Exception;	
	public Map<String, String> isSlidComponentHasData(Slide objSlide) throws Exception;
	SlideAsset detachAsset(SlideAsset slideAsset);
	public boolean updateSlideMC_SCENE_XML(SlideAsset objSlideAsset) throws Exception;
	public String getSlide_MC_SCENE_XML_ById(long slideId) throws Exception;
	public boolean updateAssetAttribtes (SlideAsset objSlideAsset) throws SQLException;
	Integer getTemplateIDForPPTSlide() throws SQLException;
	Integer getVersionIDForUploadedAsset(SlideAsset objSlideAsset)throws SQLException;
	public boolean updateSlideEmbedCodeandEmbedBit(long slideId,String embedCode,Boolean isEmbedCodeValueUpdate);
	public boolean updateSlideEmbedBit(long slideId,boolean isEmbedCode);
}
