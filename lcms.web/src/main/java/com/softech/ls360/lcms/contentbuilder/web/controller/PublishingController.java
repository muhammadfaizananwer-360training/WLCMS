package com.softech.ls360.lcms.contentbuilder.web.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.softech.ls360.lcms.contentbuilder.model.*;
import com.softech.ls360.lcms.contentbuilder.service.*;
import com.softech.ls360.lcms.contentbuilder.utils.*;
import com.softech.ls360.lcms.contentbuilder.web.vo.OfferEmailVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.softech.common.mail.MailAsyncManager;
import com.softech.ls360.lcms.contentbuilder.manager.WebinarMeetingURLManager;


@Controller
public class PublishingController {

	private static Logger logger = LoggerFactory.getLogger(PublishingController.class);
	final String TO_CONTENTOWNER_EMAIL = LCMSProperties.getLCMSProperty("offer.toContentOwner.userName");
	final String ROYALTY_TYPE = LCMSProperties.getLCMSProperty("offer.royaltyaType");
	final String EXISTING_OFFER_IN_PLACE = LCMSProperties.getLCMSProperty("offer.bitExistingOfferInPlace");
	final String OFFER_STATUS = LCMSProperties.getLCMSProperty("offer.offerStatus");
	final String NOTE_TO_DISTRIBUTOR = LCMSProperties.getLCMSProperty("offer.resellerNote");

	@Autowired
	IPublishingService publishingService;

	@Autowired
	IOfferService offerService;

	@Autowired
	IContentOwnerRoyaltySettingsService royaltySettingsService;

	@Autowired
	VU360UserService vu360UserService;

	@Autowired
	ISynchronousClassService synchronoutService;

	@Autowired
	ICourseService courseService;

	@Autowired
	MailAsyncManager mailAsyncManager;

	@RequestMapping(value = "pricing", method = RequestMethod.GET)
	public ModelAndView pricing(HttpServletRequest request) {

		logger.debug("PublishingController::pricing - Begin");

		ModelAndView courseModelView = new ModelAndView ("pricing");

		if(request.getParameter("msg")!=null){
			courseModelView.addObject("msg", "success");
		}

		if(request.getParameter("isUpdate")!=null ){
			courseModelView.addObject("isUpdate", "true");
		}

		if(request.getParameter("failureMessage")!=null){
			courseModelView.addObject("failureMessage",  request.getParameter("failureMessage"));
		}

		String idToSearch = request.getParameter("id");
		courseModelView.addObject("cType",  request.getParameter("cType"));

		if (idToSearch == null)
			return null;

		try{
			CoursePricing cp = publishingService.getCoursePricing(Integer.parseInt(idToSearch));
			if (cp != null ){
				courseModelView.addObject("command", cp);
			}

			VU360UserDetail user  = (VU360UserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			CourseCompletionReport courseCompletionReport = new CourseCompletionReport ();
			courseCompletionReport.setCourseId(Integer.parseInt(idToSearch));
			courseCompletionReport.setContentOwnerId((int) user.getAuthorId());
			courseCompletionReport = publishingService.getCompletionReport(courseCompletionReport);

			courseModelView.addObject("courseCompletionReport", courseCompletionReport);

		}
		catch (Exception ee){
			logger.error(ee.toString());
		}

		logger.debug("PublishingController::pricing - End");
		return courseModelView;
	}

	//@RequestMapping(value="updatePricing", method={RequestMethod.POST, RequestMethod.GET})
	public void updatePricing(
			HttpServletRequest request) {


		int id = Integer.parseInt(request.getParameter("id"));
		String sMSRP= request.getParameter("mSRP");
		String sLowestSalePrice = request.getParameter("lowestSalePrice");
		String courseType = request.getParameter(WlcmsConstants.PARAMETER_COURSE_TYPE);
		Double offerPrice = 0.0;
		Boolean manageSFPrice = Boolean.valueOf( request.getParameter("chkManagerOffer") != null  && request.getParameter("chkManagerOffer").contains("on") ? "true" : "false");
		String offerPriceParam = request.getParameter("offerprice");
		if(manageSFPrice){
			offerPrice = Double.valueOf (offerPriceParam == null || offerPriceParam.trim().equals("")  ? "0" : request.getParameter("offerprice"));
		}

		logger.debug("PublishingController::updatePricing - Start");

		CoursePricing cp = new CoursePricing();
		String sErrorMsg = null;

		try
		{
			//LdapUserDetailsImpl userd  = (LdapUserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			//VU360UserDetail  user = (VU360UserDetail) vu360UserService.loadUserByUsername(userd.getUsername());
			VU360UserDetail user  = (VU360UserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			cp.setUpdatedBy(String.valueOf(user.getAuthorId()));
			cp.setId(id);
			cp.setmSRP(sMSRP);
			cp.setLowestSalePrice(sLowestSalePrice);

			cp.setOfferprice(offerPrice);
			cp.setManageSFPrice(manageSFPrice);

			publishingService.updateCoursePricing(cp);

		} catch (Exception ex)
		{
			sErrorMsg = "&failureMessage=" +"Pricing Information could not be saved " + ex.getMessage();
			logger.debug("CourseController::updateCourse - Exception " + ex.getMessage());
		}
		logger.debug("PublishingController::updatePricing - End");
        /*if (sErrorMsg != null)
        {
        	sErrorMsg = "redirect:/pricing?id="+ id + sErrorMsg + "&" + WlcmsConstants.PARAMETER_COURSE_TYPE + "=" + courseType;;
        }
        else
        {
        	sErrorMsg = "redirect:/pricing?id="+ id + "&msg=success&" + WlcmsConstants.PARAMETER_COURSE_TYPE + "=" + courseType;
        }*/
		// return new ModelAndView(sErrorMsg);
	}

	@RequestMapping(value = "publishing",  method={RequestMethod.POST, RequestMethod.GET})
	ModelAndView publishing(HttpServletRequest request, HttpServletResponse response)throws Exception {
		logger.info("QuizController::publishing - BEGIN");

		ModelAndView courseModelView = new ModelAndView ("publishing");

		String idToSearch = request.getParameter("id");


		VU360UserDetail user  = (VU360UserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int contentOwnerId = Integer.parseInt(String.valueOf(user.getAuthorId()));

		CourseCompletionReport courseCompletionReport = new CourseCompletionReport ();
		courseCompletionReport.setCourseId(Integer.parseInt(idToSearch));
		courseCompletionReport.setContentOwnerId(contentOwnerId);
		//CourseCompletionReport.setContentOwnerId(Integer.parseInt(contentOwnerId));
		courseCompletionReport = publishingService.getCompletionReport(courseCompletionReport);
		String dateString = courseCompletionReport.getLastPublishDate();
		if(dateString != null && !dateString.trim().equals("")){
			courseCompletionReport.setLastPublishDate(changeFormat(dateString));
		}

		courseModelView.addObject("command", courseCompletionReport);
		courseModelView.addObject("offer", request.getParameter("offer")==null?"0":request.getParameter("offer"));
		courseModelView.addObject("id", idToSearch);
		courseModelView.addObject(WlcmsConstants.PARAMETER_COURSE_TYPE, request.getParameter(WlcmsConstants.PARAMETER_COURSE_TYPE));

		if(request.getParameter("msg")!=null){
			courseModelView.addObject("msg", "success");
		}

		if(request.getParameter("failureMessage")!=null){
			courseModelView.addObject("failureMessage",  request.getParameter("failureMessage"));
		}


		logger.info("QuizController::publishing - END");
		return courseModelView;
	}

	//change date format for view
	private String changeFormat(String dateString)throws ParseException {
		DateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");//e.g "06/02/2016 11:01 AM"
		Date dt = inputFormat.parse(dateString);
		DateFormat outputFormat = new SimpleDateFormat("MMM. dd, yyyy | hh:mma");//e.g "Jun. 02, 2016 | 11:01AM"
		return outputFormat.format(dt);
	}

	@RequestMapping(value = "webinar_publishing",  method={RequestMethod.POST, RequestMethod.GET})
	ModelAndView webinarPublishing(HttpServletRequest request, HttpServletResponse response)throws Exception {
		logger.info("QuizController::publishing - BEGIN");

		ModelAndView courseModelView = new ModelAndView ("webinar_publishing");

		String idToSearch = request.getParameter("id");
		String cType = request.getParameter("cType");

		VU360UserDetail user  = (VU360UserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int contentOwnerId = Integer.parseInt(String.valueOf(user.getAuthorId()));

		CourseCompletionReport courseCompletionReport = new CourseCompletionReport ();
		courseCompletionReport.setCourseId(Integer.parseInt(idToSearch));
		courseCompletionReport.setContentOwnerId(contentOwnerId);
		courseCompletionReport.setCourseType(TypeConvertor.AnyToInteger(cType));
		courseCompletionReport = publishingService.getWebinarCompletionReport(courseCompletionReport);

		String dateString = courseCompletionReport.getLastPublishDate();
		if(dateString != null && !dateString.trim().equals("")){
			courseCompletionReport.setLastPublishDate(changeFormat(dateString));
		}
		courseModelView.addObject("command", courseCompletionReport);
		courseModelView.addObject("id", idToSearch);
		courseModelView.addObject("cType", cType);

		if(request.getParameter("msg")!=null){
			courseModelView.addObject("msg", "success");
		}

		if(request.getParameter("failureMessage")!=null){
			courseModelView.addObject("failureMessage",  request.getParameter("failureMessage"));
		}

		logger.info("QuizController::publishing - END");
		return courseModelView;
	}

	public String makeOffer(HttpServletRequest request)throws Exception {
		//ModelAndView mv = new ModelAndView ("offerOn360Marketplace");
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		JsonResponse res = new JsonResponse();

		Integer publishedCourseId = 0;
		try {
			String courseId = request.getParameter("id");
			VU360UserDetail user  = (VU360UserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			int cType = Integer.parseInt(request.getParameter("cType"));

			if(cType==CourseType.CLASSROOM_COURSE.getId() || cType==CourseType.WEBINAR_COURSE.getId())
				publishedCourseId = Integer.valueOf(courseId);
			else
				publishedCourseId = courseService.getPublishedVersion(courseId);


			CoursePricing objPrice = publishingService.getCoursePricing(Integer.parseInt(courseId));
			VU360UserDetail toContentOwner = (VU360UserDetail) vu360UserService.loadUserByUsername(TO_CONTENTOWNER_EMAIL);

			Offer objOffer = new Offer();
			objOffer.setFromContentownerId(user.getContentOwnerId());
			objOffer.setToContentownerId(String.valueOf(toContentOwner.getContentOwnerId()));
			objOffer.setOriginalCourseId(publishedCourseId.toString());
			objOffer.setLowestPrice(objPrice.getLowestSalePrice());
			objOffer.setRoyaltyType(ROYALTY_TYPE);
			objOffer.setExistingOfferInPlace( Integer.parseInt( EXISTING_OFFER_IN_PLACE ) );
			objOffer.setCreatedDate(dateFormat.format(cal.getTime()));
			objOffer.setCreatedUserId(user.getAuthorId());
			objOffer.setOfferStatus(OFFER_STATUS);
			objOffer.setSuggestedRetailPrice(objPrice.getmSRP());
			objOffer.setNoteToDistributor(NOTE_TO_DISTRIBUTOR);
			objOffer.setAuthorId(user.getAuthorId());

			ContentOwnerRoyaltySettings royaltySettings = royaltySettingsService.getContentOwnerById((int)user.getContentOwnerId());
			CourseType courseType = CourseType.getById(cType);
			switch(courseType){
				case ONLINE_COURSE:
					objOffer.setRoyaltyAmount(royaltySettings.getOnlineRoyaltyPercentage());
					break;
				case CLASSROOM_COURSE:
					objOffer.setRoyaltyAmount(royaltySettings.getClassroomRoyaltyPercentage());
					break;
				case WEBINAR_COURSE:
					objOffer.setRoyaltyAmount(royaltySettings.getWebinarRoyaltyPercentage());
					break;
			}

			offerService.newOffer(objOffer);


			CourseAvailability availability = publishingService.getCourseAvailability(Integer.parseInt(courseId));
			OfferEmailVO oe = new OfferEmailVO();
			oe.setIndustry(availability.getIndustry());
			oe.setCourseName(availability.getCourseName());
			oe.setCourseId(availability.getBusinessKey());
			oe.setAuthorName(user.getUserDisplayName());
			oe.setAuthorEmail(user.getEmailAddress());
			oe.setToContentOwnerEmail(toContentOwner.getEmailAddress());

			sendMailOnReceivedOffer(oe);
			//res.setStatus("SUCCESS");
			return "success";
		}catch(Exception ex){
			ex.printStackTrace();
			return "fail";

		}



	}

	boolean sendMailOnReceivedOffer(OfferEmailVO oe){

		String subject = "Offer for " + oe.getIndustry() + " from "+ oe.getAuthorEmail() +" of "+ oe.getCourseName();
		StringBuffer msgBody = new StringBuffer();
		msgBody.append("You have received a course offer pertaining to "+ oe.getIndustry() +".<br/>");
		msgBody.append("<b>Course Name</b>:  "+ oe.getCourseName() +" <br/>");
		msgBody.append("<b>Course ID</b>: "+ oe.getCourseId() +"<br/>");
		msgBody.append("<b>Author Name</b>: "+ oe.getAuthorName() +"<br/>");
		msgBody.append("<b>Author Email</b>: "+ oe.getAuthorEmail() + " <br/>");

		mailAsyncManager.send(new String[] { oe.getToContentOwnerEmail() }, null, "support@360training.com",
				"360training Member Service",  subject ,   msgBody + "");

		return true;
	}

	@RequestMapping(value = "publishCourse",  method={RequestMethod.POST, RequestMethod.GET})
	ModelAndView publishCourse(HttpServletRequest request, HttpServletResponse response)throws Exception {

		String idToSearch = request.getParameter("id");
		String cType = request.getParameter("cType");

		String msg = "&msg=success";


		Boolean publishSF = Boolean.parseBoolean(request.getParameter("publishSF")==null ? "false" :request.getParameter("publishSF") );
		Boolean publishLMS = Boolean.parseBoolean(request.getParameter("publishLMS")==null ? "false" :request.getParameter("publishLMS"));
		Boolean updateCouseContent = Boolean.parseBoolean(request.getParameter("updateCouseContent")==null ? "false" :request.getParameter("updateCouseContent"));
		Boolean updateLMS = Boolean.parseBoolean(request.getParameter("updateLMS")==null ? "false" :request.getParameter("updateLMS"));
		Boolean updateSF = Boolean.parseBoolean(request.getParameter("updateSF")==null ? "false" :request.getParameter("updateSF"));
		Boolean publis360Btn = Boolean.parseBoolean(request.getParameter("publis360Btn")==null ? "false" :request.getParameter("publis360Btn"));

		String sErrorMsg = "";


		if (cType != null && (cType.equals(String.valueOf(CourseType.CLASSROOM_COURSE.getId())) ||
				cType.equals(String.valueOf(CourseType.WEBINAR_COURSE.getId()))) ) {
			sErrorMsg = publishSynchronousCourse(idToSearch, publishSF, publishLMS,
					updateCouseContent, updateLMS, updateSF);

			if (Integer.parseInt(cType) == CourseType.WEBINAR_COURSE.getId()){//this should happen only for Publishing Webinar Course
				int courseIdInt = Integer.parseInt(idToSearch);
				List<SynchronousClass> cClassList = synchronoutService.getSynchronousClassByCourseId(courseIdInt);
				if(cClassList != null && cClassList.size() > 0) {

					SynchronousClass cClass = cClassList.get(0);
					if(cClass.getWebinarServiceProvider().equals(WlcmsConstants.WEBINAR_SERVICE_PROVIDER_360TRAINING)){

						CourseDTO course = courseService.getCourseById(courseIdInt);
						cClass = WebinarMeetingURLManager.getInstance(synchronoutService).setupWebinarMeetingURL(course);

						if(cClass != null && cClass.getMeetingURL() != null){
							sendMailOnReceivedOffer(course, cClass);
						}
					}
				}
			}
		}else {

			sErrorMsg = publishOnlineCourse (idToSearch, publishSF, publishLMS,
					updateCouseContent, updateLMS, updateSF);

			String id = String.valueOf( publishingService.getNotStartedCourse((int)Integer.parseInt(idToSearch)));

			if (id != null && !id.equalsIgnoreCase("0") && !id.equalsIgnoreCase("") )
				idToSearch = id;
		}

		if (sErrorMsg.length() > 0)
			msg = sErrorMsg;


		//sErrorMsg = "redirect:/webinar_publishing?id="+ idToSearch + "&cType=" + cType + msg;
		StringBuilder builder = new StringBuilder();
		builder.append ( cType != null &&  cType.equals("4")  ?	"redirect:/publishing?id=" :	"redirect:/webinar_publishing?id=");
		builder.append (idToSearch);
		builder.append ("&cType=");
		builder.append (cType);
		builder.append(msg);

		updatePricing(request);
		if(publis360Btn) {
			if (makeOffer(request).equals("success")) {
				builder.append("&offer=1");
			} else {
				builder.append("&offer=2");
			}
		}

		return new ModelAndView(builder.toString());
	}

	boolean sendMailOnReceivedOffer(CourseDTO course,  SynchronousClass syncClass){

		String subject = "Webinar Setup | " + course.getName();
		VU360UserDetail user  = (VU360UserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		StringBuffer msgBody = new StringBuffer();

		msgBody.append("Dear "+ user.getUserDisplayName() +",<br/><br/>");
		msgBody.append("Your Webinar Course URL:<br/>" + syncClass.getMeetingURL());
		msgBody.append("<br/><br/>Thanks,<br/>");
		msgBody.append("<b>360Training Authoring Team</b>");

		mailAsyncManager.send(new String[] { syncClass.getPresenterEmail() }, null, WlcmsConstants.SUPPORT_360TRAINING_EMAIL_ADDRESS,
				"360training Member Service",  subject ,   msgBody + "");
		return true;
	}

	public String publishSynchronousCourse(String idToSearch, Boolean publishSF,
										   Boolean publishLMS, Boolean updateCouseContent, Boolean updateLMS,
										   Boolean updateSF ){
		String msg  = "";
		VU360UserDetail user  = (VU360UserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		try
		{
			publishingService.adjustCourseInfoBeforePublish(TypeConvertor.AnyToInteger(idToSearch));
			if (publishSF || updateSF)
				CoursePublishHelper.publishCourse(Integer.parseInt(idToSearch), "SyncSF", user.getAuthorId());

			if (publishLMS || updateLMS)
				CoursePublishHelper.publishCourse(Integer.parseInt(idToSearch), "SyncLMS", user.getAuthorId());

			publishingService.changeSynchrounousPublishStatus(Integer.parseInt(idToSearch), user.getAuthorId());

		} catch (Exception ex) {
			logger.error(ex.getMessage ());
			msg = "&failureMessage=Course publishing could not be performed. " ;
		}

		if (updateCouseContent)
			publishingService.updateCourseContent(Integer.parseInt(idToSearch),(int)user.getAuthorId());

		return msg;

	}

	public String publishOnlineCourse(String idToSearch, Boolean publishSF, Boolean publishLMS,
									  Boolean updateCouseContent, Boolean updateLMS, Boolean updateSF) {

		String msg  = "";
		VU360UserDetail user  = (VU360UserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		try
		{
			publishingService.updateCourseMeta(Integer.parseInt(idToSearch));

			if (publishSF || updateSF){
				CoursePublishHelper.publishCourse(Integer.parseInt(idToSearch), "SF", user.getAuthorId());
				// CoursePublishHelper.publishCourse(Integer.parseInt(idToSearch), "LMS", user.getAuthorId());
			}
			if (publishLMS || updateLMS || updateCouseContent){
				CoursePublishHelper.publishCourse(Integer.parseInt(idToSearch), "LMS", user.getAuthorId());
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage ());
			msg = "&failureMessage=Course publishing could not be performed. " ;
		}

		if (updateCouseContent)
			publishingService.updateCourseContent(Integer.parseInt(idToSearch),(int)user.getAuthorId());

		return msg;

	}

	/**
	 * This method is used to fetch course availbility settings data.
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "availability", method = RequestMethod.GET)
	public ModelAndView availabilityCourse(HttpServletRequest request) {

		logger.debug("PublishingController::availabilityCourse - Begin");
		ModelAndView courseModelView;
		int getCourseType = 0;

		if (request.getParameter("cType") !=null)
		{
			getCourseType = Integer.parseInt(request.getParameter("cType"));
		}

		if (getCourseType ==CourseType.WEBINAR_COURSE.getId())
		{
			courseModelView = new ModelAndView ("webinar_course_availability");
		}

		else if(getCourseType ==CourseType.CLASSROOM_COURSE.getId())
		{
			courseModelView = new ModelAndView ("classroom_course_availability");
		}

		else
		{
			courseModelView = new ModelAndView ("course_availability");
		}

		if(request.getParameter("msg")!=null){
			courseModelView.addObject("msg", "success");
		}

		if(request.getParameter("isUpdate")!=null ){
			courseModelView.addObject("isUpdate", "true");
		}

		if(request.getParameter("failureMessage")!=null){
			courseModelView.addObject("failureMessage",  request.getParameter("failureMessage"));
		}

		String idToSearch = request.getParameter("id");
		if (idToSearch == null)
			return null;

		try{
			CourseAvailability availability = publishingService.getCourseAvailability(Integer.parseInt(idToSearch));
			if (availability != null ){
				courseModelView.addObject("availability", availability);
			}else{

				courseModelView.addObject("failureMessage", "<strong>Error! </strong> No Data is available for this course");
				logger.debug("PublisherController::Availability");
			}

		}
		catch (Exception ee){
			logger.error(ee.toString());
		}

		courseModelView.addObject("industryList", WlcmsConstants.INDUSTRY_LIST);
		logger.debug("PublishingController::availabilityCourse - End");
		return courseModelView;
	}


	/**
	 * This method is used to fetch course availbility settings data.
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "leftMenuOfferOn360Marketplace", method = RequestMethod.GET)
	public ModelAndView leftMenuOfferOn360Marketplace(HttpServletRequest request) {
		ModelAndView courseModelView = new ModelAndView ("offerOn360Marketplace");
		return courseModelView;
	}

	/**
	 * This methods update the course availbility settings.
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "updadeCourseAvailability",  method={RequestMethod.POST, RequestMethod.GET})
	ModelAndView updateCourseAvailability(HttpServletRequest request, HttpServletResponse response)throws Exception {

		String idToSearch = request.getParameter("id");
		int getCourseType = 0;

		if (request.getParameter("cType") !=null)
		{
			getCourseType = Integer.parseInt(request.getParameter("cType"));
		}

		String msg = "&msg=success";
		boolean isErrorFound = false;
		VU360UserDetail user  = (VU360UserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Boolean thirdPartyDistr = false;
		Boolean varResaleClassroomW = false;
		Boolean mobileTablet = false;

		String industry = request.getParameter("industry");
		String standardCourseGrpId = request.getParameter("standardCourseGrpName");
		String courseExpiration = request.getParameter("courseExpiration");
		if(courseExpiration != null && courseExpiration.isEmpty()){
			courseExpiration = null;
		}
		String learnerAccessToCourse = request.getParameter("learnerAccessToCourse");

		if (getCourseType ==CourseType.ONLINE_COURSE.getId()){
			thirdPartyDistr = !Boolean.parseBoolean(request.getParameter("offeredForDistribution")==null ? "false" :request.getParameter("offeredForDistribution"));
			mobileTablet = !Boolean.parseBoolean(request.getParameter("eligibleForMobileTablet")==null ? "false" :request.getParameter("eligibleForMobileTablet"));
		}

		else{
			varResaleClassroomW = Boolean.parseBoolean(request.getParameter("varResale")==null ? "false" :request.getParameter("varResale"));
			mobileTablet = Boolean.parseBoolean(request.getParameter("eligibleForMobileTablet")==null ? "false" :request.getParameter("eligibleForMobileTablet"));
		}

		Boolean subscription = Boolean.parseBoolean(request.getParameter("subscription")==null ? "false" :request.getParameter("subscription"));
		Boolean varResale = getCourseType == CourseType.ONLINE_COURSE.getId() ? thirdPartyDistr : varResaleClassroomW;
		Boolean distributionSCORM = thirdPartyDistr;
		Boolean distributionAICC = thirdPartyDistr;
		Boolean reportingToRegulator = Boolean.parseBoolean(request.getParameter("reportingToRegulator")==null ? "false" :request.getParameter("reportingToRegulator"));;
		Boolean requireShippable = Boolean.parseBoolean(request.getParameter("requireShippableItems")==null ? "false" :request.getParameter("requireShippableItems"));
		Boolean thirdPartyCourse = Boolean.parseBoolean(request.getParameter("thirdPartyCourse")==null ? "false" :request.getParameter("thirdPartyCourse"));;

		if(idToSearch == null || idToSearch.length() == 0){

			logger.error("Course ID is missing or invalid");
			msg = "&failureMessage=Course availbility settings could not be performed, Course ID is missing or invalid ";
			isErrorFound = true;
		}

		if(!isErrorFound){
			CourseAvailability availability = new CourseAvailability();
			availability.setId(Integer.parseInt(idToSearch));
			availability.setIndustry(industry);
			availability.setCourseGroupId(standardCourseGrpId);
			availability.setCourseExpiration(courseExpiration);
			availability.setLearnerAccessToCourse((learnerAccessToCourse == null || learnerAccessToCourse.length() == 0) ? "365" : learnerAccessToCourse);
			availability.setEligibleForMobileTablet(mobileTablet);
			availability.setEligibleForSubscription(subscription);
			availability.setEligibleForVARresale(varResale);
			availability.setEligibleForDistrThruSCORM(distributionSCORM);
			availability.setEligibleForDistrThruAICC(distributionAICC);
			availability.setRequireReportToRegulator(reportingToRegulator);
			availability.setRequireShippableItems(requireShippable);
			availability.setIsThirdpartyCourse(thirdPartyCourse);
			availability.setUpdatedBy(String.valueOf(user.getAuthorId()));
			Boolean isUpdated = false;

			try{
				isUpdated = publishingService.updateCourseAvailability(availability);


				//CoursePublishHelper.publishCourse(Integer.parseInt(idToSearch), "SFONLY", user.getAuthorId());
			}
			catch (Exception e){
				msg = "&failureMessage=" +"Your course could not be saved" + e.getMessage();
				logger.error(e.toString());
			}
			if(!isUpdated){
				logger.error("PublishingController::UpdateAvailability:: Data could not saved successfully");
			}
		}

		String sErrorMsg = "redirect:/availability?id="+ idToSearch + "&cType=" + getCourseType + msg ;
		return new ModelAndView(sErrorMsg);
	}
}
