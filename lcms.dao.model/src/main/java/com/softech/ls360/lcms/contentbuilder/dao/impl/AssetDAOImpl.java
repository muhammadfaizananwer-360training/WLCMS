package com.softech.ls360.lcms.contentbuilder.dao.impl;

import com.softech.ls360.lcms.contentbuilder.dao.AssetDAO;
import com.softech.ls360.lcms.contentbuilder.dao.GenericDAOImpl;
import com.softech.ls360.lcms.contentbuilder.dao.SPCallingParams;
import com.softech.ls360.lcms.contentbuilder.model.AssetDTO;
import com.softech.ls360.lcms.contentbuilder.model.AssetGroup;
import com.softech.ls360.lcms.contentbuilder.model.SupportMaterial;
import com.softech.ls360.lcms.contentbuilder.utils.LCMSProperties;
import com.softech.ls360.lcms.contentbuilder.utils.LCMS_Util;
import com.softech.ls360.lcms.contentbuilder.utils.ObjectWrapper;
import com.softech.ls360.lcms.contentbuilder.utils.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AssetDAOImpl extends GenericDAOImpl<AssetDAO> implements AssetDAO{

	private static Logger logger = Logger.getLogger(SlideDAOImpl.class);

	@Transactional
	public AssetGroup getAssetGroupByCourseId (long courseId) throws Exception {

			logger.info("In AssetDAOImpl::AssetDAOImpl Start...");
			AssetGroup dto;
			SPCallingParams sparam1 = LCMS_Util.createSPObject("COURSE_ID", String.valueOf(courseId) , Integer.class, ParameterMode.IN);

			Object[] courseRow;
			Object[] courseRows = callStoredProc("[LCMS_WEB_SELECT_ASSETGROUP_BY_COURSEID]", sparam1).toArray();

			dto = new AssetGroup();

			try {
				for (Object assetGroup : courseRows) {

					courseRow = (Object[]) assetGroup;

					dto.setID(Long.parseLong(StringUtil.ifNullReturnZero(courseRow[0])));
					dto.setNAME(	StringUtil.ifNullReturnEmpty(courseRow[2])	);
					dto.setDESCRIPTION(	StringUtil.ifNullReturnEmpty(courseRow[3])	);


				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("In SlideDAOImpl::getSlide ..................Exception..................");
				e.printStackTrace();
			}
			logger.info("In AssetDAOImpl::AssetDAOImpl  END ....");

			return dto;
		}

	@Transactional
	public boolean insertAssetAssetGroupRelationship (long assetId, long assetGroupId, long userId) throws Exception{
		logger.info("In AssetDAOImpl::insertAssetAssetGroupRelationship Start...");
		SPCallingParams sparam1 = LCMS_Util.createSPObject("@ASSETGROUP_ID", String.valueOf(assetGroupId) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam2 = LCMS_Util.createSPObject("@ASSET_ID", String.valueOf(assetId) , Integer.class, ParameterMode.IN);
		SPCallingParams sparam3 = LCMS_Util.createSPObject("@LastUpdateUser", String.valueOf(userId) , Integer.class, ParameterMode.IN);

		StoredProcedureQuery qr = createQueryParameters("INSERT_ASSETGROUP_ASSETS", sparam1, sparam2, sparam3);
		try
		{
			qr.execute();
		}catch (Exception ex){
				ex.printStackTrace();
		}
		logger.info("In AssetDAOImpl::insertAssetAssetGroupRelationship End...");
		return true;
	}

	@Transactional
	@Override
	public String getAssetLocation(int assetId) throws Exception {
		// TODO Auto-generated method stub
		logger.info("In AssetDAOImpl::ICP_SELECT_ASSET_LOCATION --- Begin");
		String AssetLocation = "";
		StoredProcedureQuery qr = null;
		try{
			SPCallingParams sparam1 = LCMS_Util.createSPObject("ASSET_ID", String.valueOf(assetId) , Integer.class, ParameterMode.IN);
			SPCallingParams sparam2 = LCMS_Util.createSPObject("LOCATION", String.valueOf("") , String.class, ParameterMode.OUT);

			qr = createQueryParameters("GET_ASSET_CURRENT_VERSION_PATH", sparam1, sparam2);
			qr.execute();

			AssetLocation = (String)qr.getOutputParameterValue("LOCATION");
		}catch(Exception ex){
			logger.error(ex);
			return null;
		}
		logger.info("In AssetDAOImpl::ICP_SELECT_ASSET_LOCATION --- END");
		return AssetLocation;
	}

	@Transactional
	@Override
	public boolean insertSupportMaterial(SupportMaterial sm) {
		logger.info("In AssetDAOImpl :: insertSupportMaterial Start.......");

		SPCallingParams pLessonId = LCMS_Util.createSPObject("@CONTENTOBJECT_ID", String.valueOf(sm.getContentObjectId()) , Integer.class, ParameterMode.IN);
		SPCallingParams pCourseId = LCMS_Util.createSPObject("@COURSE_ID", String.valueOf(sm.getCourseId()) , Integer.class, ParameterMode.IN);
		SPCallingParams pType = LCMS_Util.createSPObject("@TYPE", String.valueOf(sm.getType()) , String.class, ParameterMode.IN);
		SPCallingParams pId = LCMS_Util.createSPObject("@ID", String.valueOf(""), Integer.class, ParameterMode.OUT);
		SPCallingParams pNewId = LCMS_Util.createSPObject("@NEWID", String.valueOf(""), Integer.class, ParameterMode.OUT);
		SPCallingParams pCreatedUserId = LCMS_Util.createSPObject("@CreateUserId", String.valueOf(sm.getCreatedUserId()) , Integer.class, ParameterMode.IN);
		SPCallingParams pAssetId = LCMS_Util.createSPObject("@ASSET_ID", String.valueOf(sm.getAssetId()) , Integer.class, ParameterMode.IN);
		SPCallingParams pSequance = LCMS_Util.createSPObject("@Sequence", String.valueOf(sm.getSequence()) , Integer.class, ParameterMode.IN);

		StoredProcedureQuery qr = createQueryParameters("INSERT_COURSEMATERIALS", pLessonId, pCourseId, pType, pId, pNewId,
                pCreatedUserId, pAssetId, pSequance);
		try
		{
			qr.execute();
		}catch (Exception ex){
				ex.printStackTrace();
		}
		logger.info("In AssetDAOImpl :: insertSupportMaterial  End.......");
		return true;
	}


	@Override
	@Transactional
	public List<SupportMaterial> getSupportMaterialListByContObject (int contObjectId){
		Query query = entityManager.createQuery(" FROM SupportMaterial WHERE contentObjectId=:CONTENTOBJECT_ID ORDER BY sequence");

		query.setParameter("CONTENTOBJECT_ID", contObjectId);
		List<SupportMaterial> lstCourseMaterial = (List<SupportMaterial>)query.getResultList();

		return lstCourseMaterial;

	}

	@Override
	@Transactional
	public List<SupportMaterial> getSupportMaterialDetailListByContObject(int contObjectId){
		EntityManager entityManager = getEntityManager();
	    //Integer publishedCourseId= -1;
	    StringBuilder sqlquery = new StringBuilder();
	    sqlquery.append(" SELECT CM.ID, CM.ASSET_ID, A.NAME  ");
	    sqlquery.append(" FROM COURSEMATERIALS CM INNER JOIN ASSET A ON CM.ASSET_ID = A.ID ");
	    //sqlquery.append(" LEFT JOIN ASSETVERSION AV ON A.ID=AV.ASSET_ID  ");
	    sqlquery.append(" WHERE CM.CONTENTOBJECT_ID = :contObjectId");

	    javax.persistence.Query query = entityManager.createNativeQuery(sqlquery.toString());
        query.setParameter("contObjectId", contObjectId);
        Object[] loRow = null;
        List<SupportMaterial> lstsm = new ArrayList<SupportMaterial>();
        SupportMaterial objsm = null;
        	Object[] loRows = query.getResultList().toArray();
        	for (Object lo : loRows) {
        		try {
	        		loRow = (Object[]) lo;
					objsm = new SupportMaterial();
					objsm.setId(Integer.valueOf(StringUtil.ifNullReturnZero(loRow[0])));
					objsm.setAssetName(StringUtil.ifNullReturnZero(loRow[2]));
					lstsm.add(objsm);
        		} catch (Exception e) {
					e.printStackTrace();
				}
			}
        	return lstsm;
	}

	@Override
	@Transactional
	public int getSpprtMtrSequanceMax(int lessonId, int courseId){
		Query query = entityManager.createQuery(" select MAX(sequence) FROM SupportMaterial WHERE contentObjectId<=:CONTENTOBJECT_ID and courseId=:COURSEID");
		query.setParameter("CONTENTOBJECT_ID", lessonId);
		query.setParameter("COURSEID", courseId);

		try{
			Integer result = (Integer) query.getSingleResult ();
			return result.intValue();
		}catch(NonUniqueResultException e){
			return 0;
		}
		catch(NullPointerException exNull){
			return 0;
		}
	}

	@Override
	@Transactional
	public int getSpprtMtrIdMax(int lessonId){
		Query query = entityManager.createQuery(" select MAX(id) FROM SupportMaterial WHERE contentObjectId=:CONTENTOBJECT_ID");
		query.setParameter("CONTENTOBJECT_ID", lessonId);
		Integer result = 0;
		try{
			result = (Integer) query.getSingleResult ();
			return result.intValue();
		}catch(NonUniqueResultException e){
			return 0;
		}
		catch(NullPointerException exNull){
			return 0;
		}
	}

	@Override
	@Transactional
	public SupportMaterial getSupportMaterialDetail(int lessonId){
		EntityManager entityManager = getEntityManager();
	    //Integer publishedCourseId= -1;
	    StringBuilder sqlquery = new StringBuilder();
	    sqlquery.append(" SELECT CM.ID, CM.ASSET_ID, A.NAME, A.KEYWORDS, A.DESCRIPTION, A.FILENAME, CM.TYPE, AV.LOCATION  ");
	    sqlquery.append(" FROM COURSEMATERIALS CM INNER JOIN ASSET A ON CM.ASSET_ID = A.ID ");
	    sqlquery.append(" LEFT JOIN ASSETVERSION AV ON A.ID=AV.ASSET_ID  ");
	    sqlquery.append(" WHERE CM.ID = :LessonId AND AV.ID = (SELECT MAX(ID) FROM ASSETVERSION WHERE ASSET_ID = A.ID)");

	    javax.persistence.Query query = entityManager.createNativeQuery(sqlquery.toString());
        query.setParameter("LessonId", lessonId);
        Object[] loRow = null;
        SupportMaterial objsm = new SupportMaterial();

        	Object[] loRows = query.getResultList().toArray();
        	for (Object lo : loRows) {
				loRow = (Object[]) lo;
				try {
					objsm.setAssetName(StringUtil.ifNullReturnZero(loRow[2]));
					objsm.setKeywords(StringUtil.ifNullReturnZero(loRow[3]));
					Clob cm = (Clob )loRow[4];

					objsm.setDescription( StringUtil.clobStringConversion(cm) );
					objsm.setFileName(StringUtil.ifNullReturnZero(loRow[5]));
					objsm.setLocation(StringUtil.ifNullReturnZero(loRow[7]));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
        	return objsm;
	}

	@Override
	@Transactional
	public boolean deleteSupportMaterial(String supMaterialId){
		EntityManager entityManager = getEntityManager();

	    Query query = entityManager.createNativeQuery("Delete FROM COURSEMATERIALS where id=:supMaterialId");
        query.setParameter("supMaterialId", supMaterialId);

        int updateCount = query.executeUpdate();
        entityManager = null;
       if(updateCount>0)
    	   return true;
       else
    	   return false;
	}

	@Override
	@Transactional
	public boolean setSpprtMtrDisplayOrder(int varSMId, int sequence){
		EntityManager entityManager;
	    entityManager = getEntityManager();

	    Query query = entityManager.createQuery("Update SupportMaterial set sequence=:varSequence where id=:varSMId");
        query.setParameter("varSequence", sequence);
        query.setParameter("varSMId", varSMId);

        int updateCount = query.executeUpdate();
        entityManager = null;

	   if(updateCount>0)
		   return true;
	   else
		   return false;
	}

	@Override
	@Transactional
	public List<SupportMaterial> getSupportMaterialListByCourse(int courseId){
		String locationPath = LCMSProperties.getLCMSProperty("code.lcms.assets.URL");
		EntityManager entityManager = getEntityManager();
	    StringBuilder sqlquery = new StringBuilder();
	    sqlquery.append(" SELECT CM.ID, CM.ASSET_ID, A.NAME, A.FILENAME, AV.LOCATION   ");
	    sqlquery.append(" FROM COURSEMATERIALS CM INNER JOIN ASSET A ON CM.ASSET_ID = A.ID  ");
	    sqlquery.append(" LEFT JOIN ASSETVERSION AV ON A.ID=AV.ASSET_ID ");
	    sqlquery.append(" WHERE CM.COURSE_ID = :courseId");
	    sqlquery.append(" AND AV.ID = (SELECT MAX(ID) FROM ASSETVERSION WHERE ASSET_ID = A.ID) ");

	    javax.persistence.Query query = entityManager.createNativeQuery(sqlquery.toString());
        query.setParameter("courseId", courseId);
        Object[] loRow = null;
        List<SupportMaterial> lstsm = new ArrayList<SupportMaterial>();
        SupportMaterial objsm = null;
        	Object[] loRows = query.getResultList().toArray();
        	for (Object lo : loRows) {
        		try {
	        		loRow = (Object[]) lo;
					objsm = new SupportMaterial();
					objsm.setAssetName(StringUtil.ifNullReturnZero(loRow[2]));
					objsm.setFileName(StringUtil.ifNullReturnZero(loRow[3]));
					objsm.setLocation(locationPath + StringUtil.ifNullReturnEmpty(loRow[4]));
					lstsm.add(objsm);
        		} catch (Exception e) {
					e.printStackTrace();
				}
			}
        	return lstsm;
	}

	@Override
	@Transactional
	public List<SupportMaterial> getSupportMaterialListGrtrThenContObject (int courseId, int contObjectId, int direction){
		EntityManager entityManager = getEntityManager();
	    StringBuilder sqlquery = new StringBuilder();
	    sqlquery.append(" SELECT ID, CONTENTOBJECT_ID,	COURSE_ID, SEQUENCE   ");
	    sqlquery.append(" FROM COURSEMATERIALS ");
	    sqlquery.append(" WHERE COURSE_ID = :courseId ");
	    sqlquery.append(" AND CONTENTOBJECT_ID > :contObjectId ");

	    javax.persistence.Query query = entityManager.createNativeQuery(sqlquery.toString());
        query.setParameter("courseId", courseId);
        query.setParameter("contObjectId", contObjectId);
        Object[] loRow = null;
        List<SupportMaterial> lstsm = new ArrayList<SupportMaterial>();
        	Object[] loRows = query.getResultList().toArray();
        	for (Object lo : loRows) {
        		loRow = (Object[]) lo;
        		Query query2 = null;
        		if(direction==1)
        			query2 = entityManager.createQuery("Update SupportMaterial set sequence=sequence+1 where id=:varSMId");
        		else if(direction==-1)
        			query2 = entityManager.createQuery("Update SupportMaterial set sequence=sequence-1 where id=:varSMId");
        		    if(query2!=null) {
                        query2.setParameter("varSMId", Integer.valueOf(loRow[0].toString()));
                        query2.executeUpdate();
                    }

			}
        	return lstsm;
	}

	@Override
	@Transactional
	public Long addAsset(String userName, long ownerId, long authorId, String assetName, String fileName, String assetType,String assetKeywords, String assetDescription, ObjectWrapper<Long> assetVersion) {

		StoredProcedureQuery qr = createQueryParameters("WLCMS_INSERT_ASSET_WITH_DETAILS"
				, LCMS_Util.createSPObject("@ContentOwner_ID", String.valueOf(ownerId), Long.class, ParameterMode.IN)
				, LCMS_Util.createSPObject("@AuthorID", String.valueOf(authorId), Long.class, ParameterMode.IN)
				, LCMS_Util.createSPObject("@AssetName", String.valueOf(assetName), String.class, ParameterMode.IN)
				, LCMS_Util.createSPObject("@FileName", String.valueOf(fileName), String.class, ParameterMode.IN)
				, LCMS_Util.createSPObject("@AssetType", String.valueOf(assetType), String.class, ParameterMode.IN)
				, LCMS_Util.createSPObject("@AssetKeywords", String.valueOf(assetKeywords), String.class, ParameterMode.IN)
				, LCMS_Util.createSPObject("@AssetDescription", String.valueOf(assetDescription), String.class, ParameterMode.IN)
				, LCMS_Util.createSPObject("@AssetVersionID", null, Long.class, ParameterMode.OUT)
				, LCMS_Util.createSPObject("@AssetID", null, Long.class, ParameterMode.OUT)

		);


		assetVersion.setValue((Long) qr.getOutputParameterValue("@AssetVersionID"));
		return (Long)qr.getOutputParameterValue("@AssetID");
	}

	@Override
	@Transactional
	public boolean updateAssetLocation(long assetVersionId, String location){
		EntityManager entityManager = getEntityManager();

	    Query query = entityManager.createNativeQuery("Update ASSETVERSION set LOCATION =:varLoc where ID=:varId");
        query.setParameter("varId", assetVersionId);
        query.setParameter("varLoc", location);
        return query.executeUpdate() > 0;
	}

	@Override
	@Transactional
	public boolean updateVSCAssetLocation(long assetId, String location) {
		EntityManager entityManager = getEntityManager();
	    Query query = entityManager.createNativeQuery("Update ASSET set VIDEOFILENAME =:varLoc where ID=:varId");
        query.setParameter("varId", assetId);
        query.setParameter("varLoc", location);
        return query.executeUpdate() > 0;
	}

	@Override
	@Transactional
	public boolean updateVSCAssetFileDetail(long userId, long assetId, String location,Long sizeInBytes) {
		EntityManager entityManager = getEntityManager();
	    Query query = entityManager.createNativeQuery("Update ASSET set VIDEOFILENAME =:varLoc,SizeInBytes=:varSize, LastUpdatedDate=:varNow, LastUpdateUser=:varUser where ID=:varId");
        query.setParameter("varId", assetId);
        query.setParameter("varLoc", location);
        query.setParameter("varSize", sizeInBytes);
        query.setParameter("varNow", new Date());
        query.setParameter("varUser", userId);
        return query.executeUpdate() > 0;
	}

	@Override
	@Transactional
	public List<AssetDTO> searchAssets(long contentOwnerId, String text, Map<String,Object> options) {

		boolean fmsOnly = (options.containsKey("fmsOnly") && ((boolean) options.get("fmsOnly")) == true);
		boolean unlinkedOnly = (options.containsKey("unlinkedOnly") && ((boolean) options.get("unlinkedOnly")) == true);

		StoredProcedureQuery qr = createQueryParameters("WLCMS_MANAGE_ASSET_SEARCH"
			, LCMS_Util.createSPObject("@contentOwnerId", String.valueOf(contentOwnerId) , Long.class, ParameterMode.IN)
			, LCMS_Util.createSPObject("@searchText", String.valueOf(text) , String.class, ParameterMode.IN)
			, LCMS_Util.createSPObject("@fmsOnly", String.valueOf(fmsOnly), Boolean.class, ParameterMode.IN)
			, LCMS_Util.createSPObject("@unlinkedOnly", String.valueOf(unlinkedOnly), Boolean.class, ParameterMode.IN)
		);


		List list = qr.getResultList();


		List<AssetDTO> assets = new ArrayList<AssetDTO>();
		for(Object obj : list) {
			Object[] row = (Object[]) obj;

			AssetDTO asset = new AssetDTO();
			asset.setId(Long.parseLong(String.valueOf(row[0])));
			asset.setContentOwnerId(Long.parseLong(String.valueOf(row[1])));
			asset.setName((String) row[2]);
			asset.setSizeInBytes((row[3] != null)? Long.parseLong(String.valueOf(row[3])): 0);
			asset.setVideoLocation((String) row[4]);
			asset.setUpdatedOn((Date) row[5]);
			asset.setFileExtension("mp4");

			if(row[7] != null) {
				asset.setVersionId(Long.parseLong(String.valueOf(row[7])));
			}

			asset.setAssetType((String) row[8]);
			asset.setIsLinked((Boolean)row[9]);
			assets.add(asset);
		}

		return assets;

	}


	@Override
	@Transactional
	public boolean deleteAsset(long assetId, long userId) {


		StoredProcedureQuery qr = createQueryParameters("WLCMS_MANAGE_ASSET_DELETE"
			, LCMS_Util.createSPObject("@assetId", String.valueOf(assetId) , Long.class, ParameterMode.IN)
		);


		qr.execute();
		return true;

	}


	@Override
	@Transactional
	public AssetDTO getAssetDetails(long assetId) {

		EntityManager entityManager = getEntityManager();
	    Query query = entityManager.createNativeQuery("SELECT a.ID,a.CONTENTOWNER_ID,a.NAME,a.SizeInBytes AS SizeInBytes,a.VIDEOFILENAME,a.LastUpdatedDate,a.LastUpdateUser,a.CURRENT_ASSETVERSION_ID,a.ASSETTYPE, d.duration " +
				"\n FROM ASSET a " +
				"\n LEFT JOIN assetversion v ON a.id = v.asset_id " +
				"\n LEFT JOIN assetversiondetail d ON v.id = d.assetversion_id AND a.assettype = 'VSC' " +
				"\n WHERE a.ID = :varId");
	    query.setParameter("varId", assetId);

	    Object obj = query.getSingleResult();
		Object[] row = (Object[]) obj;
		AssetDTO asset = new AssetDTO();
		asset.setId(Long.parseLong(String.valueOf(row[0])));
		asset.setContentOwnerId(Long.parseLong(String.valueOf(row[1])));
		asset.setName((String) row[2]);
		asset.setSizeInBytes((row[3] != null)? Long.parseLong(String.valueOf(row[3])): 0);
		asset.setVideoLocation((String) row[4]);
		asset.setUpdatedOn((Date) row[5]);
		//asset.setVersionId(Long.parseLong(String.valueOf(row[6])));

		if(row[7] != null) {
			asset.setVersionId(Long.parseLong(String.valueOf(row[7])));
		}

		asset.setAssetType((String) row[8]);
		asset.setDuration(row[9]==null || row[9].toString().trim().equals("") ? 0 : Integer.parseInt(row[9].toString().trim()));
		return asset;

	}


	@Override
	@Transactional
	public long getFMSUsedSpaceInBytes(long ownerId) {
		Query query = entityManager.createNativeQuery("SELECT SUM(sizeInBytes) FROM ASSET where CONTENTOWNER_ID=:varOwnerId");
		query.setParameter("varOwnerId", ownerId);
		Object space = query.getSingleResult();
		return space == null ? 0 : Long.parseLong(String.valueOf(space));
	}

    @Override
    @Transactional
    public String getAssetLocationByVersonId(long versionId) {
        Query query = entityManager.createNativeQuery("select location from ASSETVERSION where id =:versionId");
		query.setParameter("versionId", versionId);
		return (String) query.getSingleResult();
    }



}
