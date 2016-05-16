package com.softech.ls360.lcms.contentbuilder.web.controller;

import javax.naming.NamingException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.security.ldap.userdetails.InetOrgPerson;

import com.softech.ls360.lcms.contentbuilder.model.VU360UserDetail;


@Controller
public class UserController  {
	private String username;
	private static Logger logger = LoggerFactory.getLogger(CourseController.class);

	@RequestMapping(value="/user", method = RequestMethod.GET)
	public String User(Model model) throws NamingException {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();


		if (principal instanceof UserDetails) {
			this.username = ((UserDetails)principal).getUsername();

		} else {
			this.username = principal.toString();

		}
		model.addAttribute("username", username);        
		model.addAttribute("roomNumber", ((InetOrgPerson) principal).getRoomNumber());
		return "user";
	}

	@RequestMapping(value = "/userprofile", method = RequestMethod.GET)
	public ModelAndView getuserprofile() {

		logger.debug("UserController::getuserprofile - Start");

		ModelAndView userprofileMV = new ModelAndView ("userprofile");
		VU360UserDetail principal = (VU360UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		userprofileMV.addObject("loginuser", principal);	

		logger.debug("UserController::getuserprofile - End");
		return userprofileMV;
	}
}