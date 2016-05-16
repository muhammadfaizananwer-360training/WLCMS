package com.softech.ls360.lcms.contentbuilder.web.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;






import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.softech.ls360.lcms.contentbuilder.model.ClassInstructor;
import com.softech.ls360.lcms.contentbuilder.service.*;
import com.softech.ls360.lcms.contentbuilder.utils.TypeConvertor;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.softech.ls360.lcms.contentbuilder.model.CourseDTO;
import com.softech.ls360.lcms.contentbuilder.model.SlideAsset;
import com.softech.ls360.lcms.contentbuilder.model.VU360UserDetail;
import com.softech.ls360.lcms.contentbuilder.utils.LCMSProperties;
import com.softech.ls360.lcms.contentbuilder.utils.ObjectWrapper;


@Controller
public class MarketingController {

	private static Logger logger = LoggerFactory
			.getLogger(MarketingController.class);

	@Autowired
	VU360UserService vu360UserService;

	@Autowired
	IMarketing marketingService;

	@Autowired
	ISlideService slideService;
	
	@Autowired
	IAssetService assetService;

	@Autowired
	public IClassInstructorService classInstructorService;

	@RequestMapping(value = "setMarketing", method = RequestMethod.GET)
	public ModelAndView setMarketing(HttpServletRequest request) {

		logger.debug("MarketingController::setMarketing - Begin");

		ModelAndView courseModelView = new ModelAndView("marketing");
		int courseID = (int) Long.parseLong(request.getParameter("id"));
		VU360UserDetail user = (VU360UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		CourseDTO courseDTO = null;
		SlideAsset slideAsset =null;
        List<ClassInstructor> classInstructors = null;
		try {
			courseDTO = marketingService.getCourseByID(courseID);
			slideAsset = marketingService.getSlideAssetForVideoAsset(courseDTO);
			classInstructors = classInstructorService.findByContentOwnerId(user.getContentOwnerId());
			//String duration = marketingService.findDurationbyCourseId(courseID);
			//courseDTO.setDuration(duration);
		} catch (Exception e) {
			logger.error(e.getMessage());

		}
		courseModelView.addObject("command", courseDTO);
		courseModelView.addObject("slideAsset", slideAsset);
        courseModelView.addObject("classInstructors", classInstructors);
		logger.debug("MarketingController::setMarketing - END");

		return courseModelView;
	}

	@RequestMapping(value = "updateMarketing", method = RequestMethod.POST)
	@ResponseBody
	public CourseDTO updateMarketing(HttpServletRequest request) {
		CourseDTO course = new CourseDTO();

		/*VU360UserDetail user = (VU360UserDetail) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();*/
		int courseID = (int) Long.parseLong(request.getParameter("id"));
		String authorBackground = StringUtils.isEmpty(request
				.getParameter("authorBackground")) ? "" : request
				.getParameter("authorBackground");
		String intendedAudience = StringUtils.isEmpty(request
				.getParameter("intendedAudience")) ? "" : request
				.getParameter("intendedAudience");
		String preReqa = StringUtils.isEmpty(request.getParameter("preReqa")) ? ""
				: request.getParameter("preReqa");
		String additionalMaterials = StringUtils.isEmpty(request
				.getParameter("additionalMaterials")) ? "" : request
				.getParameter("additionalMaterials");
		String eocInstructions = StringUtils.isEmpty(request
				.getParameter("eocInstructions")) ? "" : request
				.getParameter("eocInstructions");
		
		String duration = request.getParameter("duration");
        Long classInstructorId = TypeConvertor.AnyToLong(request.getParameter("classInstructorId").equals("undefined")?"-1":request.getParameter("classInstructorId"));


		course.setId(courseID);
        course.setClassInstructorId(classInstructorId);
		course.setAuthorBackground(authorBackground);
		course.setIntendedAudience(intendedAudience);
		course.setCoursePreReq(preReqa);
		course.setAdditionalMaterials(additionalMaterials);
		course.setEndOfCourseInstructions(eocInstructions);
		BigDecimal bd = new BigDecimal(duration.equals("")?"0.00":duration);
		course.setCeus(bd);
		try {
			course = marketingService.updateMarketing(course);
			marketingService.updateCourseDuration(duration,courseID);

		} catch (SQLException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return course;
	}

	@RequestMapping(value = "uploadAuthorImage", method = RequestMethod.POST)
	@ResponseBody
	public CourseDTO uploadAuthorImage(HttpServletRequest request) throws Exception {

		int iCourseId = Integer.parseInt(request.getParameter("id"));
		String authorImgName = request.getParameter("AuthorImageName");
		String authorImgKeywords = request.getParameter("AuthorImageKeywords");
		String authorImgDesc = request.getParameter("AuthorImageDescription");
		String assetUploadType = request.getParameter("assetUploadType");
		String filePath = request.getParameter("filePath");
		String requestId = request.getParameter("requestId");
		String imageWidth = request.getParameter("image_width");
		String imageHeight = request.getParameter("image_height");
		//BigInteger fileSize = new BigInteger(request.getParameter("image_size")); 
		VU360UserDetail user = (VU360UserDetail) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();

		CourseDTO course = new CourseDTO();

		Long asset_id = 0L;
		String assetType = "Image";
		ObjectWrapper<Long> assetVersionWrapper = new ObjectWrapper<Long>();
		asset_id = assetService.addAsset(requestId, user.getUsername(),user.getContentOwnerId(),user.getAuthorId(),authorImgName, filePath, assetType,authorImgKeywords,authorImgDesc,assetVersionWrapper);
		
		
		if (asset_id != 0) {
			course.setId(iCourseId);
			if (assetUploadType.equalsIgnoreCase("AuthorImage"))
				course.setCourseAuthorImageId(asset_id.intValue());
			else
				course.setImageOfCourse(asset_id.intValue());

			try {
				VU360UserDetail principal = (VU360UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				marketingService.UpdateImage(course, assetUploadType,principal.getAuthorId());
				SlideAsset objSlideAsset = new SlideAsset();
				objSlideAsset.setId(asset_id);
				objSlideAsset.setWidth(imageWidth);
				objSlideAsset.setHeight(imageHeight);
				slideService.updateAssetAttribtes(objSlideAsset);
			} catch (SQLException e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		}
		return course;

	}

	@RequestMapping(value = "getMarketingAssetSearch", method = RequestMethod.GET)
	public @ResponseBody
	List<SlideAsset> getSlideAssetSearch(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		VU360UserDetail user = (VU360UserDetail) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();

		List<SlideAsset> lstAssets = slideService.getSlideAssetSearch(
				request.getParameter("assetSearchTerm"),
				(int) user.getContentOwnerId(), 1);
		/*
		for (int i = 0; i < lstAssets.size(); i++) {
			String locationPath = LCMSProperties
					.getLCMSProperty("code.lcms.assets.URL");
			SlideAsset objAsset = (SlideAsset) lstAssets.get(i);
			objAsset.setStreamingServerPath(locationPath
					+ objAsset.getLocation());
			objAsset.setLocation(locationPath + objAsset.getLocation());
		}*/
		return lstAssets;
	}

	@RequestMapping(value = "acceptMarketingAssetSearch", method = RequestMethod.POST)
	public @ResponseBody
	CourseDTO acceptMarketingAssetSearch(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		CourseDTO course = new CourseDTO();
		int courseId = Integer.parseInt(request.getParameter("id"));
		int asset_Id = Integer.parseInt(request.getParameter("asset_id"));
		String assetType = request.getParameter("assetType");

		String systemAssetType = LCMSProperties
				.getLCMSProperty("marketing.authorImage");

		course.setId(courseId);
		if (assetType.equalsIgnoreCase(systemAssetType))
			course.setCourseAuthorImageId(asset_Id);
		else {
			if (asset_Id > 0)
				course.setImageOfCourse(asset_Id);
			else
				course.setImageOfCourse(0);
		}
		VU360UserDetail principal = (VU360UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		marketingService.UpdateImage(course, assetType,principal.getAuthorId());

		return course;
	}
	
	
	@RequestMapping(value = "acceptMarketingAssetVideoAdd", method = RequestMethod.POST)
	public @ResponseBody
	boolean acceptMarketingAssetVideoAdd(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		VU360UserDetail user = (VU360UserDetail) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		long assetId = 0;
		
		String filePath = request.getParameter("filePath");
		String requestId = request.getParameter("requestId");
		String audioAssetDescription =  request.getParameter("audioAssetDescription");
		String videoAssetName = request.getParameter("videoAssetName");
		String audioAssetKeywords = request.getParameter("audioAssetKeywords");
		String assetType = "VSC";
		ObjectWrapper<Long> assetVersionWrapper = new ObjectWrapper<Long>();
		assetId = assetService.addAsset(requestId, user.getUsername(),user.getContentOwnerId(),user.getAuthorId(),videoAssetName, filePath, assetType,audioAssetKeywords,audioAssetDescription,assetVersionWrapper);

		
		CourseDTO course = new CourseDTO();
		long courseId = Long.parseLong(request.getParameter("id"));
		
		course.setId(courseId);
		if (assetId > 0)
				course.setCourseVideoAssetId(assetId);
		else
			course.setCourseVideoAssetId(TypeConvertor.AnyToLong(0));
	
		
		course.setLastUpdateUser(user.getAuthorId());
		
		
		return marketingService.UpdateVideo(course);

		//return course;
	}

	
	@RequestMapping(value = "acceptMarketingAssetVideoSearch", method = RequestMethod.POST)
	public @ResponseBody
	boolean acceptMarketingAssetVideoSearch(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		
		
		CourseDTO course = new CourseDTO();
		long courseId = Long.parseLong(request.getParameter("id"));
		long asset_Id = Long.parseLong(request.getParameter("asset_id"));
		
		course.setId(courseId);
		if (asset_Id > 0)
				course.setCourseVideoAssetId(asset_Id);
		else
			course.setCourseVideoAssetId(TypeConvertor.AnyToLong(0));
		VU360UserDetail user = (VU360UserDetail) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		
		course.setLastUpdateUser(user.getAuthorId());
		
		
		return marketingService.UpdateVideo(course);

		//return course;
	}
	
	
	
	@RequestMapping(value = "listMarketingAsset", method = RequestMethod.GET)
	public @ResponseBody
	SlideAsset listMarketingAsset(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		CourseDTO course = new CourseDTO();
		int courseId = Integer.parseInt(request.getParameter("id"));
		String assetType = request.getParameter("assetType");

		course.setId(courseId);

		SlideAsset slideAsset = marketingService.getAssetImage(course,
				assetType);

		return slideAsset;
	}



@RequestMapping(value = "listMarketingVideoAsset", method = RequestMethod.GET)
public @ResponseBody
SlideAsset listMarketingVideoAsset(HttpServletRequest request,
		HttpServletResponse response) throws Exception {

	CourseDTO course = new CourseDTO();
	int courseId = Integer.parseInt(request.getParameter("id"));
	
	course.setId(courseId);

	SlideAsset slideAsset = marketingService.getSlideAssetForVideoAsset(course);

	return slideAsset;
}

}
