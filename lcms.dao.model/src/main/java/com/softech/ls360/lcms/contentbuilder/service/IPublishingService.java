package com.softech.ls360.lcms.contentbuilder.service;


import com.softech.ls360.lcms.contentbuilder.model.CourseAvailability;
import com.softech.ls360.lcms.contentbuilder.model.CourseCompletionReport;
import com.softech.ls360.lcms.contentbuilder.model.CoursePricing;

public interface IPublishingService {
	public CoursePricing getCoursePricing (long courseId) throws Exception ;	
	boolean updateCoursePricing(CoursePricing cp) throws Exception;	
	public CourseCompletionReport getCompletionReport(CourseCompletionReport courseCompletionReport);
	boolean updateCourseContent(int courseId, int userId);
	int getNotStartedCourse(int courseID);
	public CourseAvailability getCourseAvailability (long courseId) throws Exception;
	public boolean updateCourseAvailability(CourseAvailability availability) throws Exception;
	public CourseCompletionReport getWebinarCompletionReport(CourseCompletionReport courseCompletionReport);
	boolean changeSynchrounousPublishStatus(int courseId, long authorId);
	boolean updateCourseMeta(int CourseID);
	boolean adjustCourseInfoBeforePublish(int courseId);
}
