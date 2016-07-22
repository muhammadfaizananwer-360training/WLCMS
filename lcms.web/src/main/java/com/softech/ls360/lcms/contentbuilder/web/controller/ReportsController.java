package com.softech.ls360.lcms.contentbuilder.web.controller;

import java.io.OutputStream;
import java.net.HttpCookie;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.softech.ls360.lcms.contentbuilder.manager.ExcelDataWriter;
import com.softech.ls360.lcms.contentbuilder.model.VU360UserDetail;
import com.softech.ls360.lcms.contentbuilder.service.ReportService;
import com.softech.ls360.lcms.contentbuilder.service.VU360UserService;
import com.softech.ls360.lcms.contentbuilder.utils.UserFeature;

@Controller
public class ReportsController {

	private static Logger logger = LoggerFactory
			.getLogger(ReportsController.class);

	@Autowired
	VU360UserService vu360UserService;

	@Autowired
	UserFeature userFeature;
	
	@Autowired
	ReportService reportService;

	@RequestMapping(value = "reports", method = RequestMethod.GET)
	public ModelAndView getReports(HttpServletRequest request) throws Exception{
		VU360UserDetail user = (VU360UserDetail) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();

		if (!user.hasFeaturePermission(userFeature.getViewWLCMSReportPermssion())) {
			throw new Exception("View WLCMS Reports Permission Error.");
		}
		return new ModelAndView("reports");
	}
	

	@RequestMapping(value = "exportSignupAuthorData", method = RequestMethod.GET)
	public @ResponseBody
	void exportSignupAuthorData(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		List<Object> dataList = reportService.getSignupAuthorData();
		String downloadTokenValue = request.getParameter("download_token_value_id");
				
		 response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
         response.setHeader("Content-Disposition", "attachment; filename="+"Author_Data.xlsx");
         response.addCookie(new Cookie("fileDownloadToken", downloadTokenValue));
		ExcelDataWriter writer = new ExcelDataWriter();
		XSSFWorkbook workBook =  writer.writeData(dataList);
		OutputStream stream = response.getOutputStream();
		workBook.write(stream);
		if(stream != null){
			stream.close();
		}
	}
	
	/*
	 * Report to export a course structure to XLS 
	@RequestMapping(value = "exportCourseLessons", method = RequestMethod.GET)
	public @ResponseBody
	void exportCourseLessons(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		OnlineCourseExportManager manager = new OnlineCourseExportManager(courseService);
		
		String courseId = request.getParameter("id");
		
		if(courseId == null){
			new Exception("Course Id is missing for course excel import");
		}
		
		Integer courseIdInt = Integer.parseInt(courseId);
		List<Object> dataList = manager.exportCourse(courseIdInt);
		
		//return new ModelAndView("excelWriter", "data", dataList);
		
		 response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
         response.setHeader("Content-Disposition", "attachment; filename="+"CourseStructure.xlsx");
         
		ExcelDataWriter writer = new ExcelDataWriter();
		XSSFWorkbook workBook =  writer.writeData(dataList);
		OutputStream stream = response.getOutputStream();
		workBook.write(stream);
		if(stream != null){
			stream.close();
		}
	}
	*/
}
