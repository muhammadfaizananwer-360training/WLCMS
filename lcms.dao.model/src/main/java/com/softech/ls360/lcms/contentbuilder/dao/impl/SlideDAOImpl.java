package com.softech.ls360.lcms.contentbuilder.dao.impl;

import com.softech.ls360.lcms.contentbuilder.dao.GenericDAOImpl;
import com.softech.ls360.lcms.contentbuilder.dao.SPCallingParams;
import com.softech.ls360.lcms.contentbuilder.dao.SlideDAO;
import com.softech.ls360.lcms.contentbuilder.model.Slide;
import com.softech.ls360.lcms.contentbuilder.model.SlideAsset;
import com.softech.ls360.lcms.contentbuilder.utils.LCMSProperties;
import com.softech.ls360.lcms.contentbuilder.utils.LCMS_Util;
import com.softech.ls360.lcms.contentbuilder.utils.StringUtil;
import com.softech.ls360.lcms.contentbuilder.utils.TypeConvertor;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SlideDAOImpl extends GenericDAOImpl<Slide> implements SlideDAO {


    private static Logger logger = Logger.getLogger(SlideDAOImpl.class);

    @Transactional
    public List<Slide> getSlides(int iContentObjectID) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }


    // return same object with updated Slide ID in the same bean
    @Transactional
    public Slide addSlide(Slide objSlide) throws SQLException {

        logger.info("In SlideDAOImpl::addSlide Begin");
        StoredProcedureQuery qr = null;
        try {

            //get scene templae id
            if (objSlide.getTemplateID() == 0) {
                SPCallingParams sparam_1 = LCMS_Util.createSPObject("TEMPLATE_NAME", String.valueOf(objSlide.getTemplateName()), String.class, ParameterMode.IN);
                SPCallingParams sparam_2 = LCMS_Util.createSPObject("CONTENTOWNER_ID", String.valueOf(objSlide.getContentOwner_ID()), Integer.class, ParameterMode.IN);

                //StoredProcedureQuery qr = createQueryParameters("LCMS_WEB_GET_SCENE_TEMPLATE", sparam1, sparam2);
                Object[] courseRows = callStoredProc("LCMS_WEB_GET_SCENE_TEMPLATE", sparam_1, sparam_2).toArray();
                if (courseRows != null && courseRows.length > 0) {
                    objSlide.setTemplateID(Long.parseLong(courseRows[0].toString()));
                }
            }

            SPCallingParams sparam1 = LCMS_Util.createSPObject("NAME", String.valueOf(objSlide.getName()), String.class, ParameterMode.IN);
            SPCallingParams sparam2 = LCMS_Util.createSPObject("CONTENTOWNER_ID", String.valueOf(objSlide.getContentOwner_ID()), Integer.class, ParameterMode.IN);
            SPCallingParams sparam3 = LCMS_Util.createSPObject("SCENE_GUID", objSlide.getSceneGUID(), String.class, ParameterMode.IN);
            SPCallingParams sparam4 = LCMS_Util.createSPObject("COURSE_ID", String.valueOf(objSlide.getCourse_ID()), Integer.class, ParameterMode.IN);
            SPCallingParams sparam5 = LCMS_Util.createSPObject("CreateUserId", objSlide.getCreateUserId().toString(), Integer.class, ParameterMode.IN);
            SPCallingParams sparam6 = LCMS_Util.createSPObject("ASSET_ID", objSlide.getAsset_ID().toString(), Integer.class, ParameterMode.IN);
            SPCallingParams sparam7 = LCMS_Util.createSPObject("ORIENTATIONKEY", objSlide.getOrientationKey(), String.class, ParameterMode.IN);
            SPCallingParams sparam8 = LCMS_Util.createSPObject("DURATION", String.valueOf(objSlide.getDuration()), Integer.class, ParameterMode.IN);
            SPCallingParams sparam9 = LCMS_Util.createSPObject("SCENE_ID", String.valueOf(objSlide.getScene_Id()), Integer.class, ParameterMode.OUT);
            SPCallingParams sparam10 = LCMS_Util.createSPObject("ContentObject_Id", String.valueOf(objSlide.getContentObject_id()), Integer.class, ParameterMode.IN);
            SPCallingParams sparam11 = LCMS_Util.createSPObject("TEMPLATE_ID", String.valueOf(objSlide.getTemplateID()), Integer.class, ParameterMode.IN);
            SPCallingParams sparam12 = LCMS_Util.createSPObject("CONTENTOBJECT_SCENE_ID", String.valueOf(objSlide.getContentObjectScene_id()), Integer.class, ParameterMode.OUT);
            SPCallingParams sparam13 = LCMS_Util.createSPObject("NAMEVISIBLETF", String.valueOf(objSlide.getNameVisbileTF()), Boolean.class, ParameterMode.IN);
            SPCallingParams sparam14 = LCMS_Util.createSPObject("DISPLAYSTANDARDTF", String.valueOf(objSlide.getDisplayStandardTF()), Boolean.class, ParameterMode.IN);
            SPCallingParams sparam15 = LCMS_Util.createSPObject("DISPLAYWIDESCREENTF", String.valueOf(objSlide.getDisplayWideScreenTF()), Boolean.class, ParameterMode.IN);


            qr = createQueryParameters("LCMS_WEB_INSERT_SCENE", sparam1, sparam2, sparam3, sparam4, sparam5, sparam6, sparam7, sparam8, sparam9, sparam10, sparam11, sparam12, sparam13, sparam14, sparam15);


            qr.execute();
        } catch (Exception ex) {
            Integer sceneID = null;
            if (qr != null) {
                sceneID = (Integer) qr.getOutputParameterValue("SCENE_ID");
                objSlide.setId(sceneID);
                objSlide.setContentObjectScene_id((Integer) qr.getOutputParameterValue("CONTENTOBJECT_SCENE_ID"));
            }

            logger.error(ex.getMessage());
            try {
                throw ex;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        Integer sceneID = (Integer) qr.getOutputParameterValue("SCENE_ID");
        objSlide.setId(sceneID);
        objSlide.setContentObjectScene_id((Integer) qr.getOutputParameterValue("CONTENTOBJECT_SCENE_ID"));

        logger.info("In SlideDAOImpl::addSlide END");

        return objSlide;
    }

    @Transactional
    public Slide deleteSlide(Slide objSlide) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Transactional
    public Slide getSlide(long slideId) throws Exception {
        logger.info("In SlideDAOImpl::getSlide Start");
        Slide dto;
        SPCallingParams sparam1 = LCMS_Util.createSPObject("COURSE_ID", String.valueOf(slideId), Integer.class, ParameterMode.IN);

        Object[] courseRow;
        Object[] courseRows = callStoredProc("[SELECT_SCENE]", sparam1).toArray();

        dto = new Slide();

        try {
            for (Object course : courseRows) {

                courseRow = (Object[]) course;

                dto.setId(Long.parseLong(StringUtil.ifNullReturnZero(courseRow[0])));
                dto.setName(courseRow[1].toString());
                dto.setDuration(Long.parseLong(StringUtil.ifNullReturnZero(courseRow[5])));
                dto.setTemplateID(Long.parseLong(StringUtil.ifNullReturnZero(courseRow[4])));
                dto.setDisplayStandardTF(courseRow[22] == null ? Boolean.FALSE : TypeConvertor.AnyToBoolean(courseRow[22].toString()));
                dto.setDisplayWideScreenTF(courseRow[23] == null ? Boolean.FALSE : TypeConvertor.AnyToBoolean(courseRow[23].toString()));
                dto.setEmbedCode((String) courseRow[24]);
                dto.setIsEmbedCode(Boolean.valueOf(courseRow[25].toString()));


                //get template name
                SPCallingParams sparam2 = LCMS_Util.createSPObject("TEMPLATE_ID", String.valueOf(dto.getTemplateID()), Integer.class, ParameterMode.IN);
                SPCallingParams sparam3 = LCMS_Util.createSPObject("CONTENTOWNER_ID", String.valueOf(courseRow[3]), Integer.class, ParameterMode.IN);

                Object[] courseRows_T = callStoredProc("LCMS_WEB_GET_SCENE_TEMPLATE_NAME", sparam2, sparam3).toArray();
                if (courseRows_T != null && courseRows_T.length > 0) {

                    dto.setTemplateName((((Object[]) courseRows_T[0])[0].toString()));
                    dto.setSlideTemplateURL((((Object[]) courseRows_T[0])[1].toString()));
                    dto.setTemplateType((((Object[]) courseRows_T[0])[2].toString()));
                    logger.info("In SlideDAOImpl::TEMPLATE : " + dto.getTemplateName());
                }
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error("In SlideDAOImpl::getSlide ..................Exception..................");
            e.printStackTrace();
        }
        logger.info("In SlideDAOImpl::getSlide END");

        return dto;
    }

    @Transactional
    public Slide updateSlide(Slide objSlide) throws Exception {

        logger.info("In SlideDAOImpl::updateSlide Begin");

        SPCallingParams sparam1 = LCMS_Util.createSPObject("ID", objSlide.getId() + "", Integer.class, ParameterMode.IN);
        SPCallingParams sparam2 = LCMS_Util.createSPObject("NAME", String.valueOf(objSlide.getName()), String.class, ParameterMode.IN);
        SPCallingParams sparam3 = LCMS_Util.createSPObject("DURATION", objSlide.getDuration() + "", Integer.class, ParameterMode.IN);
        SPCallingParams sparam4 = LCMS_Util.createSPObject("DISPLAYSTANDARDTF", String.valueOf(objSlide.getDisplayStandardTF()), Boolean.class, ParameterMode.IN);
        SPCallingParams sparam5 = LCMS_Util.createSPObject("DISPLAYWIDESCREENTF", String.valueOf(objSlide.getDisplayWideScreenTF()), Boolean.class, ParameterMode.IN);
        SPCallingParams sparam6 = LCMS_Util.createSPObject("LastUpdateUser", String.valueOf(objSlide.getLastUpdateUser()), Integer.class, ParameterMode.IN);

        StoredProcedureQuery qr = createQueryParameters("[LCMS_WEB_UPDATE_SCENE]", sparam1, sparam2, sparam3, sparam4, sparam5, sparam6);
        qr.execute();

        //Integer sceneID = (Integer)qr.getOutputParameterValue("SCENE_ID");
        //objSlide.setId( sceneID);

        logger.info("In SlideDAOImpl::updateSlide END");

        return objSlide;
    }

    @Transactional
    public Slide updateSelectedSlideTemplate(Slide objSlide) throws Exception {

        logger.info("In SlideDAOImpl::updateSelectedSlideTemplate Begin");

        SPCallingParams sparam1 = LCMS_Util.createSPObject("ID", objSlide.getId() + "", Integer.class, ParameterMode.IN);
        SPCallingParams sparam2 = LCMS_Util.createSPObject("@TEMPLATEID", String.valueOf(objSlide.getTemplateID()), Integer.class, ParameterMode.IN);


        StoredProcedureQuery qr = createQueryParameters("[LCMS_WEB_UPDATE_SCENE_Template]", sparam1, sparam2);
        qr.execute();

        logger.info("In SlideDAOImpl::updateSelectedSlideTemplate END");

        return objSlide;
    }

    @Transactional
    public List<SlideAsset> getSlideTextAgainstSlideId(long varSlideId) {

        logger.info("In SlideDAOImpl::GetSlideText Begin");

        List<SlideAsset> coursesDTOList = new ArrayList<SlideAsset>();
        SlideAsset dto;
        SPCallingParams sparam1 = LCMS_Util.createSPObject("SCENE_ID", String.valueOf(varSlideId), Integer.class, ParameterMode.IN);

        Object[] courseRows = callStoredProc("LCMS_WEB_SELECT_SCENE_ASSETTEXT", sparam1).toArray();

        Object[] courseRow;

        try {
            for (Object slideAsset : courseRows) {
                courseRow = (Object[]) slideAsset;

                dto = new SlideAsset();

                dto.setId(TypeConvertor.AnyToInteger(courseRow[0]));//TypeConvertor.AnyToLong(courseRow[0]));
                dto.setName(courseRow[1].toString());
                dto.setKeywords(courseRow[2].toString());
                dto.setAssettype(courseRow[3].toString());
                Clob cb = (Clob) courseRow[5];
                dto.setContent(StringUtil.clobStringConversion(cb));
                coursesDTOList.add(dto);

            }
        } catch (Exception ex) {
            logger.error(ex);
        }

        logger.info("In SlideDAOImpl::GetSlideText END");
        return coursesDTOList;
    }

    @Transactional
    public List<SlideAsset> getSlideCloseCaptionAgainstSlideId(long varSlideId) {

        logger.info("In SlideDAOImpl::GetSlideCloseCaption Begin");

        List<SlideAsset> coursesDTOList = new ArrayList<SlideAsset>();
        SlideAsset dto;
        SPCallingParams sparam1 = LCMS_Util.createSPObject("SCENE_ID", String.valueOf(varSlideId), Integer.class, ParameterMode.IN);

        Object[] courseRows = callStoredProc("LCMS_WEB_SELECT_SCENE_ASSETCLOSECAPTION", sparam1).toArray();

        Object[] courseRow;

        try {
            for (Object slideAsset : courseRows) {
                courseRow = (Object[]) slideAsset;

                dto = new SlideAsset();

                dto.setId(TypeConvertor.AnyToLong(courseRow[0]));
                dto.setName(courseRow[1].toString());
                dto.setKeywords(courseRow[2].toString());
                dto.setAssettype(courseRow[3].toString());
                Clob cb = (Clob) courseRow[5];
                dto.setContent(StringUtil.clobStringConversion(cb));
                coursesDTOList.add(dto);

            }
        } catch (Exception ex) {
            logger.error(ex);
        }

        logger.info("In SlideDAOImpl::GetSlideCloseCaption END");
        return coursesDTOList;
    }


    @Transactional
    public List<SlideAsset> getVisualAssetBySlideId(long varSlideId, String assetType) {

        logger.info("In SlideDAOImpl::GetSlideCloseCaption Begin");

        List<SlideAsset> coursesDTOList = new ArrayList<SlideAsset>();
        SlideAsset dto;
        SPCallingParams sparam1 = LCMS_Util.createSPObject("SCENE_ID", String.valueOf(varSlideId), Integer.class, ParameterMode.IN);
        SPCallingParams sparam2 = LCMS_Util.createSPObject("ASSETTYPE", String.valueOf(assetType), String.class, ParameterMode.IN);

        Object[] courseRows = callStoredProc("LCMS_WEB_SELECT_SCENE_ASSET", sparam1, sparam2).toArray();

        Object[] courseRow;

        try {
            for (Object slideAsset : courseRows) {
                courseRow = (Object[]) slideAsset;

                dto = new SlideAsset();

                dto.setId(Long.parseLong(StringUtil.ifNullReturnZero(courseRow[0])));
                dto.setName(StringUtil.ifNullReturnEmpty(courseRow[1]));
                dto.setAssettype(StringUtil.ifNullReturnEmpty(courseRow[3]));
                // Height + Width = Dimension
                dto.setHeight(StringUtil.ifNullReturnZero(courseRow[8]));
                dto.setWidth(StringUtil.ifNullReturnZero(courseRow[9]));
                String sizeInKb = StringUtil.ifNullReturnZero(courseRow[12]);
                if (StringUtil.isNumber(sizeInKb)) {
                    Long size = (TypeConvertor.AnyToLong(sizeInKb) / 1024);
                    sizeInKb = size.toString() + "KB";
                }
                dto.setSize(sizeInKb);
                dto.setVersion(StringUtil.ifNullReturnZero(courseRow[4]));
                dto.setDuration(Integer.parseInt(StringUtil.ifNullReturnZero(courseRow[11])));
                dto.setVersionId(Integer.parseInt(StringUtil.ifNullReturnZero(courseRow[7])));

                if (courseRow[10] != null) {
                    Clob cm = (Clob) courseRow[10];
                    dto.setDescription(StringUtil.clobStringConversion(cm));
                }

                coursesDTOList.add(dto);

            }
        } catch (Exception ex) {
            logger.error(ex);
        }

        logger.info("In SlideDAOImpl::GetSlideCloseCaption END");
        return coursesDTOList;
    }

    @Transactional
    @Override
    public List<SlideAsset> getSlideAssetSearch(String criteria, int ContentOwnerID, int assetype) throws SQLException {
        SPCallingParams sparam1 = LCMS_Util.createSPObject("NAME", criteria, String.class, ParameterMode.IN);
        SPCallingParams sparam2 = LCMS_Util.createSPObject("CONTENTOWNER_ID", String.valueOf(ContentOwnerID), Integer.class, ParameterMode.IN);
        SPCallingParams sparam3 = LCMS_Util.createSPObject("ASSETTYPE", String.valueOf(assetype), Integer.class, ParameterMode.IN);

        StoredProcedureQuery qr = createQueryParameters("LCMS_WEB_SEARCH_ASSET", sparam1, sparam2, sparam3);
        Object[] rows = qr.getResultList().toArray();

        List<SlideAsset> lstAsset = this.convertSlideAssetSearch(rows, assetype);
        return lstAsset;
        //return (List<SlideAsset>) qr.getResultList();
    }

    @Transactional
    public SlideAsset updateSlideText(SlideAsset objSlideAsset) throws Exception {
        logger.info("In SlideDAOImpl::updateSlideText Begin");

        Integer loginUserId = objSlideAsset.getCreateUser_id();
        SPCallingParams sparam1 = LCMS_Util.createSPObject("SCENEID", objSlideAsset.getId() + "", Integer.class, ParameterMode.IN);
        SPCallingParams sparam2 = LCMS_Util.createSPObject("CONTENT", String.valueOf(objSlideAsset.getContent()), String.class, ParameterMode.IN);
        SPCallingParams sparam3 = LCMS_Util.createSPObject("ORIENTATIONKEY", String.valueOf(objSlideAsset.getOrientationkey()), String.class, ParameterMode.IN);
        SPCallingParams sparam4 = LCMS_Util.createSPObject("USER_ID", loginUserId.toString(), Integer.class, ParameterMode.IN);
        SPCallingParams sparam5 = LCMS_Util.createSPObject("LastUpdateUser", objSlideAsset.getLastUpdateUser().toString(), Integer.class, ParameterMode.IN);

        StoredProcedureQuery qr = createQueryParameters("LCMS_WEB_UPDATE_SCENE_TEXTANDCLOSECAPTION", sparam1, sparam2, sparam3, sparam4, sparam5);
        qr.execute();

        //Integer sceneID = (Integer)qr.getOutputParameterValue("SCENE_ID");
        //objSlide.setId( sceneID);

        logger.info("In SlideDAOImpl::updateSlideText END");

        return objSlideAsset;
    }

    @Transactional
    public SlideAsset updateSlideCloseCaption(SlideAsset objSlideAsset) throws Exception {

        logger.info("In SlideDAOImpl::updateSlideCloseCaption Begin");

        SPCallingParams sparam1 = LCMS_Util.createSPObject("SCENEID", objSlideAsset.getId() + "", Integer.class, ParameterMode.IN);
        SPCallingParams sparam2 = LCMS_Util.createSPObject("CONTENT", String.valueOf(objSlideAsset.getContent()), String.class, ParameterMode.IN);
        SPCallingParams sparam3 = LCMS_Util.createSPObject("ORIENTATIONKEY", String.valueOf(objSlideAsset.getOrientationkey()), String.class, ParameterMode.IN);
        SPCallingParams sparam4 = LCMS_Util.createSPObject("USER_ID", String.valueOf(objSlideAsset.getCreateUser_id()), Integer.class, ParameterMode.IN);
        SPCallingParams sparam5 = LCMS_Util.createSPObject("LastUpdateUser", objSlideAsset.getLastUpdateUser().toString(), Integer.class, ParameterMode.IN);

        StoredProcedureQuery qr = createQueryParameters("LCMS_WEB_UPDATE_SCENE_TEXTANDCLOSECAPTION", sparam1, sparam2, sparam3, sparam4, sparam5);
        qr.execute();

        //Integer sceneID = (Integer)qr.getOutputParameterValue("SCENE_ID");
        //objSlide.setId( sceneID);

        logger.info("In SlideDAOImpl::updateSlideCloseCaption END");

        return objSlideAsset;
    }

    @Override
    @Transactional
    public SlideAsset insertSelectedAsset(SlideAsset slideAsset) throws SQLException {

        SPCallingParams sparam1 = LCMS_Util.createSPObject("SCENE_ID", String.valueOf(slideAsset.getScene_id()), Integer.class, ParameterMode.IN);
        SPCallingParams sparam2 = LCMS_Util.createSPObject("ASSET_ID", String.valueOf(slideAsset.getId()), Integer.class, ParameterMode.IN);
        SPCallingParams sparam3 = LCMS_Util.createSPObject("ORIENTATIONKEY", slideAsset.getOrientationkey(), String.class, ParameterMode.IN);
        SPCallingParams sparam4 = LCMS_Util.createSPObject("ASSETVERSION_ID", String.valueOf(slideAsset.getAssetversion_id()), Integer.class, ParameterMode.IN);
        SPCallingParams sparam5 = LCMS_Util.createSPObject("CreateUserId", String.valueOf(slideAsset.getCreateUser_id()), Integer.class, ParameterMode.IN);

//		// Delete any asset that was uploaded previously.
//		StoredProcedureQuery qr1 = createQueryParameters("[LCMS_WEB_DELETE_SCENE_ASSET]", sparam1, sparam2);
//		qr1.execute();

        StoredProcedureQuery qr = createQueryParameters("LCMS_WEB_INSERT_SCENE_ASSET", sparam1, sparam2, sparam3, sparam4, sparam5);
        qr.execute();
        return slideAsset;
    }

    @Transactional
    public List<SlideAsset> convertSlideAssetSearch(Object[] courseRows, int assettype) {
        List<SlideAsset> slideList = new ArrayList<SlideAsset>();
        Object[] courseRow;
        SlideAsset dto;
        String locationPath = "";


        try {
            for (Object slideAsset : courseRows) {
                courseRow = (Object[]) slideAsset;

                dto = new SlideAsset();

                dto.setId(TypeConvertor.AnyToLong(courseRow[0]));
                dto.setName(courseRow[1].toString());
                dto.setAssettype(courseRow[2].toString());
                dto.setHeight(StringUtil.ifNullReturnEmpty(courseRow[3]));
                dto.setWidth(StringUtil.ifNullReturnEmpty(courseRow[4]));
                dto.setVersion(StringUtil.ifNullReturnEmpty(courseRow[5]));
                dto.setAssetversion_id(TypeConvertor.AnyToInteger(courseRow[6]));
                dto.setDuration(TypeConvertor.AnyToInteger(courseRow[7]));
                if (assettype == 3) {
                    locationPath = LCMSProperties.getLCMSProperty("lcms.preview.streaming");
                    dto.setLocation(locationPath + StringUtil.ifNullReturnEmpty(courseRow[10]));
                } else {
                    locationPath = LCMSProperties.getLCMSProperty("code.lcms.assets.URL");
                    dto.setLocation(locationPath + StringUtil.ifNullReturnEmpty(courseRow[8]));
                }


                Clob cb = (Clob) courseRow[9];
                dto.setDescription(StringUtil.clobStringConversion(cb));
                slideList.add(dto);
            }
        } catch (Exception ex) {
            logger.error(ex);
        }
        return slideList;
    }
    @Transactional
    @Override
    public SlideAsset detachAsset(SlideAsset slideAsset) {

        SPCallingParams sparam1 = LCMS_Util.createSPObject("SCENE_ID", String.valueOf(slideAsset.getScene_id()), Integer.class, ParameterMode.IN);
        SPCallingParams sparam2 = LCMS_Util.createSPObject("ASSETVERSION_ID", String.valueOf(slideAsset.getAssetversion_id()), Integer.class, ParameterMode.IN);

        StoredProcedureQuery qr = createQueryParameters("DELETE_SCENE_ASSET", sparam1, sparam2);
        qr.execute();

        return slideAsset;
    }

    @Transactional
    public boolean deleteSlide(String slideId, String courseId) throws SQLException {
        logger.info("In SlideDAOImpl::deleteSlide --- Begin");

        try {
            SPCallingParams sparam1 = LCMS_Util.createSPObject("SCENE_ID", slideId, Integer.class, ParameterMode.IN);
            SPCallingParams sparam2 = LCMS_Util.createSPObject("COURSE_ID", courseId, Integer.class, ParameterMode.IN);

            StoredProcedureQuery qr = createQueryParameters("LCMS_WEB_DELETE_SCENE", sparam1, sparam2);
            qr.execute();
        } catch (Exception ex) {
            logger.error(ex);
            return false;
        }
        logger.info("In SlideDAOImpl::deleteSlide --- END");

        return true;
    }

    @Transactional
    @Override
    public Map<String, String> isSlidComponentHasData(Slide objSlide) throws Exception {

        Map<String, String> map = new HashMap<String, String>();

        try {

            SPCallingParams sparam1 = LCMS_Util.createSPObject("SCENE_ID", String.valueOf(objSlide.getId()), Integer.class, ParameterMode.IN);

            Object[] courseRow;
            Object[] courseRows = callStoredProc("[LCMS_WEB_ICP_SELECT_SCENE_ASSET_EXISTS]", sparam1).toArray();

            if (courseRows != null && courseRows.length > 0) {

                for (Object slideAsset : courseRows) {
                    courseRow = (Object[]) slideAsset;

                    String asstType = courseRow[6].toString();
                    String asstType2 = courseRow[2].toString();

                    if (!StringUtils.isEmpty(asstType)) {

                        if ("$Audio".equals(asstType))
                            map.put("slideAudioIcon", "true");
                        if ("$VisualTop".equals(asstType))
                            map.put("slideVideoIcon", "true");
                        if ("$Text".equals(asstType))
                            map.put("slideTextIcon", "true");
                        if ("$VOText".equals(asstType))
                            map.put("closeCaptionIcon", "true");
                        if (asstType2.equalsIgnoreCase("VSC"))
                            map.put("VSC", "true");
                    }


                }

            }

        } catch (Exception ex) {
            logger.error(ex);
        }

        return map;

    }


    @Override
    @Transactional
    public boolean updateSlideMC_SCENE_XML(SlideAsset objSlideAsset) {
        logger.info("In SlideDAOImpl::updateSlideMC_SCENE_XML --- Begin");

        try {
            SPCallingParams sparam1 = LCMS_Util.createSPObject("SCENE_ID", String.valueOf(objSlideAsset.getScene_id()), Integer.class, ParameterMode.IN);
            SPCallingParams sparam2 = LCMS_Util.createSPObject("MC_SCENE_XML", objSlideAsset.getMC_SCENE_XML(), String.class, ParameterMode.IN);
            System.out.println("getMC_SCENE_XML:::" + objSlideAsset.getScene_id());
            StoredProcedureQuery qr = createQueryParameters("LCMS_WEB_SCENE_UPDATE_MC_SCENE_XML", sparam1, sparam2);
            qr.execute();
        } catch (Exception ex) {
            logger.error(ex);
            return false;
        }
        logger.info("In SlideDAOImpl::updateSlideMC_SCENE_XML --- END");

        return true;
    }


    @Override
    @Transactional
    public String getSlide_MC_SCENE_XML_ById(long slideId) throws Exception {
        // TODO Auto-generated method stub
        logger.info("In SlideDAOImpl::getSlide_MC_SCENE_XML_ById --- Begin");
        String MC_SCENE_XML = "";

        try {
            SPCallingParams sparam1 = LCMS_Util.createSPObject("SCENE_ID", String.valueOf(slideId), Integer.class, ParameterMode.IN);
            System.out.println("getSlide_MC_SCENE_XML_ById:::" + slideId);
            Object[] courseRows = callStoredProc("LCMS_WEB_SCENE_GET_MC_SCENE_XML", sparam1).toArray();

            if (courseRows != null && courseRows.length > 0 && courseRows[0] != null) {

                MC_SCENE_XML = clobToString((Clob) courseRows[0]);
                System.out.println("MC_Scene_XML::" + MC_SCENE_XML);
            }
        } catch (Exception ex) {
            logger.error(ex.getCause());
            return null;
        }
        logger.info("In SlideDAOImpl::getSlide_MC_SCENE_XML_ById --- END");
        return MC_SCENE_XML;

    }

    private String clobToString(Clob data) {
        StringBuilder sb = new StringBuilder();
        try {
            Reader reader = data.getCharacterStream();
            BufferedReader br = new BufferedReader(reader);

            String line;
            while (null != (line = br.readLine())) {
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

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean updateAssetAttribtes(SlideAsset objSlideAsset) throws SQLException {

        EntityManager entityManager;
        entityManager = getEntityManager();

        Query query = entityManager.createNativeQuery("update ASSETVERSIONDETAIL set WIDTH = :assetWidth , height= :assetHeight, duration= :duration " +
                " where ASSETVERSION_ID = (select id from ASSETVERSION where ASSET_ID=:assetId)");
        query.setParameter("assetId", objSlideAsset.getId());
        query.setParameter("assetWidth", objSlideAsset.getWidth());
        query.setParameter("assetHeight", objSlideAsset.getHeight());
        query.setParameter("duration", objSlideAsset.getDuration());

        int updateCount = query.executeUpdate();
        entityManager = null;
        if (updateCount > 0)
            return true;
        else
            return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Integer getTemplateIDForPPTSlide() throws SQLException {

        Query query = getEntityManager().createNativeQuery("SELECT ID  FROM SCENETEMPLATE  WHERE NAME = 'Visual Center' AND CONTENTOWNER_ID=1");

        List result = query.getResultList();
        Integer TemplateID = 0;
        if (result != null && result.size() > 0) {
            // Object[] result2 = (Object[]) result.get(0);
            TemplateID = (Integer) result.get(0);
        }

        return TemplateID;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Integer getVersionIDForUploadedAsset(SlideAsset objSlideAsset) throws SQLException {

        Query query = getEntityManager().createNativeQuery("SELECT CURRENT_ASSETVERSION_ID FROM ASSET WHERE ID= :asset_id");
        query.setParameter("asset_id", objSlideAsset.getId());

        List result = query.getResultList();
        Integer assetID = 0;
        if (result != null && result.size() > 0) {
            // Object[] result2 = (Object[]) result.get(0);
            assetID = (Integer) result.get(0);
        }

        return assetID;
    }

    @Override
    @Transactional
    public boolean updateSlideEmbedCodeandEmbedBit(long slideId, String embedCode, Boolean isEmbedCodeValueUpdate) {

        StringBuilder strBuilder = new StringBuilder("update scene ");


        Map<String, Object> queryParams = new HashMap();

        if ((embedCode == null || embedCode.equals("")) || !isEmbedCodeValueUpdate) {
            strBuilder.append("set isEmbedCode=:isEmbedCode ");
            queryParams.put("isEmbedCode", Boolean.FALSE);

        } else {
            strBuilder.append("set isEmbedCode=:isEmbedCode,EmbedCodeValue=:embedCode ");
            queryParams.put("isEmbedCode", Boolean.TRUE);
            queryParams.put("embedCode", embedCode);
        }

        strBuilder.append("where id=:slideId");
        Query query = getEntityManager().createNativeQuery(strBuilder.toString());
        queryParams.put("slideId", slideId);
        for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        int result = query.executeUpdate();

        return result >= 1 ? true : false;

    }

    @Override
    @Transactional
    public boolean updateSlideEmbedBit(long slideId, boolean isEmbedCode) {
        Query query = getEntityManager().createNativeQuery("update scene set isEmbedCode=:isEmbedCode where id=:slideId");
        query.setParameter("slideId", slideId);
        query.setParameter("isEmbedCode", isEmbedCode);

        int result = query.executeUpdate();

        return result >= 1 ? true : false;


    }

}
