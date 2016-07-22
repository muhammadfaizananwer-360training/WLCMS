package com.softech.ls360.lcms.contentbuilder.web.controller;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.softech.ls360.lcms.contentbuilder.model.CourseRating;
import com.softech.ls360.lcms.contentbuilder.model.CourseRatingPublish;
import com.softech.ls360.lcms.contentbuilder.model.VU360UserDetail;
import com.softech.ls360.lcms.contentbuilder.service.ICourseRatingService;
import com.softech.ls360.lcms.contentbuilder.utils.LCMSProperties;
import com.softech.ls360.lcms.contentbuilder.utils.UserFeature;

@Controller
public class CourseRatingController {

	@Autowired
	ICourseRatingService courseRatingService;

	@Autowired
	UserFeature userFeature;

	private static Logger logger = LoggerFactory
			.getLogger(CourseController.class);

	@RequestMapping(value = "RatingCourseSearch", method = RequestMethod.GET)
	public @ResponseBody
	 ModelAndView RatingCourseSearch(HttpServletRequest request) throws Exception {
		 
		VU360UserDetail user = (VU360UserDetail) SecurityContextHolder
					.getContext().getAuthentication().getPrincipal();
			
			if (!user.hasFeaturePermission(userFeature.getNpsCourseRating())) {
				throw new Exception("Rating Review Page Permission Error.");
			}
		
		return new ModelAndView("RatingCourseSearch");
	}

		
	private boolean SynchCourseRatingSFWS(String CourseId,String Student,String StudentCity,String StudentState,String StarRating,String ReviewText,String TimeStamp)throws Exception {
		
		   String soapXml=""+
		   "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:upd=\"http://www.threesixtytraining.com/UpdateCoursesReviewsServices.xsd\">"+
		   "<soapenv:Header/>"+
		   "<soapenv:Body>"+
		      "<upd:GetUpdateCoursesReviewsRequest>"+
		         "<TransactionID>"+UUID.randomUUID().toString()+"</TransactionID>"+
		         "<CourseReview>"+
		            "<CourseGuid>"+CourseId+"</CourseGuid>"+
		            "<Student>"+Student+"</Student>"+
		            "<StudentCity>"+StudentCity+"</StudentCity>"+
		            "<StudentState>"+StudentState+"</StudentState>"+
		            "<Stars>"+StarRating+"</Stars>"+
		            "<ReviewText>"+ReviewText+"</ReviewText>"+
		            "<ReviewDate>"+TimeStamp+"</ReviewDate>"+
		         "</CourseReview>"+
		      "</upd:GetUpdateCoursesReviewsRequest>"+
		   "</soapenv:Body>"+
		"</soapenv:Envelope>";
		   
		   
		   java.net.URL url;
		try {
			url = new java.net.URL(LCMSProperties.getLCMSProperty("SF.CourseRating.URL"));
		
		   java.net.URLConnection connection = url.openConnection();
		   // Set the necessary header fields
		   //conn.setRequestProperty("SOAPAction", "http://www.example.org/ThreeSixtyTraining-CourseLevelRating/NewOperation");
		   //URL url = new URL(wsURL);
		   //URLConnection connection = url.openConnection();
		   HttpURLConnection httpConn = (HttpURLConnection)connection;
		   String responseString = "";
		   String outputString = "";
		   ByteArrayOutputStream bout = new ByteArrayOutputStream();

		   //byte[] buffer = new byte[soapXml.length()];
			byte[] buffer = soapXml.getBytes();
		   bout.write(buffer);
		   byte[] b = bout.toByteArray();
		   String SOAPAction =
		   "http://www.example.org/ThreeSixtyTraining-CourseLevelRating/NewOperation";
		   // Set the appropriate HTTP parameters.
		   httpConn.setRequestProperty("Content-Length",String.valueOf(b.length));
		   httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
		   httpConn.setRequestProperty("SOAPAction", SOAPAction);
		   httpConn.setRequestMethod("POST");
		   httpConn.setDoOutput(true);
		   httpConn.setDoInput(true);
		   OutputStream out = httpConn.getOutputStream();
		   //Write the content of the request to the outputstream of the HTTP Connection.
		   out.write(b);
		   out.close();
		   //Ready with sending the request.
		    
		   //Read the response.
		   InputStreamReader isr =
		   new InputStreamReader(httpConn.getInputStream());
		   BufferedReader in = new BufferedReader(isr);
		    
		   //Write the SOAP message response to a String.
		   while ((responseString = in.readLine()) != null) {
			   outputString = outputString + responseString;
		   }
		   System.out.println(outputString);
		   in.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "getRatingCourseSearch",  method={RequestMethod.GET})	
	public @ResponseBody String RatingCourseSearch(HttpServletRequest request, HttpServletResponse response)throws Exception {
		
		ModelAndView courseModelView = new ModelAndView("ratingCourseSearchResult");
		
			//try{
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
				SimpleDateFormat simpleDateFormatSql = new SimpleDateFormat("yyyy-MM-dd");

				Date datePicker_1 = simpleDateFormat.parse(request.getParameter("datePicker_1"));
				Date datePicker_2 = simpleDateFormat.parse(request.getParameter("datePicker_2"));
				
				 GregorianCalendar cal = new GregorianCalendar();				 
				 cal.setTime(datePicker_2);
				 cal.add(Calendar.DATE, 1);				 
				 datePicker_2 = cal.getTime();
				
				String[] NPSRatingArray = request.getParameterValues("NPSRating");
				String hideNullReviews = request.getParameter("chkHideNull");
				
				String NPSRating = getNPSRattingStr(NPSRatingArray);
				//06-07-2015
				
				System.out.println("datePicker_1:"+simpleDateFormatSql.format(datePicker_1));
				System.out.println("datePicker_2:"+simpleDateFormatSql.format(datePicker_2));
				System.out.println("NPSRating:"+NPSRating);
				
				List<CourseRating> listCourseRating = courseRatingService.SearchCourseRating(simpleDateFormatSql.format(datePicker_1),simpleDateFormatSql.format(datePicker_2) , NPSRating, Integer.parseInt(hideNullReviews));
				JSONObject jsonObject = new JSONObject();
				JSONArray arrayData = new JSONArray();
				int rowNo = 1;
				for(CourseRating rating : listCourseRating){
					
					JSONObject jsonInnerObject = new JSONObject();
					jsonInnerObject.put("submission_date", rating.getTimeStamp());
					jsonInnerObject.put("publish_status", rating.getPublishStatus());
					
					jsonInnerObject.put("NpsRating", rating.getNpsRating());
					jsonInnerObject.put("userReviewText", StringEscapeUtils.escapeJavaScript(rating.getUserReviewText()));
					jsonInnerObject.put("rating_course", rating.getRatingCourse());
					jsonInnerObject.put("RatingShoppingExp", rating.getRatingShoppingExp());
					jsonInnerObject.put("RatingLearningTech", rating.getRatingLearningTech());
					jsonInnerObject.put("RatingCS", rating.getRatingCS());
					jsonInnerObject.put("RatingCourseSecondary", rating.getRatingCourseSecondary());
					jsonInnerObject.put("RatingShoppingExpSecondary", rating.getRatingShoppingExpSecondary());
					jsonInnerObject.put("RatingLearningTectSecondary", rating.getRatingLearningTectSecondary());
					jsonInnerObject.put("RatingCSSecondary", rating.getRatingCSSecondary());
					jsonInnerObject.put("PublishStatus", rating.getPublishStatus());
					jsonInnerObject.put("CourseName", rating.getCourseName());
					jsonInnerObject.put("CourseBusinessKey", rating.getCourseBusinessKey());
					
					jsonInnerObject.put("RatingCourseSecondary_Q", rating.getRatingCourseSecondary_Q());
					jsonInnerObject.put("RatingShoppingExpSecondary_Q", rating.getRatingShoppingExpSecondary_Q());
					jsonInnerObject.put("RatingLearningTectSecondary_Q", rating.getRatingLearningTectSecondary_Q());
					jsonInnerObject.put("RatingCSSecondary_Q", rating.getRatingCSSecondary_Q());
					jsonInnerObject.put("CourseId", rating.getCourseId());
					jsonInnerObject.put("EnrollmentId", rating.getEnrollmentId());
					jsonInnerObject.put("ratingId", rating.getRatingId());
					jsonInnerObject.put("rowNo", rowNo++);
					arrayData.add(jsonInnerObject);
				}
				jsonObject.put("aaData", arrayData);
				jsonObject.put("sEcho", 1);
				jsonObject.put("iTotalRecords", listCourseRating.size());
				jsonObject.put("iTotalDisplayRecords", listCourseRating.size());
				
				courseModelView.addObject("listCourseRating", listCourseRating);
				return jsonObject.toJSONString();
			//} removed un-necessary try-catch
			//catch (Exception e){
			//	logger.error(e.toString());
			//}
		//return null;
	}

	private String getNPSRattingStr(String[] nPSRatingArray) {
		
		StringBuilder strBuilder = new StringBuilder();
		if(nPSRatingArray == null || nPSRatingArray.length == 0 || nPSRatingArray[0].equals("null")){
			
			int ratingUpperLimit = 10;
			for(int i = 0; i <= ratingUpperLimit; i++){
				strBuilder.append(i);
				if(i < ratingUpperLimit){
					strBuilder.append(",");
				}
			}
		}else{
			
			for(int i = 0; i < nPSRatingArray.length; i++){
				strBuilder.append(nPSRatingArray[i]);
				if(i < nPSRatingArray.length -1){
					strBuilder.append(",");
				}
			}
		}
		
		return strBuilder.toString();
	}

	@RequestMapping(value = "SaveRatingStatus", method = RequestMethod.POST)
	public @ResponseBody
	Boolean SaveRatingStatus(@RequestParam("Course_Id") int Course_Id,
			@RequestParam("Enrollment_id") int Enrollment_Id,@RequestParam("Status") String Status) {
		
		if(Status.equals("") || Status.length() == 0){
			Status = null;
		}
		boolean bResult = false;
		try {
			bResult = courseRatingService.SaveCourseRatingStatus(Course_Id, Enrollment_Id, Status);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bResult;
	}

	@RequestMapping(value = "PublishAllCourse", method = RequestMethod.POST)
	public @ResponseBody Boolean SynchCourseRatingSF(HttpServletRequest request, HttpServletResponse response){
		
		String reviewIds = request.getParameter("reviewIds");
		reviewIds = reviewIds.replaceAll("'", "");
		
		boolean status = false;
		try{
			List<CourseRatingPublish> listCourseRating = courseRatingService.GetCourseRatingPublishList(reviewIds);
			double averageRating = 0;
			if(listCourseRating != null){

				for (CourseRatingPublish courseRating : listCourseRating) {
					try {
						averageRating = (Double.valueOf(courseRating.getRatingCourse())  + Double.valueOf(courseRating.getRatingShopping()) + Double.valueOf(courseRating.getRatingLearningTech()) + Double.valueOf(courseRating.getRatingCS()))/4;
					}
					catch (Exception ee){
						logger.error(ee.getMessage());
					}
					if(SynchCourseRatingSFWS(courseRating.getCourseGuid(),courseRating.getLearnerFirstName(),courseRating.getLearnerCity(),
							courseRating.getLearnerState() , String.valueOf(Math.round(averageRating)),
							courseRating.getUserReviewText(),courseRating.getRatingTimeStamp()))
					{
						courseRatingService.SaveCourseRatingStatus(courseRating.getCourseId(), courseRating.getEnrollmentId(), "PUBLISHED");
						status = true;
					}
				}
			}

		}catch(Exception e){
			status = false;
			e.printStackTrace();
		}
		return status;
	}
}
