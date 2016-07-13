package com.softech.ls360.lcms.contentbuilder.web.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.softech.ls360.lcms.contentbuilder.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.softech.common.mail.MailAsyncManager;
import com.softech.ls360.lcms.contentbuilder.model.ContentOwnerRoyaltySettings;
import com.softech.ls360.lcms.contentbuilder.model.CourseAvailability;
import com.softech.ls360.lcms.contentbuilder.model.CourseCompletionReport;
import com.softech.ls360.lcms.contentbuilder.model.CourseDTO;
import com.softech.ls360.lcms.contentbuilder.model.CoursePricing;
import com.softech.ls360.lcms.contentbuilder.model.Offer;
import com.softech.ls360.lcms.contentbuilder.model.VU360UserDetail;
import com.softech.ls360.lcms.contentbuilder.service.IContentOwnerRoyaltySettingsService;
import com.softech.ls360.lcms.contentbuilder.service.ICourseService;
import com.softech.ls360.lcms.contentbuilder.service.IOfferService;
import com.softech.ls360.lcms.contentbuilder.service.IPublishingService;
import com.softech.ls360.lcms.contentbuilder.service.VU360UserService;
import com.softech.ls360.lcms.contentbuilder.web.vo.OfferEmailVO;

@Controller
public class OfferController {

	@Autowired
	IOfferService offerService;

	@Autowired
	IPublishingService publishingService;

	@Autowired
	ICourseService courseService;

	@Autowired
	VU360UserService vu360UserService;

	@Autowired
	MailAsyncManager mailAsyncManager;

	@Autowired
	IContentOwnerRoyaltySettingsService royaltySettingsService;

	final String TO_CONTENTOWNER_EMAIL = LCMSProperties.getLCMSProperty("offer.toContentOwner.userName");
	final String ROYALTY_TYPE = LCMSProperties.getLCMSProperty("offer.royaltyaType");
	final String EXISTING_OFFER_IN_PLACE = LCMSProperties.getLCMSProperty("offer.bitExistingOfferInPlace");
	final String OFFER_STATUS = LCMSProperties.getLCMSProperty("offer.offerStatus");
	final String NOTE_TO_DISTRIBUTOR = LCMSProperties.getLCMSProperty("offer.resellerNote");


	@RequestMapping(value = "displayOfferPage", method={RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody JsonResponse  displayOfferPage(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView ("offerOn360Marketplace");
		Offer objOffer = new Offer();
		int cType = Integer.parseInt(request.getParameter("cType"));


		//String idToSearch = request.getParameter("id");
		String idToSearch = request.getParameter("hidCourseId");
		Integer publishedCourseId = 0;

		VU360UserDetail user  = (VU360UserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ContentOwnerRoyaltySettings royatlySettings = royaltySettingsService.getRoyaltySettingsByUserName(user.getUsername());
		int contentOwnerId = Integer.parseInt(String.valueOf(user.getContentOwnerId()));

		CourseCompletionReport courseCmpltnRprt = new CourseCompletionReport ();
		if(idToSearch!=null) {
			courseCmpltnRprt.setCourseId(TypeConvertor.AnyToInteger(idToSearch));
		}
		courseCmpltnRprt.setContentOwnerId(contentOwnerId);
		CourseType courseType = CourseType.getById(cType);
		if(CourseType.CLASSROOM_COURSE.equals(courseType) || CourseType.WEBINAR_COURSE.equals(courseType)){
			CourseDTO objCourse = courseService.getCourseById(courseCmpltnRprt.getCourseId());
			if(WlcmsConstants.PUBLISH_STATUS_PUBLISHED.equals(objCourse.getCourseStatus())){
				courseCmpltnRprt.setPublishStatus(true);
				objOffer.setOriginalCourseId(idToSearch);
				objOffer.setFromContentownerId(contentOwnerId);

				try{
					objOffer = offerService.getOffer(objOffer);
				}catch (Exception ex){
					ex.printStackTrace();
				}
			}
		}else{
			courseCmpltnRprt = publishingService.getCompletionReport(courseCmpltnRprt);
			if(courseCmpltnRprt.isPublishStatus()){
				try {
					publishedCourseId = courseService.getPublishedVersion(idToSearch);
					objOffer.setOriginalCourseId(publishedCourseId.toString());
					objOffer.setFromContentownerId(contentOwnerId);
					objOffer = offerService.getOffer(objOffer);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		if (idToSearch != null) {
			CourseDTO course = courseService.getCourseById(Long
					.parseLong(idToSearch));
			courseCmpltnRprt.setCourseName(course.getName());
			courseCmpltnRprt.setBussinessKey(course.getBussinesskey());
		}

		switch(courseType){
			case ONLINE_COURSE:
				mv.addObject("royaltyPercentage", royatlySettings.getOnlineRoyaltyPercentage());
				break;
			case CLASSROOM_COURSE:
				mv.addObject("royaltyPercentage", royatlySettings.getClassroomRoyaltyPercentage());
				break;
			case WEBINAR_COURSE:
				mv.addObject("royaltyPercentage", royatlySettings.getWebinarRoyaltyPercentage());
				break;
		}
		//mv.addObject("command", courseCmpltnRprt);
		//mv.addObject("offer", objOffer);
		JsonResponse res = new JsonResponse();
		res.setResult(objOffer);
		return res;
	}

	@RequestMapping(value = "cancelOffer", method = RequestMethod.POST)
	public @ResponseBody
	JsonResponse  cancelOffer(HttpServletRequest request, HttpServletResponse response)throws Exception {
		JsonResponse res = new JsonResponse();
		Integer publishedCourseId = 0;
		try {
			String courseId = request.getParameter("hidCourseId");
			VU360UserDetail user  = (VU360UserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			int cType = Integer.parseInt(request.getParameter("cType"));

			if(cType==CourseType.CLASSROOM_COURSE.getId() || cType==CourseType.WEBINAR_COURSE.getId())
				publishedCourseId = Integer.valueOf(courseId);
			else
				publishedCourseId = courseService.getPublishedVersion(courseId);



			Offer objOffer = new Offer();
			objOffer.setFromContentownerId(user.getContentOwnerId());
			objOffer.setOriginalCourseId(publishedCourseId.toString());
			objOffer.setCreatedUserId(user.getAuthorId());
			offerService.cancelOffer(objOffer);
			res.setStatus("SUCCESS");
		}catch(Exception ex){
			ex.printStackTrace();
			res.setStatus("Fail");
		}

		return res;

	}


	@RequestMapping(value = "makeOffer", method = RequestMethod.POST)
	@Deprecated
	public @ResponseBody
	JsonResponse  makeOffer(HttpServletRequest request, HttpServletResponse response)throws Exception {
		//ModelAndView mv = new ModelAndView ("offerOn360Marketplace");
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		JsonResponse res = new JsonResponse();

		Integer publishedCourseId = 0;
		try {
			String courseId = request.getParameter("hidCourseId");
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

			objOffer = offerService.getOffer(objOffer);
			if(StringUtils.isEmpty(objOffer.getOfferStatus())) {
				objOffer.setOfferStatus(OFFER_STATUS);
				offerService.newOffer(objOffer);
			} else {
				objOffer.setOfferStatus(OFFER_STATUS);
				offerService.remakeOffer(objOffer);
			}


			CourseAvailability availability = publishingService.getCourseAvailability(Integer.parseInt(courseId));
			OfferEmailVO oe = new OfferEmailVO();
			oe.setIndustry(availability.getIndustry());
			oe.setCourseName(availability.getCourseName());
			oe.setCourseId(availability.getBusinessKey());
			oe.setAuthorName(user.getUserDisplayName());
			oe.setAuthorEmail(user.getEmailAddress());
			oe.setToContentOwnerEmail(toContentOwner.getEmailAddress());

			sendMailOnReceivedOffer(oe);
			res.setStatus("SUCCESS");
		}catch(Exception ex){
			ex.printStackTrace();
			res.setStatus("Fail");
		}

		return res;

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

}

