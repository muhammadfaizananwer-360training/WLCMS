package com.softech.ls360.lcms.contentbuilder.dao;

import com.softech.ls360.lcms.contentbuilder.model.CourseAvailability;
import com.softech.ls360.lcms.contentbuilder.model.CourseCompletionReport;
import com.softech.ls360.lcms.contentbuilder.model.CoursePricing;

public interface PublishingDAO extends GenericDAO<CoursePricing> {

	public CoursePricing getCoursePricing (long courseId)  throws Exception;
	public boolean updateCoursePricing(CoursePricing cp);
	CourseCompletionReport getCompletionReport(CourseCompletionReport courseCompletionReport);
	boolean updateCourseContent(int courseId, int userId);
	int getNotStartedCourse(int courseID);
	public CourseAvailability getCourseAvailability(long courseId) throws Exception ;
	public boolean updateCourseAvailability(CourseAvailability availability);
	public CourseCompletionReport getWebinarCompletionReport(CourseCompletionReport courseCompletionReport);
	boolean changeSynchrounousPublishStatus(int courseId, long authorId);
	boolean updateCourseMeta(int CourseID);
	public boolean adjustCourseInfoBeforePublish(int courseId);
}
