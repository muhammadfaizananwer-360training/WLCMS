package com.softech.ls360.lcms.contentbuilder.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.softech.ls360.lcms.contentbuilder.model.CourseDTO;
import com.softech.ls360.lcms.contentbuilder.model.CourseSettings;
import com.softech.ls360.lcms.contentbuilder.service.ICourseService;
import com.softech.ls360.lcms.contentbuilder.service.ICourseSettingsService;
import com.softech.ls360.lcms.contentbuilder.utils.CourseType;
import com.softech.ls360.lcms.contentbuilder.utils.JsonResponse;
import com.softech.ls360.lcms.contentbuilder.utils.WlcmsConstants;

/**
 * 
 * @author haider.ali
 * 
 */
@Controller
public class CourseSettingController {

	private static Logger logger = LoggerFactory
			.getLogger(CourseSettingController.class);

	@Autowired
	ICourseSettingsService courseSettingsService;

	@Autowired
	ICourseService courseService;

	/**
	 * Course Settings initialization (open) page.
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "courseSettings", method = RequestMethod.GET)
	public ModelAndView courseSettings(HttpServletRequest request) {
		logger.info("CourseSettingController::createCourseD - Start");

		String course_id = request.getParameter("id");

		CourseSettings courseSettings = new CourseSettings();
		courseSettings.setCourse_Id(Long.parseLong(course_id));
		courseSettings = courseSettingsService
				.getCourseSettings(courseSettings);

		ModelAndView model = new ModelAndView("course_setting");
		model.addObject("courseSettingModel", courseSettings);
		model.addObject(WlcmsConstants.PARAMETER_COURSE_TYPE,  request.getParameter(WlcmsConstants.PARAMETER_COURSE_TYPE));

		logger.info("CourseSettingController::course_setting - End");

		return model;
	}

	@RequestMapping(value = "getCourseSettingByJSON", method = RequestMethod.GET)
	public @ResponseBody
	JsonResponse getCourseSettingByJSON(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JsonResponse res = new JsonResponse();

		logger.info("CourseSettingController::createCourseD - Start");

		String course_id = request.getParameter("id");

		CourseSettings courseSettings = new CourseSettings();
		courseSettings.setCourse_Id(Long.parseLong(course_id));
		courseSettings = courseSettingsService
				.getCourseSettings(courseSettings);

		if (courseSettings.getAttemptTheExam()
				|| courseSettings.getPassTheExam())
			res.setStatus("true");
		else
			res.setStatus("false");

		logger.info("CourseSettingController::course_setting - End");

		return res;
	}
	
	@RequestMapping(value = "getCourseSettingObj", method = RequestMethod.GET)
	public @ResponseBody
	CourseSettings getCourseSettingObj(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		logger.info("CourseSettingController::createCourseD - Start");

		String course_id = request.getParameter("id");

		CourseSettings courseSettings = new CourseSettings();
		courseSettings.setCourse_Id(Long.parseLong(course_id));
		courseSettings = courseSettingsService
				.getCourseSettings(courseSettings);

		
		logger.info("CourseSettingController::course_setting - End");

		return courseSettings;
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "courseSettingSave", method = RequestMethod.POST)
	public ModelAndView courseSettingSave(HttpServletRequest request) {

		logger.info("CourseSettingController::courseSettingSave - Start");

		String ccid = request.getParameter("courseConfiguration_Id");
		String cid = request.getParameter("course_Id");
		ModelAndView model = new ModelAndView("redirect:/courseSettings?id="
				+ cid);
		if (!StringUtils.isEmpty(cid) && !StringUtils.isEmpty(ccid)) {

			Long course_id = Long.parseLong(cid);
			Long courseConfiguration_id = Long.parseLong(ccid);
			Boolean passAllQuizzes = convertToBoolean(request
					.getParameter("passAllQuizzes"));
			Boolean attemptTheExam = convertToBoolean(request
					.getParameter("attemptTheExam"));
			Boolean passTheExam = convertToBoolean(request
					.getParameter("passTheExam"));
			Boolean agreeWithSpecifiedText = convertToBoolean(request
					.getParameter("agreeWithSpecifiedText"));
			String specifiedText = (String) request
					.getParameter("specifiedText");
			Boolean displaySlidesTOC = convertToBoolean(request
					.getParameter("displaySlidesTOC"));
			
			// filling object for updating...
			CourseSettings coursUpdate = new CourseSettings();
			coursUpdate.setCourse_Id(course_id);
			coursUpdate.setCourseConfiguration_Id(courseConfiguration_id);
			coursUpdate.setPassAllQuizzes(passAllQuizzes);
			coursUpdate.setAttemptTheExam(attemptTheExam);
			coursUpdate.setPassTheExam(passTheExam);
			coursUpdate.setAgreeWithSpecifiedText(agreeWithSpecifiedText);
			coursUpdate.setSpecifiedText(specifiedText);
			coursUpdate.setDisplaySlidesTOC(displaySlidesTOC);
			
			courseSettingsService.update(coursUpdate);

			// get Course Setting of specified course.
			CourseSettings updatedSetting = new CourseSettings();
			updatedSetting.setCourse_Id(course_id);
			updatedSetting = courseSettingsService
					.getCourseSettings(updatedSetting);

			model.addObject("courseSettingModel", updatedSetting);
			model.addObject(WlcmsConstants.PARAMETER_COURSE_TYPE,  request.getParameter(WlcmsConstants.PARAMETER_COURSE_TYPE));
			model.setViewName(model.getViewName() + "&msg=success");

			logger.info("CourseSettingController::courseSettingSave - End");
		}

		return model;
	}

	private static boolean convertToBoolean(String value) {
		boolean returnValue = false;
		if ("1".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value)
				|| "true".equalsIgnoreCase(value)
				|| "on".equalsIgnoreCase(value))
			returnValue = true;
		return returnValue;
	}

	// Course Setting Page for Classroom Course
	@RequestMapping(value = "coursesettingc", method = RequestMethod.GET)
	public ModelAndView coursesettingc(HttpServletRequest request) {
		String courseId = request.getParameter("id");

		ModelAndView model = new ModelAndView("coursesettingc");
		model.addObject("id", courseId);
		model.addObject("cType", CourseType.CLASSROOM_COURSE.getId());
        
		if (courseId != null) {
			CourseDTO course = courseService.getCourseById(Long
					.parseLong(courseId));
			model.addObject("businessKey", course.getBussinesskey());
			model.addObject("name", course.getName());
		}
		
		return model;
	}

	@RequestMapping(value = "coursesettingw", method = RequestMethod.GET)
	public ModelAndView coursesettingw(HttpServletRequest request) {
		String courseId = request.getParameter("id");

		ModelAndView model = new ModelAndView("coursesettingw");
		model.addObject("id", courseId);
		model.addObject("cType", CourseType.WEBINAR_COURSE.getId());

		if (courseId != null) {
			CourseDTO course = courseService.getCourseById(Long
					.parseLong(courseId));
			model.addObject("businessKey", course.getBussinesskey());
			model.addObject("name", course.getName());
		}
		return model;
	}

	@RequestMapping(value = "courseSettingCSave", method = RequestMethod.POST)
	public ModelAndView courseSettingCSave(HttpServletRequest request) {
		String cid = request.getParameter("id");
		ModelAndView model = new ModelAndView("redirect:/coursesettingc?id="
				+ cid + "&cType=" + CourseType.CLASSROOM_COURSE.getId()
				+ "&msg=success");
		return model;
	}

	@RequestMapping(value = "courseSettingWSave", method = RequestMethod.POST)
	public ModelAndView courseSettingWSave(HttpServletRequest request) {
		String cid = request.getParameter("id");
		ModelAndView model = new ModelAndView("redirect:/coursesettingw?id="
				+ cid + "&cType=" + CourseType.WEBINAR_COURSE.getId()
				+ "&msg=success");
		return model;
	}

}
