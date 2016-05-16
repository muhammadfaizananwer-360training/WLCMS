package com.softech.ls360.lcms.contentbuilder.service.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.softech.ls360.lcms.contentbuilder.dao.SlideDAO;
import com.softech.ls360.lcms.contentbuilder.model.Slide;
import com.softech.ls360.lcms.contentbuilder.model.SlideAsset;
import com.softech.ls360.lcms.contentbuilder.service.ISlideService;



public class SlideServiceImpl implements ISlideService {

	@Autowired
	private SlideDAO slideDAO;

	public SlideDAO getSlideDAO() {
		return slideDAO;
	}

	public void setSlideDAO(SlideDAO slideDAO) {
		this.slideDAO = slideDAO;
	}
	
	 
	public List<Slide> getSlides(int iContentObjectID) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Slide addSlide(Slide objSlide) throws SQLException {
		return slideDAO.addSlide(objSlide);
		
	}
	
	@Override 
	public Slide getSlide (long slideId) throws Exception{
		return slideDAO.getSlide(slideId);
	}
	
	@Override 
	public Slide updateSlide (Slide objSlide) throws Exception{
		return slideDAO.updateSlide(objSlide);
	}
	
	
	@Override 
	public Slide updateSelectedSlideTemplate (Slide objSlide) throws Exception{
		return slideDAO.updateSelectedSlideTemplate(objSlide);
	}
	
	@Override
	public SlideAsset updateSlideText (SlideAsset objSlideAsset) throws Exception{
		return slideDAO.updateSlideText(objSlideAsset);
	}
	
	@Override
	public SlideAsset updateSlideCloseCaption (SlideAsset objSlideAsset) throws Exception{
		return slideDAO.updateSlideCloseCaption(objSlideAsset);
	}
	
	@Override
	public List<SlideAsset> getSlideTextAgainstSlideId(long varSlideId)	throws SQLException {
		return slideDAO.getSlideTextAgainstSlideId(varSlideId);
	}
	@Override
	public List<SlideAsset> getSlideCloseCaptionAgainstSlideId(long varSlideId)	throws SQLException {
		return slideDAO.getSlideCloseCaptionAgainstSlideId(varSlideId);
	}

	@Override
	public List<SlideAsset> getSlideAssetSearch(String criteria, int ContentOwnerID,
			int assetype) throws SQLException {
		return slideDAO.getSlideAssetSearch(criteria, ContentOwnerID, assetype);
	}
	
	@Override
	public List<SlideAsset> getVisualAssetBySlideId(long varSlideId, String assetType) throws Exception{
		return slideDAO.getVisualAssetBySlideId(varSlideId, assetType);
	}

	@Override
	public SlideAsset insertSelectedAsset (SlideAsset slideAsset) throws SQLException {
		return slideDAO.insertSelectedAsset(slideAsset);
	}
	
	@Override
	public boolean deleteSlide (String slideId, String courseId) throws SQLException {
		return slideDAO.deleteSlide( slideId,  courseId);
	}

	@Override
	public Map<String, String> isSlidComponentHasData(Slide objSlide)
			throws Exception {
		return slideDAO.isSlidComponentHasData(objSlide);
	}
	@Override
	public SlideAsset detachAsset (SlideAsset slideAsset ) {
		return slideDAO.detachAsset(slideAsset);
	}

	@Override
	public boolean updateSlideMC_SCENE_XML(SlideAsset objSlideAsset)
			throws Exception {
		// TODO Auto-generated method stub
		slideDAO.updateSlideMC_SCENE_XML(objSlideAsset);
		return true;
	}

	@Override
	public String getSlide_MC_SCENE_XML_ById(long slideId) throws Exception {
		// TODO Auto-generated method stub
		return slideDAO.getSlide_MC_SCENE_XML_ById(slideId);
	}
	
	@Override
	public boolean updateAssetAttribtes (SlideAsset objSlideAsset) throws SQLException{
		return slideDAO.updateAssetAttribtes(objSlideAsset);
	}
	
	@Override
	public Integer getTemplateIDForPPTSlide () throws SQLException{
		return slideDAO.getTemplateIDForPPTSlide();
	}
	
	@Override
	public Integer getVersionIDForUploadedAsset (SlideAsset objSlideAsset) throws SQLException{
		return slideDAO.getVersionIDForUploadedAsset(objSlideAsset);
	}
	
	@Override
	public boolean updateSlideEmbedCodeandEmbedBit(long slideId,String embedCode,Boolean isEmbedCodeValueUpdate){
			
		return slideDAO.updateSlideEmbedCodeandEmbedBit(slideId, embedCode,isEmbedCodeValueUpdate);
	}
	
	@Override
	public boolean updateSlideEmbedBit(long slideId,boolean isEmbedCode){
		return slideDAO.updateSlideEmbedBit(slideId, isEmbedCode);
	}
}	
