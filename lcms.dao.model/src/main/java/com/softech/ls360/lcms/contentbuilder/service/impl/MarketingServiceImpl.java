package com.softech.ls360.lcms.contentbuilder.service.impl;

import java.sql.SQLException;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;

import com.softech.ls360.lcms.contentbuilder.dao.MarketingDAO;
import com.softech.ls360.lcms.contentbuilder.model.CourseDTO;
import com.softech.ls360.lcms.contentbuilder.model.SlideAsset;
import com.softech.ls360.lcms.contentbuilder.service.IMarketing;



public class MarketingServiceImpl  implements	IMarketing {
	
	@Autowired
	MarketingDAO marketingDAO;

	public MarketingDAO getMarketingDAO() {
		return marketingDAO;
	}

	public void setMarketingDAO(MarketingDAO marketingDAO) {
		this.marketingDAO = marketingDAO;
	}

	@Override
	public CourseDTO getCourseByID(int id) throws Exception {
		return  marketingDAO.getCourseByID(id);		
	}
	
	@Override
	public CourseDTO updateMarketing (CourseDTO courseDTO) throws SQLException, Exception{
		return  marketingDAO.updateMarketing(courseDTO);
	}
	
	@Override
	public CourseDTO UpdateImage(CourseDTO courseDTO , String AssetType,long lastUpdateUser) throws SQLException, Exception{
		return  marketingDAO.UpdateImage(courseDTO, AssetType,lastUpdateUser);	
	}
	
	@Override	
	public SlideAsset getAssetImage(CourseDTO courseDTO, String AssetType) throws SQLException, Exception{
		return marketingDAO.getAssetImage (courseDTO, AssetType);
	}

	@Override
	public boolean UpdateVideo(CourseDTO course)
			throws SQLException, Exception {
		// TODO Auto-generated method stub
		return marketingDAO.UpdateVideo(course);
	}

	@Override
	public SlideAsset getSlideAssetForVideoAsset(CourseDTO course)
			throws SQLException, Exception {
		// TODO Auto-generated method stub
		return marketingDAO.getSlideAssetForVideoAsset(course);
	}

	@Override
	public boolean updateCourseDuration(String duration,long courseId) {
		// TODO Auto-generated method stub
		
		return marketingDAO.updateCourseDuration(duration,courseId);
	}
	
	@Override
	public String findDurationbyCourseId(long courseId){
		return marketingDAO.findDurationbyCourseId(courseId);
	}
	

}
