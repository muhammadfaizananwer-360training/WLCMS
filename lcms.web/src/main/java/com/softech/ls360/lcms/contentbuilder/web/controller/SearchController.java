package com.softech.ls360.lcms.contentbuilder.web.controller;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.softech.ls360.lcms.contentbuilder.model.Course;
import com.softech.ls360.lcms.contentbuilder.model.VU360UserDetail;

@Controller
public class SearchController {

	@Autowired
	private static Logger logger = LoggerFactory.getLogger(CourseController.class);
	
	@RequestMapping(value="quickSearch", method={RequestMethod.POST, RequestMethod.GET})
	public
	@ResponseBody
	ModelAndView quickSearch(
		//@RequestParam("search_string") String search_string
			HttpServletRequest request, HttpServletResponse response) {
		String search_string = "";
		logger.debug("SearchController::quickSearch:"+search_string);
		System.out.println("SearchController::quickSearch:"+search_string);
		ModelAndView searchModelView = new ModelAndView("search");
        try
		{
        	searchModelView.addObject("", "");
 			
		} catch (Exception ex)
		{			
			logger.debug("CourseController::updateCourse - Exception " + ex.getMessage());
		}
       logger.debug("SearchController::quickSearch - End");
        return searchModelView;
	}

	
	
}
