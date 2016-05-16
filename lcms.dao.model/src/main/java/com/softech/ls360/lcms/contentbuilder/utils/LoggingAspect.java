package com.softech.ls360.lcms.contentbuilder.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;



@Aspect
public class LoggingAspect {
 
	@Before("execution(* com.softech.ls360.lcms.contentbuilder.service.ICourseService.getCourse(..))")
	public void logBefore(JoinPoint joinPoint) {
 
		System.out.println("logBefore() is running!");
		System.out.println("hijacked : " + joinPoint.getSignature().getName());
		System.out.println("******");
	}
 
}
