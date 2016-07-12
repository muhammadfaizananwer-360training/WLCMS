package com.softech.ls360.lcms.contentbuilder.dao.impl;

import com.softech.ls360.lcms.contentbuilder.dao.GenericDAOImpl;
import com.softech.ls360.lcms.contentbuilder.dao.PublishingDAO;
import com.softech.ls360.lcms.contentbuilder.dao.SPCallingParams;
import com.softech.ls360.lcms.contentbuilder.model.CourseAvailability;
import com.softech.ls360.lcms.contentbuilder.model.CourseCompletionReport;
import com.softech.ls360.lcms.contentbuilder.model.CoursePricing;
import com.softech.ls360.lcms.contentbuilder.utils.LCMS_Util;
import com.softech.ls360.lcms.contentbuilder.utils.StringUtil;
import com.softech.ls360.lcms.contentbuilder.utils.TypeConvertor;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import java.sql.Clob;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

public class PublishingDAOImpl extends GenericDAOImpl<CoursePricing> implements PublishingDAO{

	private static Logger logger = Logger.getLogger(PublishingDAOImpl.class);

	@Transactional
	@Override
	public CoursePricing getCoursePricing (long courseId) throws Exception{
		CoursePricing pricing  = new CoursePricing();
		pricing.setId(courseId);

		SPCallingParams sparam1 = LCMS_Util.createSPObject("COURSE_ID", String.valueOf(courseId) , Integer.class, ParameterMode.IN);

		Object[] courseRow;
		Object[] courseRows = callStoredProc("[LCMS_WEB_SELECT_COURSEPRICING]", sparam1).toArray();

		for (Object course : courseRows) {

			courseRow = (Object[]) course;
			Clob cm = (Clob )courseRow[1];
			String courseName = StringUtil.clobStringConversion(cm);
			pricing.setId(Long.parseLong(courseRow[0].toString()));
			pricing.setName(courseName);
			pricing.setBusinessKey(courseRow[2] == null ? "" : courseRow[2].toString());
			pricing.setmSRP(courseRow[3] == null ? "1.00" : courseRow[3].toString());
			pricing.setLowestSalePrice(courseRow[4] == null ? "0.00" : courseRow[4].toString());
			pricing.setOfferprice((courseRow[5] == null ? 0.0f : new Double (courseRow[5].toString())));
			pricing.setManageSFPrice(courseRow[6] == null ? false : Boolean.valueOf (courseRow[6].toString()));
		}

		return pricing;
	}

	@Transactional
	@Override
	public boolean changeSynchrounousPublishStatus(int courseId, long authorId){

		/*SPCallingParams sparam1 = LCMS_Util.createSPObject("COURSE_ID", String.valueOf(courseId) , Integer.class, ParameterMode.IN);

		StoredProcedureQuery qr = createQueryParameters("ChangePublishStatusSynchronousCourse", sparam1);
		qr.execute();
		*/

		Query query = getEntityManager ().createNativeQuery("Update Course SET COURSESTATUS='Published', LastUpdatedDate = GetDate() , LastPublishedDate = GetDate(), LastUpdateUser = :author_Id WHERE ID = :course_id");
		query.setParameter( "course_id", courseId);
		query.setParameter( "author_Id", authorId);

		query.executeUpdate();

		return true;
	}

	@Transactional
	@Override
	public boolean updateCoursePricing(CoursePricing cp){

		SPCallingParams sparam1 = LCMS_Util.createSPObject("COURSE_ID", String.valueOf(cp.getId()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam2 = LCMS_Util.createSPObject("MSRP", String.valueOf(cp.getmSRP()) , Float.class, ParameterMode.IN);
		SPCallingParams sparam3 = LCMS_Util.createSPObject("LowestPrice", String.valueOf(cp.getLowestSalePrice()) , Float.class, ParameterMode.IN);
		SPCallingParams sparam4 = LCMS_Util.createSPObject("LastUpdateUser", String.valueOf(cp.getUpdatedBy()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam5 = LCMS_Util.createSPObject("offerprice", String.valueOf(cp.getOfferprice()) , Float.class, ParameterMode.IN);
		SPCallingParams sparam6 = LCMS_Util.createSPObject("manageSFPrice", String.valueOf(cp.isManageSFPrice()) , Boolean.class, ParameterMode.IN);


		StoredProcedureQuery qr = createQueryParameters("LCMS_WEB_UPDATE_COURSEPRICING", sparam1, sparam2, sparam3, sparam4,sparam5, sparam6);
		qr.execute();
		return true;
	}

	@Transactional
	@Override
	public boolean updateCourseMeta(int CourseID){

		SPCallingParams sparam1 = LCMS_Util.createSPObject("COURSE_ID", String.valueOf(CourseID) , Integer.class, ParameterMode.IN);
		StoredProcedureQuery qr = createQueryParameters("WLCMS_UPDATE_COURSE_META", sparam1);
		qr.execute();
		return true;
	}

	@Transactional
	@Override
	public CourseCompletionReport getCompletionReport(CourseCompletionReport courseCompletionReport){

		SPCallingParams sparam1 = LCMS_Util.createSPObject("COURSE_ID", String.valueOf(courseCompletionReport.getCourseId()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam2 = LCMS_Util.createSPObject("CONTENTOWNER_ID", String.valueOf(courseCompletionReport.getContentOwnerId()) , Integer.class, ParameterMode.IN);

		Object[] coursCompletionRpts = callStoredProc("CourseCompletionTestReport", sparam1,sparam2).toArray();

		for (Object objCoursCompletionRpt : coursCompletionRpts) {

			Object [] coursCompletionRptRow = (Object[]) objCoursCompletionRpt;

			courseCompletionReport.setPostAssesssmentEnabled(coursCompletionRptRow[1] == null ? false : Boolean.parseBoolean(coursCompletionRptRow[1].toString()));
			courseCompletionReport.setQuizEnabled(coursCompletionRptRow[2] == null ? false : Boolean.parseBoolean(coursCompletionRptRow[2].toString()));
			courseCompletionReport.setContentObjectPresent(coursCompletionRptRow[3] == null ? false : Boolean.parseBoolean(coursCompletionRptRow[3].toString()));
			courseCompletionReport.setAtleastOneScenePerCO(coursCompletionRptRow[4] == null ? false : Boolean.parseBoolean(coursCompletionRptRow[4].toString()));
			courseCompletionReport.setCourseExpired(coursCompletionRptRow[5] == null ? false : Boolean.parseBoolean(coursCompletionRptRow[5].toString()));
			courseCompletionReport.setPublishStatus(coursCompletionRptRow[6] == null ? false : Boolean.parseBoolean(coursCompletionRptRow[6].toString()));
			courseCompletionReport.setCourseId(coursCompletionRptRow[7] == null ? 0 : Integer.parseInt(coursCompletionRptRow[7].toString()));
			courseCompletionReport.setCourseName(coursCompletionRptRow[8] == null ? "" : coursCompletionRptRow[8].toString());
			courseCompletionReport.setBussinessKey(coursCompletionRptRow[9] == null ? "" : coursCompletionRptRow[9].toString());
			courseCompletionReport.setContentOwnerStoreFront(coursCompletionRptRow[10] == null ? false : Boolean.parseBoolean(coursCompletionRptRow[10].toString()));
			courseCompletionReport.setExamMastery(coursCompletionRptRow[11] == null ? false : Boolean.parseBoolean(coursCompletionRptRow[11].toString()));
			courseCompletionReport.setQuizMastery(coursCompletionRptRow[12] == null ? false : Boolean.parseBoolean(coursCompletionRptRow[12].toString()));
			courseCompletionReport.setQuizQuestionCount(coursCompletionRptRow[13] == null ? 0 : Integer.parseInt(coursCompletionRptRow[13].toString()));
			courseCompletionReport.setExamQuestionCount(coursCompletionRptRow[14] == null ? 0 : Integer.parseInt(coursCompletionRptRow[14].toString()));
			courseCompletionReport.setAgreeWithSpecifiedTextEnabled(coursCompletionRptRow[15] == null ? false : Boolean.parseBoolean(coursCompletionRptRow[15].toString()));
			courseCompletionReport.setLastPublishDate(TypeConvertor.DateTimeToString((Date) coursCompletionRptRow[20]));
			courseCompletionReport.setOfferPrice(TypeConvertor.AnyToDouble(coursCompletionRptRow[21]));
			courseCompletionReport.setLowestPrice(TypeConvertor.AnyToDouble(coursCompletionRptRow[22]));
			Clob cm = (Clob) coursCompletionRptRow[16];
			String specifiedText = null;
			try {
				specifiedText = StringUtil.clobStringConversion(cm);
				courseCompletionReport.setAgreeWithSpecifiedText(specifiedText);
			} catch (Exception e) {
				logger.error("Error in method:getCompletionReport" + e.getMessage() );
				e.printStackTrace();
			}
			courseCompletionReport.setCompletionPostAssessmentAttempted(coursCompletionRptRow[17] == null ? false : Boolean.parseBoolean(coursCompletionRptRow[17].toString()));
			courseCompletionReport.setSetCourseExpiration (coursCompletionRptRow[18] == null ? false : Boolean.parseBoolean(coursCompletionRptRow[18].toString()));
			courseCompletionReport.setExamQuestionToAskCount(TypeConvertor.AnyToInteger(coursCompletionRptRow[19]));
		}

		return courseCompletionReport;
	}


	@Transactional
	@Override
	public CourseCompletionReport getWebinarCompletionReport(CourseCompletionReport courseCompletionReport){

		SPCallingParams sparam1 = LCMS_Util.createSPObject("COURSE_ID", String.valueOf(courseCompletionReport.getCourseId()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam2 = LCMS_Util.createSPObject("CONTENTOWNER_ID", String.valueOf(courseCompletionReport.getContentOwnerId()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam3 = LCMS_Util.createSPObject("COURSE_TYPE", String.valueOf(courseCompletionReport.getCourseType()) , Integer.class, ParameterMode.IN);

		Object[] coursCompletionRpts = callStoredProc("CompletionReportSynchronous", sparam1,sparam2,sparam3).toArray();

		for (Object objCoursCompletionRpt : coursCompletionRpts) {

			Object [] coursCompletionRptRow = (Object[]) objCoursCompletionRpt;
			courseCompletionReport.setIsScheduleSet(coursCompletionRptRow[0] == null ? false : Boolean.parseBoolean(coursCompletionRptRow[0].toString()));
			courseCompletionReport.setIsPresenterSet(coursCompletionRptRow[1] == null ? false : Boolean.parseBoolean(coursCompletionRptRow[1].toString()));
			courseCompletionReport.setIsMeetingSet(coursCompletionRptRow[2] == null ? false : Boolean.parseBoolean(coursCompletionRptRow[2].toString()));
			courseCompletionReport.setCourseName(coursCompletionRptRow[3] == null ? "" : coursCompletionRptRow[3].toString());
			courseCompletionReport.setBussinessKey(coursCompletionRptRow[4] == null ? "" : coursCompletionRptRow[4].toString());
			courseCompletionReport.setContentOwnerStoreFront(coursCompletionRptRow[5] == null ? false : Boolean.parseBoolean(coursCompletionRptRow[5].toString()));
			courseCompletionReport.setPublishStatus(coursCompletionRptRow[6] == null ? false : Boolean.parseBoolean(coursCompletionRptRow[6].toString()));
			courseCompletionReport.setIsClassRoomSet(coursCompletionRptRow[7] == null ? false : Boolean.parseBoolean(coursCompletionRptRow[7].toString()));
			courseCompletionReport.setAuthorInfoProvided(coursCompletionRptRow[8] == null ? false : Boolean.parseBoolean(coursCompletionRptRow[8].toString()));
			courseCompletionReport.setLastPublishDate(TypeConvertor.DateTimeToString((Date) coursCompletionRptRow[9]));
			courseCompletionReport.setOfferPrice(TypeConvertor.AnyToDouble(coursCompletionRptRow[10]));
			courseCompletionReport.setLowestPrice(TypeConvertor.AnyToDouble(coursCompletionRptRow[11]));
		}

		return courseCompletionReport;
	}

	@Transactional
	@Override
	public boolean updateCourseContent (int courseId, int userId) {

		boolean bResult = false;

		SPCallingParams sparam1 = LCMS_Util.createSPObject("SOURCECOURSEID", String.valueOf(courseId), Integer.class, ParameterMode.IN);
		SPCallingParams sparam2 = LCMS_Util.createSPObject("TARGETCOURSEID", String.valueOf("1"), Integer.class, ParameterMode.IN);
		SPCallingParams sparam3 = LCMS_Util.createSPObject("CreateUserId", String.valueOf(userId), Integer.class, ParameterMode.IN);

		callStoredProc("MERGE_DRAFTED_COURSE", sparam1, sparam2, sparam3);

		return bResult;
	}


	@Override
	@Transactional
	public int getNotStartedCourse(int courseID) {


		SPCallingParams sparam1 = LCMS_Util.createSPObject("COURSE_ID", String.valueOf(courseID), Integer.class, ParameterMode.IN);
		SPCallingParams sparam2 = LCMS_Util.createSPObject("NOT_STARTED", String.valueOf("1"), Integer.class, ParameterMode.OUT);


		StoredProcedureQuery qr = createQueryParameters("GET_NOT_STARTED_COURSE_ID_FROM_PUBLISHED", sparam1, sparam2);
		qr.execute();
		return (Integer) (qr.getOutputParameterValue("NOT_STARTED") == null ? 0 : qr.getOutputParameterValue("NOT_STARTED"));
	}

	@Transactional
	@Override
	public CourseAvailability getCourseAvailability(long courseId) throws Exception{
		CourseAvailability availability  = null;

		SPCallingParams sparam1 = LCMS_Util.createSPObject("COURSE_ID", String.valueOf(courseId) , Integer.class, ParameterMode.IN);

		Object[] courseRow;
		Object[] courseRows = callStoredProc("[LCMS_WEB_GET_COURSEAVAILIBILITY]", sparam1).toArray();

		if(courseRows.length > 0){
			availability  = new CourseAvailability();
			availability.setId(courseId);
		}

		for (Object course : courseRows) {

			courseRow = (Object[]) course;
			//Clob cm = (Clob )courseRow[1];
			//String courseName = StringUtil.clobStringConversion(cm);
			availability.setId( Long.parseLong(courseRow[0].toString()));
			Clob cm = (Clob )courseRow[1];
			String courseName = StringUtil.clobStringConversion(cm);
			availability.setCourseName(courseName);
			availability.setBusinessKey(courseRow[2] == null ? "" : courseRow[2].toString());
			String industryName = null;
			if(courseRow[3] == null || courseRow[3].toString().trim().length()  <= 0){
				industryName = "Other";
			}else{
				industryName = courseRow[3].toString();
			}
			availability.setIndustry(industryName);
			availability.setCourseGroupId(courseRow[5] == null ? "" : courseRow[5].toString());
			availability.setCourseGroupName(courseRow[6] == null ? "" : courseRow[6].toString());
			availability.setCourseExpiration(courseRow[7] == null ? "" : courseRow[7].toString());
			availability.setLearnerAccessToCourse(courseRow[4] == null ? "" : courseRow[4].toString());

			availability.setEligibleForMobileTablet((Boolean)( courseRow[8] == null ? "false" : courseRow[8]));
			availability.setEligibleForSubscription((Boolean)( courseRow[9] == null ? "false" : courseRow[9]));
			availability.setEligibleForVARresale((Boolean)( courseRow[10] == null ? "false" : courseRow[10]));
			availability.setEligibleForDistrThruSCORM((Boolean)( courseRow[11] == null ? "false" : courseRow[11]));
			availability.setEligibleForDistrThruAICC((Boolean)( courseRow[12] == null ? "false" : courseRow[12]));

			availability.setRequireReportToRegulator((Boolean)( courseRow[13] == null ? "false" : courseRow[13]));
			availability.setRequireShippableItems((Boolean)( courseRow[14] == null ? "false" : courseRow[14]));

			availability.setIsThirdpartyCourse((Boolean)( courseRow[15] == null ? "false" : courseRow[15]));

		}

		return availability;
	}

	@Transactional
	@Override
	public boolean updateCourseAvailability(CourseAvailability availability){

		SPCallingParams sparam1 = LCMS_Util.createSPObject("COURSE_ID", String.valueOf(availability.getId()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam2 = LCMS_Util.createSPObject("Industry", String.valueOf(availability.getIndustry()) , String.class, ParameterMode.IN);
//		SPCallingParams sparam3 = LCMS_Util.createSPObject("CourseGroupId", String.valueOf(availability.getCourseGroupId()) , Integer.class, ParameterMode.IN);
//		SPCallingParams sparam4 = LCMS_Util.createSPObject("CourseGroupName", String.valueOf(availability.getCourseGroupName()) , String.class, ParameterMode.IN);

		SPCallingParams sparam3 = null;
		boolean isCourseExpirationNull = false;
		if(availability.getCourseExpiration() == null){
			isCourseExpirationNull = true;
			availability.setCourseExpiration("");

			sparam3 = LCMS_Util.createSPObject("CourseExpiration", String.valueOf(availability.getCourseExpiration()) ,String.class, ParameterMode.IN);
		}else{
			sparam3 = LCMS_Util.createSPObject("CourseExpiration", String.valueOf(availability.getCourseExpiration()) , Timestamp.class, ParameterMode.IN);
		}


		SPCallingParams sparam4 = LCMS_Util.createSPObject("LearnerAccessToCourse", String.valueOf(availability.getLearnerAccessToCourse()) , Integer.class, ParameterMode.IN);


		SPCallingParams sparam5 = LCMS_Util.createSPObject("EligibleForMobileTablet", String.valueOf(availability.getEligibleForMobileTablet()) , Boolean.class, ParameterMode.IN);
		SPCallingParams sparam6 = LCMS_Util.createSPObject("EligibleForSubscription", String.valueOf(availability.getEligibleForSubscription()) , Boolean.class, ParameterMode.IN);
		SPCallingParams sparam7 = LCMS_Util.createSPObject("EligibleForVARresale", String.valueOf(availability.getEligibleForVARresale()) , Boolean.class, ParameterMode.IN);
		SPCallingParams sparam8 = LCMS_Util.createSPObject("EligibleForDistrThruSCORM", String.valueOf(availability.getEligibleForDistrThruSCORM()) , Boolean.class, ParameterMode.IN);
		SPCallingParams sparam9 = LCMS_Util.createSPObject("EligibleForDistrThruAICC", String.valueOf(availability.getEligibleForDistrThruAICC()) , Boolean.class, ParameterMode.IN);
		SPCallingParams sparam10 = LCMS_Util.createSPObject("RequireReportToRegulator", String.valueOf(availability.getRequireReportToRegulator()) , Boolean.class, ParameterMode.IN);
		SPCallingParams sparam11 = LCMS_Util.createSPObject("RequireShippableItems", String.valueOf(availability.getRequireShippableItems()) , Boolean.class, ParameterMode.IN);
		SPCallingParams sparam12 = LCMS_Util.createSPObject("ThirdPartyCourse", String.valueOf(availability.getIsThirdpartyCourse()) , Boolean.class, ParameterMode.IN);
		SPCallingParams sparam13 = LCMS_Util.createSPObject("LastUpdateUser", String.valueOf(availability.getUpdatedBy()) , Integer.class, ParameterMode.IN);

		StoredProcedureQuery qr = createQueryParameters("LCMS_WEB_UPDATE_COURSEAVAILABILITY", sparam1, sparam2, sparam3, sparam4, sparam5, sparam6, sparam7, sparam8, sparam9, sparam10, sparam11, sparam12, sparam13);
		Timestamp dateTimestamp = null;

		if(!isCourseExpirationNull){
			try {
				dateTimestamp = LCMS_Util.parseStringToTimestamp(
						availability.getCourseExpiration(), "MM-dd-yyyy");
			} catch (ParseException e) {

				e.printStackTrace();
				logger.error(" ERROR WHILE PRARSING DATE:" + e.getMessage());
			}

			qr.setParameter("CourseExpiration", dateTimestamp);
		}
		return qr.execute();
	}

	@Override
	@Transactional
	public boolean adjustCourseInfoBeforePublish(int courseId) {
		SPCallingParams prmCourseId = LCMS_Util.createSPObject("COURSE_ID", String.valueOf(courseId) , Integer.class, ParameterMode.IN);
		callStoredProc("[WLCMS_ADJUST_COURSE_INFO_BEFORE_PUBLISH]", prmCourseId);
		return true;
	}
}

