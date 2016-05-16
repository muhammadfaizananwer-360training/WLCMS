package com.softech.ls360.lcms.contentbuilder.dao.impl;

import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import com.softech.ls360.lcms.contentbuilder.utils.*;

import org.springframework.transaction.annotation.Transactional;









import com.softech.ls360.lcms.contentbuilder.dao.GenericDAOImpl;
import com.softech.ls360.lcms.contentbuilder.dao.MarketingDAO;
import com.softech.ls360.lcms.contentbuilder.dao.SPCallingParams;
import com.softech.ls360.lcms.contentbuilder.model.CourseDTO;
import com.softech.ls360.lcms.contentbuilder.model.SlideAsset;

public class MarketingDAOImpl  extends GenericDAOImpl<CourseDTO> implements MarketingDAO{
	
	
	private enum COURSE_ASSET{
		COURSE_THUMBNAIL_IMAGE("CourseThumbnailImage");
		
		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		private String value;
		
		
		private COURSE_ASSET(String value){
			this.value = value;
			
		}
		
		
	}

	@Override
	@Transactional
	public CourseDTO getCourseByID(int id) throws Exception {
		
		CourseDTO courseDTO = new CourseDTO ();
		SPCallingParams sparam1 = LCMS_Util.createSPObject("COURSE_ID",	String.valueOf(id), Integer.class, ParameterMode.IN);
		Object[] courseRows = callStoredProc("GetCourseMarketing", sparam1).toArray();
		
		for (Object course : courseRows) {
			
			Object[] courseRow = (Object[]) course;
			
			courseDTO.setId(id);
			Clob cb = (Clob)courseRow[1];
			courseDTO.setName(StringUtil.clobStringConversion(cb));
			
			courseDTO.setBussinesskey( (String) courseRow[2] );
			
			cb = (Clob)courseRow[3];
			courseDTO.setCoursePreReq(StringUtil.clobStringConversion(cb));
			
			cb = (Clob)courseRow[4];
			courseDTO.setEndOfCourseInstructions(StringUtil.clobStringConversion(cb));
			
			cb = (Clob)courseRow[5];
			courseDTO.setAdditionalMaterials (StringUtil.clobStringConversion(cb));
			
			cb = (Clob)courseRow[6];	
			courseDTO.setAuthorBackground (StringUtil.clobStringConversion(cb));
			
			cb = (Clob)courseRow[7];
			courseDTO.setIntendedAudience(StringUtil.clobStringConversion(cb));
			BigDecimal bg = (BigDecimal) courseRow[11];
			courseDTO.setCeus(bg);
			
			courseDTO.setImageOfCourse(TypeConvertor.AnyToInteger(courseRow[8]));
			courseDTO.setCourseAuthorImageId(TypeConvertor.AnyToInteger(courseRow[9]));
            courseDTO.setClassInstructorId(TypeConvertor.AnyToLong ( courseRow[12]  ));
		}
		
		courseDTO.setAuthorImage( this.getAssetImage(courseDTO, LCMSProperties.getLCMSProperty("marketing.authorImage")) );
		courseDTO.setCourseImage( this.getAssetImage(courseDTO, LCMSProperties.getLCMSProperty("marketing.courseImage")) );
		courseDTO.setCourseImageThumbnail( this.getAssetImage(courseDTO, COURSE_ASSET.COURSE_THUMBNAIL_IMAGE.getValue()) );
		return courseDTO;
	}
	
	@Override
	@Transactional
	public CourseDTO updateMarketing (CourseDTO courseDTO) throws SQLException, Exception{
		
		SPCallingParams sparam1 = LCMS_Util.createSPObject("COURSE_ID",	String.valueOf(courseDTO.getId()), Integer.class, ParameterMode.IN);
		SPCallingParams sparam2 = LCMS_Util.createSPObject("COURSEPRE_REQ",	courseDTO.getCoursePreReq(), String.class, ParameterMode.IN);
		SPCallingParams sparam3 = LCMS_Util.createSPObject("ENDOFCOURSEINSTRUCTIONS",	courseDTO.getEndOfCourseInstructions(), String.class, ParameterMode.IN);
		SPCallingParams sparam4 = LCMS_Util.createSPObject("ADDITIONALMATERIALS",	courseDTO.getAdditionalMaterials(), String.class, ParameterMode.IN);
		SPCallingParams sparam5 = LCMS_Util.createSPObject("AUTHOR_BACKGROUND",	courseDTO.getAuthorBackground(), String.class, ParameterMode.IN);
		SPCallingParams sparam6 = LCMS_Util.createSPObject("INTENDED_AUDIENCE",	courseDTO.getIntendedAudience(), String.class, ParameterMode.IN);
		SPCallingParams sparam7 = LCMS_Util.createSPObject("CEUS",	String.valueOf(courseDTO.getCeus()), BigDecimal.class, ParameterMode.IN);
		SPCallingParams sparam8 = LCMS_Util.createSPObject("CLASSINSTRUCTOR_ID",	String.valueOf(courseDTO.getClassInstructorId()), Integer.class, ParameterMode.IN);

		StoredProcedureQuery query = createQueryParameters("UpdateCourseMarketing", sparam1, sparam2, sparam3, sparam4, sparam5, sparam6,sparam7,sparam8);
		query.execute(); 
		
		return courseDTO;
	}
	
	@Override
	@Transactional
	public CourseDTO UpdateImage(CourseDTO courseDTO, String AssetType,long lastUpdateUser) throws SQLException, Exception{
		
		SPCallingParams sparam1 = LCMS_Util.createSPObject("COURSE_ID",	String.valueOf(courseDTO.getId()), Integer.class, ParameterMode.IN);
		SPCallingParams sparam2 = null;
		SPCallingParams sparam3 = LCMS_Util.createSPObject("AssetType",	AssetType, String.class, ParameterMode.IN);
		SPCallingParams sparam4 = LCMS_Util.createSPObject("LASTUPDATEUSER",	String.valueOf(lastUpdateUser), String.class, ParameterMode.IN);
		if (AssetType.equalsIgnoreCase(LCMSProperties.getLCMSProperty("marketing.authorImage")) ) 
			sparam2 = LCMS_Util.createSPObject("COURSEAUTHORIMAGE_ID",	String.valueOf(courseDTO.getCourseAuthorImageId()), Integer.class, ParameterMode.IN);					
		else 		
			sparam2 = LCMS_Util.createSPObject("COURSEAUTHORIMAGE_ID",	String.valueOf(courseDTO.getImageOfCourse()), Integer.class, ParameterMode.IN);

		StoredProcedureQuery query = createQueryParameters("UpdateAuthorImage", sparam1, sparam2, sparam3, sparam4);
		query.execute(); 

		return courseDTO;
	}
	
	@Override
	@Transactional
	public SlideAsset getAssetImage(CourseDTO courseDTO, String AssetType) throws SQLException, Exception{
	
		SPCallingParams sparam1 = LCMS_Util.createSPObject("COURSE_ID",	String.valueOf(courseDTO.getId()), Integer.class, ParameterMode.IN);		
		SPCallingParams sparam2 = LCMS_Util.createSPObject("AssetType",	AssetType, String.class, ParameterMode.IN);
		
		
		Object[] courseRows = callStoredProc("GetMarketingAssets", sparam1, sparam2).toArray();
		Object[] courseRow;
		String locationPath = "";
		SlideAsset asset = null;
		for (Object slideAsset : courseRows) {
			asset = new SlideAsset ();
			courseRow = (Object[]) slideAsset;
			asset.setId(TypeConvertor.AnyToLong(courseRow[0]));
			asset.setName(StringUtil.ifNullReturnEmpty(courseRow[1]));
			asset.setAssettype(StringUtil.ifNullReturnEmpty(courseRow[3]));
			// Height + Width = Dimension
			asset.setHeight(StringUtil.ifNullReturnZero(courseRow[6]));
			asset.setWidth(StringUtil.ifNullReturnZero(courseRow[7]));
			
				
			
			asset.setSize(AssetUtil.getFileSizeInKb(StringUtil.ifNullReturnZero(courseRow[12])));
			asset.setVersion(StringUtil.ifNullReturnZero(courseRow[4]));
			asset.setDuration(Integer.parseInt(StringUtil.ifNullReturnZero(courseRow[9])));
			asset.setVersionId(Integer.parseInt(StringUtil.ifNullReturnZero(courseRow[10])));
			locationPath = LCMSProperties.getLCMSProperty("code.lcms.assets.URL");
			asset.setLocation(locationPath+StringUtil.ifNullReturnEmpty(courseRow[11]));
		
			if(courseRow[8]!=null){
				Clob cm = (Clob )courseRow[8];
				asset.setDescription(StringUtil.clobStringConversion(cm));
			}
		}
		return asset;
	}
	
	
	
	@Override
	@org.springframework.transaction.annotation.Transactional
	public boolean UpdateVideo(CourseDTO course)
			throws SQLException, Exception {
		String updateQuery = "update CourseDTO set courseVideoAssetId=:courseVideoId,lastUpdateUser=:lastUpdateUser,lastUpdatedDate=:lastUpdatedDate where id=:course_id";
		Query query = entityManager.createQuery(updateQuery);
		query.setParameter("course_id", course.getId());
		query.setParameter("courseVideoId", course.getCourseVideoAssetId());
		query.setParameter("lastUpdateUser",course.getLastUpdateUser() );
		query.setParameter("lastUpdatedDate",new Date());
		boolean isQueryexecuteSuccessfull = query.executeUpdate()>1 ? true : false;
		return isQueryexecuteSuccessfull;
		
		
		
		
	}
	
	
	@Override
	@org.springframework.transaction.annotation.Transactional
	public SlideAsset getSlideAssetForVideoAsset(CourseDTO course)
			throws SQLException, Exception {
		String updateQuery = "SELECT ASSET.ID, ASSET.NAME,ASSET.KEYWORDS,ASSET.ASSETTYPE,ASSETVERSION.VERSION as VERSION,ASSETVERSIONDETAIL.CONTENT,ASSETVERSIONDETAIL.HEIGHT,ASSETVERSIONDETAIL.WIDTH,ASSET.DESCRIPTION,ASSETVERSIONDETAIL.DURATION,ASSETVERSIONDETAIL.ID  as VersionDetailID,ASSET.VIDEOFILENAME,ASSET.SizeInBytes FROM ASSET INNER JOIN ASSETVERSION   ON (ASSET.ID=ASSETVERSION.ASSET_ID) INNER JOIN ASSETVERSIONDETAIL ON (ASSETVERSIONDETAIL.ASSETVERSION_ID=ASSETVERSION.ID) INNER JOIN COURSE  ON (ASSET.ID=COURSE.COURSEVIDEOASSET_ID) WHERE COURSE.ID = :courseId";
		Query query = entityManager.createNativeQuery(updateQuery);
		query.setParameter("courseId", course.getId());
		Object[] objServer = query.getResultList().toArray();
		SlideAsset asset = null;
		String locationPath = "";
		Object[] courseRow;
		for(Object slideAsset : objServer){
			asset = new SlideAsset ();
			courseRow = (Object[]) slideAsset;

			asset.setId(Long.valueOf(StringUtil.ifNullReturnZero(courseRow[0])));
			asset.setName(StringUtil.ifNullReturnEmpty(courseRow[1]));
			asset.setAssettype(StringUtil.ifNullReturnEmpty(courseRow[3]));
			// Height + Width = Dimension
			asset.setHeight(StringUtil.ifNullReturnZero(courseRow[6]));
			asset.setWidth(StringUtil.ifNullReturnZero(courseRow[7]));
			asset.setSize(AssetUtil.getFileSizeInKb(StringUtil.ifNullReturnZero(courseRow[12])));
			asset.setVersion(StringUtil.ifNullReturnZero(courseRow[4]));
			asset.setDuration(Integer.parseInt(StringUtil.ifNullReturnZero(courseRow[9])));
			asset.setVersionId(Integer.parseInt(StringUtil.ifNullReturnZero(courseRow[10])));
			locationPath = LCMSProperties.getLCMSProperty("lcms.preview.streaming");
			asset.setLocation(locationPath+StringUtil.ifNullReturnEmpty(courseRow[11]));
			
			if(courseRow[8]!=null){
				Clob cm = (Clob )courseRow[8];
				asset.setDescription(StringUtil.clobStringConversion(cm));
			}
		}
		return asset;
			
			
			
		
		
		
		
	}

	@Override
	@org.springframework.transaction.annotation.Transactional
	public boolean updateCourseDuration(String duration,long id) {
		Query query = entityManager.createNativeQuery("update course set ceus=:duration where id=:id");
		query.setParameter("duration", duration.equals("")?null:duration);
		query.setParameter("id", id);
		int result = query.executeUpdate();
		return result>=1 ? true:false;
	}
	
	@Override
	public String findDurationbyCourseId(long courseId){
		Query query = entityManager.createNativeQuery("select duration from course where id="+courseId);
		List object = query.getResultList();
		return (String)(object.get(0));
		
	}

	@Override
	public boolean IsInstructorExist(List<Long> instructorIds) {
		Query query = entityManager.createNativeQuery("select count(id) from course where CLASSINSTRUCTOR_ID in (:instructorIds)");
		query.setParameter("instructorIds", instructorIds);
		int resultCount = TypeConvertor.AnyToInteger(query.getSingleResult());
		return resultCount>0?true:false;
	}
}
