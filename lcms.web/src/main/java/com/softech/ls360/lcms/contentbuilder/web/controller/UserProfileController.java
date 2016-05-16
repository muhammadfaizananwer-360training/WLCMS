package com.softech.ls360.lcms.contentbuilder.web.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.softech.ls360.lcms.contentbuilder.model.UserProfile;
import com.softech.ls360.lcms.contentbuilder.model.VU360User;
import com.softech.ls360.lcms.contentbuilder.model.VU360UserDetail;
import com.softech.ls360.lcms.contentbuilder.service.IAssetService;
import com.softech.ls360.lcms.contentbuilder.service.ISlideService;
import com.softech.ls360.lcms.contentbuilder.service.IUserProfile;
import com.softech.ls360.lcms.contentbuilder.service.VU360UserService;
import com.softech.ls360.lcms.contentbuilder.utils.AssetUploadHelper;
import com.softech.ls360.lcms.contentbuilder.utils.FileMeta;
import com.softech.ls360.lcms.contentbuilder.utils.LCMSProperties;
import com.softech.ls360.lcms.contentbuilder.utils.ObjectWrapper;

@Controller
public class UserProfileController {
	
	private static Logger logger = LoggerFactory.getLogger(UserProfileController.class);
	
	@Autowired
	IUserProfile userProfileService;
	
	@Autowired
	VU360UserService vu360UserService;
	
	@Autowired
	ISlideService slideService;
	
	@Autowired
	IAssetService asssetService;
	
	@RequestMapping(value = "updateProfile", method = RequestMethod.POST)
	public @ResponseBody
	ModelAndView updateUserProfile(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String userName = request.getParameter("username");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String assetId = request.getParameter("assetId");
		String redirectPageTo = request.getParameter("redirectPageTo");
		
		VU360UserDetail user = (VU360UserDetail) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		
		UserProfile profileDB = userProfileService.getUserProfile(user.getVu360UserID());
		
		//UserProfile profile = new UserProfile();
		profileDB.setUsername(userName);
		profileDB.setFirstName(firstName);
		profileDB.setLastName(lastName);
		profileDB.setEmailAddress(email);
		
		if(assetId != null && assetId.length() > 0){
			Integer assetIdInt = Integer.parseInt(assetId);
			profileDB.setProfileImageAssetId(assetIdInt);
		}
		
		userProfileService.updateUserProfile(profileDB);
		
		VU360User vu360User = null;
		
		if(!password.equals(profileDB.getPassword())){
			
			vu360User = (VU360User) vu360UserService.getVU360User(profileDB.getUsername());
			
			vu360User = vu360UserService.changeVU360UserPassword(vu360User, password);
			profileDB.setPassword(vu360User.getPassword());
		}
		
		if(profileDB.getProfileImageAssetId() != null && profileDB.getProfileImageAssetId() != 0){
			
			user.setUserProfileURL(LCMSProperties.getLCMSProperty("code.lcms.assets.URL") + asssetService.getAssetLocation(profileDB.getProfileImageAssetId()));
		}
		
		String retURL = "redirect:/getProfile?redirectPageTo=" + redirectPageTo + "&success=1";
		return  new ModelAndView(retURL);
	}
	
	@RequestMapping(value = "getProfile", method = RequestMethod.GET)
	public ModelAndView getUserProfile(HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		
		VU360UserDetail user = (VU360UserDetail) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		UserProfile profile = userProfileService.getUserProfile(user.getVu360UserID());
		
		String profileImageUrl = null;
		if(profile.getProfileImageAssetId() != null && profile.getProfileImageAssetId() != 0){
			
			profileImageUrl = LCMSProperties.getLCMSProperty("code.lcms.assets.URL") + asssetService.getAssetLocation(profile.getProfileImageAssetId());
		}

		String redirectPageTo = request.getParameter("redirectPageTo");
		
		ModelAndView view = new ModelAndView("userProfile");
		view.addObject("profileObject", profile);
		view.addObject("redirectPageTo", redirectPageTo);
		view.addObject("profileImageUrl", profileImageUrl);
		return view;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "uploadProfileImage", method = RequestMethod.POST, produces = "text/html; charset=utf-8")
	@ResponseBody
	public String uploadProfilePicture(MultipartHttpServletRequest request) throws Exception {
		
		String authorImgKeywords = "";
		String authorImgDesc = "";

		String filePath = request.getParameter("filePath");
		String requestId = request.getParameter("requestId");
		String authorImgName = FilenameUtils.getBaseName(filePath);

		VU360UserDetail user = (VU360UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Long asset_id = 0L;
		String assetType = "Image";
		ObjectWrapper<Long> assetVersionWrapper = new ObjectWrapper<Long>();
		asset_id = asssetService.addAsset(requestId, user.getUsername(),user.getContentOwnerId(),user.getAuthorId(),authorImgName, filePath, assetType,authorImgKeywords,authorImgDesc,assetVersionWrapper);
		
		if (asset_id != 0) {
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("assetId", asset_id + "");
			
			String profileImageUrl = LCMSProperties.getLCMSProperty("code.lcms.assets.URL");
			profileImageUrl += asssetService.getAssetLocation(asset_id.intValue());
			jsonObject.put("assetUrl", profileImageUrl);
			return jsonObject.toString();
		}
		return "";
	}
}