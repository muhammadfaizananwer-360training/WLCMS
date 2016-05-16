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

import com.softech.ls360.lcms.contentbuilder.model.PermissionSettings;
import com.softech.ls360.lcms.contentbuilder.model.VU360UserDetail;
import com.softech.ls360.lcms.contentbuilder.service.IPermissionSettingsService;
import com.softech.ls360.lcms.contentbuilder.utils.UserFeature;


@Controller
public class PermissionSettingsController {

	@Autowired
	IPermissionSettingsService userPermissionSettingsService;
	
	@RequestMapping(value = "userPermissionSettings", method = RequestMethod.GET)
	public ModelAndView getRoyaltySettings() throws Exception {

		VU360UserDetail user = (VU360UserDetail) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		
		if (!user.hasFeaturePermission(UserFeature.userPermission)) {
			throw new Exception("User Permission Settings Page Permission Error.");
		}
		return new ModelAndView("authorPermissionSettings");
	}

	@RequestMapping(value = "getAuthorsPerm", method = RequestMethod.GET)
	    public ModelAndView getContentOwners(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		 String searchText = request.getParameter("searchText");
		 
		 ModelAndView view = new ModelAndView("authorPermissionSettingsResult");
		 
		 List<PermissionSettings> permissionSettingsList = userPermissionSettingsService.searchContentOwnersWithAuthorGruop(searchText);
		 view.addObject("permissionList", permissionSettingsList);
		 view.addObject("size", permissionSettingsList.size());
		 
		 return view;
	 }
	
	
	@RequestMapping(value = "savePermissionSettings", method = RequestMethod.POST)
    public @ResponseBody Boolean savePermissionSettings(HttpServletRequest request, HttpServletResponse response) throws Exception {
		 
	 String authorGroupId = request.getParameter("authorGrpId");
	 String userPermValue = request.getParameter("userPermValue");
	 String npsRatingReviewPermValue = request.getParameter("npsRatingReviewPermValue");
	 String royaltySettingPermValue = request.getParameter("royaltySettingPermValue");
	 String bulkUploadPermValue = request.getParameter("bulkUploadPermValue");
	 String viewReportPermValue = request.getParameter("viewReportPermValue");
	 
	 if(authorGroupId == null || userPermValue == null || npsRatingReviewPermValue == null && royaltySettingPermValue == null){
		 throw new Exception("Invalid Data Error in Permission Page.");
	 }
	 VU360UserDetail user = (VU360UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	 boolean returnValue = false;
	 int authorGropIdInt = Integer.parseInt(authorGroupId);
	 returnValue = userPermissionSettingsService.updatePermissionSettings(authorGropIdInt, UserFeature.npsCourseRating, Integer.parseInt(npsRatingReviewPermValue), user.getVu360UserID());
	 returnValue = userPermissionSettingsService.updatePermissionSettings(authorGropIdInt, UserFeature.contentOwnerRoyaltySettings, Integer.parseInt(royaltySettingPermValue), user.getVu360UserID());
	 returnValue = userPermissionSettingsService.updatePermissionSettings(authorGropIdInt, UserFeature.userPermission, Integer.parseInt(userPermValue), user.getVu360UserID());
	 returnValue = userPermissionSettingsService.updatePermissionSettings(authorGropIdInt, UserFeature.bulkCourseImportPermssion, Integer.parseInt(bulkUploadPermValue), user.getVu360UserID());
	 returnValue = userPermissionSettingsService.updatePermissionSettings(authorGropIdInt, UserFeature.viewWLCMSReportPermssion, Integer.parseInt(viewReportPermValue), user.getVu360UserID());
	 
	 return returnValue;
 }
	    
}
