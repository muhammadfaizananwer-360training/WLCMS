package com.softech.ls360.lcms.contentbuilder.web.controller;

import com.softech.ls360.lcms.contentbuilder.dataimport.ClassroomParsingHndlr;
import com.softech.ls360.lcms.contentbuilder.exception.BulkUplaodCourseException;
import com.softech.ls360.lcms.contentbuilder.manager.CourseImportUtility;
import com.softech.ls360.lcms.contentbuilder.model.*;
import com.softech.ls360.lcms.contentbuilder.service.ICourseService;
import com.softech.ls360.lcms.contentbuilder.service.ISlideService;
import com.softech.ls360.lcms.contentbuilder.service.ISynchronousClassService;
import com.softech.ls360.lcms.contentbuilder.service.VU360UserService;
import com.softech.ls360.lcms.contentbuilder.utils.*;
import com.softech.ls360.lcms.contentbuilder.utils.WlcmsConstants.DeliveryMethod;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class CourseController {

	// @Autowired
	private static Logger logger = LoggerFactory
			.getLogger(CourseController.class);

	@Autowired
	ICourseService courseService;

	@Autowired
	VU360UserService vu360UserService;

	@Autowired
	ISlideService SlideService;

	@Autowired
	ISynchronousClassService synchronoutService;

	@Autowired
	CourseImportUtility importCourseManager;

	@Autowired
	ClassroomParsingHndlr classroomParser;

	SimpleDateFormat simplesyncsessionDateFormat = new SimpleDateFormat(
			"MM/dd/yyyy hh:mm aa", Locale.getDefault());


	@RequestMapping(value = "courseImport", method = RequestMethod.GET)
	public @ResponseBody
	ModelAndView courseImport(HttpServletRequest request,
							  HttpServletResponse response) throws Exception, BulkUplaodCourseException {

		String filePath = request.getParameter("filePath");
		String courseId = request.getParameter("courseId");

		int courseIdInt = Integer.parseInt(courseId);

		importCourseManager.processCourseImport(filePath, CourseType.ONLINE_COURSE.getId(), courseService, courseIdInt);

		return new ModelAndView("course_overview");

	}

	@RequestMapping(value = "processParsingFile", method = RequestMethod.POST)
	public @ResponseBody
	Object processExcelCourseFile(HttpServletRequest request,
								  HttpServletResponse response) throws Exception {

		String filePath = request.getParameter("filePath");
		String courseId = request.getParameter("courseId");
		String responseUploading = "true";
		boolean gotError = false;

		int courseIdInt = Integer.parseInt(courseId);
		try{
			importCourseManager.processCourseImport(filePath, CourseType.ONLINE_COURSE.getId(), courseService, courseIdInt);
		}catch(BulkUplaodCourseException e){
			responseUploading = e.getMessage();
			gotError = true;
		}

		if(!gotError){
			StringBuilder sb = new StringBuilder();
			sb.append("redirect:/coursestructure?id=" + courseId);
			sb.append("&msg=success");
			sb.append("&" + WlcmsConstants.PARAMETER_COURSE_TYPE + "=4");
			new ModelAndView(sb.toString());
		}

		return responseUploading;

	}

	// Function use to get Slides by Content Object and display slides list
	@RequestMapping(value = "getSlidesByContentObject", method = RequestMethod.POST)
	public @ResponseBody
	List<Slide> getSlidesByContentObject(HttpServletRequest request,
										 HttpServletResponse response) throws Exception {

		String varContendOjectId = request.getParameter("varCobjectId");
		if (varContendOjectId != null) {
			List<Slide> lstSlide = courseService.getSlidesByContentObject(Long
					.parseLong(varContendOjectId));
			return lstSlide;
		} else
			return null;

	}

	/**
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "isSlideComponentsHasData", method = RequestMethod.POST)
	public @ResponseBody
	String isSlideComponentsHasData(HttpServletRequest request,
									HttpServletResponse response) throws Exception {

		Slide slide = new Slide();
		String id = request.getParameter("varSlideId");

		slide.setId(Long.parseLong(id));
		Map<String, String> map = SlideService.isSlidComponentHasData(slide);
		JSONObject jsonObject = new JSONObject();

		if (!map.isEmpty()) {

			if (map.containsKey("slideTextIcon"))
				jsonObject.put("slideTextIcon", "true");
			else
				jsonObject.put("slideTextIcon", "false");

			if (map.containsKey("slideAudioIcon"))
				jsonObject.put("slideAudioIcon", "true");
			else
				jsonObject.put("slideAudioIcon", "false");

			if (map.containsKey("slideVideoIcon"))
				jsonObject.put("slideVideoIcon", "true");
			else
				jsonObject.put("slideVideoIcon", "false");

			if (map.containsKey("closeCaptionIcon"))
				jsonObject.put("closeCaptionIcon", "true");
			else
				jsonObject.put("closeCaptionIcon", "false");

			if (map.containsKey("VSC"))
				jsonObject.put("VSC", "true");
			else
				jsonObject.put("VSC", "false");

		}

		logger.info(jsonObject.toJSONString());
		return jsonObject.toJSONString();

	}

	// get ALLOWQUIZTF field from CONTENTOBJECT table
	// use this field to decide weather to display 'add quiz' link or not
	@RequestMapping(value = "getContentObjectAllowQuiz", method = RequestMethod.POST)
	public @ResponseBody
	ContentObject getContentObjectAllowQuiz(HttpServletRequest request,
											HttpServletResponse response) throws Exception {

		String varContendOjectId = request.getParameter("varCobjectId");
		if (varContendOjectId != null) {
			ContentObject dto = courseService.getContentObject2(Integer
					.parseInt(varContendOjectId));
			return dto;
		} else
			return null;
	}

	// Function use to get Content Object and dispaly in Edit Mode
	@RequestMapping(value = "getContentObjectForEdit", method = RequestMethod.POST)
	public @ResponseBody
	ContentObject getContentObjectForEdit(HttpServletRequest request,
										  HttpServletResponse response) throws Exception {

		String varContendOjectId = request.getParameter("varCobjectId");
		if (varContendOjectId != null) {
			ContentObject dto = courseService.getContentObject2(Integer
					.parseInt(varContendOjectId));
			return dto;
		} else
			return null;
	}

	// updating Content Object
	@RequestMapping(value = "updateContentObject", method = {
			RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody
	JsonResponse updateContentObject(HttpServletRequest request,
									 HttpServletResponse response) throws Exception {
		JsonResponse res = new JsonResponse();

		String varContendOjectId = request.getParameter("varCobjectId");
		String varName = request.getParameter("varName");
		String varDescription = request.getParameter("varDescription");
		String varlearningObjective[] = request
				.getParameterValues("varlearningObjective");
		String varlearningObjectiveIds[] = request
				.getParameterValues("varlearningObjectiveIds");
		String sCourseId = request.getParameter("id");

		VU360UserDetail user = (VU360UserDetail) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();

		String varlearningObjectiveId = "";
		for (String a : varlearningObjectiveIds) {
			a = a.replace("[", "");
			a = a.replace("]", "");
			a = a.replace("\"", "");
			varlearningObjectiveId = varlearningObjectiveId + "," + a;
		}
		//

		String learningObjective3 = "";
		for (String a : varlearningObjective) {
			a = a.replace("[", "");
			a = a.replace("]", "");
			a = a.replace("\"", "");
			learningObjective3 = learningObjective3 + "," + a;
		}
		VU360UserDetail principal = (VU360UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ContentObject dto = new ContentObject();
		dto.setID(Integer.parseInt(varContendOjectId));
		dto.setName(varName);
		dto.setDescription(varDescription);
		dto.setLearningObjective(learningObjective3);
		dto.setLearningObjectiveId(varlearningObjectiveId);
		dto.setCreateUserId((int) user.getAuthorId());
		dto.setCourseID(Integer.parseInt(sCourseId));
		dto.setLastUpdateUser(principal.getAuthorId());
		courseService.updateContentObject(dto);

		res.setStatus("SUCCESS");
		return res;
	}

	// Get Content Structure Page
	@RequestMapping(value = "coursestructure", method = RequestMethod.GET)
	public ModelAndView createCourseStructure(HttpServletRequest request,HttpServletResponse response) {

		logger.debug("CourseController::createCourseStructure - Start");

		ModelAndView courseModelView = null;
		if (request.getParameter("lessonOnly") != null && request.getParameter("lessonOnly").equals("true")) {
			courseModelView = new ModelAndView("lesson_accordian");
		}else{
			courseModelView = new ModelAndView("course_structure");
		}


		if (request.getParameter("msg") != null) {
			courseModelView.addObject("msg", "success");
		}

		if (request.getParameter("id") == null) {
			return null;
		}
		// get Value from Database
		Integer id = Integer.parseInt(request.getParameter("id"));
		courseModelView.addObject("courseid", id.toString());

		CourseDTO objCourse = null;
		try {
			objCourse = courseService.getCourse(id);
			courseModelView.addObject("courseobj", objCourse);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			List<ContentObject> lstContentObject = courseService
					.getContentObjectList(id);
			boolean isFinalExamenable = courseService.isFinalExamEnabled(id);

			if (isFinalExamenable)
				courseModelView.addObject("isFinalExamEnabled", "yes");
			else
				courseModelView.addObject("isFinalExamEnabled", "no");

			courseModelView.addObject("lstContentObject", lstContentObject);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.debug("CourseController::createCourseStructure - End");

		return courseModelView;
	}

	// send on courseOverview page
	@RequestMapping(value = "courseoverview", method = RequestMethod.GET)
	public ModelAndView getCourseOverview(ModelMap model) {
		logger.debug("CourseController::getCourseOverview - Start");

		logger.debug("CourseController::getCourseOverview - End");

		return new ModelAndView("course_overview", model);
	}

	// send to dashboard page
	@RequestMapping(value = { "", "/", "dashboard" }, method = RequestMethod.GET)
	public ModelAndView getDashboard(ModelMap model) {
		logger.debug("CourseController::getDashboard - Start");
		logger.debug("CourseController::getDashboard - End");

		return new ModelAndView("course_type", model);
	}

	// send to CourseType page
	@RequestMapping(value = "coursetype", method = RequestMethod.GET)
	public ModelAndView getCourseType(ModelMap model) {
		logger.debug("CourseController::getCourseType - Start");
		logger.debug("CourseController::getCourseType - End");
		return new ModelAndView("course_type", model);
	}

	@RequestMapping(value = "getSlideTemplateID", method = RequestMethod.GET)
	public @ResponseBody
	List<SlideTemplate> getSlideTemplateID(HttpServletRequest request) {

		VU360UserDetail user = (VU360UserDetail) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		List<SlideTemplate> slideTemplates = null;
		try {
			slideTemplates = courseService.getSlideTemplateID((int) user
					.getContentOwnerId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return slideTemplates;
	}

	// Add Lesson - AJAX
	@RequestMapping(value = "addLesson", method = RequestMethod.POST)
	public @ResponseBody
	ContentObject addLesson(HttpServletRequest request) {

		VU360UserDetail user = (VU360UserDetail) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();

		ContentObject co = new ContentObject();
		// Integer course_ID = Integer.parseInt(
		// request.getParameter("varCobjectId") );

		Integer couse_id =Integer.parseInt( request.getParameter("id")==null ? "0" : request.getParameter("id") );
		String name = request.getParameter("name")==null ? "" : request.getParameter("name");
		String description = request.getParameter("description")==null ? "" : request.getParameter("description");
		String[] learningObject = request.getParameterValues("learningObject")== null ? null : request.getParameterValues("learningObject");

		co.setCourseID(couse_id);
		co.setName(name);
		co.setDescription(description);
		co.setCO_GUID(UUID.randomUUID().toString().replaceAll("-", ""));
		co.setContentOwner_Id((int) user.getContentOwnerId());
		co.setCreateUserId((int) user.getAuthorId());

		String sLearningObject = "";
		if(learningObject!=null) {
			for (String a : learningObject) {
				a = a.replace("[", "");
				a = a.replace("]", "");
				a = a.replace("\"", "");
				sLearningObject = sLearningObject + "," + a;
			}
		}

		co.setLearningObjective(sLearningObject);

		co = courseService.addLesson(co);

		return co;
	}

	// Add Lesson - AJAX
	@RequestMapping(value = "deleteLLO", method = RequestMethod.POST)
	public @ResponseBody
	String deleteLLO(@RequestParam("lessonId") int LLO_id,
					 @RequestParam("contentObject_id") int contentObject_id) {
		boolean bResult = false;
		String sReturnval;
		try {
			bResult = courseService.deleteLLO(LLO_id, contentObject_id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (bResult)
			sReturnval = "Success";
		else
			sReturnval = "Error";

		return sReturnval;
	}

	@RequestMapping(value = "editCourseOverview", method = RequestMethod.GET)
	public ModelAndView editCourseD(HttpServletRequest request) {

		logger.debug("CourseController::editCourseD - Start");

		// This attribute is added to display left menu bar w.r.t course type
		if (request.getParameter("cType") != null)
			request.getSession().setAttribute("courseTypeForMenu",
					request.getParameter("cType"));

		ModelAndView courseModelView = new ModelAndView("course_overview_edit");

		if (request.getParameter("msg") != null) {
			courseModelView.addObject("msg", "success");
		}

		if (request.getParameter("isUpdate") != null) {
			courseModelView.addObject("isUpdate", "true");
		}

		if (request.getParameter("failureMessage") != null) {
			courseModelView.addObject("failureMessage",
					request.getParameter("failureMessage"));
		}
		if (request.getParameter("id") == null) {
			return null;
		}

		// get Value from Database
		String idToSearch = request.getParameter("id");
		if (idToSearch == null)
			return null;

		courseModelView.addObject("courseid", idToSearch.toString());
		courseModelView.addObject("cType", request.getParameter("cType"));
		// if there is nothing to search or


		// Prepare bean to search course for particular CourseID
		CourseDTO objCourse = null;
		try {
			objCourse = courseService.getCourse(Integer.parseInt(idToSearch));

			/* Yasin */if (objCourse.getCeus() != null
					&& objCourse.getCeus() == new BigDecimal(0.0))
				objCourse.setCeus(null);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (objCourse != null) {
			courseModelView.addObject("command", objCourse);
		}

		logger.debug("CourseController::editCourseD - End");
		return courseModelView;
	}

	@RequestMapping(value = "updateCourse", method = { RequestMethod.POST,
			RequestMethod.GET })
	public ModelAndView updateCourse(
			@RequestParam("id") int id,
			@RequestParam("name") String name,
			@RequestParam("description") String description,
			@RequestParam("language_id") int language_id,
			@RequestParam("businessunitName") String businessunitName,
			@RequestParam("keywords") String keywords,
			@RequestParam(value = WlcmsConstants.PARAMETER_COURSE_TYPE, required = false) String courseType) {

		logger.debug("CourseController::updateCourse - Start");

		Course crs = new Course();

		String sErrorMsg = null;

		try {

			VU360UserDetail user = (VU360UserDetail) SecurityContextHolder
					.getContext().getAuthentication().getPrincipal();

			crs.setContentowner_id((int) user.getContentOwnerId());
			crs.setCreateUserId(user.getAuthorId());

			// Setting default values
			crs.setId(id);
			crs.setName(name);
			// Yasin WLCMS-183
			crs.setBusinessunit_name(businessunitName);
			crs.setDescription(description);
			crs.setKeywords(keywords);
			crs.setLanguage_id(language_id);
			crs.setLastUpdateUser(user.getAuthorId());
			crs.setContentowner_id((int) user.getContentOwnerId());

			courseService.updateCourse(crs);

		} catch (Exception ex) {
			sErrorMsg = "&failureMessage=" + "Your course could not be saved"
					+  "&" + WlcmsConstants.PARAMETER_COURSE_TYPE + "=" + courseType;;
			logger.debug("CourseController::updateCourse - Exception "
					+ ex.getMessage());
		}
		logger.debug("CourseController::updateCourse - End");
		if (sErrorMsg != null) {
			sErrorMsg = "redirect:/editCourseOverview?id=" + id + "&" + WlcmsConstants.PARAMETER_COURSE_TYPE + "=" + courseType;;
		} else {
			sErrorMsg = "redirect:/editCourseOverview?id=" + id
					+ "&msg=success&isUpdate=true&"
					+ WlcmsConstants.PARAMETER_COURSE_TYPE + "=" + courseType;
		}
		return new ModelAndView(sErrorMsg);
	}

	@RequestMapping(value = "searchcourseonline", method = RequestMethod.GET)
	public ModelAndView searchCourseOnline(ModelMap model) throws SQLException {
		logger.debug("CourseController::searchCourseOnline - Start");

		ModelAndView courseModelView = new ModelAndView("course_search");

		// LdapUserDetailsImpl userd =
		// (LdapUserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		// VU360UserDetail user = (VU360UserDetail)
		// vu360UserService.loadUserByUsername(userd.getUsername());

		VU360UserDetail user = (VU360UserDetail) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		List<Course> lstCourses = courseService.getCoursesList((int) user
				.getContentOwnerId());
		/*
		 * Course objCourse = new Course(); objCourse.setName("test");
		 * lstCourses.add(objCourse);
		 */
		courseModelView.addObject("lstCourses", lstCourses);

		logger.debug("CourseController::searchCourseOnline - End");
		return courseModelView;
	}

	@RequestMapping(value = "createcourse", method = RequestMethod.GET)
	public ModelAndView uploadcourseonline(ModelMap model,
										   HttpServletRequest request) throws SQLException {

		String courseType = request
				.getParameter(WlcmsConstants.PARAMETER_COURSE_TYPE);
		ModelAndView courseModelView = null;

		int courseTypeInt = CourseType.ONLINE_COURSE.getId();
		if (courseType != null && courseType.length() >= 0) {
			courseTypeInt = Integer.parseInt(courseType);
		}

		CourseType courseTypeE = CourseType.getById(courseTypeInt);

		switch (courseTypeE) {

			case ONLINE_COURSE:
				courseModelView = new ModelAndView("course_overview");
				break;
			case CLASSROOM_COURSE:
				courseModelView = new ModelAndView("classroom_course_overview");
				break;
			case WEBINAR_COURSE:
				courseModelView = new ModelAndView("webinar_course_overview");
				break;
			default:
				courseModelView = new ModelAndView("course_overview");
				break;
		}

		request.getSession().setAttribute("courseTypeForMenu",
				courseTypeE.getId());

		courseModelView.addObject(WlcmsConstants.PARAMETER_COURSE_TYPE,
				courseType);

		logger.debug("CourseController::uploadcourseonline - End");
		return courseModelView;
	}

	@RequestMapping(value = "createCourseD", method = { RequestMethod.POST,
			RequestMethod.GET })
	public ModelAndView addCourse(
			@RequestParam("name") String name,
			@RequestParam("description") String description,
			// @RequestParam("file") MultipartFile file,
			@RequestParam("language_id") int language_id,
			@RequestParam("industry") String industry,
			@RequestParam("keywords") String keywords) {
		logger.debug("CourseController::addCourse - START");

		ModelAndView courseModelView = new ModelAndView("course_overview");
		HashMap<String, String> context = new HashMap<String, String>();
		Course crs = new Course();

		String retURL = null;
		int courseId = 0;
		try {
			String varCourseType = LCMSProperties.getLCMSProperty("course.default.coursetype");
			String varCode = LCMSProperties.getLCMSProperty("course.default.code");
			String varBusinessUnitId = LCMSProperties.getLCMSProperty("course.default.businessunitId");
			String varBusinessUnitName = industry;
			String varCourseConfTempId = LCMSProperties.getLCMSProperty("course.default.courseConfigurationTemplateId");
			String varQuickBuildCourseType = LCMSProperties.getLCMSProperty("course.default.quickBuildCoursetype");

			Date date = new Date(0);
			crs.setName(name);
			crs.setCeus(new BigDecimal(0));

			crs.setDescription(description);
			crs.setKeywords(keywords);
			crs.setLanguage_id(language_id);

			VU360UserDetail user = (VU360UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			crs.setContentowner_id((int) user.getContentOwnerId());
			crs.setCreateUserId(user.getAuthorId());

			crs.setCourseType(varCourseType);
			crs.setCode(varCode);

			Calendar cal = Calendar.getInstance();

			String cOntentOwnerIDwithMasking = LCMSDateUtils.addLeadingZeroes(6, user.getContentOwnerId());
			String monthWithMasking = LCMSDateUtils.addLeadingZeroes(2,cal.get(Calendar.MONTH) + 1);
			String dayWithMasking = LCMSDateUtils.addLeadingZeroes(2,cal.get(Calendar.DATE));
			long n3 = Math.round(Math.random() * 1000);
			String GUIDD = cOntentOwnerIDwithMasking + "-"+ LCMSDateUtils.getTwoDigitYearValue() + monthWithMasking + dayWithMasking + "-" + n3;

			crs.setGuid(GUIDD);
			crs.setProductprice(BigDecimal.ONE);
			crs.setBusinessunit_id(Integer.valueOf(varBusinessUnitId));
			crs.setCreatedDate(date);
			crs.setLastUpdatedDate(date);
			crs.setBusinessunit_name(varBusinessUnitName);
			crs.setCourseconfigurationtemplate_id(Integer.valueOf(varCourseConfTempId));
			crs.setQuick_build_coursetype(Integer.valueOf(varQuickBuildCourseType));

			courseId = courseService.addCourse(crs);
		} catch (Exception ex) {
			context.put("failureMessage","Your Course could not be saved. The error message is: "+ ex.getMessage());
			logger.debug("CourseController::addCourse - Exception "+ ex.getMessage());
		}

		if (courseId > 0) {
			StringBuilder sb = new StringBuilder();
			sb.append("redirect:/coursestructure?id=" + courseId);
			sb.append("&msg=success");
			sb.append("&" + WlcmsConstants.PARAMETER_COURSE_TYPE + "=4");
			retURL = sb.toString();
			return new ModelAndView(retURL);
		} else {
			courseModelView.addObject("name", name);
			courseModelView.addObject("description", description);
			courseModelView.addObject("language_id");
			courseModelView.addObject("industry", industry);
			courseModelView.addObject("keywords", keywords);
			courseModelView.addObject("context", context);
		}
		return courseModelView;
	}

	@RequestMapping(value = "searchcourse", method = RequestMethod.GET)
	public ModelAndView searchCourse(ModelMap model) {
		logger.debug("In searchCourse Controller");

		return new ModelAndView("searchcourse", model);
	}

	@RequestMapping(value = "/fetchCourses", method = RequestMethod.GET, headers = "Accept=*/*")
	@ResponseBody
	public List<CourseDTO> fetchCourses(HttpServletRequest request) {
		logger.info("In fetchCourses Controller:Begin");
		List<CourseDTO> courseList = null;
		try {
			VU360UserDetail principal = (VU360UserDetail) SecurityContextHolder
					.getContext().getAuthentication().getPrincipal();
			// VU360UserDetail principal1 = (VU360UserDetail)
			// vu360UserService.getVu360UserDetail();
			String textToSearch = request.getParameter("title");
			SearchCourseFilter filter = new SearchCourseFilter();
			filter.setLoginAuthorId(Long.toString(principal.getAuthorId()));
			filter.setContentOwnerId(Long.toString(principal
					.getContentOwnerId()));
			filter.setSearchCriteria(textToSearch);
			courseList = courseService.searchCourses(filter);
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		logger.info("Courses found: "
				+ (courseList == null ? 0 : courseList.size()));
		logger.info("In fetchCourses Controller:End");
		return courseList;
	}

	String uploadFileHandler(MultipartFile file) {

		String filePath = "";
		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();

				// Creating the directory to store file
				String rootPath = System.getProperty("catalina.home");

				filePath = rootPath + File.separator + "tmpFiles"
						+ File.separator + UUID.randomUUID().toString();
				logger.debug("filePath:" + filePath);
				File dir = new File(filePath);
				if (!dir.exists())
					if(!dir.mkdirs())
						logger.warn("Could not create folders");

				// Create the file on server
				File serverFile = new File(dir.getAbsolutePath()
						+ File.separator + file.getOriginalFilename());
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();

				logger.debug("Server File Location="
						+ serverFile.getAbsolutePath());

				return filePath;
			} catch (Exception e) {
				logger.debug(e.getMessage());
				return null;
			}
		} else {
			return filePath;
		}
	}

	@RequestMapping(value = "deleteLesson", method = RequestMethod.POST)
	public @ResponseBody
	JsonResponse deleteLesson(HttpServletRequest request,
							  HttpServletResponse response) throws Exception {
		JsonResponse res = new JsonResponse();

		VU360UserDetail user = (VU360UserDetail) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();

		String varLessonId = request.getParameter("varLessonId");
		String varCourseId = request.getParameter("varCourseId");
		courseService.deleteLesson(varLessonId, varCourseId,user.getAuthorId());

		res.setStatus("SUCCESS");
		return res;
	}

	// send to Schedule page
	/*
	 * @RequestMapping(value = "classroomsetup", method={RequestMethod.POST,
	 * RequestMethod.GET}) public ModelAndView
	 * showclassroomsetup(HttpServletRequest request) throws SQLException {
	 * logger.debug("CourseController::classroomsetup - Start");
	 *
	 * ModelAndView scheduleView = new ModelAndView
	 * ("/classroom/classroom_setup");
	 *
	 *
	 * logger.debug("CourseController::classroomsetup - End");
	 *
	 * return scheduleView; }
	 */

	@RequestMapping(value = "setCourseDisplayOrder", method = RequestMethod.POST)
	public @ResponseBody
	JsonResponse setCourseDisplayOrder(HttpServletRequest request,
									   HttpServletResponse response) throws Exception {
		JsonResponse res = new JsonResponse();

		String varLessonId = request.getParameter("varItemId");
		String sCourseId = request.getParameter("varCourseId");
		String item_type = request.getParameter("item_type");
		String direction = request.getParameter("direction");
		VU360UserDetail user = (VU360UserDetail) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		courseService.setCourseDisplayOrder(Integer.parseInt(sCourseId),
				Integer.parseInt(varLessonId), item_type,
				Integer.parseInt(direction), (int) user.getAuthorId());

		res.setStatus("SUCCESS");
		return res;
	}

	@RequestMapping(value = "createClassroomWebinarCourse", method = {
			RequestMethod.POST, RequestMethod.GET })
	public ModelAndView addClassroomWebinarCourse(
			@RequestParam("name") String name,
			@RequestParam("description") String description,
			// @RequestParam("file") MultipartFile file,
			@RequestParam("language_id") int language_id,
			@RequestParam("duration") String duration,
			@RequestParam("keywords") String keywords,
			@RequestParam(WlcmsConstants.PARAMETER_COURSE_TYPE) String courseType) {

		logger.debug("CourseController::addCourse - START");

		ModelAndView courseModelView = new ModelAndView(
				"webinar_course_overview");
		HashMap<String, String> context = new HashMap<String, String>();
		CourseDTO crs = new CourseDTO();

		String retURL = null;
		long courseId = 0;
		try {

			String varBrandingId = LCMSProperties
					.getLCMSProperty("course.default.brandingId");
			String varCourseStatus = LCMSProperties
					.getLCMSProperty("course.default.coursestatus");
			String varCourseType = CourseType.getById(
					TypeConvertor.AnyToInteger(courseType)).getName();
			String varCode = LCMSProperties
					.getLCMSProperty("course.default.code");
			String varCurrency = LCMSProperties
					.getLCMSProperty("course.default.currency");
			String varBusinessUnitId = LCMSProperties
					.getLCMSProperty("course.default.businessunitId");
			String varBusinessUnitName = LCMSProperties
					.getLCMSProperty("course.default.businessunitName");
			// String varCourseConfTempId =
			// LCMSProperties.getLCMSProperty("course.default.courseConfigurationTemplateId");
			// String varQuickBuildCourseType =
			// LCMSProperties.getLCMSProperty("course.default.quickBuildCoursetype");

			// Date date = new Date(0);
			Timestamp date = new Timestamp(new java.util.Date().getTime());
			crs.setCourseStatus(varCourseStatus);
			crs.setBrandingId(Integer.valueOf(varBrandingId));
			crs.setName(name);

			try {
				if (duration != null && !duration.equals(""))
					crs.setCeus(new BigDecimal(duration));
				else
					crs.setCeus(new BigDecimal(0));
			} catch (Exception ex) {

				courseModelView.addObject("name", name);
				courseModelView.addObject("description", description);
				courseModelView.addObject("language_id", language_id);
				courseModelView.addObject("duration", duration);
				courseModelView.addObject("keywords", keywords);
				courseModelView.addObject("failureMessage", LCMSProperties
						.getLCMSProperty("course.duration.error.notBeString"));

				return courseModelView;

			}


			crs.setDescription(description);
			crs.setKeywords(keywords);
			crs.setLanguage_id(language_id);

			// LdapUserDetailsImpl userd =
			// (LdapUserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			VU360UserDetail user = (VU360UserDetail) SecurityContextHolder
					.getContext().getAuthentication().getPrincipal();
			crs.setContentownerId((int) user.getContentOwnerId());
			crs.setCreateUserId(user.getAuthorId());

			crs.setCourseType(varCourseType);
			crs.setCode(varCode);
			String source = user.getContentOwnerId() ==1?"360training":"eLM";
			crs.setSource(source);
			Calendar cal = Calendar.getInstance();

			String cOntentOwnerIDwithMasking = LCMSDateUtils.addLeadingZeroes(
					6, user.getContentOwnerId());
			String monthWithMasking = LCMSDateUtils.addLeadingZeroes(2,
					cal.get(Calendar.MONTH) + 1);
			String dayWithMasking = LCMSDateUtils.addLeadingZeroes(2,
					cal.get(Calendar.DATE));
			long n3 = Math.round(Math.random() * 1000);
			String businessKey = cOntentOwnerIDwithMasking + "-"
					+ LCMSDateUtils.getTwoDigitYearValue() + monthWithMasking
					+ dayWithMasking + "-" + n3;
			crs.setBussinesskey(businessKey);
			crs.setProductPrice(BigDecimal.ONE);
			crs.setCurrency(varCurrency);
			crs.setBusinessunitId(Integer.valueOf(varBusinessUnitId));
			crs.setCreatedDate(date);
			crs.setLastUpdatedDate(date);
			crs.setBusinessunitName(varBusinessUnitName);
			crs.setLastModifiedDate(simplesyncsessionDateFormat
					.format(new Date()));
			String GUID = UUID.randomUUID().toString().replaceAll("-", "");
			crs.setGuid(GUID);

			int courseTypeInt = CourseType.ONLINE_COURSE.getId();
			if (/*courseType != null &&*/ /* commentout for sonarqube*/ courseType.length() >= 0) {
				courseTypeInt = Integer.parseInt(courseType);
			}

			CourseType courseTypeE = CourseType.getById(courseTypeInt);
			List<CourseGroup> lstcg = null;

			switch (courseTypeE) {

				case CLASSROOM_COURSE:
					crs.setDeliveryMethodId(DeliveryMethod.Classroom.getDeliveryMethod());
					courseModelView.setViewName("classroom_course_overview");
					crs.setCourseType(CourseType.CLASSROOM_COURSE.getName());
					lstcg = courseService.getCourseGroupByContentOwner(
							user.getContentOwnerId(),
							WlcmsConstants.COURSE_GROUP_CLASSROOM);
					break;
				case WEBINAR_COURSE:
					crs.setDeliveryMethodId(DeliveryMethod.Webinar.getDeliveryMethod());
					courseModelView.setViewName("webinar_course_overview");
					crs.setCourseType(CourseType.WEBINAR_COURSE.getName());
					lstcg = courseService.getCourseGroupByContentOwner(
							user.getContentOwnerId(),
							WlcmsConstants.COURSE_GROUP_WEBINAR);
					break;
				default:
					break;
			}

			Set<CourseGroup> lstNew = new HashSet<CourseGroup>();

			if (lstcg != null && lstcg.size() > 0)
				lstNew.add(lstcg.get(0));
			else {
				CourseGroup cg = new CourseGroup();

				if (CourseType.CLASSROOM_COURSE.getId() == courseTypeE.getId()) {
					cg.setName(WlcmsConstants.COURSE_GROUP_CLASSROOM);
					cg.setDescription(WlcmsConstants.COURSE_GROUP_CLASSROOM);
					cg.setKeyword(WlcmsConstants.COURSE_GROUP_CLASSROOM);
				} else {
					cg.setName(WlcmsConstants.COURSE_GROUP_WEBINAR);
					cg.setDescription(WlcmsConstants.COURSE_GROUP_WEBINAR);
					cg.setKeyword(WlcmsConstants.COURSE_GROUP_WEBINAR);
				}

				cg.setContentOwnerId((int) user.getContentOwnerId());
				cg.setCourseGroupGuid(GUID);
				cg.setCreatedDate(new Date());
				cg.setCreatedUserId(user.getAuthorId());

				lstNew.add(cg);

			}

			crs.setCourseGroup(lstNew);
			CourseDTO course = courseService.saveCourse(crs);
			courseId = course.getId();

		} catch (Exception ex) {
			context.put("failureMessage",
					"Your Course could not be saved. The error message is: "
							+ ex.getMessage());
			logger.debug("CourseController::addCourse - Exception "
					+ ex.getMessage());
		}

		logger.debug("CourseController::addCourse - END");

		if (courseId > 0) {
			retURL = "redirect:/editClassroomWebinarCourse?id=" + courseId
					+ "&msg=success&" + WlcmsConstants.PARAMETER_COURSE_TYPE
					+ "=" + courseType;
			return new ModelAndView(retURL);
		} else {
			courseModelView.addObject("name", name);
			courseModelView.addObject("description", description);
			courseModelView.addObject("language_id");
			courseModelView.addObject("duration", duration);
			courseModelView.addObject("keywords", keywords);
			courseModelView.addObject("context", context);
			courseModelView.addObject(WlcmsConstants.PARAMETER_COURSE_TYPE,
					courseType);

		}
		return courseModelView;
	}

	@RequestMapping(value = "editClassroomWebinarCourse", method = RequestMethod.GET)
	public ModelAndView editClassroomWebinarCourseD(HttpServletRequest request)
			throws NumberFormatException, SQLException {

		logger.debug("CourseController::editCourseD - Start");

		if (request.getParameter(WlcmsConstants.PARAMETER_COURSE_TYPE) != null)
			request.getSession().setAttribute("courseTypeForMenu",
					request.getParameter(WlcmsConstants.PARAMETER_COURSE_TYPE));

		String courseType = request
				.getParameter(WlcmsConstants.PARAMETER_COURSE_TYPE);
		int courseTypeInt = CourseType.WEBINAR_COURSE.getId();
		if (courseType != null && courseType.length() >= 0) {
			courseTypeInt = Integer.parseInt(courseType);
		}

		CourseType courseTypeE = CourseType.getById(courseTypeInt);
		ModelAndView courseModelView = null;

		switch (courseTypeE) {

			case CLASSROOM_COURSE:
				courseModelView = new ModelAndView("classroom_course_overview_edit");
				break;
			case WEBINAR_COURSE:
				courseModelView = new ModelAndView("webinar_course_overview_edit");
				break;
			default:
				courseModelView = new ModelAndView("classroom_course_overview_edit");
				break;
		}

		courseModelView.addObject(WlcmsConstants.PARAMETER_COURSE_TYPE,
				courseTypeE.getId());
		if (request.getParameter("msg") != null) {
			courseModelView.addObject("msg", "success");
		}

		if (request.getParameter("isUpdate") != null) {
			courseModelView.addObject("isUpdate", "true");
		}

		if (request.getParameter("failureMessage") != null) {
			courseModelView.addObject("failureMessage",
					request.getParameter("failureMessage"));
		}
		if (request.getParameter("id") == null) {
			return null;
		}

		String idToSearch = request.getParameter("id");
		if (idToSearch == null || idToSearch.length() <= 0) {
			return null;
		}
		courseModelView.addObject("courseid", idToSearch.toString());


		CourseDTO objCourse = null;

		try {
			objCourse = courseService.getCourseById(Integer
					.parseInt(idToSearch));

		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		if (objCourse != null) {
			courseModelView.addObject("command", objCourse);
			if (objCourse.getCeus() != null
					&& objCourse.getCeus() == new BigDecimal(0.0)) {
				objCourse.setCeus(null);
			}

			if (objCourse.getCourseStatus().equalsIgnoreCase(WlcmsConstants.PUBLISH_STATUS_NOT_STARTED)) {
				objCourse.setCoursePublishStatus(WlcmsConstants.PUBLISH_STATUS_NOT_PUBLISHED);
			}
			else if (objCourse.getCourseStatus().equalsIgnoreCase(WlcmsConstants.PUBLISH_STATUS_PUBLISHED) && objCourse.getLastPublishedDate()!= null && (objCourse.getLastUpdatedDate().getTime()-objCourse.getLastPublishedDate().getTime() )>2000) {//in webinarcase somehow the publishing date and update date is different at publishing time so a 2 seconds difference
				objCourse.setCoursePublishStatus(WlcmsConstants.PUBLISH_STATUS_CHANGES_NOT_PUBLISHED);
			}
			else {
				objCourse.setCoursePublishStatus(WlcmsConstants.PUBLISH_STATUS_PUBLISHED);
			}
			if (objCourse.isRetiredTF()) {
				objCourse.setCourseStatus(WlcmsConstants.COURSE_STATUS_RETIRED);
			} else {
				objCourse.setCourseStatus(WlcmsConstants.COURSE_STATUS_ACTIVE);
			}
			if (objCourse.getCourseRating()==null){
				objCourse.setCourseRating(WlcmsConstants.COURSE_RATING_PENDING);
			}
		}

		logger.debug("CourseController::editCourseD - End");
		return courseModelView;
	}

	@RequestMapping(value = "updateCRWCourse", method = { RequestMethod.POST,
			RequestMethod.GET })
	public ModelAndView updateClassroomWebinarCourse(
			@RequestParam("id") int id,
			@RequestParam("name") String name,
			@RequestParam("description") String description,
			@RequestParam("language_id") int language_id,
			@RequestParam("duration") String duration,
			@RequestParam("keywords") String keywords,
			@RequestParam(WlcmsConstants.PARAMETER_COURSE_TYPE) String courseType) {

		logger.debug("CourseController::updateCourse - Start");

		//CourseDTO crs = new CourseDTO();
		String sErrorMsg = null;

		try {

			VU360UserDetail user = (VU360UserDetail) SecurityContextHolder
					.getContext().getAuthentication().getPrincipal();

			CourseDTO courseDB = courseService.getCourseById(id);
			if (courseDB == null) {
				throw new IllegalStateException("Unknown Course id: " + id);
			}

			Timestamp date = new java.sql.Timestamp(
					new java.util.Date().getTime());
			courseDB.setContentownerId((int) user.getContentOwnerId());
			courseDB.setCreateUserId(user.getAuthorId());

			// Setting default values
			courseDB.setId(id);
			courseDB.setName(name);
			// Yasin WLCMS-183
			duration = StringUtils.isEmpty(duration) ? "0" : duration;
			courseDB.setCeus(new BigDecimal(duration));
			courseDB.setDescription(description);
			courseDB.setKeywords(keywords);
			courseDB.setLanguage_id(language_id);
			courseDB.setLastUpdateUser(user.getAuthorId());
			courseDB.setContentownerId((int) user.getContentOwnerId());
			courseDB.setLastUpdatedDate(date);
			courseService.updateCourse(courseDB);

		} catch (IllegalStateException ex) {
			sErrorMsg = "&failureMessage=" + "Your course could not be saved"
					+ ex.getMessage();
			logger.debug("CourseController::updateCourse - Exception "
					+ ex.getMessage());
		} catch (Exception ex) {
			sErrorMsg = "&failureMessage=" + "Your course could not be saved"
					+ ex.getMessage();
			logger.debug("CourseController::updateCourse - Exception "
					+ ex.getMessage());
		}
		logger.debug("CourseController::updateCourse - End");

		if (sErrorMsg != null) {
			sErrorMsg = "redirect:/editClassroomWebinarCourse?id=" + id
					+ sErrorMsg + "&" + WlcmsConstants.PARAMETER_COURSE_TYPE
					+ "=" + courseType;
		} else {
			sErrorMsg = "redirect:/editClassroomWebinarCourse?id=" + id
					+ "&msg=success&isUpdate=true&"
					+ WlcmsConstants.PARAMETER_COURSE_TYPE + "=" + courseType;
		}
		return new ModelAndView(sErrorMsg);
	}

	@RequestMapping(value = "displayCourseOverview", method = RequestMethod.GET)
	public ModelAndView showCourseOverview(ModelMap model,
										   HttpServletRequest request) throws SQLException {
		logger.debug("CourseController::showCourseOverview - Start");

		String courseType = request
				.getParameter(WlcmsConstants.PARAMETER_COURSE_TYPE);

		ModelAndView courseModelView = new ModelAndView(
				"webinar_course_overview");

		courseModelView.addObject(WlcmsConstants.PARAMETER_COURSE_TYPE,
				courseType);

		logger.debug("CourseController::showCourseOverview - End");
		return courseModelView;
	}

	@RequestMapping(value = "/getWebinarSchedule", method = RequestMethod.GET, headers = "Accept=*/*")
	@ResponseBody
	public List<CourseDTO> fetchWebinarCourseSchedule(HttpServletRequest request) {
		logger.info("In fetchCourses Controller:Begin");
		List<CourseDTO> courseList = null;
		try {
			VU360UserDetail principal = (VU360UserDetail) SecurityContextHolder
					.getContext().getAuthentication().getPrincipal();
			// VU360UserDetail principal1 = (VU360UserDetail)
			// vu360UserService.getVu360UserDetail();
			String textToSearch = request.getParameter("title");
			SearchCourseFilter filter = new SearchCourseFilter();
			filter.setLoginAuthorId(Long.toString(principal.getAuthorId()));
			filter.setContentOwnerId(Long.toString(principal
					.getContentOwnerId()));
			filter.setSearchCriteria(textToSearch);
			courseList = courseService.searchCourses(filter);
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		logger.info("Courses found: "
				+ (courseList == null ? 0 : courseList.size()));
		logger.info("In fetchCourses Controller:End");
		return courseList;
	}

	@RequestMapping(value = "/getWebinarInstructor", method = RequestMethod.GET, headers = "Accept=*/*")
	@ResponseBody
	public List<CourseDTO> fetchWebinarCourseInstructor(
			HttpServletRequest request) {
		logger.info("In fetchWebinarCourseInstructors Controller:Begin");
		List<CourseDTO> courseList = null;
		try {
			VU360UserDetail principal = (VU360UserDetail) SecurityContextHolder
					.getContext().getAuthentication().getPrincipal();
			// VU360UserDetail principal1 = (VU360UserDetail)
			// vu360UserService.getVu360UserDetail();
			String textToSearch = request.getParameter("title");
			SearchCourseFilter filter = new SearchCourseFilter();
			filter.setLoginAuthorId(Long.toString(principal.getAuthorId()));
			filter.setContentOwnerId(Long.toString(principal
					.getContentOwnerId()));
			filter.setSearchCriteria(textToSearch);
			courseList = courseService.searchCourses(filter);
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		logger.info("Courses found: "
				+ (courseList == null ? 0 : courseList.size()));
		logger.info("In fetchCourses Controller:End");
		return courseList;
	}

	@RequestMapping(value = "/getWebinarSetup", method = RequestMethod.GET, headers = "Accept=*/*")
	@ResponseBody
	public List<CourseDTO> fetchWebinarCourseSetup(HttpServletRequest request) {
		logger.info("In fetchCourses Controller:Begin");
		List<CourseDTO> courseList = null;
		try {
			VU360UserDetail principal = (VU360UserDetail) SecurityContextHolder
					.getContext().getAuthentication().getPrincipal();
			// VU360UserDetail principal1 = (VU360UserDetail)
			// vu360UserService.getVu360UserDetail();
			String textToSearch = request.getParameter("title");
			SearchCourseFilter filter = new SearchCourseFilter();
			filter.setLoginAuthorId(Long.toString(principal.getAuthorId()));
			filter.setContentOwnerId(Long.toString(principal
					.getContentOwnerId()));
			filter.setSearchCriteria(textToSearch);
			courseList = courseService.searchCourses(filter);
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		logger.info("Courses found: "
				+ (courseList == null ? 0 : courseList.size()));
		logger.info("In fetchCourses Controller:End");
		return courseList;
	}
}
