package com.softech.ls360.lcms.contentbuilder.manager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.springframework.stereotype.Component;

import com.softech.ls360.lcms.contentbuilder.model.CourseDTO;
import com.softech.ls360.lcms.contentbuilder.model.MyWebinarPlace;
import com.softech.ls360.lcms.contentbuilder.model.SynchronousClass;
import com.softech.ls360.lcms.contentbuilder.service.ISynchronousClassService;
import com.softech.ls360.lcms.contentbuilder.utils.LCMSProperties;
import com.softech.ls360.lcms.contentbuilder.utils.RestClient;
import com.softech.ls360.lcms.contentbuilder.utils.WlcmsConstants;

@Component
public class WebinarMeetingURLManager {
	
	private static WebinarMeetingURLManager instance = new WebinarMeetingURLManager();
	
	private ISynchronousClassService synchronousService;
	
	private WebinarMeetingURLManager(){
		
	}
	
	public static WebinarMeetingURLManager getInstance(ISynchronousClassService synchronousService){
		instance.synchronousService = synchronousService;
		return instance;
	}
	
	public SynchronousClass setupWebinarMeetingURL(CourseDTO course) {
			
			SynchronousClass syncClass = null;
			List<SynchronousClass> syncClassList = synchronousService.getSynchronousClassByCourseId(course.getId());
			if(syncClassList != null && syncClassList.size() > 0){
	
				syncClass = syncClassList.get(0);
				String meetingId = this.generateWebinarMeetingID(course, syncClass);
				String meetingURL = this.generateWebinarMeetingURL(syncClass,meetingId);
				syncClass.setMeetingId(meetingId);
				syncClass.setMeetingURL(meetingURL);
				syncClass.setWebinarServiceProvider(WlcmsConstants.WEBINAR_SERVICE_PROVIDER_360TRAINING);
				synchronousService.saveSynchronousClass(syncClass);
			}
			
			return syncClass;
		}
	
	private String generateWebinarMeetingID(CourseDTO course, SynchronousClass synchClass) {
		// Generate meeting ID
		
		MyWebinarPlace webinarPlace = populateMyWebinarPlaceBean(course, synchClass);
		String meetingID = this.getMeetingInfoForSessions(webinarPlace);
		synchClass.setMeetingId(meetingID);
		return meetingID;
	}

	private String generateWebinarMeetingURL(SynchronousClass synchClass,
			String meetingID) {

		StringBuilder presenterURL = new StringBuilder(
				LCMSProperties
						.getLCMSProperty("lms.instructor.mywebinarplace.meetingURL"));
		presenterURL.append("&");
		presenterURL
				.append(LCMSProperties
						.getLCMSProperty("lms.instructor.mywebinarplace.meetingURL.presenter.parameters.mt"));
		presenterURL.append("=");
		presenterURL.append(meetingID);
		presenterURL.append("&");
		presenterURL
				.append(LCMSProperties
						.getLCMSProperty("lms.instructor.mywebinarplace.meetingURL.presenter.parameters.first_name"));
		presenterURL.append("=");
		presenterURL.append(synchClass.getPresenterFirstName());
		presenterURL.append("&");
		presenterURL
				.append(LCMSProperties
						.getLCMSProperty("lms.instructor.mywebinarplace.meetingURL.presenter.parameters.last_name"));
		presenterURL.append("=");
		presenterURL.append(synchClass.getPresenterLastName());
		presenterURL.append("&");
		presenterURL
				.append(LCMSProperties
						.getLCMSProperty("lms.instructor.mywebinarplace.meetingURL.presenter.parameters.email"));
		presenterURL.append("=");
		presenterURL.append(synchClass.getPresenterEmail());

		return presenterURL.toString();
	}

	private MyWebinarPlace populateMyWebinarPlaceBean(CourseDTO course, SynchronousClass synchClass) {
		
		MyWebinarPlace webinar = new MyWebinarPlace();
		webinar.setTopic(course.getName());
		webinar.setAgenda(course.getName());
		webinar.setEvent_reference(course.getName());

		Calendar startCalendar = GregorianCalendar.getInstance();
		Calendar endCalendar = GregorianCalendar.getInstance();

		startCalendar.setTime(synchClass.getClassStartDate());
		endCalendar.setTime(synchClass.getClassEndDate());

		com.softech.ls360.lcms.contentbuilder.model.TimeZone timeZoneDB = synchronousService.getTimeZoneById(synchClass.getTimeZoneId());
		
		startCalendar.add(Calendar.HOUR, (-1) * timeZoneDB.getHours());
		startCalendar.add(Calendar.HOUR, - 6);
		startCalendar.add(Calendar.MINUTE, timeZoneDB.getMinutes());
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

		String startDateStr = formatter.format(startCalendar.getTime());
		webinar.setDate(startDateStr);
		
		/*String timeHours = String.valueOf(startCalendar.get(Calendar.HOUR));
		if(timeHours != null && timeHours.length() > 0 &&  timeHours.length() < 2){
			timeHours = "0" + timeHours;
		}*/
		webinar.setTime_hrs(String.valueOf(startCalendar.get(Calendar.HOUR)));
		
		String timeMints = String.valueOf(startCalendar.get(Calendar.MINUTE));
		if(timeMints != null && timeMints.length() > 0 &&  timeMints.length() < 2){
			timeMints = "0" + timeMints;
		}
		webinar.setTime_mins(timeMints);
		
		if (startCalendar.get(Calendar.AM_PM) == Calendar.PM) {
			webinar.setTime_type("PM");
		}else{
			webinar.setTime_type("AM");
		}
		

		return webinar;
	}
	
	public String getMeetingInfoForSessions(MyWebinarPlace myWebinarPlace) {

		String path = LCMSProperties
				.getLCMSProperty("lms.instructor.mywebinarplace.api")
				.concat(LCMSProperties
						.getLCMSProperty("lms.instructor.mywebinarplace.api.key"));
		int timeout = 30 * 60 * 1000;
		String meetingGUID = null;
		try {
			String callResponse = new RestClient().postForObject(
					myWebinarPlace, path, timeout);

			JSONObject jsonObject = (JSONObject) JSONSerializer
					.toJSON(callResponse);

			if (jsonObject.get("mt_number") != null) {
				meetingGUID = (String) jsonObject.get("mt_number");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return meetingGUID;
	}
}
