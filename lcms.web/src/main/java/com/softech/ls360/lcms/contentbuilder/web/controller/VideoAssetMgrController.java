package com.softech.ls360.lcms.contentbuilder.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.softech.ls360.lcms.contentbuilder.model.AssetDTO;
import com.softech.ls360.lcms.contentbuilder.model.SlideAsset;
import com.softech.ls360.lcms.contentbuilder.model.VU360UserDetail;
import com.softech.ls360.lcms.contentbuilder.service.IAssetService;
import com.softech.ls360.lcms.contentbuilder.service.ISlideService;
import com.softech.ls360.lcms.contentbuilder.utils.UserFeature;
import com.softech.ls360.lcms.contentbuilder.web.model.RestResponse;


@Controller
public class VideoAssetMgrController {
	private static Logger logger = LoggerFactory.getLogger(SignupAuthorController.class);


	@Autowired
	IAssetService assetService;
	
	@Autowired
	ISlideService slideService;
	
	@RequestMapping(value = "videoAssetMgrPage", method = RequestMethod.GET)
	public ModelAndView getVideoAssetMgrPage() throws Exception {

		VU360UserDetail user = (VU360UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();		
 
		ModelAndView mv = new ModelAndView("videoAssetMgrPage");
		mv.addObject("fmsUsedSpaceInBytes", assetService.getFMSUsedSpaceInBytes(user.getContentOwnerId()));
		return mv;
	}
	

	@RequestMapping(value = "searchVideoAssets", method = RequestMethod.GET)
	public ModelAndView searchVideoAssets(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		VU360UserDetail user = (VU360UserDetail) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		 
		String searchText = request.getParameter("searchText");
		ModelAndView view = new ModelAndView("videoAssetMgrPageResult");
		 
		 
		 Map<String,Object> options =  new HashMap<String,Object>();
		 options.put("fmsOnly", true);
		 
		 
		 List<AssetDTO> assets = assetService.searchAssets(user.getContentOwnerId(), searchText,options);
		 view.addObject("assets", assets);
		 view.addObject("size", assets.size());
		 
		 return view;
	}
	
	@RequestMapping(value = "videoAssetMgrPage/deleteAsset", method = RequestMethod.POST)
	@ResponseBody
	public RestResponse deleteAsset(@RequestParam("assetId") Long assetId) {
		RestResponse response = new RestResponse();
		try {
			
			VU360UserDetail user = (VU360UserDetail) SecurityContextHolder
					.getContext().getAuthentication().getPrincipal();
			
			assetService.deleteAsset(assetId, user.getAuthorId());
						
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			response.setError((ex.getMessage()==null)? "unhandled exception": ex.getMessage());
		}
		return response;
	}
	
	
	@RequestMapping(value = "videoAssetMgrPage/updateAsset", method = RequestMethod.POST)
	@ResponseBody
	public RestResponse updateAsset(@RequestParam("assetId") Long assetId,
			@RequestParam("requestId") String requestId,
			@RequestParam("filePath") String filePath,
			@RequestParam(value="duration", required=false, defaultValue="0") Integer duration) {
		RestResponse response = new RestResponse();
		try {
			
			VU360UserDetail user = (VU360UserDetail) SecurityContextHolder
					.getContext().getAuthentication().getPrincipal();
			

			AssetDTO asset = assetService.updateAsset(requestId, user.getContentOwnerId(), user.getAuthorId(), assetId, filePath);
			response.setData(asset);
			//update media related parameters.
			SlideAsset objSlideAsset = new SlideAsset();
			objSlideAsset.setId(assetId);
			objSlideAsset.setWidth("0");
			objSlideAsset.setHeight("0");
			if(duration > 0) {
				objSlideAsset.setDuration(duration);
			}
			slideService.updateAssetAttribtes(objSlideAsset);
			
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			response.setError((ex.getMessage()==null)? "unhandled exception": ex.getMessage());
		}
		return response;
	}

	
	
	
	    
}
