package com.softech.ls360.lcms.contentbuilder.web.controller;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.softech.ls360.lcms.contentbuilder.model.*;
import com.softech.ls360.lcms.contentbuilder.service.*;
import com.softech.ls360.lcms.contentbuilder.utils.TypeConvertor;
import com.softech.ls360.lcms.contentbuilder.web.vo.ClassInstructorVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.softech.ls360.lcms.contentbuilder.utils.WlcmsConstants;
import com.softech.ls360.lcms.contentbuilder.web.vo.SynchronousClassVO;

@Controller
public class SynchronousClassController {

	@Autowired
	ISynchronousClassService synchronousService;

	@Autowired
	ICourseService courseService;

	@Autowired
	ISynchronousClassService synchronoutService;

	@Autowired
	IOfferService offerService;

	@Autowired
	IPublishingService publishingService;

	@Autowired
	VU360UserService vu360UserService;

	@Autowired
	ICourseProviderService courseProviderService;

    @Autowired
    IClassInstructorService classInstructorService;
	
	final String COURSE_STATUS = "Active";
	final String CLASS_TYPE= "Unlimited";

	DateFormat formatonlyDate = new SimpleDateFormat("MM/dd/yyyy");
	SimpleDateFormat formatOnlyTime = new SimpleDateFormat("h:mm a");
	SimpleDateFormat simplesyncsessionDateFormat=new SimpleDateFormat("MM/dd/yyyy hh:mm aa",Locale.getDefault());
	SimpleDateFormat formatDateWithTime = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");

	private static Logger logger = LoggerFactory.getLogger(SynchronousClassController.class);

	// Send to Class room page
	@RequestMapping(value = "classroomsetup", method={RequestMethod.POST, RequestMethod.GET})
	public ModelAndView classroomSetup(HttpServletRequest request) throws SQLException {
		logger.debug("classroomsetup - Start");

		String varSyncClassId = request.getParameter("varSyncClassId");
		SynchronousClassVO  syncClassVO =null;
		String courseId = request.getParameter("id");
		ModelAndView scheduleView = new ModelAndView ("/classroom/classroom_setup");
		SynchronousClass sccc=null;
		Location objLoc = null;
		CourseDTO courseDB = courseService.getCourseById(Long.parseLong(courseId));

			if(varSyncClassId==null){

				List <SynchronousClass> lstSyncClass = synchronousService.getSynchronousClassByCourseId(Long.parseLong(courseId));

				if(lstSyncClass.size()>0){
					syncClassVO = new SynchronousClassVO();
					syncClassVO.setSyncClassId(lstSyncClass.get(0).getId());
					sccc= synchronousService.getSynchronousClassWithLocationInfo(lstSyncClass.get(0).getId());
				}
				else
				{
					scheduleView.addObject("syncClassnull", 0);
					 return scheduleView;
				}
			}else{
				sccc= synchronousService.getSynchronousClassWithLocationInfo(Long.parseLong(varSyncClassId));
			}
			scheduleView.addObject(WlcmsConstants.PARAMETER_COURSE_TYPE,  request.getParameter(WlcmsConstants.PARAMETER_COURSE_TYPE));

			try{
				//syncClassVO.setClassSize("0");
				scheduleView.addObject("courseObj", courseDB);
				scheduleView.addObject("syncClassnull", 1);
				objLoc = sccc.getLocation();
				syncClassVO.setClassroomSetupPageData("1");
				scheduleView.addObject("syncClassVO", syncClassVO);
			}catch(NullPointerException npe){
				scheduleView.addObject("syncClassVO", syncClassVO);
				return scheduleView;
			}


			syncClassVO.setClassSize(sccc.getMaximumClassSize()+"");
			syncClassVO.setLocation(objLoc.getLocationname());
			syncClassVO.setCity(objLoc.getCity());
			syncClassVO.setZipPostal(objLoc.getZip());
			syncClassVO.setCountry(objLoc.getCountry());
			syncClassVO.setState(objLoc.getState());
			syncClassVO.setPhone(objLoc.getPhone());
			syncClassVO.setDescription(objLoc.getDescription());
			syncClassVO.setAddress(objLoc.getAddress().getStreetAddress());



		 if (request.getParameter("msg") != null) {
			 scheduleView.addObject("msg", "success");
		 }

		logger.debug("classroomsetup - End");

		scheduleView.addObject("syncClassnull", 1);
		scheduleView.addObject("courseObj", courseDB);
		scheduleView.addObject("syncClassVO", syncClassVO);

		 return scheduleView;
	}


	@RequestMapping(value = "saveClassroomSetup", method={RequestMethod.POST, RequestMethod.GET})
	public ModelAndView saveClassroomSetup(HttpServletRequest request) {
		


		String maximumClassSize = request.getParameter("maximumClassSize");
		String location_name = request.getParameter("location_name");
		String location_address = request.getParameter("location_address");
		String location_city = request.getParameter("location_city");
		String location_postcode = request.getParameter("location_postcode");
		String country = request.getParameter("country");
		String state = request.getParameter("state");
		String location_phone = request.getParameter("location_phone");
		String location_description = request.getParameter("location_description");

		String rdoClassroomlimit = request.getParameter("rdoClassroomlimit");

		String courseId = request.getParameter("varCourseId");
		String courseType = request.getParameter("cType");
		String varSyncClassId = request.getParameter("varSyncClassId");
		SynchronousClass scForSessAdd=null;
		Address objAddress = new Address();
		Location objLocation = new Location();


		VU360UserDetail principal = (VU360UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    scForSessAdd = synchronousService.getSynchronousClassWithLocationInfo(Long.parseLong( varSyncClassId ));

	    if(scForSessAdd==null)
	    {
		    scForSessAdd = synchronousService.getSynchronousClassById(Long.parseLong( varSyncClassId ));

		    if(rdoClassroomlimit != null &&  rdoClassroomlimit.equals(CLASS_TYPE))
		    	scForSessAdd.setMaximumClassSize(Long.MAX_VALUE);
		    else
		    	scForSessAdd.setMaximumClassSize(Long.valueOf(maximumClassSize));


		    objLocation.setLocationname(location_name);
		    objLocation.setCity(location_city);
		    objLocation.setZip(location_postcode);
		    objLocation.setCountry(country);
		    objLocation.setState(state);
		    objLocation.setPhone(location_phone);
		    objLocation.setDescription(location_description);
		    objLocation.setEnabledtf("1");
		    objLocation.setContentownerId((int)principal.getContentOwnerId());

		    objAddress.setCity(location_city);
		    objAddress.setStreetAddress(location_address);
		    objAddress.setZipcode(location_postcode);
		    objAddress.setState(state);
		    objAddress.setCountry(country);

		    objLocation.setAddress(objAddress);
		    scForSessAdd.setLocation(objLocation);
	    }else{
		    if(rdoClassroomlimit != null &&  rdoClassroomlimit.equals("Unlimited"))
		    	scForSessAdd.setMaximumClassSize(Long.MAX_VALUE);
		    else
		    	scForSessAdd.setMaximumClassSize(Long.valueOf(maximumClassSize));
    		objLocation = scForSessAdd.getLocation();
    		objLocation.setLocationname(location_name);
		    objLocation.setCity(location_city);
		    objLocation.setZip(location_postcode);
		    objLocation.setCountry(country);
		    objLocation.setState(state);
		    objLocation.setPhone(location_phone);
		    objLocation.setDescription(location_description);

		    objAddress = scForSessAdd.getLocation().getAddress();
		    objAddress.setCity(location_city);
		    objAddress.setStreetAddress(location_address);
		    objAddress.setZipcode(location_postcode);
		    objAddress.setState(state);
		    objAddress.setCountry(country);

		    objLocation.setAddress(objAddress);
		    scForSessAdd.setLocation(objLocation);
	    }

	    synchronousService.saveSynchronousClass(scForSessAdd);



	    return new ModelAndView("redirect:/classroomsetup?id="+courseId + "&msg=success&" + WlcmsConstants.PARAMETER_COURSE_TYPE + "=" + courseType);
	}
	// send to Schedule page
	@RequestMapping(value = "schedule", method={RequestMethod.POST, RequestMethod.GET})
	public ModelAndView showSchedule(HttpServletRequest request) throws SQLException {
		logger.debug("CourseController::showSchedule - Start");
		SynchronousClassVO  syncClassVO =null;
		ModelAndView scheduleView = new ModelAndView ("schedule");
		boolean isCoursepublished = false;

		try
		{
			//if(request.getParameter("varSyncClassId")!=null){
				String courseId = request.getParameter("id");
				List <SynchronousClass> lstSyncClass = synchronoutService.getSynchronousClassByCourseId(Long.parseLong(courseId));
				CourseDTO courseDB = courseService.getCourseById(Long.parseLong(courseId));

				syncClassVO = this.setScheduleFormFields(lstSyncClass);
			//}

			TimeZone objtimezone = new TimeZone();
			List<TimeZone> lstTimeZone = null;

			lstTimeZone = synchronoutService.getAllTimezone(objtimezone);

			if(courseDB != null)
			{
				if(courseDB.getCourseStatus() == "Published")
				{
					scheduleView.addObject("isPublished", 1);
				}
			}
			 //if(lstTimeZone != null)
			 //logger.debug("TimeZone: "+ lstTimeZone.size());
			 scheduleView.addObject("lstTimeZone", lstTimeZone);

			 String idToSearch = request.getParameter("id");
			 scheduleView.addObject("courseid", idToSearch.toString());
			 scheduleView.addObject("status", (courseDB==null?null:courseDB.getCourseStatus()));
			 scheduleView.addObject("name", (courseDB==null?null:courseDB.getName()));
			 scheduleView.addObject("type", (courseDB==null?null:courseDB.getCourseType()));
			 scheduleView.addObject("courseObj", courseDB);
			 scheduleView.addObject(WlcmsConstants.PARAMETER_COURSE_TYPE,  request.getParameter(WlcmsConstants.PARAMETER_COURSE_TYPE));

			 if (request.getParameter("msg") != null) {
				 scheduleView.addObject("msg", "success");
				}


		}
		catch (Exception e)
		{
			logger.debug("TimeZone Error: "+ e.getMessage());
		}
		logger.debug("CourseController::showSchedule - End");


		 scheduleView.addObject("syncClassVO", syncClassVO);
		 return scheduleView;
	}



	public SynchronousClassVO setScheduleFormFields(List <SynchronousClass> lstSyncClass){

		SynchronousClassVO vo = new SynchronousClassVO();

		Iterator<SynchronousClass> iterSyncClass = lstSyncClass.iterator();
		while (iterSyncClass.hasNext()) {
			SynchronousClass sc1 = iterSyncClass.next();
			Set <SynchronousSession> sccc1 =  sc1.getSyncSession();

			Iterator<SynchronousSession> iter2 = sccc1.iterator();


			if(sc1.getEnrollmentCloseDate()!=null)
				vo.setSyncClassId(sc1.getId());
				vo.setEnrollmentCloseDate( formatonlyDate.format(sc1.getEnrollmentCloseDate()));


			if(sc1.getClassStartDate()!=null)
				vo.setCourseStartDate(formatonlyDate.format(sc1.getClassStartDate()));

			if(sc1.getTimeZoneId() !=null)
				vo.setTimeZone(Integer.toString(sc1.getTimeZoneId()));


			    while (iter2.hasNext()){
			    	SynchronousSession sc1aaa = iter2.next();

			    	if(sc1aaa.getStartDateTime()!=null)
			    		vo.setCourseStartTime(formatOnlyTime.format(sc1aaa.getStartDateTime()));

			    	if(sc1aaa.getEndDateTime()!=null)
			    		vo.setCourseEndTime(formatOnlyTime.format(sc1aaa.getEndDateTime()));

			    }

		}

		return vo;
	}


	@RequestMapping(value = "saveSchedule", method={RequestMethod.POST, RequestMethod.GET})
	public ModelAndView saveSchedule(HttpServletRequest request) {


		String timezone = request.getParameter("timezome");
		String enrollmentCloseDate = request.getParameter("enroll_date");
		String courseStartDate = request.getParameter("start_date");
		String start_time = request.getParameter("start_time");
		String end_time = request.getParameter("end_time");
		String courseId = request.getParameter("varCourseId");
		String varSyncClassId = request.getParameter("varSyncClassId");
		String courseType = request.getParameter("cType");

		VU360UserDetail principal = (VU360UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		try {


			String completeStartDate = courseStartDate + " " + start_time;
			String completeEndDate = courseStartDate + " " + end_time;

			SynchronousClass scForSessAdd=null;


			if(varSyncClassId != null && Long.parseLong(varSyncClassId)>0 ){
				List <SynchronousClass> lstSyncClass = synchronoutService.getSynchronousClassByCourseId(Long.parseLong(courseId));


				Iterator<SynchronousClass> iter = lstSyncClass.iterator();
	    		while (iter.hasNext()) {
	    			scForSessAdd = iter.next();
	    			CourseDTO secondCourse = courseService.getCourseById(Long.parseLong(courseId));
	    			secondCourse.setLastUpdatedDate(new Date());
	    			secondCourse.setLastUpdateUser(principal.getAuthorId());
					scForSessAdd.setCourse(secondCourse);

					scForSessAdd.setClassStatus("Active");
		    		scForSessAdd.setClassStartDate(simplesyncsessionDateFormat.parse(completeStartDate));
					scForSessAdd.setClassEndDate(simplesyncsessionDateFormat.parse(completeEndDate));
					scForSessAdd.setEnrollmentCloseDate(formatDateWithTime.parse(enrollmentCloseDate+ " 23:59:59"));
					scForSessAdd.setTimeZoneId(Integer.valueOf(timezone));
					
	    			
	    			Set <SynchronousSession> lstSyncSess =  scForSessAdd.getSyncSession();
	    			Iterator<SynchronousSession> iter2 = lstSyncSess.iterator();
	    			    while (iter2.hasNext()){
	    			    	SynchronousSession syncSess = iter2.next();

	    		    		syncSess.setSyncClass(scForSessAdd);
	    		    		syncSess.setStartDateTime(simplesyncsessionDateFormat.parse(completeStartDate));
	    		    		syncSess.setEndDateTime(simplesyncsessionDateFormat.parse(completeEndDate));
	    		    		Set<SynchronousSession> getSyncSession = new HashSet<SynchronousSession>();
	    		    		getSyncSession.add(syncSess);
	    		    		scForSessAdd.setSyncSession(getSyncSession);
	    			    }

	    			}

	    		synchronoutService.saveSynchronousClass(scForSessAdd);
			}else{

				scForSessAdd = new SynchronousClass();
	    		CourseDTO secondCourse = courseService.getCourseById(Long.parseLong(courseId));
    			secondCourse.setLastUpdatedDate(new Date());
    			secondCourse.setLastUpdateUser(principal.getAuthorId());
				//CourseDTO secondCourse = new CourseDTO();
				//secondCourse.setId(Long.valueOf(courseId));
				scForSessAdd.setCourse(secondCourse);

	    		scForSessAdd.setClassName(secondCourse.getName());
	    		scForSessAdd.setClassStatus(COURSE_STATUS);
	    		scForSessAdd.setClassStartDate(simplesyncsessionDateFormat.parse(completeStartDate));
				scForSessAdd.setClassEndDate(simplesyncsessionDateFormat.parse(completeStartDate));
				scForSessAdd.setEnrollmentCloseDate(formatDateWithTime.parse(enrollmentCloseDate+ " 23:59:59"));
				scForSessAdd.setTimeZoneId(Integer.valueOf(timezone));
				
				SynchronousSession syncSess = new SynchronousSession();
	    		syncSess.setSyncClass(scForSessAdd);
	    		syncSess.setStartDateTime(simplesyncsessionDateFormat.parse(completeStartDate));
	    		syncSess.setEndDateTime(simplesyncsessionDateFormat.parse(completeEndDate));
	    		Set<SynchronousSession> getSyncSession = new HashSet<SynchronousSession>();
	    		getSyncSession.add(syncSess);
	    		scForSessAdd.setSyncSession(getSyncSession);
	    		synchronoutService.saveSynchronousClass(scForSessAdd);
			}
			
    		


		} catch (RuntimeException ex) {
			throw ex;//SonarQube recommendation
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return new ModelAndView("redirect:/schedule?id="+courseId + "&msg=success&" + WlcmsConstants.PARAMETER_COURSE_TYPE + "=" + courseType);
	}

	// send to Schedule page
	@RequestMapping(value = "instructor", method={RequestMethod.POST, RequestMethod.GET})
	public ModelAndView showinstructor(HttpServletRequest request) throws SQLException {
		logger.debug("CourseController::showinstructor - Start");
		SynchronousClassVO  syncClassVO =null;
        ClassInstructorVO classInstructorVO =new ClassInstructorVO();
		ModelAndView instructorView = new ModelAndView ("instructor");
		String courseId = request.getParameter("id");
		CourseDTO courseDB = courseService.getCourseById(Long.parseLong(courseId));
		syncClassVO = new SynchronousClassVO();
		syncClassVO.setCourseStatus(courseDB.getCourseStatus());
        String failureMessage = request.getParameter("failureMessage");

		try{
			List <SynchronousClass> lstSyncClass = synchronousService.getDeletedandUnDeletedSynchronousClassByCourseId(Long.parseLong(courseId));//synchronoutService.getSynchronousClassByCourseId(Long.parseLong(courseId));
			CourseProvider courseProvider = null;
            ClassInstructor classInstructor =null;
			courseProvider = courseProviderService.loadProviderbyCourseId(Long.valueOf(courseId));
            classInstructor = classInstructorService.findById(courseDB.getClassInstructorId()!=null?courseDB.getClassInstructorId():-1);
			if(request.getParameter("cType").equals("5") && courseProvider!=null){
				syncClassVO.setFullName(courseProvider.getName());
				syncClassVO.setEmailAddress(courseProvider.getEmail());
				syncClassVO.setCourseProviderId(courseProvider.getId());
				syncClassVO.setProviderPhoneNo(courseProvider.getPhoneNo());
			}
            else if(request.getParameter("cType").equals("6") && classInstructor!=null) {
                classInstructorVO.setPresenterFirstName(classInstructor.getFirstName());
                classInstructorVO.setPresenterLastName(classInstructor.getLastName());
                classInstructorVO.setPresenterEmail(classInstructor.getEmail());
                classInstructorVO.setPresenterPhone(classInstructor.getPhoneNo());
                classInstructorVO.setId(classInstructor.getId());

            }

			for(SynchronousClass objSyncClass : lstSyncClass){
				if(objSyncClass.getPresenterEmail()!=null && !objSyncClass.getPresenterEmail().equals("")){
						syncClassVO.setSyncClassId(objSyncClass.getId());
						syncClassVO.setPresenterFirstName(objSyncClass.getPresenterFirstName());
						syncClassVO.setPresenterLastName(objSyncClass.getPresenterLastName());
						syncClassVO.setPresenterEmail(objSyncClass.getPresenterEmail());
						syncClassVO.setPresenterPhone(objSyncClass.getPresenterPhone());
						if(courseProvider!=null){
							syncClassVO.setFullName(courseProvider.getName());
							syncClassVO.setEmailAddress(courseProvider.getEmail());
							syncClassVO.setCourseProviderId(courseProvider.getId());
							syncClassVO.setProviderPhoneNo(courseProvider.getPhoneNo());
						}
						break;
					}
				syncClassVO.setSyncClassId(objSyncClass.getId());


				}

			/*if(lstSyncClass!=null){
				SynchronousClass objSyncClass = lstSyncClass.get(0);
				syncClassVO.setSyncClassId(objSyncClass.getId());
				syncClassVO.setPresenterFirstName(objSyncClass.getPresenterFirstName());
				syncClassVO.setPresenterLastName(objSyncClass.getPresenterLastName());
				syncClassVO.setPresenterEmail(objSyncClass.getPresenterEmail());
				syncClassVO.setPresenterPhone(objSyncClass.getPresenterPhone());
			}*/

		}catch(Exception ex){
			logger.debug(ex.getMessage());
		}
		logger.debug("CourseController::showinstructor - End");
		instructorView.addObject("courseObj", courseDB);
        if(failureMessage!=null && !failureMessage.equals(""))
            instructorView.addObject("failureMessage", failureMessage);
		instructorView.addObject("syncClassVO", syncClassVO);
        instructorView.addObject("classInstructorVO",classInstructorVO);
		instructorView.addObject(WlcmsConstants.PARAMETER_COURSE_TYPE,  request.getParameter(WlcmsConstants.PARAMETER_COURSE_TYPE));

		return instructorView;
	}


    @RequestMapping(value = "checkInstructorEmail", method = RequestMethod.POST)
    public @ResponseBody Boolean checkInstructorEmail(
            HttpServletRequest request, HttpServletResponse response) {


        String email = request.getParameter("presenter_email");
        Long id = TypeConvertor.AnyToLong(request.getParameter("classInstructorId"));

        return classInstructorService.emailAlreadyExist(email,id);
    }

	// send to Schedule page
	@RequestMapping(value = "saveInstructor", method={RequestMethod.POST, RequestMethod.GET})
	public ModelAndView saveInstructor(HttpServletRequest request) throws SQLException {

		logger.debug("CourseController::showinstructor - Start");
		String cType = request.getParameter("cType");

		//ModelAndView scheduleView = new ModelAndView ("instructor");
		String courseId = request.getParameter("varCourseId");
		String presenterFirstName = request.getParameter("presenter_firstname");
		String presenterLastName = request.getParameter("presenter_lastname");
		String presenterEmail = request.getParameter("presenter_email");
		String presenterPhone = request.getParameter("presenter_phonenumber");
		String courseType = request.getParameter("cType");
		String fullName = request.getParameter("full_name");
		String email = request.getParameter("email_address");
		String courseProviderId = request.getParameter("courseProviderId");
		String phoneNo = request.getParameter("phone_no");



		CourseProvider courseProvider = new CourseProvider();
		//CourseDTO course = new CourseDTO();
		CourseDTO course = courseService.getCourseById(Long.parseLong(courseId));
		courseProvider.setCourse(course);
		courseProvider.setName(fullName);
		courseProvider.setEmail(email);
		courseProvider.setPhoneNo(phoneNo);
		courseProvider.setId((courseProviderId!=null && !courseProviderId.equals(""))?Long.valueOf(courseProviderId):null);
		courseProviderService.save(courseProvider);
        ClassInstructor persist = new ClassInstructor();
		
		VU360UserDetail principal = (VU360UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();	
		List <SynchronousClass> lstSyncClass = synchronoutService.getDeletedandUnDeletedSynchronousClassByCourseId(Long.parseLong(courseId));
        if(cType!=null && !cType.equals("") && request.getParameter("cType").equals("6")){ // digit 6 shows webinar course
                if(course.getClassInstructorId()!=null){
                    persist = classInstructorService.findById(course.getClassInstructorId());
                }

                persist.setEmail(presenterEmail);
                persist.setPhoneNo(presenterPhone);
                persist.setLastName(presenterLastName);
                persist.setFirstName(presenterFirstName);
                classInstructorService.save(persist);
                if(persist.getEmail().equals("Email exist")) {
                    return new ModelAndView("redirect:/instructor?id=" + courseId + "&failureMessage=" + persist.getEmail() + "&" + WlcmsConstants.PARAMETER_COURSE_TYPE + "=" + courseType);

                }
                course.setClassInstructorId(persist.getId());
                courseService.saveCourse(course);

        }
		SynchronousClass scForSessAdd = null;
		Iterator<SynchronousClass> iter = lstSyncClass.iterator();
		while (iter.hasNext()) {
			scForSessAdd = iter.next();
			CourseDTO secondCourse = courseService.getCourseById(Long.parseLong(courseId));
			secondCourse.setLastUpdatedDate(new Date());
			secondCourse.setLastUpdateUser(principal.getAuthorId());
			scForSessAdd.setCourse(secondCourse);
			scForSessAdd.setPresenterFirstName(presenterFirstName);
			scForSessAdd.setPresenterLastName(presenterLastName);
			scForSessAdd.setPresenterEmail(presenterEmail);
			scForSessAdd.setPresenterPhone(presenterPhone);
            scForSessAdd.setClassInstructorId(persist.getId());
            synchronoutService.saveSynchronousClass(scForSessAdd);
	
			logger.debug("CourseController::showinstructor - End");
		}

		return new ModelAndView("redirect:/instructor?id=" + courseId + "&msg=success&" + WlcmsConstants.PARAMETER_COURSE_TYPE + "=" + courseType);
	}


	// send to Schedule page
		@RequestMapping(value = "saveWebinarSetup", method={RequestMethod.POST, RequestMethod.GET})
		public ModelAndView saveWebinarSetUp(HttpServletRequest request) throws SQLException {
			logger.debug("SynchronousCourseController::showWebinarSetup - Start");

			String courseId = request.getParameter("varCourseId");
			String meetingURL = request.getParameter("MEETINGURL");
			String meetingPassCode = request.getParameter("MEETINGPASSCODE");
			String dialinNumber = request.getParameter("DIAL_IN_NUMBER");
			String additionalInformation = request.getParameter("ADDITIONAL_INFORMATION");
			String webinarServiceProvider = request.getParameter("WEBINAR_SERVICE_PROVIDER");

			VU360UserDetail principal = (VU360UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			List <SynchronousClass> lstSyncClass = synchronoutService.getSynchronousClassByCourseId(Long.parseLong(courseId));

			SynchronousClass scForSessAdd = null;
			Iterator<SynchronousClass> iter = lstSyncClass.iterator();
			while (iter.hasNext()) {
				scForSessAdd = iter.next();
				CourseDTO secondCourse = courseService.getCourseById(Long.parseLong(courseId));
				secondCourse.setLastUpdatedDate(new Date());
				secondCourse.setLastUpdateUser(principal.getAuthorId());
				scForSessAdd.setCourse(secondCourse);
				scForSessAdd.setMeetingURL(meetingURL);
				scForSessAdd.setMeetingPassCode(meetingPassCode);
				scForSessAdd.setDialInNumber(dialinNumber);
				scForSessAdd.setAdditionalInformation(additionalInformation);
				scForSessAdd.setWebinarServiceProvider(webinarServiceProvider);

				synchronoutService.saveSynchronousClass(scForSessAdd);

				logger.debug("SynchronousCourseController::showWebinarSetup - End");
			}
				
	    		return new ModelAndView("redirect:/webinarSetup?id="+courseId + "&cType=6" + "&msg=success");
		}

		@RequestMapping(value = "webinarSetup", method={RequestMethod.POST, RequestMethod.GET})
		public ModelAndView showWebinarSetup(HttpServletRequest request) throws SQLException {
			logger.debug("CourseController::webinar_setup - Start");
			SynchronousClassVO  syncClassVO =null;
			ModelAndView scheduleView = new ModelAndView ("webinar_setup");
			String courseId = request.getParameter("id");
			CourseDTO courseDB = courseService.getCourseById(Long.parseLong(courseId));


			try{
			List <SynchronousClass> lstSyncClass = synchronoutService.getSynchronousClassByCourseId(Long.parseLong(courseId));

			if(lstSyncClass!=null){
				SynchronousClass objSyncClass = lstSyncClass.get(0);
				syncClassVO = new SynchronousClassVO();
				syncClassVO.setSyncClassId(objSyncClass.getId());
				syncClassVO.setMeetingURL(objSyncClass.getMeetingURL());
				syncClassVO.setMeetingPassCode(objSyncClass.getMeetingPassCode());
				syncClassVO.setDialInNumber(objSyncClass.getDialInNumber());
				syncClassVO.setAdditionalInformation(objSyncClass.getAdditionalInformation());
				syncClassVO.setWebinarServiceProvider(objSyncClass.getWebinarServiceProvider());
				syncClassVO.setMeetingId(objSyncClass.getMeetingId());

				if(objSyncClass.getClassStartDate() != null){
					syncClassVO.setCourseStartDate(objSyncClass.getClassStartDate().toString());
				}
				syncClassVO.setPresenterEmail(objSyncClass.getPresenterEmail());

			}
			}catch(Exception ex){
				logger.debug(ex.getMessage());
			}
			logger.debug("CourseController::webinar_setup - End");
			scheduleView.addObject("coursename", courseDB.getName());
			scheduleView.addObject("bussinesskey", courseDB.getBussinesskey());
			scheduleView.addObject("syncClassVO", syncClassVO);
			scheduleView.addObject("courseObj", courseDB);
			return scheduleView;
		}

		@RequestMapping(value = "deleteClassroom", method = RequestMethod.POST)
		public @ResponseBody String deleteClassroom(
				HttpServletRequest request, HttpServletResponse response
				) {

			logger.debug("LocationController::deleteLocation - Start");

			String deleteId =  request.getParameter("class_id");


			//location.setAddress(address);
			//location.setId(Long.parseLong(locationId));

			synchronousService.deleteClassroom(Long.parseLong(deleteId));
			
			




			return deleteId;

		}

}
