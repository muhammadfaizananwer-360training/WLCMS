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

import com.softech.ls360.lcms.contentbuilder.model.SupportMaterial;
import com.softech.ls360.lcms.contentbuilder.model.VU360UserDetail;
import com.softech.ls360.lcms.contentbuilder.service.IAssetService;
import com.softech.ls360.lcms.contentbuilder.utils.JsonResponse;

@Controller
public class SupportMaterialController {

	@Autowired
	IAssetService assetService;
	
	@RequestMapping(value = "setSpprtMtrDisplayOrder", method = RequestMethod.POST)
	public @ResponseBody
	JsonResponse setSpprtMtrDisplayOrder(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JsonResponse res = new JsonResponse();

		String varSMId = request.getParameter("varSMId");
		String varLessonId = request.getParameter("varLessonId");
		//String item_type = request.getParameter("item_type");
		String direction = request.getParameter("direction");

		assetService.setSpprtMtrDisplayOrder(Integer.parseInt(varLessonId), Integer.parseInt(varSMId), Integer.parseInt(direction));

		res.setStatus("SUCCESS");
		return res;
	}
	
	
	@RequestMapping(value = "getSpprtMtrByCourse", method = RequestMethod.POST)
	public @ResponseBody
	List<SupportMaterial> getSpprtMtrByCourse(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String varCourseId = request.getParameter("varCourseId");
		List<SupportMaterial> lstCourseMaterial = assetService.getSupportMaterialListByCourse(Integer.parseInt(varCourseId));
		return lstCourseMaterial;
	}
	
	
}
