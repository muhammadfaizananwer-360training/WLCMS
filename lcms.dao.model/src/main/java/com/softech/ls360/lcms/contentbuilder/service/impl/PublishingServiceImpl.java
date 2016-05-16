package com.softech.ls360.lcms.contentbuilder.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;

import com.softech.ls360.lcms.contentbuilder.dao.PublishingDAO;
import com.softech.ls360.lcms.contentbuilder.model.CourseAvailability;
import com.softech.ls360.lcms.contentbuilder.model.CourseCompletionReport;
import com.softech.ls360.lcms.contentbuilder.model.CoursePricing;
import com.softech.ls360.lcms.contentbuilder.service.IPublishingService;

public class PublishingServiceImpl implements IPublishingService {
	
	@Autowired
	private PublishingDAO publishingDAO;

	public PublishingDAO getPublishingDAO() {
		return publishingDAO;
	}

	public void setPublishingDAO(PublishingDAO publishingDAO) {
		this.publishingDAO = publishingDAO;
	}

	@Override
	public CoursePricing getCoursePricing(long courseId) throws Exception {
		CoursePricing cp =  publishingDAO.getCoursePricing(courseId);
		cp.setLowestSalePrice(formatPrice(cp.getLowestSalePrice()));
		cp.setmSRP(formatPrice(cp.getmSRP()));
		
		return cp;
	}
	
	@Override
	public boolean updateCoursePricing(CoursePricing cp)  {
		boolean result = false;
		if (cp.getLowestSalePrice().length()==0) cp.setLowestSalePrice("0.00");
		if (cp.getmSRP().length()==0) cp.setmSRP("1.00");
		result = publishingDAO.updateCoursePricing(cp);
		return result;
	}
	
	private String formatPrice(String price){
		BigDecimal bd =  new BigDecimal(price).setScale(2, BigDecimal.ROUND_DOWN);
		return bd.toString();
	}

	@Override
	public CourseCompletionReport getCompletionReport(
			CourseCompletionReport courseCompletionReport) {
		return publishingDAO.getCompletionReport(courseCompletionReport);
	}
	
	@Override
	public CourseCompletionReport getWebinarCompletionReport(CourseCompletionReport courseCompletionReport) {
		return publishingDAO.getWebinarCompletionReport(courseCompletionReport);
	}
	
	@Override
	public boolean changeSynchrounousPublishStatus (int courseId, long authorId) {
		
		boolean bResult = false;
		
		publishingDAO.changeSynchrounousPublishStatus(courseId, authorId);
	
		return bResult;
	}
	
	@Override
	public boolean updateCourseContent (int courseId, int userId) {
		
		boolean bResult = false;
		
		publishingDAO.updateCourseContent(courseId, userId);
	
		return bResult;
	}
	
	@Override
	public boolean updateCourseMeta(int CourseID){
		return publishingDAO.updateCourseMeta(CourseID);
	}
	
	@Override
	public int getNotStartedCourse(int courseID) {
		return publishingDAO.getNotStartedCourse(courseID);
	}
	
	@Override
	public CourseAvailability getCourseAvailability(long courseId) throws Exception {
		CourseAvailability courseAvailability =  publishingDAO.getCourseAvailability(courseId);
		return courseAvailability;
	}
	
	@Override
	public boolean updateCourseAvailability(CourseAvailability availability){
		boolean result = false;
		result = publishingDAO.updateCourseAvailability(availability);
		return result;
	}

	@Override
	public boolean adjustCourseInfoBeforePublish(int courseId) {
		return publishingDAO.adjustCourseInfoBeforePublish(courseId);
	}
}
	
