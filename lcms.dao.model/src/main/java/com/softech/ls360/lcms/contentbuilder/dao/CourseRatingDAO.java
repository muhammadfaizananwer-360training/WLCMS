package com.softech.ls360.lcms.contentbuilder.dao;

import java.util.List;

import com.softech.ls360.lcms.contentbuilder.model.CourseRating;
import com.softech.ls360.lcms.contentbuilder.model.CourseRatingPublish;

public interface CourseRatingDAO {

	List<CourseRating> SearchCourseRating(String ToDate,String FromDate,String NpsRating, int hideNullReview);
	boolean SaveCourseRatingStatus(int CourseId,int EnrollmentId,String Publish);
	List<CourseRatingPublish> GetCourseRatingPublishList(String reviewIds);
}
