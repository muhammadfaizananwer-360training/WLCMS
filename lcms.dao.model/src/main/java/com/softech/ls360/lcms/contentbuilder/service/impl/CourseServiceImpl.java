package com.softech.ls360.lcms.contentbuilder.service.impl;


import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.softech.ls360.lcms.contentbuilder.utils.TypeConvertor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.softech.ls360.lcms.contentbuilder.dao.CourseDAO;
import com.softech.ls360.lcms.contentbuilder.model.ContentObject;
import com.softech.ls360.lcms.contentbuilder.model.Course;
import com.softech.ls360.lcms.contentbuilder.model.CourseDTO;
import com.softech.ls360.lcms.contentbuilder.model.CourseDisplayOrder;
import com.softech.ls360.lcms.contentbuilder.model.CourseGroup;
import com.softech.ls360.lcms.contentbuilder.model.Lesson;
import com.softech.ls360.lcms.contentbuilder.model.SearchCourseFilter;
import com.softech.ls360.lcms.contentbuilder.model.Slide;
import com.softech.ls360.lcms.contentbuilder.model.SlideTemplate;
import com.softech.ls360.lcms.contentbuilder.model.VU360UserDetail;
import com.softech.ls360.lcms.contentbuilder.service.ICourseService;
import com.softech.ls360.lcms.contentbuilder.service.ISlideService;
import com.softech.ls360.lcms.contentbuilder.utils.LCMSDateUtils;
import com.softech.ls360.lcms.contentbuilder.utils.LCMSProperties;
import com.softech.ls360.lcms.contentbuilder.utils.StringUtil;

public class CourseServiceImpl implements ICourseService {
	
	private static Logger logger = LoggerFactory
			.getLogger(CourseServiceImpl.class);
	

	@Autowired
	public CourseDAO courseDAO;
	
	@Autowired
	public ISlideService slideService;

	public CourseDAO getCourseDAO() {
		return courseDAO;
	}

	public void setCourseDAO(CourseDAO courseDAO) {
		this.courseDAO = courseDAO;
	}

	@Override
	public int addCourse(Course myCourse) throws SQLException { 
			return courseDAO.addCourse(myCourse);
	}

	public boolean editCourse() {
		// TODO Auto-generated method stub
		return false;
	}

/*
	@Override
	public List<Course> getCourse(String courseName) {

		Course searchedCourse = new Course();
		searchedCourse.setName(courseName);

		List<Course> courseList = null;//courseDAO.find(searchedCourse);

		return courseList;
	}
	
	*/

	@Override
	public CourseDTO getCourse(int id) throws SQLException {

		CourseDTO crs = courseDAO.getCourseByID(id);
		return crs;
	}
/*
	@Override
	public List<Course> getCourse(Course myCourse) {

		List<Course> courseDTO = null;//courseDAO.find(myCourse);
		if (courseDTO == null || courseDTO.size() < 0)
			return null;

		return courseDTO;
	}
	*/
	@Override
	public List<CourseDTO> searchCourses(SearchCourseFilter filter) throws SQLException {
	
		//List<CourseDTO> coursesDTOList = new ArrayList<CourseDTO>();
		/*
		CourseDTO dto;

	
	
		SPCallingParams sparam1 = LCMS_Util.createSPObject("SEARCH_CRITERIA",
				filter.getSearchCriteria(), String.class, ParameterMode.IN);
		SPCallingParams sparam2 = LCMS_Util.createSPObject("CONTENTOWNER_ID",
				filter.getContentOwnerId(), Integer.class, ParameterMode.IN);
		SPCallingParams sparam3 = LCMS_Util.createSPObject("LOGIN_AUTHOR_ID",
				filter.getLoginAuthorId(), Integer.class, ParameterMode.IN);

		Object[] courseRows = courseDAO.callStoredProc(
				"SEARCH_COURSE_WEB_BASED", sparam1, sparam2, sparam3).toArray();
		 
		
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
			dto.setCourseRating(Integer.parseInt(courseRow[8].toString()) > 5 ? courseRow[7]
					.toString() : "");

			coursesDTOList.add(dto);
		}
		*/
		List<CourseDTO> coursesDTOList  = courseDAO.searchCourses(filter);
		
		return coursesDTOList;
	}

	
	@Override
	public List<Course> getCoursesList(int AuthorId) throws SQLException {
	
		//List<Course> coursesDTOList = new ArrayList<Course>();
		List<Course> coursesDTOList = courseDAO.getCoursesList(AuthorId);
		
		return coursesDTOList;
	}
	@Override
	public boolean updateCourse(Course myCourse) throws SQLException {
		return courseDAO.updateCourse(myCourse);
	}

	@Override
	public boolean addScene(String name, long contentOwner_Id,
			String sceneGuid, long course_Id, long createUser_Id, int asset_Id) throws SQLException {		
			return courseDAO.addScene(name, contentOwner_Id, sceneGuid,course_Id, createUser_Id, asset_Id);
	}

	@Override
	public boolean deleteScene(long course_Id) throws SQLException {
		return courseDAO.deleteScene(course_Id);	
	}

	@Override
	public List<ContentObject> getContentObjectList(int courseID)
			throws SQLException {
		
		return courseDAO.getContentObjectList(courseID);
	}

	@Override
	public boolean isFinalExamEnabled(int courseID)	throws Exception {
		
		return courseDAO.isFinalExamEnabled(courseID);
	}

	@Override
	public ContentObject addLesson(ContentObject co) {
		return courseDAO.addLesson(co);	
		
	}
	
	@Override
	public List<Slide> getSlidesByContentObject(long varContendOjectId)	throws SQLException {
		return courseDAO.getSlidesByContentObject(varContendOjectId);
	}

	public ContentObject getContentObject(long varContendOjectId) throws Exception{
		
		ContentObject dto = new ContentObject();
		Object[] contentObjectRows =	courseDAO.getContentObject(varContendOjectId);
		Object[] contentObjectRow;
		
		for (Object slide : contentObjectRows) {
			contentObjectRow = (Object[]) slide;
			dto.setID(TypeConvertor.AnyToInteger(contentObjectRow[0]));
			dto.setName(contentObjectRow[4].toString());
			Clob cb = (Clob)contentObjectRow[5];
			dto.setDescription(StringUtil.clobStringConversion(cb));
			
			/*dto.setPARENT_CO_ID(TypeConvertor.AnyToInteger(contentObjectRow[2]));
			dto.setContentObjectType(contentObjectRow[3].toString());
			if(contentObjectRow[6]!=null && contentObjectRow[6].toString().equalsIgnoreCase("false"))
				dto.setALLOWQUIZTF(new Integer(0));
			else
				dto.setALLOWQUIZTF(new Integer(1));
			
			if(contentObjectRow[8]!=null && contentObjectRow[8].toString().equalsIgnoreCase("false"))
				dto.setSHOWINOUTLINETF(new Integer(0));
			else
				dto.setSHOWINOUTLINETF(new Integer(1));
			
			if(contentObjectRow[9]!=null && contentObjectRow[9].toString().equalsIgnoreCase("false"))
				dto.setROOTNODETF(new Integer(0));
			else
				dto.setROOTNODETF(new Integer(1));
			
			dto.setMAXQUIZQUESTIONSTOASK(new Integer( contentObjectRow[11].toString()));
			
			if(contentObjectRow[12]!=null && contentObjectRow[12].toString().equalsIgnoreCase("false"))
				dto.setOVERRIDEMAXQUIZQUESTIONSTOASK(new Integer(0));
			else
				dto.setOVERRIDEMAXQUIZQUESTIONSTOASK(new Integer(1));*/
			
		}
		
		
		return dto;
	}
	
	@Override
	public ContentObject getContentObject2(long varContendOjectId) throws SQLException{
		return courseDAO.getContentObject2(varContendOjectId);	
		
	}
	
	@Override
	public boolean updateContentObject(ContentObject co) throws SQLException {
		return courseDAO.updateContentObject(co);
	}
	
	
	@Override
	public boolean deleteLesson (String varLessonId, String courseId,long loginUserId) throws Exception{
		return courseDAO.deleteLesson(varLessonId, courseId,loginUserId);
	}

	@Override
	public boolean deleteLLO(int id, int contentObjectId) throws Exception {
		return courseDAO.deleteLLO(id,  contentObjectId);
	}

	@Override
	public List<SlideTemplate> getSlideTemplateID(int contentOwnerId)
			throws Exception {
		// TODO Auto-generated method stub
		//List<SlideTemplate> slideTemplaeList = new ArrayList<SlideTemplate>();
		List<SlideTemplate> slideTemplaeList = courseDAO.getSlideTemplateID(contentOwnerId);
		return slideTemplaeList;
	}

	@Override
	public Integer getPublishedVersion(String courseId) throws Exception{
		return courseDAO.getPublishedVersion(courseId); 
	}
	
	
	
	@Override
	public CourseDTO saveCourse(CourseDTO entity){
		return courseDAO.saveCourse(entity);
	}
	
	@Override
	public CourseDTO getCourseById(long courseId){
		return courseDAO.getCourseById(courseId);
	}

	@Override
	public boolean setCourseDisplayOrder(long courseId, int item_Id,
			String item_type, int direction,long lastModifiedUser) {
		// TODO Auto-generated method stub
		try {
			int item_index=-1;
			int dest_item_index=-1;
			List<CourseDisplayOrder> courseDisplayOrderList = courseDAO.getCourseDisplay(courseId);
			List<CourseDisplayOrder> courseDisplayOrderList_TobeMoved = new ArrayList<CourseDisplayOrder>();

			if(item_type.equals("Scene")){
				CourseDisplayOrder courseDisplayOrder_src = null;
				CourseDisplayOrder courseDisplayOrder_other = null;
				for (int i=0;i<courseDisplayOrderList.size();i++) {
					CourseDisplayOrder courseDisplayOrder = courseDisplayOrderList.get(i);
					if(courseDisplayOrder.getItem_Id()==item_Id){
						
						if(direction==1){//up
							courseDisplayOrder_src = courseDisplayOrder;
							courseDisplayOrder_src.setDisplayOrder(courseDisplayOrder_src.getDisplayOrder()-1);
							courseDisplayOrder_other = courseDisplayOrderList.get(i-1);
							courseDisplayOrder_other.setDisplayOrder(courseDisplayOrder_other.getDisplayOrder()+1);
						}
						else{
							courseDisplayOrder_src = courseDisplayOrder;
							courseDisplayOrder_src.setDisplayOrder(courseDisplayOrder_src.getDisplayOrder()+1);
							courseDisplayOrder_other = courseDisplayOrderList.get(i+1);
							courseDisplayOrder_other.setDisplayOrder(courseDisplayOrder_other.getDisplayOrder()-1);
							
						}
						break;
					}
				}
				if(courseDisplayOrder_src!=null)
					courseDAO.UpdateCourseDisplayOrder(courseDisplayOrder_src, courseId, lastModifiedUser);
				if(courseDisplayOrder_other!=null)
					courseDAO.UpdateCourseDisplayOrder(courseDisplayOrder_other, courseId, lastModifiedUser);

			}
			else{
				for (int i=0;i<courseDisplayOrderList.size();i++) {
					//System.out.println(courseDisplayOrderList.get(i).getId() +":"+courseDisplayOrderList.get(i).getItem_Id()+":"+courseDisplayOrderList.get(i).getItem_Type()+":"+courseDisplayOrderList.get(i).getDisplayOrder());
					if(courseDisplayOrderList.get(i).getItem_Id()==item_Id){
						
						item_index = i;//9
						courseDisplayOrderList_TobeMoved.add(courseDisplayOrderList.get(i));
					}
					if(item_index>-1 && i>item_index ){
						if(courseDisplayOrderList.get(i).getItem_Type().equals("ContentObject"))
							break;
						
						courseDisplayOrderList_TobeMoved.add(courseDisplayOrderList.get(i));
					}
						
				}
				
				courseDisplayOrderList.removeAll(courseDisplayOrderList_TobeMoved);
	
				if(direction==1){//up
					for (int i=item_index-1;i>=0;i--) {
						if(courseDisplayOrderList.get(i).getItem_Type().equals("ContentObject")){
							dest_item_index=i;
							break;
						}
					}
				}else{//down
					if(item_index+1>=courseDisplayOrderList.size())
						dest_item_index=item_index+1;
					else{
					for (int i=item_index+1;i<courseDisplayOrderList.size();i++) {
						if(courseDisplayOrderList.get(i).getItem_Type().equals("ContentObject")){
							dest_item_index=i;
							break;
						}
					}
					if(dest_item_index==-1)
						  dest_item_index = courseDisplayOrderList.size();
	
					}
					
				}
				
				if(dest_item_index==0)
					return true;
				
				courseDisplayOrderList.addAll(dest_item_index, courseDisplayOrderList_TobeMoved);
				for (int i=0;i<courseDisplayOrderList.size();i++) {
					System.out.println("index:"+i+":"+courseDisplayOrderList.get(i).getId() +":"+courseDisplayOrderList.get(i).getItem_Id()+":"+courseDisplayOrderList.get(i).getItem_Type()+":"+courseDisplayOrderList.get(i).getDisplayOrder());
					CourseDisplayOrder courseDisplayOrder = courseDisplayOrderList.get(i);
					courseDisplayOrder.setDisplayOrder(i+1);
					courseDAO.UpdateCourseDisplayOrder(courseDisplayOrder, courseId, lastModifiedUser);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		
		return true;
	}
	
	@Override
	public CourseDTO updateCourse (CourseDTO course){
		return courseDAO.updateCourse(course);
	}
	
	
	@Override
	public List<CourseGroup> getCourseGroupByContentOwner (long contentOwnerId, String courseGroupName){
		return courseDAO.getCourseGroupByContentOwner( contentOwnerId,  courseGroupName);
		
	}
	
	@Override
	public boolean updateCourseModifiedDate(long courseId){
		return courseDAO.updateCourseModifiedDate(courseId);
	}
	
	public Course populateCourseWithDefaultData(Course crs){
		
		String varBrandingId = LCMSProperties
				.getLCMSProperty("course.default.brandingId");
		String varCourseStatus = LCMSProperties
				.getLCMSProperty("course.default.coursestatus");
		String varCourseType = LCMSProperties
				.getLCMSProperty("course.default.coursetype");
		String varCode = LCMSProperties
				.getLCMSProperty("course.default.code");
		String varCurrency = LCMSProperties
				.getLCMSProperty("course.default.currency");
		String varBusinessUnitId = LCMSProperties
				.getLCMSProperty("course.default.businessunitId");
		String varBusinessUnitName = LCMSProperties
				.getLCMSProperty("course.default.businessunitName");
		String varCourseConfTempId = LCMSProperties
				.getLCMSProperty("course.default.courseConfigurationTemplateId");
		String varQuickBuildCourseType = LCMSProperties
				.getLCMSProperty("course.default.quickBuildCoursetype");

		Date date = new Date(0);
		crs.setCoursestatus(varCourseStatus);
		crs.setBranding_id(Integer.valueOf(varBrandingId));

		VU360UserDetail user = (VU360UserDetail) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		crs.setContentowner_id((int) user.getContentOwnerId());
		crs.setCreateUserId(user.getAuthorId());

		crs.setCourseType(varCourseType);
		crs.setCode(varCode);

		Calendar cal = Calendar.getInstance();

		String cOntentOwnerIDwithMasking = LCMSDateUtils.addLeadingZeroes(
				6, user.getContentOwnerId());
		String monthWithMasking = LCMSDateUtils.addLeadingZeroes(2,
				cal.get(Calendar.MONTH) + 1);
		String dayWithMasking = LCMSDateUtils.addLeadingZeroes(2,
				cal.get(Calendar.DATE));
		//double n = Math.random();
		long n3 = Math.round(Math.random() * 1000);
		String GUIDD = cOntentOwnerIDwithMasking + "-"
				+ LCMSDateUtils.getTwoDigitYearValue() + monthWithMasking
				+ dayWithMasking + "-" + n3;
		crs.setGuid(GUIDD);
		crs.setProductprice(BigDecimal.ONE);
		crs.setCurrency(varCurrency);
		crs.setBusinessunit_id(Integer.valueOf(varBusinessUnitId));
		crs.setCreatedDate(date);
		crs.setLastUpdatedDate(date);
		crs.setBusinessunit_name(varBusinessUnitName);
		crs.setCourseconfigurationtemplate_id(Integer
				.valueOf(varCourseConfTempId));
		crs.setQuick_build_coursetype(Integer
				.valueOf(varQuickBuildCourseType));
		
		return crs;
	}
	
	@Override
	public boolean createLessonFromObject(List<Lesson> lessonList, CourseDTO crs) throws SQLException{
		
		logger.debug("::addCourse - START");

		boolean courseCreated = true;
		
		for(Lesson lesson : lessonList){
			ContentObject contentObject = this.populateLessonWithDefaultData(lesson, crs.getId(), crs.getContentownerId(), crs.getCreateUserId());
			lesson.setContentObject(this.addLesson(contentObject));
			if (lesson.getSlideList()!=null && lesson.getSlideList().size() >0){
				for(Slide slide : lesson.getSlideList()){
					slide = this.populateSlideWithDefaultData(slide, lesson, crs);
					try {
						this.slideService.addSlide(slide);
					} catch (SQLException e) {
						logger.debug("Exception::while addling Slide - END" + e.getMessage());
						e.printStackTrace();
						courseCreated = false;
						throw new SQLException();
					}
				}
			}
		}
		
		return courseCreated;
	}
	
	private Slide populateSlideWithDefaultData(Slide slide, Lesson lesson, CourseDTO crs) {
		
		slide.setCourse_ID((int)crs.getId());
		slide.setContentObject_id(lesson.getContentObject().getID());
		slide.setDisplayStandardTF(false);
		slide.setDisplayWideScreenTF(true);

		slide.setOrientationKey("Left");
		slide.setAsset_ID(0);
		slide.setContentOwner_ID(crs.getContentownerId());
		slide.setCreateUserId(crs.getCreateUserId().intValue());
		slide.setSceneGUID(UUID.randomUUID().toString().replaceAll("-", ""));
		
		return slide;
	}

	public ContentObject populateLessonWithDefaultData(Lesson lesson, long courseId, int conetnownerId, long authorId){
		
		ContentObject contentObject = new ContentObject();
		
		contentObject.setCourseID((int)courseId);
		contentObject.setName(lesson.getTitle());
		contentObject.setDescription((lesson.getDescription() == null) ? "" : lesson.getDescription());
		contentObject.setCO_GUID(UUID.randomUUID().toString().replaceAll("-", ""));
		contentObject.setContentOwner_Id(conetnownerId);
		contentObject.setCreateUserId((int) authorId);
		contentObject.setLearningObjective("");
		
		return contentObject;
	}	
	
	@Override
	public int getStreamingTemplateId(){
		return courseDAO.getStreamingTemplateId();	
	}
	
	@Override
	public boolean updateCourseLastUpdateUserandModifiedDate(long lastUpdateUser,long courseId){
		return courseDAO.updateCourseLastUpdateUserandModifiedDate(lastUpdateUser, courseId);
	}
}

