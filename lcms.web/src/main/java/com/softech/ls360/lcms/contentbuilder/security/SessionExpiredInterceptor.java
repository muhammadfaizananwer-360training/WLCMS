package com.softech.ls360.lcms.contentbuilder.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.softech.ls360.lcms.contentbuilder.web.controller.CourseController;

@Component
public class SessionExpiredInterceptor extends HandlerInterceptorAdapter {

	private static Logger logger = LoggerFactory
			.getLogger(CourseController.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		/*
		 * HttpSession session = request.getSession(false); 
		 * if (session == null
		 * && Boolean.parseBoolean(request.getHeader("isAjax"))) {
		 * 
		 * logger.info("Sessione scaduta, redirect home page");
		 * response.sendError(403, "Forbidden"); return false; }
		 */
		/*HttpSession session = request.getSession(false);
		if (session == null
				&& Boolean.parseBoolean(request.getHeader("isAjax"))) {
			// valid session doesn't exist
			// do something like send the user to a login screen

			response.sendError(403, "Forbidden");
			return false;
		}
		if (session != null) {
			if (session.getAttribute("username") == null
					&& Boolean.parseBoolean(request.getHeader("isAjax"))) {
				// no username in session
				// user probably hasn't logged in properly
				response.sendError(403, "Forbidden");
				return false;
			}
		}*/
		
		boolean logout = false;
		
		 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		 if (auth != null ){
			 User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			  if (user == null && 
					  Boolean.parseBoolean(request.getHeader("isAjax"))){
				  
				  response.sendError(302, "login");
				 // response.sendRedirect("login");
				 return false;
			  }
		 } else {
			 if (Boolean.parseBoolean(request.getHeader("isAjax"))) {
				 response.sendError(302, "login");
				 //response.sendRedirect("login");
				 return false;
			 }
		 }
		
			
		return true;

	}
}
