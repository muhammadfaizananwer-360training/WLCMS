package com.softech.ls360.lcms.contentbuilder.web.controller;

import java.net.URLEncoder;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.client.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.cas.authentication.CasAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.softech.ls360.lcms.contentbuilder.model.VU360UserDetail;
import com.softech.ls360.lcms.contentbuilder.service.VU360UserService;

//@test :: This is test class, this can be use to fix CAS integration issues 
@Controller
public class CasValidationController {
	
	@Autowired
	VU360UserService userService;
	
	@PostConstruct
	public void init() {
		System.out.println();
	}
	
	@RequestMapping(value = "casLogin", method = RequestMethod.POST)
	public void casLogin(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		
	    /**
	     * 
	    
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if (principal instanceof UserDetails) {
			String username = ((UserDetails)principal).getUsername();
			System.out.println();
		} else {
			String username = principal.toString();
			System.out.println();
		}
		 */
		//String path = request.getPathInfo();
		
		String myUsername = request.getParameter("j_username");
		String myPassword = request.getParameter("j_password");
		
		String casLoginUrl = "https://cas-dev.360training.com:8443/cas/login?auto=true&username=" + myUsername + "&password=" + myPassword;
		
		String targetUrl = casLoginUrl + "&service=http://localhost:8080/lcms/j_spring_cas_security_check";
		try {
			response.sendRedirect(targetUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@RequestMapping(value = "courseType", method = RequestMethod.GET)
	public ModelAndView success(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		
		/*String targetUrl = "";
		
		final CasAuthenticationToken token = (CasAuthenticationToken) request.getUserPrincipal();
		UserDetails userDetails = token.getUserDetails();
		String userName = userDetails.getUsername();
		String password = userDetails.getPassword();
		*/
		VU360UserDetail user = (VU360UserDetail) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		if (user.isFirstTimeLogin()) {
			userService.updateUserFirstTimeLogin(user.getVu360UserID(), false);
		}
		request.getSession().setAttribute("vu360User", user);
		
		return new ModelAndView("course_type", model);
	}
	
	@RequestMapping(value = "casFailure", method = RequestMethod.GET)
	public String failure(HttpServletRequest request, HttpServletResponse response) {
        return "/login"; 
	}
	
	@RequestMapping(value = "casPgt", method = RequestMethod.GET)
	public ModelAndView proxyGrantingTicket(HttpServletRequest request, HttpServletResponse response) {
		//TODO following code is not tested because this function is not configured properly in spring-security.xml
		
		//String targetUrl = "";
		
		//final CasAuthenticationToken token = (CasAuthenticationToken) request.getUserPrincipal();
		//UserDetails userDetails = token.getUserDetails();
		//String userName = userDetails.getUsername();
		//String password = userDetails.getPassword();
		
		/*final String proxyTicket = token.getAssertion().getPrincipal().getProxyTicketFor(targetUrl);
		
		try {
			//final String serviceUrl = targetUrl+"?ticket="+URLEncoder.encode(proxyTicket, "UTF-8");
		    //String proxyResponse = CommonUtils.getResponseFromServer(serviceUrl, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	    return null;
	}
	
}
