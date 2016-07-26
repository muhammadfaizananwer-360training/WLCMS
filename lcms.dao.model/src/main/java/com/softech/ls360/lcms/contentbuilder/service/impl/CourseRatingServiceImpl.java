package com.softech.ls360.lcms.contentbuilder.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import com.softech.ls360.lcms.contentbuilder.dao.CourseRatingDAO;

import com.softech.ls360.lcms.contentbuilder.model.CourseRating;
import com.softech.ls360.lcms.contentbuilder.model.CourseRatingPublish;
import com.softech.ls360.lcms.contentbuilder.service.ICourseRatingService;


public class CourseRatingServiceImpl implements ICourseRatingService {

	@Autowired
	private CourseRatingDAO courseRatingDAO;
	

	@PreAuthorize("hasPermission(#firstOrLastName, @userFeature.npsCourseRating)")
	public CourseRatingDAO getCourseRatingDAO() {
		return courseRatingDAO;
	}

	@PreAuthorize("hasPermission(#firstOrLastName, @userFeature.npsCourseRating)")
	public void setCourseRatingDAO(CourseRatingDAO courseRatingDAO) {
		this.courseRatingDAO = courseRatingDAO;
	}
	
	@Override
	@PreAuthorize("hasPermission(#firstOrLastName, @userFeature.npsCourseRating)")
	public List<CourseRating> SearchCourseRating(String FromDate,String ToDate,String NpsRating, int hideNullReview) {
		// TODO Auto-generated method stub
		return courseRatingDAO.SearchCourseRating(FromDate, ToDate, NpsRating, hideNullReview);
	}
	
	@Override
	@PreAuthorize("hasPermission(#firstOrLastName, @userFeature.npsCourseRating)")
	public boolean SaveCourseRatingStatus(int CourseId,int EnrollmentId,String Publish)
	{
		return courseRatingDAO.SaveCourseRatingStatus(CourseId, EnrollmentId, Publish);
	}

	@Override
	@PreAuthorize("hasPermission(#firstOrLastName, @userFeature.npsCourseRating)")
	public List<CourseRatingPublish> GetCourseRatingPublishList(String reviewIds){
		return courseRatingDAO.GetCourseRatingPublishList(reviewIds);
	}
}
