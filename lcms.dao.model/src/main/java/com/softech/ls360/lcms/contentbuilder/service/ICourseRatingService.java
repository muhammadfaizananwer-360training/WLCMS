package com.softech.ls360.lcms.contentbuilder.service;

import java.util.List;

import com.softech.ls360.lcms.contentbuilder.model.CourseRating;
import com.softech.ls360.lcms.contentbuilder.model.CourseRatingPublish;

public interface ICourseRatingService {

	List<CourseRating> SearchCourseRating(String FromDate,String ToDate,String NpsRating , int hideNullReview);
	boolean SaveCourseRatingStatus(int CourseId,int EnrollmentId,String Publish);
	List<CourseRatingPublish> GetCourseRatingPublishList(String reviewIds);
}
