package com.softech.ls360.lcms.contentbuilder.dao.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.ParameterMode;
import javax.persistence.Query;

import com.softech.ls360.lcms.contentbuilder.utils.TypeConvertor;
import org.springframework.transaction.annotation.Transactional;

import com.softech.ls360.lcms.contentbuilder.dao.CourseRatingDAO;
import com.softech.ls360.lcms.contentbuilder.dao.GenericDAOImpl;
import com.softech.ls360.lcms.contentbuilder.dao.SPCallingParams;
import com.softech.ls360.lcms.contentbuilder.model.CourseRating;
import com.softech.ls360.lcms.contentbuilder.model.CourseRatingPublish;
import com.softech.ls360.lcms.contentbuilder.utils.LCMS_Util;

public class CourseRatingDAOImpl extends GenericDAOImpl<CourseRating> implements CourseRatingDAO {

	@Override
	@Transactional
	public List<CourseRating> SearchCourseRating(String FromDate,
			String ToDate, String NpsRating, int hideNullReview) {
		// TODO Auto-generated method stub
		List<CourseRating> listCourseRating = new ArrayList<CourseRating>();
		SPCallingParams sparam1 = LCMS_Util.createSPObject("NPS_RATING", String.valueOf(NpsRating) , String.class, ParameterMode.IN);
		SPCallingParams sparam2 = LCMS_Util.createSPObject("START_DATE", String.valueOf(FromDate) , java.sql.Date.class, ParameterMode.IN);
		SPCallingParams sparam3 = LCMS_Util.createSPObject("END_DATE", String.valueOf(ToDate) , java.sql.Date.class, ParameterMode.IN);
		SPCallingParams sparam4 = LCMS_Util.createSPObject("IGNORE_NULL_REVIEW", String.valueOf(hideNullReview) , Integer.class, ParameterMode.IN);

		Object[] courseRow;		
		Object[] courseRows = callStoredProc("[SEARCH_COURSE_RATINGNPS]", sparam1,sparam2,sparam3,sparam4).toArray();
		String reviewText;
		
		if(courseRows != null && courseRows.length > 0){

			for (Object course : courseRows) {
				courseRow = (Object[]) course;
	
				reviewText = courseRow[1] == null ? "" : courseRow[1].toString().replaceAll("'", "\\\\'");
				if ( reviewText.replaceAll("\\s+","").length() == 0 && hideNullReview >0) continue;
				
				CourseRating courseRating = new CourseRating();
				courseRow = (Object[]) course;

				courseRating.setNpsRating(TypeConvertor.AnyToShort(courseRow[0]));
				courseRating.setUserReviewText(reviewText);
				courseRating.setRatingCourse(courseRow[2] == null ? -1 : TypeConvertor.AnyToShort(courseRow[2]));
				courseRating.setRatingShoppingExp(courseRow[3] == null ? -1 : TypeConvertor.AnyToShort(courseRow[3]));
				courseRating.setRatingLearningTech(courseRow[4] == null ? -1 : TypeConvertor.AnyToShort(courseRow[4]));
				courseRating.setRatingCS(courseRow[5] == null ? -1 : TypeConvertor.AnyToShort(courseRow[5]));
				
				courseRating.setRatingCourseSecondary(courseRow[6] == null ? "" : courseRow[6].toString().replaceAll("'", "\\\\'"));
				courseRating.setRatingShoppingExpSecondary(courseRow[7] == null ? "" : courseRow[7].toString().replaceAll("'", "\\\\'"));
				courseRating.setRatingLearningTectSecondary(courseRow[8] == null ? "" : courseRow[8].toString().replaceAll("'", "\\\\'"));
				courseRating.setRatingCSSecondary(courseRow[9] == null ? "" : courseRow[9].toString().replaceAll("'", "\\\\'"));
				courseRating.setPublishStatus(courseRow[10] == null ? "" : courseRow[10].toString());
				courseRating.setCourseName(courseRow[11] == null ? "" : clobToString((Clob)courseRow[11]));
				courseRating.setCourseBusinessKey(courseRow[12] == null ? "" : courseRow[12].toString());
				courseRating.setCourseId(Integer.parseInt(courseRow[13].toString()));
				courseRating.setEnrollmentId(Integer.parseInt(courseRow[14].toString()));
				courseRating.setTimeStamp(courseRow[15] == null ? "" : courseRow[15].toString());
				
				courseRating.setRatingCourseSecondary_Q(courseRow[16] == null ? "" : courseRow[16].toString().replaceAll("'", "\\\\'"));
				courseRating.setRatingShoppingExpSecondary_Q(courseRow[17] == null ? "" : courseRow[17].toString().replaceAll("'", "\\\\'"));
				courseRating.setRatingLearningTectSecondary_Q(courseRow[18] == null ? "" : courseRow[18].toString().replaceAll("'", "\\\\'"));
				courseRating.setRatingCSSecondary_Q(courseRow[19] == null ? "" : courseRow[19].toString().replaceAll("'", "\\\\'"));
				courseRating.setRatingId(Integer.parseInt(courseRow[20].toString()));
				
				listCourseRating.add(courseRating);
			}
		}
		return listCourseRating;
	}
	
	@Transactional
	public boolean SaveCourseRatingStatus(int CourseId,int EnrollmentId,String Publish)
	{
		Query query = entityManager.createNativeQuery("UPDATE COURSERATING SET PUBLISH_STATUS = ? WHERE COURSE_ID=? AND LEARNERENROLLMENT_ID=?");  
		query.setParameter(1,Publish);
		query.setParameter(2,CourseId);
		query.setParameter(3,EnrollmentId);
		int rowAffected = query.executeUpdate();
		if(rowAffected>0)
			return true;
		else
			return false;
	}
	
	@Transactional
	public List<CourseRatingPublish> GetCourseRatingPublishList(String reviewIds){
		List<CourseRatingPublish> listCourseRating = new ArrayList<CourseRatingPublish>();
		String strQuery=""+
				"SELECT "+
				" C.GUID,"+
				" CR.COURSE_ID,"+
				" V.FIRSTNAME,"+
				" A.CITY,"+
				" A.STATE,"+
				" CR.RATING_COURSE,"+
				" CR.USER_REVIEW_TEXT,"+
				" CR.CREATEDATE,"+
				" C.ID,"+
				" CR.LEARNERENROLLMENT_ID, RATING_SHOPPINGEXP, RATING_LEARNINGTECH, RATING_CS " +
				" FROM COURSERATING CR INNER JOIN"+
				" LEARNERENROLLMENT LE ON LE.ID=CR.LEARNERENROLLMENT_ID INNER JOIN"+ 
				" LEARNER L ON LE.LEARNER_ID = L.ID INNER JOIN"+
				" LEARNERPROFILE LP ON LE.LEARNER_ID = LP.LEARNER_ID INNER JOIN"+
				" VU360USER V ON L.VU360USER_ID = V.ID INNER JOIN"+
				" ADDRESS A ON LP.ADDRESS_ID = A.ID INNER JOIN"+
				" COURSE C ON LE.COURSE_ID = C.ID"+
				" WHERE "+
				" CR.ID IN(:selectedValues) AND CR.PUBLISH_STATUS='READY'";
				
				Query query = entityManager.createNativeQuery(strQuery);
				String[] reviewIdsArray = reviewIds.split(",");
				if(reviewIdsArray == null || reviewIdsArray.length == 0){
					return null;
				}
				query.setParameter("selectedValues", Arrays.asList(reviewIdsArray));
				Object[] courseRow;		
				Object[] courseRows = query.getResultList().toArray();
				if(courseRows != null && courseRows.length > 0){

					for (Object course : courseRows) {
						CourseRatingPublish courseRating = new CourseRatingPublish();
						courseRow = (Object[]) course;

						courseRating.setCourseGuid(courseRow[0] == null ? "" : courseRow[0].toString());
						//1
						courseRating.setLearnerFirstName(courseRow[2] == null ? "" : courseRow[2].toString());
						courseRating.setLearnerCity(courseRow[3] == null ? "" : courseRow[3].toString());
						courseRating.setLearnerState(courseRow[4] == null ? "" : courseRow[4].toString());
						courseRating.setRatingCourse(courseRow[5] == null ? -1 : TypeConvertor.AnyToShort(courseRow[5]));
						courseRating.setUserReviewText(courseRow[6] == null ? "" : courseRow[6].toString());
						courseRating.setRatingTimeStamp(courseRow[7] == null ? "" : courseRow[7].toString());
						courseRating.setCourseId(Integer.parseInt(courseRow[8].toString()));
						courseRating.setEnrollmentId( Integer.parseInt(courseRow[9].toString()));
						courseRating.setRatingShopping(courseRow[10] == null ? -1 : TypeConvertor.AnyToShort(courseRow[10]));
						courseRating.setRatingLearningTech(courseRow[11] == null ? -1 : TypeConvertor.AnyToShort(courseRow[11]));
						courseRating.setRatingCS(courseRow[12] == null ? -1 : TypeConvertor.AnyToShort(courseRow[12]));
						listCourseRating.add(courseRating);
					}
				}
				
				return listCourseRating;
		}
	
	private String clobToString(Clob data) {
	    StringBuilder sb = new StringBuilder();
	    try {
	        Reader reader = data.getCharacterStream();
	        BufferedReader br = new BufferedReader(reader);

	        String line;
	        while(null != (line = br.readLine())) {
	            sb.append(line);
	        }
	        br.close();
	    } catch (SQLException e) {
	        // handle this exception
	    } catch (IOException e) {
	        // handle this exception
	    }
	    return sb.toString();
	}

		


}
