package com.softech.ls360.lcms.contentbuilder.dao;

import java.sql.SQLException;
import java.util.List;

import com.softech.ls360.lcms.contentbuilder.model.CourseDTO;
import com.softech.ls360.lcms.contentbuilder.model.SlideAsset;

public interface  MarketingDAO extends GenericDAO<CourseDTO>{
	public CourseDTO getCourseByID (int id) throws SQLException, Exception;

	CourseDTO updateMarketing(CourseDTO courseDTO)throws SQLException, Exception;

	CourseDTO UpdateImage(CourseDTO courseDTO, String AssetType,long lastUpdateUser)	throws SQLException, Exception;

	SlideAsset getAssetImage(CourseDTO courseDTO, String AssetType)	throws SQLException, Exception;
	
	boolean UpdateVideo(CourseDTO course) throws SQLException, Exception;
	
	public SlideAsset getSlideAssetForVideoAsset(CourseDTO course)
			throws SQLException, Exception;
	public boolean updateCourseDuration(String duration,long courseId);
	public String findDurationbyCourseId(long courseId);
	public boolean IsInstructorExist(List<Long> instructorId);
}
