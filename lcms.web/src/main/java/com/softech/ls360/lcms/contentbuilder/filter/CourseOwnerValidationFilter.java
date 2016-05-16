package com.softech.ls360.lcms.contentbuilder.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.softech.ls360.lcms.contentbuilder.model.CourseDTO;
import com.softech.ls360.lcms.contentbuilder.model.VU360UserDetail;
import com.softech.ls360.lcms.contentbuilder.service.ICourseService;

public class CourseOwnerValidationFilter implements Filter{

	@Autowired
	ICourseService courseService;
	
	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain fc) throws IOException, ServletException {
		String courseId = request.getParameter("id");
		
		if(courseId != null){
		 Authentication authentication = (Authentication) SecurityContextHolder
				.getContext().getAuthentication();
		 VU360UserDetail user = null;
		 if(authentication != null){
			 user = (VU360UserDetail)authentication.getPrincipal();
			 CourseDTO course = courseService.getCourseById(Long.parseLong(courseId));
			if(course == null || course.getContentownerId() != user.getContentOwnerId()){
				HttpServletResponse hhtpResponse = (HttpServletResponse) response;
				hhtpResponse.sendRedirect("/lcms/404");
				return;
			}
		 }
		
		}
		fc.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}

}
