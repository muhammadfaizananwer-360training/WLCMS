package com.softech.ls360.lcms.contentbuilder.filter;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

public class MyAuthenticationException extends AbstractSecurityInterceptor implements Filter {
	
	private FilterInvocationSecurityMetadataSource securityMetadataSource;
	
	
	
	
	@Override
	protected InterceptorStatusToken beforeInvocation(Object object) {
		// TODO Auto-generated method stub
		return super.beforeInvocation(object);
	}


	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
	}
	
	
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Class<? extends Object> getSecureObjectClass() {
		//MyAccessDecisionManagersupportstrue,
		return FilterInvocation.class;
	}
	
	@Override
	public SecurityMetadataSource obtainSecurityMetadataSource() {
		return this.securityMetadataSource;
	}
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		FilterInvocation fi = new FilterInvocation(request, response, chain);
		invoke(fi);
		
	}
	
	public FilterInvocationSecurityMetadataSource getSecurityMetadataSource() {
		return securityMetadataSource;
	}

	public void setSecurityMetadataSource(FilterInvocationSecurityMetadataSource securityMetadataSource) {
		this.securityMetadataSource = securityMetadataSource;
	}
	
	private void invoke(FilterInvocation fi) throws IOException, ServletException {
		 //objectFilterInvocation
          //       super.beforeInvocation(fi);
		//1.
		//Collection<ConfigAttribute> attributes = SecurityMetadataSource.getAttributes(fi);
		//2.
		//this.accessDecisionManager.decide(authenticated, object, attributes);
		InterceptorStatusToken token = super.beforeInvocation(fi);
		try {
			fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
		}catch(AccessDeniedException ex){
			HttpServletResponse hhtpResponse = (HttpServletResponse) fi.getResponse();
			hhtpResponse.sendRedirect("/lcms/login");
		}
		finally {
		}
			super.afterInvocation(token, null);
		}
	

}
