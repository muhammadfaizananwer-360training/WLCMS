package com.softech.ls360.lcms.contentbuilder.dao.impl;

import com.softech.ls360.lcms.contentbuilder.dao.CourseDAO;
import com.softech.ls360.lcms.contentbuilder.dao.GenericDAOImpl;
import com.softech.ls360.lcms.contentbuilder.dao.SPCallingParams;
import com.softech.ls360.lcms.contentbuilder.model.*;
import com.softech.ls360.lcms.contentbuilder.utils.LCMS_Util;
import com.softech.ls360.lcms.contentbuilder.utils.StringUtil;
import com.softech.ls360.lcms.contentbuilder.utils.TypeConvertor;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class CourseDAOImpl extends GenericDAOImpl<CourseDTO> implements CourseDAO
{
	private static Logger logger = Logger.getLogger(CourseDAOImpl.class);

	private static final String INSERT_RECENT_COURSE = "INSERT INTO RecentCourses (COURSE_ID) VALUES( ? ) ";

	public CourseDAOImpl() {

	}

	@Transactional
	@Override
	public CourseDTO getCourseByID (int id) throws SQLException
	{

		logger.info("In CourseDAOImpl::getCourseByID BEGIN");

		CourseDTO dto;


		SPCallingParams sparam1 = LCMS_Util.createSPObject("COURSE_ID", String.valueOf(id) , Integer.class, ParameterMode.IN);


		Object[] courseRow;
 		Object[] courseRows = callStoredProc("LCMS_WEB_GET_COURSE_COURSE_GROUP_PB_STATUS", sparam1).toArray();


 		dto = new CourseDTO();

 		try {
			for (Object course : courseRows) {

				courseRow = (Object[]) course;

				dto.setId(TypeConvertor.AnyToLong(courseRow[0]));
				dto.setCoursePublishStatus(courseRow[1] == null || StringUtil.ifNullReturnEmpty(courseRow[11]).equalsIgnoreCase("Retired") ? ""  : courseRow[1].toString());
				dto.setName(courseRow[3].toString());
				dto.setBussinesskey(courseRow[4] == null ? "" : courseRow[4].toString());
				dto.setCourseId(courseRow[4] == null ? "" : courseRow[4].toString());

				//String description = null;

				Clob cm = (Clob )courseRow[5];
				String description = StringUtil.clobStringConversion(cm);
				dto.setDescription(TypeConvertor.AnyToString(description));
				dto.setLanguage_id(Integer.parseInt(courseRow[6].toString()));
				dto.setCeus(new BigDecimal(courseRow[7].toString()));
				dto.setKeywords(courseRow[8] == null ? "" : courseRow[8].toString());
				dto.setRatingTF(Boolean.parseBoolean(courseRow[9].toString()));
				dto.setCourseRating(Double.parseDouble(courseRow[12].toString()) > 20 ? courseRow[10].toString() : "Pending");
				dto.setCourseStatus(StringUtil.ifNullReturnEmpty(courseRow[11]));
				dto.setBusinessunitName(StringUtil.ifNullReturnEmpty(courseRow[13]));
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("In CourseDAOImpl::getCourseByID END");

		return dto;
	}



	@Transactional
	@Override
	public boolean updateCourse(Course thisCourse) throws SQLException
	{
		logger.info("In CourseDAOImpl::updateCourse Begin");

		SPCallingParams sparam1 = LCMS_Util.createSPObject("COURSE_ID", String.valueOf(thisCourse.getId()) , Integer.class, ParameterMode.IN);
		//Yasin WLCMS-183
		SPCallingParams sparam2 = LCMS_Util.createSPObject("name", thisCourse.getName(), String.class, ParameterMode.IN);
		SPCallingParams sparam3 = LCMS_Util.createSPObject("description", thisCourse.getDescription(), String.class, ParameterMode.IN);
		SPCallingParams sparam4 = LCMS_Util.createSPObject("language_id", String.valueOf(thisCourse.getLanguage_id()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam5 = LCMS_Util.createSPObject("businessunit", thisCourse.getBusinessunit_name() , String.class, ParameterMode.IN);
		SPCallingParams sparam6 = LCMS_Util.createSPObject("keywords", thisCourse.getKeywords() , String.class, ParameterMode.IN);
		SPCallingParams sparam7 = LCMS_Util.createSPObject("CONTENTOWNER_ID",String.valueOf( thisCourse.getContentowner_id() ) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam8 = LCMS_Util.createSPObject("LastUpdateUser", String.valueOf(thisCourse.getLastUpdateUser()) , Integer.class, ParameterMode.IN);


		StoredProcedureQuery qr = createQueryParameters("COURSE_UPDATE", sparam1, sparam2, sparam3, sparam4, sparam5, sparam6, sparam7, sparam8);
		qr.execute();


		SPCallingParams sCourseID = LCMS_Util.createSPObject("COURSE_ID", String.valueOf(thisCourse.getId()), String.class, ParameterMode.IN);
		SPCallingParams sUserID = LCMS_Util.createSPObject("USER_ID", String.valueOf(thisCourse.getLastUpdateUser()), String.class, ParameterMode.IN);
		StoredProcedureQuery qr1 = createQueryParameters("SET_LCMS_WEB_ASSESSMENTITEM_BANK_MAPPING", sCourseID,sUserID );
		qr1.execute();

		return true;
	}

	@Transactional
	@Override
	public int addCourse(Course thisCourse) throws SQLException
	{
		logger.info("In CourseDAOImpl::addCourse Begin");

		String GUID = UUID.randomUUID().toString().replaceAll("-", "");

		SPCallingParams sparam1 = LCMS_Util.createSPObject("NAME", thisCourse.getName(), String.class, ParameterMode.IN);
		SPCallingParams sparam2 = LCMS_Util.createSPObject("KEYWORDS", thisCourse.getKeywords() , String.class, ParameterMode.IN);
		SPCallingParams sparam3 = LCMS_Util.createSPObject("LANGUAGE_ID", String.valueOf(thisCourse.getLanguage_id()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam4 = LCMS_Util.createSPObject("CONTENTOWNER_ID", String.valueOf(thisCourse.getContentowner_id()) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam5 = LCMS_Util.createSPObject("COURSETYPE", thisCourse.getCourseType(), String.class, ParameterMode.IN);
		SPCallingParams sparam6 = LCMS_Util.createSPObject("DESCRIPTION", thisCourse.getDescription(), String.class, ParameterMode.IN);
		SPCallingParams sparam7 = LCMS_Util.createSPObject("COURSE_GUID", GUID, String.class, ParameterMode.IN);
		SPCallingParams sparam8 = LCMS_Util.createSPObject("BUSSINESSKEY", thisCourse.getGuid() , String.class, ParameterMode.IN);
		SPCallingParams sparam9 = LCMS_Util.createSPObject("CEUS", String.valueOf(thisCourse.getCeus()), BigDecimal.class, ParameterMode.IN);
		SPCallingParams sparam10 = LCMS_Util.createSPObject("CreateUserId", String.valueOf(thisCourse.getCreateUserId()), Integer.class, ParameterMode.IN);
		SPCallingParams sparam11 = LCMS_Util.createSPObject("QUICK_BUILD_COURSETYPE", String.valueOf(thisCourse.getQuick_build_coursetype()), Integer.class, ParameterMode.IN);
		SPCallingParams sparam12 = LCMS_Util.createSPObject("NEWCOURSEID", String.valueOf(thisCourse.getId()), Integer.class, ParameterMode.OUT);
		SPCallingParams sparam13 = LCMS_Util.createSPObject("BUSINESSUNIT_NAME",  thisCourse.getBusinessunit_name(), String.class, ParameterMode.IN);

		StoredProcedureQuery qr = createQueryParameters("LCMS_WEB_INSERT_COURSE", sparam1, sparam2, sparam3, sparam4, sparam5, sparam6, sparam7, sparam8, sparam9, sparam10, sparam11, sparam12, sparam13);
		qr.execute();
		int CourseID = (Integer)qr.getOutputParameterValue("NEWCOURSEID");

		SPCallingParams sCourseID = LCMS_Util.createSPObject("NAME", String.valueOf(CourseID), String.class, ParameterMode.IN);
		StoredProcedureQuery qr1 = createQueryParameters("SET_LCMS_WEB_ASSESSMENTITEM_BANK_MAPPING", sCourseID,sparam10 );
		qr1.execute();


		return CourseID;
	}

	@Transactional
	public boolean addScene(String name,long contentOwner_Id,String sceneGuid,long course_Id,long createUser_Id,int asset_Id) throws SQLException{
		logger.info("In CourseDAOImpl::addScene Begin");
		String GUID = UUID.randomUUID().toString().replaceAll("-", "");

		SPCallingParams sparam1 = LCMS_Util.createSPObject("NAME", name, String.class, ParameterMode.IN);
		SPCallingParams sparam2 = LCMS_Util.createSPObject("CONTENTOWNER_ID", String.valueOf(contentOwner_Id) , Long.class, ParameterMode.IN);
		SPCallingParams sparam3 = LCMS_Util.createSPObject("SCENE_GUID", GUID , String.class, ParameterMode.IN);
		SPCallingParams sparam4 = LCMS_Util.createSPObject("COURSE_ID", String.valueOf(course_Id) , Long.class, ParameterMode.IN);
		SPCallingParams sparam5 = LCMS_Util.createSPObject("CreateUserId", String.valueOf(createUser_Id) , Long.class, ParameterMode.IN);
		SPCallingParams sparam6 = LCMS_Util.createSPObject("ASSET_ID", String.valueOf(asset_Id) , Integer.class, ParameterMode.IN);

		StoredProcedureQuery qr = createQueryParameters("LCMS_WEB_INSERT_SCENE", sparam1, sparam2, sparam3, sparam4, sparam5, sparam6);
		qr.execute();

		return true;

	}

	@Transactional
	public boolean deleteScene(long course_Id) throws SQLException{
		logger.info("In CourseDAOImpl::deleteScene Begin");

		SPCallingParams sparam1 = LCMS_Util.createSPObject("COURSE_ID", String.valueOf(course_Id), Integer.class, ParameterMode.IN);
		StoredProcedureQuery qr = createQueryParameters("DELETE_SCENE_ALL", sparam1);
		qr.execute();
		return true;


	}

	@Override
	@Transactional
	public List<CourseDTO> searchCourses(SearchCourseFilter filter) throws SQLException{
		logger.info("In CourseDAOImpl::searchCourses Begin");


		List<CourseDTO> coursesDTOList = new ArrayList<CourseDTO>();

		CourseDTO dto;
		SPCallingParams sparam1 = LCMS_Util.createSPObject("SEARCH_CRITERIA",	filter.getSearchCriteria(), String.class, ParameterMode.IN);
		SPCallingParams sparam2 = LCMS_Util.createSPObject("CONTENTOWNER_ID",	filter.getContentOwnerId(), Integer.class, ParameterMode.IN);
		SPCallingParams sparam3 = LCMS_Util.createSPObject("LOGIN_AUTHOR_ID",	filter.getLoginAuthorId(), Integer.class, ParameterMode.IN);

		Object[] courseRows = callStoredProc("SEARCH_COURSE_WEB_BASED", sparam1, sparam2, sparam3).toArray();

		Object[] courseRow;

		for (Object course : courseRows) {
			courseRow = (Object[]) course;

			dto = new CourseDTO();
			dto.setId(TypeConvertor.AnyToLong(courseRow[0]));
			dto.setCourseId(courseRow[2] == null ? "" : courseRow[2].toString());
			dto.setName(courseRow[1].toString());
			dto.setLastModifiedDate(courseRow[3] == null ? "" : courseRow[3]
					.toString());
			dto.setCourseStatus(courseRow[4].toString());
			dto.setCoursePublishStatus(courseRow[6].toString());
			dto.setCourseGroupName(courseRow[5].toString());
			dto.setCourseRating(Integer.parseInt(courseRow[8].toString()) > 5 ? courseRow[7].toString() : "");
			coursesDTOList.add(dto);
		}

		return coursesDTOList;

	}

	@Override
	@Transactional
	public List<Course> getCoursesList(int AuthorId) throws SQLException{
		logger.info("In CourseDAOImpl::getCoursesList Begin");

		SPCallingParams sparam1 = LCMS_Util.createSPObject("CONTENTOWNER_ID", String.valueOf(AuthorId), Integer.class, ParameterMode.IN);
		List<Course> coursesDTOList = new ArrayList<Course>();

		Course dto;
		Object[] courseRows = callStoredProc("LCMS_WEB_SELECT_COURSE", sparam1).toArray();
		Object[] courseRow;

		for (Object course : courseRows) {
			courseRow = (Object[]) course;

			dto = new Course();
			dto.setId(Integer.parseInt(courseRow[0].toString()));
			dto.setCourseid(courseRow[1] == null ? "" : courseRow[1].toString());
			dto.setName(courseRow[1].toString());
			dto.setBussinesskey(courseRow[2].toString());
			dto.setLastModifiedDate(courseRow[3].toString());
			dto.setCourseType(courseRow[4].toString());
			dto.setCourseTypeName(courseRow[5].toString());
			dto.setCourseStatusRetired(courseRow[6].toString());
			coursesDTOList.add(dto);
		}

		return coursesDTOList;
	}


	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<ContentObject> getContentObjectList(int CourseID) {
		SPCallingParams sparam1 = LCMS_Util.createSPObject("COURSE_ID", String.valueOf(CourseID) , Integer.class, ParameterMode.IN);
		StoredProcedureQuery qr = createQueryParameters("LCMS_WEB_SELECT_COURSE_CONTENTOBJECT", sparam1);
		List<ContentObject> coList = (List<ContentObject>) qr.getResultList();
		return coList;

	}

	@Transactional
	@Override
	public ContentObject addLesson(ContentObject co) {

		SPCallingParams sparam1 = LCMS_Util.createSPObject("COURSE_ID", co.getCourseID().toString(), Integer.class, ParameterMode.IN);
		SPCallingParams sparam2 = LCMS_Util.createSPObject("NAME", co.getName(), String.class, ParameterMode.IN);
		SPCallingParams sparam3 = LCMS_Util.createSPObject("DESCRIPTION", co.getDescription(), String.class, ParameterMode.IN);
		SPCallingParams sparam4 = LCMS_Util.createSPObject("CONTENTOBJECT_GUID", co.getCO_GUID(), String.class, ParameterMode.IN);
		SPCallingParams sparam7 = LCMS_Util.createSPObject("@NEWID", String.valueOf(""), Integer.class, ParameterMode.OUT);
		SPCallingParams sparam5 = LCMS_Util.createSPObject("CONTENTOWNER_ID", co.getContentOwner_Id().toString(), Integer.class, ParameterMode.IN);
		SPCallingParams sparam6 = LCMS_Util.createSPObject("CreateUserId", co.getCreateUserId().toString(), Integer.class, ParameterMode.IN);
		// Need to change this, make it something like accept array
		SPCallingParams sparam8 = LCMS_Util.createSPObject("LearningObjective", co.getLearningObjective(), String.class, ParameterMode.IN);
		StoredProcedureQuery qr = createQueryParameters("LCMS_WEB_INSERT_CONTENTOBJECT", sparam1,sparam2,sparam3,sparam4,sparam7,sparam5,sparam6, sparam8);
		qr.execute();
		Integer new_ID =  (Integer) qr.getOutputParameterValue("@NEWID");
		co.setID(new_ID);

		StoredProcedureQuery qr1 = createQueryParameters("SET_LCMS_WEB_ASSESSMENTITEM_BANK_MAPPING", sparam1, sparam6 );
		qr1.execute();


		//WLCMS-163 - Auto-create Lesson Item Banks for Final Exam
		SPCallingParams sparam9 = LCMS_Util.createSPObject("CONTENTOBJECT_ID", new_ID.toString(), Integer.class, ParameterMode.IN);
		SPCallingParams sparam10 = LCMS_Util.createSPObject("USER_ID", co.getCreateUserId().toString(), Integer.class, ParameterMode.IN);
		SPCallingParams sparam11 = LCMS_Util.createSPObject("CONTENTOBJECT_NAME", co.getName(), String.class, ParameterMode.IN);
		StoredProcedureQuery qr2 = createQueryParameters("LESSON_SET_EXAM_BANKS_ADD", sparam1, sparam9, sparam10, sparam11, sparam5 );
		qr2.execute();

		return co;
	}

	@Override
	@Transactional
	public List<Slide> getSlidesByContentObject(long varContendOjectId){

		List<Slide> coursesDTOList = new ArrayList<Slide>();
		Slide dto;
		SPCallingParams sparam1 = LCMS_Util.createSPObject("CONTENTOBJECT_ID",	String.valueOf(varContendOjectId), Integer.class, ParameterMode.IN);

		Object[] courseRows = callStoredProc("SELECT_CONTENTOBJECT_SCENES", sparam1).toArray();

		Object[] courseRow;

		try{
			for (Object slide : courseRows) {
				courseRow = (Object[]) slide;

				dto = new Slide();

				dto.setId(TypeConvertor.AnyToLong(courseRow[1]));
				dto.setName(StringUtil.ifNullReturnEmpty(courseRow[2]));
				dto.setTemplateID( Long.parseLong(StringUtil.ifNullReturnEmpty(courseRow[9])) );
				dto.setContentObjectScene_id(Integer.parseInt(StringUtil.ifNullReturnEmpty(courseRow[11])));
				coursesDTOList.add(dto);

			}
		}catch(Exception ex){
			logger.error(ex);
		}
		return coursesDTOList;
	}

	@Override
	@Transactional
	public Object[] getContentObject(long varContendOjectId) throws SQLException{
		SPCallingParams sparam1 = LCMS_Util.createSPObject("ID", String.valueOf(varContendOjectId) , Integer.class, ParameterMode.IN);
		//StoredProcedureQuery qr = createQueryParameters("SELECT_CONTENTOBJECT", sparam1);
		Object[] courseRows = callStoredProc("SELECT_CONTENTOBJECT", sparam1).toArray();
		return courseRows;
	}


	@Override
	@Transactional
	public ContentObject getContentObject2(long varContendOjectId) throws SQLException{

		SPCallingParams sparam1 = LCMS_Util.createSPObject("@CONTENTOBJECT_ID", String.valueOf(varContendOjectId) , Integer.class, ParameterMode.IN);
		Object[] contentObjectRows = callStoredProc("LCMS_WEB_GET_LEARNEROBJECTIVE_CONTENTOBJECTID", sparam1).toArray();
		Object[] contentObjectRow;
		ContentObject dto = new ContentObject();

		try{
			for (Object contentObject : contentObjectRows) {
				contentObjectRow = (Object[]) contentObject;
				dto.setID(TypeConvertor.AnyToInteger(contentObjectRow[0]));
				dto.setName(contentObjectRow[1].toString());
				Clob cb = (Clob)contentObjectRow[2];
				dto.setDescription(StringUtil.clobStringConversion(cb));
				cb = (Clob)contentObjectRow[3];
				dto.setLearningObjective(StringUtil.clobStringConversion(cb));
				dto.setAllowQuiz(TypeConvertor.AnyToInteger(contentObjectRow[4]));
			}
			// For edit and listing.
			dto.setLstContentObject(this.getContentLearningObject((int)varContendOjectId));
		}catch(Exception ee){
			logger.error(ee);

		}

		return dto;

	}

	@Transactional
	@Override
	public boolean updateContentObject(ContentObject co) throws SQLException
	{
		logger.info("In CourseDAOImpl::updateCourse Begin");

		SPCallingParams sparamContentObjectId = LCMS_Util.createSPObject("ID", String.valueOf(co.getID()) , 					 Integer.class, ParameterMode.IN);
		SPCallingParams sparamContentObjectName = LCMS_Util.createSPObject("NAME", String.valueOf(co.getName()) , 				 String.class, ParameterMode.IN);
		SPCallingParams sparam3 = LCMS_Util.createSPObject("DESCRIPTION", co.getDescription() , 				 String.class, ParameterMode.IN);
		SPCallingParams sparam4 = LCMS_Util.createSPObject("LEARNINGOBJECTIVE", co.getLearningObjective() ,		 String.class, ParameterMode.IN);
		SPCallingParams sparam5 = LCMS_Util.createSPObject("LearningObjectiveId", co.getLearningObjectiveId() ,	 String.class, ParameterMode.IN);
		SPCallingParams sparamUserId = LCMS_Util.createSPObject("CreateUserID", co.getCreateUserId().toString() ,	 Integer.class, ParameterMode.IN);
		SPCallingParams sparamCourseId = LCMS_Util.createSPObject("CourseID", co.getCourseID().toString(),	 			 Integer.class, ParameterMode.IN);
		SPCallingParams sparamLastUpdateUser = LCMS_Util.createSPObject("@LastUpdateUser", co.getLastUpdateUser().toString(),	 			 Integer.class, ParameterMode.IN);

		StoredProcedureQuery qr = createQueryParameters("LCMS_WEB_UPDATE_CONTENTOBJECT", sparamContentObjectId, sparamContentObjectName, sparam3, sparam4, sparam5, sparamUserId, sparamCourseId,sparamLastUpdateUser);
		qr.execute();


		StoredProcedureQuery qr1 = createQueryParameters("SET_LCMS_WEB_ASSESSMENTITEM_BANK_MAPPING", sparamCourseId, sparamUserId );
		qr1.execute();

		//WLCMS-163 - Update Lesson's Item Banks for Final Exam on update Lesson
		StoredProcedureQuery qr2 = createQueryParameters("LESSON_SET_EXAM_BANKS_UPDATE", sparamCourseId, sparamContentObjectId, sparamUserId, sparamContentObjectName);
		qr2.execute();



		return true;
	}

	@Transactional
	@Override
	public boolean deleteLesson (String varLessonId, String courseId,long loginUserId) throws Exception{
		logger.info("In CourseDAOImpl::deleteLesson --- Begin");

		try{
			Long loginUserIdObj = loginUserId;
			SPCallingParams sparam1 = LCMS_Util.createSPObject("@CONTENTOBJECT_ID", varLessonId , Integer.class, ParameterMode.IN);
			SPCallingParams sparam2 = LCMS_Util.createSPObject("@COURSE_ID", courseId , Integer.class, ParameterMode.IN);
			SPCallingParams sparam3 = LCMS_Util.createSPObject("CreateUserId", loginUserIdObj.toString(), Integer.class, ParameterMode.IN);

			StoredProcedureQuery qr = createQueryParameters("LCMS_WEB_DELETE_CONTENTOBJECT", sparam1,sparam2,sparam3);
			qr.execute();

			/// DELETE CONTENT_LEARNING OBJECTIVE
			StoredProcedureQuery qr2 = createQueryParameters("DELETE_CONTENTOBJECT_LEARNINGOBJECTIVE", sparam1);
			qr2.execute();

			logger.info("In CourseDAOImpl::deleteLesson --- LESSON_SET_EXAM_BANKS_DELETE");
			//WLCMS-163 - disabling Lesson's Item Banks for Final Exam upon delete lesson
			SPCallingParams sparamCourseId = LCMS_Util.createSPObject("COURSE_ID", courseId , Integer.class, ParameterMode.IN);
			SPCallingParams sparamContentObjectId = LCMS_Util.createSPObject("CONTENTOBJECT_ID", varLessonId , Integer.class, ParameterMode.IN);
			StoredProcedureQuery qr3 = createQueryParameters("LESSON_SET_EXAM_BANKS_DELETE", sparamCourseId, sparamContentObjectId);
			qr3.execute();

		}catch(Exception ex){
			logger.error(ex);
			return false;
		}


		logger.info("In CourseDAOImpl::deleteLesson --- END");

		return true;
	}

	@Transactional
	@Override
	public List<LearningObject> getContentLearningObject(int contentID)
			throws SQLException {


		SPCallingParams sparam1 = LCMS_Util.createSPObject("CONTENTOBJECT_ID", String.valueOf(contentID) , Integer.class, ParameterMode.IN);
		StoredProcedureQuery qr = createQueryParameters("SELECT_CONTENTOBJECT_LEARNINGOBJECTIVE", sparam1);

		Object[] loRows = qr.getResultList().toArray();

		Object[] loRow;
		List<LearningObject>  loDTOList = new ArrayList<LearningObject>();

			for (Object lo : loRows) {
				loRow = (Object[]) lo;

				if(loRow[1]!=null && !loRow[1].toString().equals("ADDITIONAL QUESTIONS")){ //WLCMS-735

					LearningObject dto = new LearningObject();
					try {
						dto.setId(TypeConvertor.AnyToInteger(loRow[0]));
					} catch (NumberFormatException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						dto.setName(StringUtil.ifNullReturnEmpty(loRow[1]));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						dto.setDescription(StringUtil.ifNullReturnEmpty(loRow[2]));
					} catch (Exception e) {
						e.printStackTrace();
					}

					loDTOList.add(dto);
				}// if check for ADDITIONAL QUESTIONS

			}

		return loDTOList;
	}


	@Transactional
	@Override
	public boolean deleteLLO(int id, int contentObjectId) throws Exception {

		SPCallingParams sparam1 = LCMS_Util.createSPObject("contentObject_id",String.valueOf( contentObjectId ), Integer.class, ParameterMode.IN);
		SPCallingParams sparam2 = LCMS_Util.createSPObject("learningobjective_id",String.valueOf( id ), Integer.class, ParameterMode.IN);
		StoredProcedureQuery qr = createQueryParameters("DELETE_CONTENTOBJECT_LEARNINGOBJECTIVE1", sparam1, sparam2);
		qr.execute();

		return true;
	}

	@Transactional
	public boolean isFinalExamEnabled(int courseID)	throws Exception {
		SPCallingParams sparam1 = LCMS_Util.createSPObject("COURSE_ID", String.valueOf(courseID) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam2 = LCMS_Util.createSPObject("USER_ID", String.valueOf("-1") , Integer.class, ParameterMode.IN);
		SPCallingParams sparam3 = LCMS_Util.createSPObject("EXAMTYPE", String.valueOf("PostAssessment") , String.class, ParameterMode.IN);
		StoredProcedureQuery qr = createQueryParameters("LCMS_WEB_UPDATECCT_BY_TYPE_GET", sparam1, sparam2, sparam3);
		Boolean coList = (Boolean) qr.getSingleResult();

		if(coList!=null)
			return coList.booleanValue();
		else
			return false;
	}

	@Override
	@Transactional
	public List<SlideTemplate> getSlideTemplateID(int contentOwnerId)
			throws Exception {
		// TODO Auto-generated method stub
		List<SlideTemplate> slideTemplates = new ArrayList<SlideTemplate>();
		//SPCallingParams sparam1 = LCMS_Util.createSPObject("CONTENTOWNER_ID", String.valueOf(contentOwnerId) , Integer.class, ParameterMode.IN);
		//StoredProcedureQuery qr = createQueryParameters("SELECT_SCENETEMPLATES", sparam1);

		//Object[] loRows = qr.getResultList().toArray();//qr.getResultList().toArray();
		Object[] loRows = entityManager.createNativeQuery("exec SELECT_SCENETEMPLATES "+contentOwnerId).getResultList().toArray();

		Object[] loRow;

			for (Object lo : loRows) {
				loRow = (Object[]) lo;

				if(loRow != null){
					SlideTemplate slideTemplate = new SlideTemplate();
					slideTemplate.setSlideTemplateID(TypeConvertor.AnyToInteger(loRow[0]));
					slideTemplate.setSlideTemplateName(StringUtil.ifNullReturnEmpty(loRow[1]));
					slideTemplate.setSlideTemplateURL(StringUtil.ifNullReturnEmpty(loRow[4]));
					slideTemplates.add(slideTemplate);
				}
			}


		return slideTemplates;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Integer getPublishedVersion(String courseId) throws Exception{

		EntityManager entityManager;
	    entityManager = getEntityManager();
	    Integer publishedCourseId= -1;
	    javax.persistence.Query query = entityManager.createNativeQuery("select FROM_COURSE_ID from Course_derived_from_course where to_course_id=:courseId and Type='Drafted'");
        query.setParameter("courseId", courseId);

        try{

        	publishedCourseId = (Integer)query.getSingleResult();
        	entityManager = null;
        }catch(NoResultException  ex){
        	ex.printStackTrace();
        }

        return publishedCourseId ;
	}




	@Transactional
	public CourseDTO saveCourse(CourseDTO course){
		//String GUID = UUID.randomUUID().toString().replaceAll("-", "");
		//course.setGuid(GUID);
		course = super.save(course);
		entityManager.flush();
		this.addRecentCourse(course.getId());
		return course;
	}

	@Override
	@Transactional
	public CourseDTO getCourseById(long courseId) {
		// TODO Auto-generated method stub
		CourseDTO course = super.getById(courseId);
		return course;
	}


	@Override
	@Transactional
	public List<CourseDisplayOrder> getCourseDisplay(long courseId) throws Exception{
		// TODO Auto-generated method stub
		List<CourseDisplayOrder> courseDisplayOrderList = new ArrayList<CourseDisplayOrder>();
		entityManager.createNativeQuery("UPDATE COURSE SET LastUpdatedDate=GETDATE()  WHERE ID="+courseId).executeUpdate();
		Object[] loRows = entityManager.createNativeQuery("exec GET_COURSE_DISPLAYORDERS "+courseId).getResultList().toArray();

		Object[] loRow;

			for (Object lo : loRows) {
				loRow = (Object[]) lo;

				if(loRow != null){
					CourseDisplayOrder courseDisplayOrder = new CourseDisplayOrder();
					courseDisplayOrder.setId(TypeConvertor.AnyToInteger(loRow[0]));
					courseDisplayOrder.setItem_Id(TypeConvertor.AnyToInteger(loRow[1]));
					courseDisplayOrder.setItem_Type(StringUtil.ifNullReturnEmpty(loRow[2]));
					courseDisplayOrder.setDisplayOrder(TypeConvertor.AnyToInteger(loRow[3]));

					courseDisplayOrderList.add(courseDisplayOrder);
				}
			}


		return courseDisplayOrderList;
	}

	@Transactional
	@Override
	public boolean UpdateCourseDisplayOrder(CourseDisplayOrder courseDisplayOrder,long course_Id,long lastUpdateUser) throws Exception {

		SPCallingParams sparam1 = LCMS_Util.createSPObject("ID",String.valueOf( courseDisplayOrder.getId() ), Integer.class, ParameterMode.IN);
		SPCallingParams sparam2 = LCMS_Util.createSPObject("ITEM_ID",String.valueOf( courseDisplayOrder.getItem_Id() ), Integer.class, ParameterMode.IN);
		SPCallingParams sparam3 = LCMS_Util.createSPObject("ITEMTYPE",String.valueOf( courseDisplayOrder.getItem_Type() ), String.class, ParameterMode.IN);
		SPCallingParams sparam4 = LCMS_Util.createSPObject("DISPLAYORDER",String.valueOf( courseDisplayOrder.getDisplayOrder() ), Integer.class, ParameterMode.IN);
		SPCallingParams sparam5 = LCMS_Util.createSPObject("COURSE_ID",String.valueOf( course_Id ), Integer.class, ParameterMode.IN);
		SPCallingParams sparam6 = LCMS_Util.createSPObject("LastUpdateUser",String.valueOf( lastUpdateUser ), Long.class, ParameterMode.IN);
		StoredProcedureQuery qr = createQueryParameters("UPDATE_COURSEDISPLAYORDER", sparam1, sparam2,sparam3,sparam4,sparam5,sparam6);
		qr.execute();

		return true;
	}

	@Transactional
	public CourseDTO updateCourse(CourseDTO course){

		CourseDTO  courseDTO = entityManager.merge(course);
		entityManager.flush();
		return courseDTO;
	}

	@Override
	@Transactional
	public int addRecentCourse(long courseId){

		 Query query = entityManager.createNativeQuery(INSERT_RECENT_COURSE);
		 query.setParameter(1, courseId);
		 return query.executeUpdate();
	}




	@Override
	@Transactional
	public List<CourseGroup> getCourseGroupByContentOwner (long contentOwnerId, String courseGroupName){
		Query query = entityManager.createQuery(" SELECT cg FROM CourseGroup cg "
												+ " WHERE cg.contentOwnerId=:CONTENTOWNER_ID and cg.name=:COURSEGROUP_NAME");

		query.setParameter("CONTENTOWNER_ID", contentOwnerId);
		query.setParameter("COURSEGROUP_NAME", courseGroupName);

		List<CourseGroup> lstCG = (List<CourseGroup>)query.getResultList();

		return lstCG;

	}

	@Override
	@Transactional
	public boolean updateCourseModifiedDate(long courseId){
		try{
		Query query = entityManager.createNativeQuery("UPDATE COURSE SET LastUpdatedDate=GETDATE() WHERE ID=:COURSE_ID");

		query.setParameter("COURSE_ID", courseId);

		int rocordUpdated = query.executeUpdate();

		if(rocordUpdated>0)
			return true;
		else
			return false;
		}catch(Exception exp){
			logger.error(" Error in method::updateCourseModifiedDate. Message:" + exp.getMessage());
			return false;
		}


	}
    @Override
    @Transactional
    public List<CourseDTO> getCoursesByOwnerIdAndBusinessKey(Integer ownerId, String businessKey) {
       TypedQuery<CourseDTO> query = entityManager.createQuery("select c from CourseDTO c where bussinesskey =:courseId and contentownerId = :ownerId", CourseDTO.class);
        query.setParameter("ownerId", ownerId);
        query.setParameter("courseId", businessKey);
        return query.getResultList();
    }

    @Override
    @Transactional
    public List<CourseDTO> getCoursesByOwnerIdAndBusinessKeys(Integer ownerId, Collection<String> businessKeys) {
        TypedQuery<CourseDTO> query = entityManager.createQuery("select c from CourseDTO c where bussinesskey in :courseIds and contentownerId = :ownerId", CourseDTO.class);
        query.setParameter("ownerId", ownerId);
        query.setParameter("courseIds", businessKeys);
        return query.getResultList();
    }

    @Override
    @Transactional
    public CourseDTO getCourseByOwnerIdAndBusinessKey(Integer ownerId, String businessKey) {
        List<CourseDTO> courses = getCoursesByOwnerIdAndBusinessKey(ownerId, businessKey);
        if(courses.size() > 0) {
          return courses.get(0);
        }
        return null;
    }

    @Override
    @Transactional
	public int getStreamingTemplateId(){
		Query query = entityManager.createNativeQuery("Select ID From scenetemplate where name  = 'Video Streaming Center' and ContentOwner_Id=1");
		Object id = query.getSingleResult();
		return id == null ? 0 : Integer.parseInt((id.toString()));
    }

    @Override
    @Transactional
    public boolean updateCourseLastUpdateUserandModifiedDate(long lastUpdateUser,long courseId){
    	try{
    		Query query = entityManager.createNativeQuery("UPDATE COURSE SET LastUpdatedDate=GETDATE(),LastUpdateUser=:lastUpdateUser WHERE ID=:COURSE_ID");

    		query.setParameter("COURSE_ID", courseId);
    		query.setParameter("lastUpdateUser", lastUpdateUser);

    		int rocordUpdated = query.executeUpdate();

    		if(rocordUpdated>0)
    			return true;
    		else
    			return false;
    		}catch(Exception exp){
    			logger.error(" Error in method::updateCourseLastUpdateUserandModifiedDate. Message:" + exp.getMessage());
    			return false;
    		}


    }
}
