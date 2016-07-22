package com.softech.ls360.lcms.contentbuilder.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.softech.ls360.lcms.contentbuilder.model.ContentOwnerRoyaltySettings;
import com.softech.ls360.lcms.contentbuilder.model.VU360UserDetail;
import com.softech.ls360.lcms.contentbuilder.service.IContentOwnerRoyaltySettingsService;
import com.softech.ls360.lcms.contentbuilder.utils.UserFeature;


@Controller
public class RoyaltySettingsController {

	@Autowired
	IContentOwnerRoyaltySettingsService royaltySettingsService;

	@Autowired
	UserFeature userFeature;
	
	@RequestMapping(value = "royaltySettings", method = RequestMethod.GET)
	public ModelAndView getRoyaltySettings() throws Exception {

		VU360UserDetail user = (VU360UserDetail) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		
		if (!user.hasFeaturePermission(userFeature.getContentOwnerRoyaltySettings())) {
			throw new Exception("Royalty Settings Page Permission Error.");
		}
		return new ModelAndView("royaltSettings");
	}
	 
	 public IContentOwnerRoyaltySettingsService getRoyaltySettingsService() {
		return royaltySettingsService;
	}

	public void setRoyaltySettingsService(
			IContentOwnerRoyaltySettingsService royaltySettingsService) {
		this.royaltySettingsService = royaltySettingsService;
	}

	@RequestMapping(value = "getAllContentOwner", method = RequestMethod.GET)
	    public ModelAndView getContentOwners(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		 String searchText = request.getParameter("searchText");
		 
		 ModelAndView view = new ModelAndView("royaltSettingsResult");
		 
		 List<ContentOwnerRoyaltySettings> royaltySettingsList = royaltySettingsService.searchContentOwners(searchText);
		 view.addObject("royaltyList", royaltySettingsList);
		 view.addObject("size", royaltySettingsList.size());
		 
		 return view;
	 }
	
	
	@RequestMapping(value = "saveSettings", method = RequestMethod.POST)
    public @ResponseBody Boolean saveRoyaltySettings(HttpServletRequest request, HttpServletResponse response) {
		 
	 String contentOwnerId = request.getParameter("cId");
	 String onlinePerc = request.getParameter("onlinePerc");
	 String classroomPerc = request.getParameter("classroomPerc");
	 String webinarPerc = request.getParameter("webinarPerc");
	 
	 float onlinePercFloat = 0, classroomPercFloat = 0, webinarPercFloat = 0;
	 
	 if(onlinePerc == null || onlinePerc.length() == 0){
		 return false;
	 }
	 onlinePercFloat = Float.parseFloat(onlinePerc);
	 
	 if(classroomPerc == null || classroomPerc.length() == 0){
		return false;
	 }
	 classroomPercFloat = Float.parseFloat(classroomPerc);
	 
	 if(webinarPerc == null || webinarPerc.length() == 0){
		 return false;
	 }
	 webinarPercFloat = Float.parseFloat(webinarPerc);
	 
	 if(contentOwnerId == null || contentOwnerId.length() == 0){
		return false;
	 }
	 Integer cIdInt = Integer.parseInt(contentOwnerId);
	 
	 boolean returnValue = royaltySettingsService.updateRoyaltSettings(cIdInt, onlinePercFloat, classroomPercFloat, webinarPercFloat);
	 
	 return returnValue;
 }
	    
}
