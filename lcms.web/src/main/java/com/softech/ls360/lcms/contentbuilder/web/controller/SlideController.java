package com.softech.ls360.lcms.contentbuilder.web.controller;

import com.softech.ls360.lcms.contentbuilder.model.*;
import com.softech.ls360.lcms.contentbuilder.service.IAssetService;
import com.softech.ls360.lcms.contentbuilder.service.ICourseService;
import com.softech.ls360.lcms.contentbuilder.service.ISlideService;
import com.softech.ls360.lcms.contentbuilder.service.VU360UserService;
import com.softech.ls360.lcms.contentbuilder.utils.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Controller
public class SlideController {
	private static Logger logger = LoggerFactory
			.getLogger(CourseController.class);

	@Autowired
	ISlideService slideService;
	@Autowired
	IAssetService assetService;

	@Autowired
	VU360UserService vu360UserService;

	@Autowired
	ICourseService courseService;

    //DateFormat completeDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//unused field

	// Function use to get Content Object and dispaly in Edit Mode
	@RequestMapping(value = "addSlide", method = RequestMethod.POST)
	public @ResponseBody
	Slide addSlide(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		logger.info("SlideController:: addSlide - Begin");

		String varSlideId = request.getParameter("templateID");
		Slide objSlide = new Slide();

		VU360UserDetail user = (VU360UserDetail) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();

		objSlide.setCourse_ID(Integer.parseInt(request.getParameter("id")));
		objSlide.setContentObject_id(Integer.parseInt(request
				.getParameter("contentObject_id")));
		objSlide.setName(request.getParameter("name"));
		objSlide.setDisplayStandardTF(Boolean.valueOf(request.getParameter("displayRatioStandard")));
		objSlide.setDisplayWideScreenTF(Boolean.valueOf(request.getParameter("displayRatioWidescreen")));
		try {

			objSlide.setTemplateID(Long.parseLong(varSlideId));

		} catch (NumberFormatException e) {
			objSlide.setTemplateID(0);
			objSlide.setTemplateName(varSlideId);
		}

		// check for MC Scenario
		if (!StringUtils.isEmpty(request.getParameter("duration")))
			objSlide.setDuration(Long.parseLong(StringUtil
					.ifNullReturnZero(request.getParameter("duration"))));
		else
			objSlide.setDuration(0);

		objSlide.setOrientationKey("Left");
		objSlide.setAsset_ID(0);
		objSlide.setContentOwner_ID((int) user.getContentOwnerId());
		objSlide.setCreateUserId((int) user.getAuthorId());
		objSlide.setSceneGUID(UUID.randomUUID().toString().replaceAll("-", ""));
		objSlide.setLastUpdateUser(user.getAuthorId());

		slideService.addSlide(objSlide);

		logger.info("SlideController:: addSlide - End");

		String filePath = request.getParameter("filePath");
		if(!StringUtils.isEmpty(filePath)) {
			uploadVisualAsset(request, request.getParameter("id")
					, FilenameUtils.getBaseName(filePath)
					, String.valueOf(objSlide.getId())
					, new String[] {"ATTACH_ASSET_WITH_SLIDE","ATTACH_ASSET_WITH_ASSETGROUP"}

			);
		}

		return objSlide;
	}

	@RequestMapping(value = "getSlideById", method = RequestMethod.POST)
	public @ResponseBody
	Slide getSlideById(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String varSlideId = request.getParameter("varSlideId");
		if (varSlideId != null) {
			Slide lstSlide = slideService.getSlide(Long.parseLong(varSlideId));
			return lstSlide;
		} else
			return null;

	}

	@RequestMapping(value = "getSlide_MC_SCENE_XML_ById", method = RequestMethod.POST)
	public @ResponseBody
	String getSlide_MC_SCENE_XML_ById(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String varSlideId = request.getParameter("varSlideId");
		String MC_SCENE_XML = slideService.getSlide_MC_SCENE_XML_ById(Long
				.parseLong(varSlideId));

		return MC_SCENE_XML;

	}

	// updating Side
	@RequestMapping(value = "updateSlide", method = RequestMethod.POST)
	public @ResponseBody
	JsonResponse updateSlide(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JsonResponse res = new JsonResponse();

		String varSlideId = request.getParameter("varSlideId");
		String varTitle = request.getParameter("varTitle");
		String varDuration = request.getParameter("varDuration");
		String displayRatioStandard = request.getParameter("displayRatioStandard");
		String displayRatioWidescreen = request.getParameter("displayRatioWidescreen");
		VU360UserDetail principal = (VU360UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Slide dto = new Slide();
		dto.setId(Integer.parseInt(varSlideId));
		dto.setName(varTitle);
		dto.setDuration(Long.parseLong(varDuration));
		dto.setDisplayStandardTF(Boolean.valueOf(displayRatioStandard));
		dto.setDisplayWideScreenTF(Boolean.valueOf(displayRatioWidescreen));
		dto.setLastUpdateUser(principal.getAuthorId());
		slideService.updateSlide(dto);

		res.setStatus("SUCCESS");
		return res;
	}

	@RequestMapping(value = "updateSlideEmbedCode", method = RequestMethod.POST)
	public @ResponseBody
	JsonResponse updateSlideEmbedCode(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JsonResponse res = new JsonResponse();

		if(request.getParameter("varSlideId")==null || request.getParameter("varSlideId").equals("") ){
			res.setStatus("ERROR");
			return res;
		}

		long varSlideId = Long.parseLong(request.getParameter("varSlideId").toString());
		String embedCode = request.getParameter("embedCode");
		Boolean isEmbedCodeValueUpdate = Boolean.valueOf(request.getParameter("isEmbedCodeValUpdate"));
		slideService.updateSlideEmbedCodeandEmbedBit(varSlideId, embedCode, isEmbedCodeValueUpdate);

		res.setStatus("SUCCESS");
		return res;
	}

	@RequestMapping(value = "updateSlideEmbedCodeBit", method = RequestMethod.POST)
	public @ResponseBody
	JsonResponse updateSlideEmbedCodeBit(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JsonResponse res = new JsonResponse();

		if(request.getParameter("varSlideId")==null || request.getParameter("varSlideId").equals("") ){
			res.setStatus("ERROR");
			return res;
		}

		long varSlideId = Long.parseLong(request.getParameter("varSlideId").toString());
		Boolean isEmbedCode = Boolean.valueOf(request.getParameter("isEmbedCode"));
		slideService.updateSlideEmbedBit(varSlideId, isEmbedCode);

		res.setStatus("SUCCESS");
		return res;
	}

	// updating Selected Slide Template Id
	@RequestMapping(value = "updateSelectedSlideTemplate", method = RequestMethod.POST)
	public @ResponseBody
	JsonResponse updateSelectedSlideTemplate(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JsonResponse res = new JsonResponse();
		String varSlideId = request.getParameter("varSlideId");
		String varSelectedSlideTemplateId = request
				.getParameter("varSelectedSlideTemplateId");
		long courseId = Long.parseLong(((request.getParameter("courseId") == null || request.getParameter("courseId").equals("")) ? "0" : request.getParameter("courseId")));
		if(varSelectedSlideTemplateId==null || varSelectedSlideTemplateId.equals("")){
			res.setStatus("FAIL");
			return res;
		}
		Slide dto = new Slide();
		dto.setId(Integer.parseInt(varSlideId));

		dto.setTemplateID(Long.parseLong(varSelectedSlideTemplateId));

		slideService.updateSelectedSlideTemplate(dto);
		updateCourseModifiedDateandLastUpdateUser(courseId);

		res.setStatus("SUCCESS");
		return res;
	}

	// updating Side
	@RequestMapping(value = "updateSlideText", method = RequestMethod.POST)
	public @ResponseBody
	JsonResponse updateSlideText(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JsonResponse res = new JsonResponse();

		VU360UserDetail user = (VU360UserDetail) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		String varSlideId = request.getParameter("varSlideId");
		String varTitle = request.getParameter("varTitle");
		String varOrientationKey = request.getParameter("varOrientationKey");
		VU360UserDetail principal = (VU360UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (varOrientationKey != null)
			varOrientationKey = "$" + varOrientationKey;

		SlideAsset dto = new SlideAsset();
		dto.setId(Integer.parseInt(varSlideId));
		dto.setContent(varTitle);
		dto.setOrientationkey(varOrientationKey);
		dto.setCreateUser_id((int) user.getAuthorId());
		dto.setLastUpdateUser(principal.getContentOwnerId());
		slideService.updateSlideText(dto);

		res.setStatus("SUCCESS");
		return res;
	}

	@RequestMapping(value = "updateSlide_MC_SCENE_XML", method = RequestMethod.POST)
	public @ResponseBody
	JsonResponse updateSlide_MC_SCENE_XML(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JsonResponse res = new JsonResponse();

		String varSlideId = request.getParameter("varSlideId");
		String MC_SCENE_XML = request.getParameter("MC_SCENE_XML");
		SlideAsset objSlideAsset = new SlideAsset();
		objSlideAsset.setScene_id(Integer.parseInt(varSlideId));
		objSlideAsset.setMC_SCENE_XML(MC_SCENE_XML);

		slideService.updateSlideMC_SCENE_XML(objSlideAsset);

		res.setStatus("SUCCESS");
		return res;
	}

	@RequestMapping(value = "updateSlideCloseCaption", method = RequestMethod.POST)
	public @ResponseBody
	JsonResponse updateSlideCloseCaption(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JsonResponse res = new JsonResponse();

		String varSlideId = request.getParameter("varSlideId");
		String varTitle = request.getParameter("varTitle");
		String varOrientationKey = request.getParameter("varOrientationKey");
		VU360UserDetail principal = (VU360UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		VU360UserDetail user = (VU360UserDetail) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();

		if (varOrientationKey != null)
			varOrientationKey = "$" + varOrientationKey;

		SlideAsset dto = new SlideAsset();
		dto.setId(Integer.parseInt(varSlideId));
		dto.setContent(varTitle);
		dto.setOrientationkey(varOrientationKey);
		dto.setCreateUser_id((int) user.getAuthorId());
		dto.setLastUpdateUser(principal.getContentOwnerId());
		slideService.updateSlideCloseCaption(dto);

		res.setStatus("SUCCESS");
		return res;
	}

	// Function use to get Slides Information against Slide ID
	@RequestMapping(value = "getSlideTextAgainstSlideId", method = RequestMethod.POST)
	public @ResponseBody
	List<SlideAsset> getSlideTextAgainstSlideId(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String varSlideId = request.getParameter("varSlideId");
		if (varSlideId != null) {
			List<SlideAsset> lstSlide = slideService
					.getSlideTextAgainstSlideId(Long.parseLong(varSlideId));
			return lstSlide;
		} else
			return null;

	}

	// Function use to get Slides Information against Slide ID
	@RequestMapping(value = "getSlideCloseCaptionAgainstSlideId", method = RequestMethod.POST)
	public @ResponseBody
	List<SlideAsset> getSlideCloseCaptionAgainstSlideId(
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String varSlideId = request.getParameter("varSlideId");
		if (varSlideId != null) {
			List<SlideAsset> lstSlide = slideService
					.getSlideCloseCaptionAgainstSlideId(Long
                            .parseLong(varSlideId));
			return lstSlide;
		} else
			return null;

	}

	// Function use to get Slides Information against Slide ID
	@RequestMapping(value = "getAssetBySlide", method = RequestMethod.POST)
	public @ResponseBody
	List<SlideAsset> getAssetBySlide(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String varSlideId = request.getParameter("varSlideId");
		String varAssetType = request.getParameter("varAssetType");
		if (varSlideId != null) {
			List<SlideAsset> lstSlide = slideService.getVisualAssetBySlideId(
                    Long.parseLong(varSlideId), varAssetType);
			return lstSlide;
		} else
			return null;

	}

	@RequestMapping(value = "detachSlideAsset", method = RequestMethod.POST)
	public @ResponseBody
	SlideAsset detachSlideAsset(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		int iSlideId = !StringUtils.isEmpty(request.getParameter("varSlideId")) ? Integer
				.parseInt(request.getParameter("varSlideId")) : 0;
		int iAssetVersionId = !StringUtils.isEmpty(request
				.getParameter("assetVersion_id")) ? Integer.parseInt(request
				.getParameter("assetVersion_id")) : 0;
		 //in case of detaching video from slide, reset duration of slide too
		if("3".equals(request.getParameter("assetType")) && iSlideId != 0) {
			Slide slide = slideService.getSlide((long) iSlideId);
			slide.setDuration(0);
			VU360UserDetail user = (VU360UserDetail) SecurityContextHolder
					.getContext().getAuthentication().getPrincipal();
			slide.setLastUpdateUser(user.getAuthorId());
			slideService.updateSlide(slide);
		}

		SlideAsset slideAsset = new SlideAsset();
		String courseId = request.getParameter("course_id");
		slideAsset.setScene_id(iSlideId);
		slideAsset.setAssetversion_id(iAssetVersionId);
		slideService.detachAsset(slideAsset);
		updateCourseModifiedDateandLastUpdateUser(Long.parseLong(((courseId == null || courseId.equals("")) ? "0" : courseId)));
		return slideAsset;
	}

	@RequestMapping(value = "getSlideAssetSearch", method = RequestMethod.GET)
	public @ResponseBody
	List<SlideAsset> getSlideAssetSearch(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		VU360UserDetail user = (VU360UserDetail) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();

		List<SlideAsset> lstAssets = null;

		if (!StringUtils.isEmpty(request.getParameter("audioSearch"))){
			//bAudioSeach = Boolean.parseBoolean(request.getParameter("audioSearch"));
		    lstAssets = slideService.getSlideAssetSearch(request.getParameter("assetSearchTerm"), (int) user.getContentOwnerId(), 2);
		}else if (!StringUtils.isEmpty(request.getParameter("VAAssetsSearch"))){
			//bVASearch = Boolean.parseBoolean(request.getParameter("VAAssetsSearch"));
			lstAssets = slideService.getSlideAssetSearch(request.getParameter("assetSearchTerm"), (int) user.getContentOwnerId(), 3);
		}else if (!StringUtils.isEmpty(request.getParameter("visualSearch"))){
			//bVisualSearch = Boolean.parseBoolean(request.getParameter("visualSearch"));
			lstAssets = slideService.getSlideAssetSearch(request.getParameter("assetSearchTerm"), (int) user.getContentOwnerId(), 1);
		}else if(!StringUtils.isEmpty(request.getParameter("SupportMaterialSearch"))){
			lstAssets = slideService.getSlideAssetSearch(request.getParameter("assetSearchTerm"), (int) user.getContentOwnerId(), 4);
		}

		return lstAssets;
	}

	@RequestMapping(value = "acceptAssetSearch", method = RequestMethod.POST)
	public @ResponseBody
	SlideAsset acceptVisualAssetBySlide(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		SlideAsset slideAsset = new SlideAsset();

		VU360UserDetail user = (VU360UserDetail) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();

		slideAsset.setScene_id(Integer.parseInt(request
				.getParameter("scene_id")));
		slideAsset.setId(Integer.parseInt(request.getParameter("asset_id")));
		slideAsset.setAssetversion_id(Integer.parseInt(request
				.getParameter("assetVersion_id")));
		slideAsset.setOrientationkey("$VisualTop");

		slideAsset.setCreateUser_id((int) user.getAuthorId());

		slideService.insertSelectedAsset(slideAsset);
		String course_id = request.getParameter("course_id");
		updateCourseModifiedDateandLastUpdateUser(Long.parseLong(((course_id == null || course_id.equals("")) ? "0" : course_id)));

		return null;
	}

	@RequestMapping(value = "previewSlide", method = RequestMethod.POST)
	public @ResponseBody
	JsonResponse previewSlide(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JsonResponse res = new JsonResponse();

		String varCourseId = request.getParameter("id");
		String varSceneId = request.getParameter("varSceneId");
		String previewURL = LCMSProperties
				.getLCMSProperty("lcms.courseplayer.previewer.URL")
				+ "?COURSEID="
				+ varCourseId
				+ "&VARIANT=En-US&BRANDCODE=DEFAULT&PREVIEW=true&SESSION="
				+ UUID.randomUUID().toString() + "&SCENEID=" + varSceneId;

		res.setStatus(previewURL);
		return res;
	}

	@RequestMapping(value = "deleteSlide", method = RequestMethod.POST)
	public @ResponseBody
	JsonResponse deleteSlide(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JsonResponse res = new JsonResponse();

		String varSlideId = request.getParameter("varSceneId");
		String varCourseId = request.getParameter("varCourseId");
		slideService.deleteSlide(varSlideId, varCourseId);

		res.setStatus("SUCCESS");
		return res;
	}

	@RequestMapping(value = "uploadAudioAsset", method = RequestMethod.POST, produces = "text/html; charset=utf-8")
	public @ResponseBody
	String uploadAudioAsset(MultipartHttpServletRequest request) {
		long asset_id = 0;

		String courseId = request.getParameter("courseIdForAudioAsset");
		String audioAssetName = request.getParameter("audioAssetName");
		String audioAssetKeywords = request.getParameter("audioAssetKeywords");
		String audioAssetDescription = request
				.getParameter("audioAssetDescription");
		String slideIdForAsset = request.getParameter("slideIdForAudioAsset");
		String assetOptions[] = request.getParameterValues("assetOptions");
		String isSupportMaterialForm = request.getParameter("isSupportMaterialForm");
		String lessonIdSupportMaterial = request.getParameter("lessonIdSupportMaterial");
		String filePath = request.getParameter("filePath");
		String requestId = request.getParameter("requestId");
		String duration = request.getParameter("duration");
		VU360UserDetail user = (VU360UserDetail) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();

			try {

				//calculate asset type
				String ext = FilenameUtils.getExtension(filePath);
				String assetType = "";

				if (ext.equals("mp3") || ext.equals("wma"))
					assetType = "Audio Clip";
				else if(ext.equals("doc") || ext.equals("docx") || ext.equals("pdf")
						|| ext.equals("ppt") || ext.equals("pptx")
							|| ext.equals("xls") || ext.equals("xlsx"))
					assetType = "Document";
				else if(ext.equals("png") || ext.equals("jpg") || ext.equals("gif"))
					assetType = "Image";

				ObjectWrapper<Long> assetVersionWrapper = new ObjectWrapper<Long>();
				asset_id = assetService.addAsset(requestId, user.getUsername(),user.getContentOwnerId(),user.getAuthorId(),audioAssetName, filePath, assetType,audioAssetKeywords,audioAssetDescription,assetVersionWrapper);



				if (assetType.equalsIgnoreCase("Audio Clip")) {
					SlideAsset objSlideAsset = new SlideAsset();
					objSlideAsset.setId(asset_id);
					if(!StringUtils.isEmpty(duration)) {
						objSlideAsset.setDuration((int) Float.parseFloat(duration));
					}
					slideService.updateAssetAttribtes(objSlideAsset);
					objSlideAsset = null;
				}

				if (assetOptions != null && assetOptions.length > 0) {
					for (String assetOption : assetOptions) {
						if (assetOption
								.equalsIgnoreCase("ATTACH_ASSET_WITH_SLIDE")) {

									SlideAsset slideAssetNew = new SlideAsset();

									slideAssetNew.setScene_id(Integer
											.parseInt(slideIdForAsset));
									slideAssetNew.setId(asset_id);
									slideAssetNew.setAssetversion_id(assetVersionWrapper.getValue().intValue());
									slideAssetNew.setOrientationkey("$Audio");
									slideAssetNew.setCreateUser_id((int) user
											.getAuthorId());
									slideService
											.insertSelectedAsset(slideAssetNew);
						} else if (assetOption
								.equalsIgnoreCase("ATTACH_ASSET_WITH_ASSETGROUP")) {
							AssetGroup objAG = assetService.getAssetGroupByCourseId(Long.parseLong(courseId));
							assetService.insertAssetAssetGroupRelationship(asset_id, objAG.getID(), user.getAuthorId());
						}
					}
				}

				//If data submitted by Support Material Form;
				if(! StringUtils.isEmpty(isSupportMaterialForm) ){
					int sequanceMax = assetService.getSpprtMtrSequanceMax(Integer.parseInt(lessonIdSupportMaterial), Integer.parseInt(courseId));
					//Calendar cal = Calendar.getInstance();
					SupportMaterial smVO =new SupportMaterial();
					smVO.setContentObjectId(Integer.valueOf(lessonIdSupportMaterial));
					smVO.setCourseId(Integer.valueOf(courseId));
					smVO.setType(assetType);
					smVO.setCreatedUserId(user.getAuthorId());
					//smVO.setCreatedDate(cal.getTime());
					smVO.setAssetId(asset_id );
					smVO.setSequence( ++sequanceMax );
					assetService.insertSupportMaterial(smVO);
					assetService.setSpprtMtrDisplayOrderOnAddDlt(Integer.parseInt(courseId), Integer.parseInt(lessonIdSupportMaterial), 1);
				}
				updateCourseModifiedDateandLastUpdateUser(Long.parseLong(((courseId == null || courseId.equals("")) ? "0" : courseId)));
				return LCMSProperties.getLCMSProperty("code.lcms.assets.URL")
						+ "/" + assetService.getAssetLocation((int) asset_id);
			} catch (Exception e) {

				e.printStackTrace();
			}



		return "courseModelView";
	}


	@RequestMapping(value = "getSpprtMtrIdMax", method = RequestMethod.POST)
	public @ResponseBody
	JsonResponse getSpprtMtrIdMax(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JsonResponse res = new JsonResponse();

		String LessonId = request.getParameter("LessonId");
		int sequanceMax = assetService.getSpprtMtrIdMax(Integer.parseInt(LessonId));
		res.setStatus(String.valueOf(sequanceMax));
		return res;
	}



	@RequestMapping(value = "deleteSupportMaterial", method = RequestMethod.POST)
	public @ResponseBody
	JsonResponse deleteSupportMaterial(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JsonResponse res = new JsonResponse();

		String varSupMaterialId = request.getParameter("varSupMaterialId");
		String courseId = request.getParameter("courseId");
		assetService.deleteSupportMaterial(varSupMaterialId);
		assetService.setSpprtMtrDisplayOrderOnAddDlt(TypeConvertor.AnyToInteger(courseId), TypeConvertor.AnyToInteger(varSupMaterialId), -1);
		updateCourseModifiedDateandLastUpdateUser(Long.parseLong(((courseId == null || courseId.equals("")) ? "0" : courseId)));
		res.setStatus("SUCCESS");
		return res;
	}

	private void updateCourseModifiedDateandLastUpdateUser(long courseId){
		VU360UserDetail principal = (VU360UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		courseService.updateCourseLastUpdateUserandModifiedDate(principal.getAuthorId(), courseId);
	}


	@RequestMapping(value = "attachSupportMaterial", method = RequestMethod.POST)
	public @ResponseBody
	JsonResponse attachSupportMaterial(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JsonResponse res = new JsonResponse();

		String asset_type = request.getParameter("asset_type");
		String asset_id = request.getParameter("asset_id");
		String lesson_Id = request.getParameter("lesson_Id");
		String course_id = request.getParameter("course_id");
		VU360UserDetail user = (VU360UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		int sequanceMax = assetService.getSpprtMtrSequanceMax(TypeConvertor.AnyToInteger(lesson_Id), TypeConvertor.AnyToInteger(course_id));

		SupportMaterial smVO =new SupportMaterial();
		smVO.setContentObjectId(TypeConvertor.AnyToInteger(lesson_Id));
		smVO.setCourseId(Integer.valueOf(course_id));
		smVO.setType(asset_type);
		smVO.setCreatedUserId(user.getAuthorId());
		//smVO.setCreatedDate(cal.getTime());
		smVO.setAssetId( Long.valueOf(asset_id ));
		smVO.setSequence( ++sequanceMax );
		assetService.insertSupportMaterial(smVO);
		assetService.setSpprtMtrDisplayOrderOnAddDlt(Integer.parseInt(course_id), Integer.parseInt(lesson_Id), 1);
		updateCourseModifiedDateandLastUpdateUser(Long.parseLong(((course_id == null || course_id.equals("")) ? "0" : course_id)));
		res.setStatus("SUCCESS");
		return res;
	}

	@RequestMapping(value = "getSupportMaterialList", method = RequestMethod.POST)
	public @ResponseBody
	List<SupportMaterial> getSupportMaterialList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String varCourseId = request.getParameter("varCourseId");
		List<SupportMaterial> lstCourseMaterial = assetService.getSupportMaterialDetailListByContObject(Integer.parseInt(varCourseId));
		return lstCourseMaterial;
	}

	@RequestMapping(value = "getSupportMaterialDetail", method = RequestMethod.POST)
	public @ResponseBody
	SupportMaterial getSupportMaterialDetail(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String locationPath = LCMSProperties.getLCMSProperty("code.lcms.assets.URL");
		String varLessonId = request.getParameter("varLessonId");
		SupportMaterial supportMaterial =  assetService.getSupportMaterialDetail(Integer.parseInt(varLessonId));
		supportMaterial.setLocation(locationPath + supportMaterial.getLocation());
		System.out.println(locationPath + supportMaterial.getLocation());
		return supportMaterial;
	}

	@RequestMapping(value = "uploadVisualAsset", method = RequestMethod.POST, produces = "text/html; charset=utf-8")
	public @ResponseBody
	String uploadVisualAsset(MultipartHttpServletRequest request) {
		String videoAssetName = "";
		if(request.getParameter("visualAssetName")!=null && !request.getParameter("visualAssetName").equals(""))
			videoAssetName = request.getParameter("visualAssetName");//request.getParameter("videoAssetName");
		else
			if(request.getParameter("videoAssetName")!=null && !request.getParameter("videoAssetName").equals(""))
				videoAssetName = request.getParameter("videoAssetName");//request.getParameter("videoAssetName");


		String courseId = "";
		String slideIdForAsset = "";
		if (request.getParameter("cboAssetTypeVisual").equals("mp4")) {
			courseId = request.getParameter("courseIdForAsset_video");
			slideIdForAsset = request.getParameter("slideIdForAsset_video");
		} else {
			courseId = request.getParameter("courseIdForAsset");
			slideIdForAsset = request.getParameter("slideIdForAsset");
		}

		return uploadVisualAsset(request, courseId, videoAssetName, slideIdForAsset
				, request.getParameterValues("visualAssetOptions")
		);


	}

	String uploadVisualAsset(HttpServletRequest request,String courseId,String videoAssetName, String slideIdForAsset, String assetOptions[]) {
		long asset_id = 0;


		String audioAssetKeywords = request.getParameter("visualAssetKeywords");
		String audioAssetDescription = (String) ObjectUtils.defaultIfNull(request
				.getParameter("visualAssetDescription"),"");

		String cboAssetTypeVisual = request.getParameter("cboAssetTypeVisual");
		String filePath = request.getParameter("filePath");

		String imageWidth = request.getParameter("imageWidth");
		String imageHeight = request.getParameter("imageHeight");
		String duration = request.getParameter("duration");
		String requestId = request.getParameter("requestId");


		VU360UserDetail user = (VU360UserDetail) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();




			String assetType = "";

			try {

				//calculate asset type
				String ext = FilenameUtils.getExtension(filePath);


				if (cboAssetTypeVisual.equals("mp4")) {
					assetType = "VSC";
				} else {
					if (ext.equals("bmp") || ext.equals("gif")

							|| ext.equals("jpg") || ext.equals("png")
							|| ext.equals("tif")) {
											assetType = "Image";
					} else if (ext.equals("mp4") || ext.equals("flv")
							|| ext.equals("swf") || ext.equals("wmv")) {
						assetType = "Movie Clip";
					} else {
						return null;
					}
				}

				ObjectWrapper<Long> assetVersionWrapper = new ObjectWrapper<Long>();
				asset_id = assetService.addAsset(requestId, user.getUsername(),user.getContentOwnerId(),user.getAuthorId(),videoAssetName, filePath, assetType,audioAssetKeywords,audioAssetDescription,assetVersionWrapper);

				// ----Start-------- WLCMS-294
				// ------------------------------------------------------------------
				if (assetType.equalsIgnoreCase("Image")
						|| assetType.equalsIgnoreCase("Movie Clip")
						|| assetType.equalsIgnoreCase("VSC")) {
					SlideAsset objSlideAsset = new SlideAsset();
					objSlideAsset.setId(asset_id);
					objSlideAsset.setWidth(imageWidth);
					objSlideAsset.setHeight(imageHeight);
					if(!StringUtils.isEmpty(duration)) {
						objSlideAsset.setDuration((int) Float.parseFloat(duration));
					}
					slideService.updateAssetAttribtes(objSlideAsset);
					objSlideAsset = null;

				}
				//update duration of slide too if video duration found
				if(assetType.equalsIgnoreCase("VSC") && !StringUtils.isEmpty(duration)) {
					Slide slide = slideService.getSlide(Long.parseLong(slideIdForAsset));
					slide.setDuration(Integer.parseInt(duration));
					slide.setLastUpdateUser(user.getAuthorId());
					slideService.updateSlide(slide);
				}
				// -----End-------- WLCMS-294
				// ------------------------------------------------------------------

				if (assetOptions != null && assetOptions.length > 0) {
					for (String assetOption : assetOptions) {
						if (assetOption
								.equalsIgnoreCase("ATTACH_ASSET_WITH_SLIDE")) {

									SlideAsset slideAssetNew = new SlideAsset();

									slideAssetNew.setScene_id(Integer
											.parseInt(slideIdForAsset));
									slideAssetNew.setId(asset_id);
									slideAssetNew.setAssetversion_id(assetVersionWrapper.getValue().intValue());
									slideAssetNew
											.setOrientationkey("$VisualTop");
									slideAssetNew.setCreateUser_id((int) user
											.getAuthorId());
									slideService
											.insertSelectedAsset(slideAssetNew);

						} else if (assetOption
								.equalsIgnoreCase("ATTACH_ASSET_WITH_ASSETGROUP")) {


							AssetGroup objAG = assetService
									.getAssetGroupByCourseId(Long
											.parseLong(courseId));
							assetService.insertAssetAssetGroupRelationship(
									asset_id, objAG.getID(),
									user.getAuthorId());
						}
					}
				}
				updateCourseModifiedDateandLastUpdateUser(Long.parseLong(((courseId == null || courseId.equals("")) ? "0" : courseId)));
				return LCMSProperties.getLCMSProperty("code.lcms.assets.URL")
						+ "/" + assetService.getAssetLocation((int) asset_id);
			} catch (Exception e) {
				e.printStackTrace();
			}
		return "courseModelView";
	}

	@RequestMapping(value = "maxfilesize", method = RequestMethod.GET)
	public @ResponseBody
	String maxfilesize(HttpServletRequest request) {
		return LCMSProperties.getLCMSProperty("import.file.PPT.size");
	}

	@RequestMapping(value = "getFileSizeLimit", method = RequestMethod.GET)
	public @ResponseBody
	String getFileSizeLimit(HttpServletRequest request) {
		String fileLimit = request.getParameter("supportMaterialFileLimit");
		return LCMSProperties.getLCMSProperty(fileLimit);
	}

	@RequestMapping(value = "uploadPPTFile", method = RequestMethod.POST , produces = "text/html; charset=utf-8")
	public @ResponseBody
	String uploadPPTFile(MultipartHttpServletRequest request) throws Exception {

		String filePath = request.getParameter("filePath");
		String requestId = request.getParameter("requestId");
		int slides = Integer.parseInt(request.getParameter("slides"));
		Integer course_id = StringUtils.isEmpty( request.getParameter("id")) ? 0 : Integer.parseInt(request.getParameter("id")) ;
		Integer content_id = StringUtils.isEmpty( request.getParameter("content_id")) ? 0 : Integer.parseInt(request.getParameter("content_id")) ;
		VU360UserDetail user = (VU360UserDetail) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();

		Long asset_id = 0L;
		AssetGroup objAG  = null;
		try {
			 objAG = assetService.getAssetGroupByCourseId ( Long.parseLong(course_id.toString()) );
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

			for (int i=0; i<slides; i++) {
				String slideName = " Slide " + (i+1);
				String slidePath = filePath + "/" + (i+1) + ".jpg";
				ObjectWrapper<Long> assetVersionWrapper = new ObjectWrapper<Long>();
				try {
					asset_id = assetService.addAsset(requestId, user.getUsername(),user.getContentOwnerId(),user.getAuthorId(),slideName, slidePath, "Image",slideName,slideName,assetVersionWrapper);
				} catch (FileNotFoundException ex) {
					logger.info( i + "Slide(s) moved to parmanet location, folder Path:" + filePath);
					break;
				}

				Integer template_id = 0;
				try {
					template_id = slideService.getTemplateIDForPPTSlide();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}


			    // Create Slide
			   Slide objSlide = createSlideforPPT (course_id, content_id, slideName, template_id, asset_id.intValue());
			   try {
				   objSlide =  slideService.addSlide(objSlide);
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}

			    // Attach asset
			   	SlideAsset objSlideAsset =  createSlideAsset(objSlide,asset_id.intValue());
			   	try {
					slideService.insertSelectedAsset(objSlideAsset);
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}

			   	try {
					assetService.insertAssetAssetGroupRelationship(
											   			asset_id, objAG.getID(),
														user.getAuthorId());
				} catch (Exception e) {
					logger.error(e.getMessage(),e);
				}

			}
		return "";
	}


	Slide createSlideforPPT (int CourseID, int content_id, String name, int template_id, int asset_id) {

		Slide objSlide = new Slide();

		VU360UserDetail user = (VU360UserDetail) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();

		objSlide.setCourse_ID((Integer) CourseID);
		objSlide.setContentObject_id((Integer) content_id);
		objSlide.setName(name);
		objSlide.setNameVisbileTF(false);
		objSlide.setTemplateID(template_id);
		objSlide.setAsset_ID(asset_id);
		//objSlide.setTemplateName();
		objSlide.setDuration(0);
		objSlide.setOrientationKey("Left");
		objSlide.setAsset_ID(0);
		objSlide.setContentOwner_ID((int) user.getContentOwnerId());
		objSlide.setCreateUserId((int) user.getAuthorId());
		objSlide.setSceneGUID(UUID.randomUUID().toString().replaceAll("-", ""));

		return objSlide;

	}

	SlideAsset createSlideAsset (Slide objSlide, int asset_id) {

		SlideAsset objSlideAsset = new SlideAsset ();
		objSlideAsset.setScene_id((int) objSlide.getId());
		objSlideAsset.setId(asset_id);
		objSlideAsset.setOrientationkey("$VisualTop");


		VU360UserDetail user = (VU360UserDetail) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		objSlideAsset.setCreateUser_id((int) user.getAuthorId());

		try {
			Integer version_id = slideService.getVersionIDForUploadedAsset (objSlideAsset);
			objSlideAsset.setAssetversion_id(version_id);
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}

		return objSlideAsset;

	}


}
