package com.softech.ls360.lcms.contentbuilder.service.impl;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.softech.ls360.lcms.contentbuilder.dao.ReportDAO;
import com.softech.ls360.lcms.contentbuilder.service.ReportService;


public class ReportServiceImpl implements ReportService{
	
	@Autowired
	private ReportDAO reportDAO;
	
	
 	public ReportDAO getReportDAO() {
		return reportDAO;
	}

	public void setReportDAO(ReportDAO reportDAO) {
		this.reportDAO = reportDAO;
	}

	@Override
	public List<Object>  getSignupAuthorData(){
		List<?> records = reportDAO.getSignupAuthorData();
		
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy HH:mm a");
		
		List<Object> dataList = new ArrayList<Object>();
		
		dataList.add(new Object[]{"First Name","Last Name","Email","Loginname","Creation Date","Activated","Activation Date","Created Course","Created Lesson","Published Course","UTM Source"});	

		for(int i = 0; i < records.size(); i++){
			Object[] arrayObject   = (Object[])records.get(i);
			dataList.add(new Object[]{arrayObject[0].toString(), arrayObject[1],arrayObject[2],arrayObject[3],arrayObject[4]!=null ?  DATE_FORMAT.format((Date)arrayObject[4]): "",arrayObject[5],arrayObject[6]!=null? DATE_FORMAT.format((Date)arrayObject[6]): "",arrayObject[7],arrayObject[8],arrayObject[9],arrayObject[10]!=null? arrayObject[10].toString(): ""});
		}
		
		return dataList;
	}
}
