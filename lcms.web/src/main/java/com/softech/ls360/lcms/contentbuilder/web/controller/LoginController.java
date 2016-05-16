package com.softech.ls360.lcms.contentbuilder.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.softech.ls360.lcms.contentbuilder.utils.LCMSProperties;

@Controller

public class LoginController {

	 @RequestMapping(value = "login", method = RequestMethod.GET)
	    public ModelAndView login(ModelMap model) {
			 String CASServerBaseUrl = LCMSProperties.getLCMSProperty("lcms.cas.BaseUrl");
			 String wlcmsServerBaseUrl = LCMSProperties.getLCMSProperty("lcms.environment.Host");
			 model.addAttribute("CASServerBaseUrl", CASServerBaseUrl);
			 model.addAttribute("wlcmsServerBaseUrl", wlcmsServerBaseUrl);
			 
			 return new ModelAndView("login",model);
	    }
	 
	 @RequestMapping(value = "/login1", method = RequestMethod.GET)
	    public String loginAttemp(ModelMap model) {
		 System.out.println("login2");
	        return "default";
	    }	 
	 
	 @RequestMapping(value = "/loginfailed", method = RequestMethod.GET)
	    public ModelAndView loginfailed(ModelMap model) {
		 System.out.println("login failed");
		 	model.addAttribute("error", "true");
			
			return new ModelAndView("login",model);
	    }
	
}
