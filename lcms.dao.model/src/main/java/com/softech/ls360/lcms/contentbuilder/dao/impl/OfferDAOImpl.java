package com.softech.ls360.lcms.contentbuilder.dao.impl;


import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;


import com.softech.ls360.lcms.contentbuilder.dao.GenericDAOImpl;
import com.softech.ls360.lcms.contentbuilder.dao.OfferDAO;
import com.softech.ls360.lcms.contentbuilder.dao.SPCallingParams;
import com.softech.ls360.lcms.contentbuilder.model.Offer;
import com.softech.ls360.lcms.contentbuilder.utils.LCMS_Util;
import com.softech.ls360.lcms.contentbuilder.utils.StringUtil;

import java.util.Date;

public class OfferDAOImpl extends GenericDAOImpl<Offer> implements OfferDAO{
	private static Logger logger = Logger.getLogger(OfferDAOImpl.class);

	@Override
	@Transactional
	public void remakeOffer (Offer offer) throws Exception {
		try {
			Query query = entityManager.createNativeQuery("Update CONTENTOWNER_OFFER set PUBLISH_STATUS = 'Published', LOWEST_PRICE = :LOWEST_PRICE, ROYALTY_AMOUNT = :ROYALTY_AMOUNT, \n" +
					"OFFER_STATUS = :OFFER_STATUS, SUGGESTED_RETAIL_PRICE = :SUGGESTED_RETAIL_PRICE, UPDATEDUSER_ID = :UPDATEDUSER_ID, UPDATEDDATE = :UPDATEDDATE WHERE FROM_CONTENTOWNER_ID= :FROM_CONTENTOWNER_ID AND ORIGINAL_COURSE_ID=:ORIGINAL_COURSE_ID");

			query.setParameter("LOWEST_PRICE", offer.getLowestPrice());
			query.setParameter("ROYALTY_AMOUNT", offer.getRoyaltyAmount());
			query.setParameter("OFFER_STATUS", offer.getOfferStatus());
			query.setParameter("SUGGESTED_RETAIL_PRICE", offer.getSuggestedRetailPrice());
			query.setParameter("UPDATEDUSER_ID", offer.getCreatedUserId());
			query.setParameter("UPDATEDDATE", new Date());
			query.setParameter("FROM_CONTENTOWNER_ID", offer.getFromContentownerId());
			query.setParameter("ORIGINAL_COURSE_ID", offer.getOriginalCourseId());

			query.executeUpdate();

		} catch (Exception exp) {
			logger.error(" Error in method::remakeOffer. Message:" + exp.getMessage());
			throw exp;
		}

	}

	@Override
	@Transactional
	public void cancelOffer (Offer offer) throws Exception {
		try {
			Query query = entityManager.createNativeQuery("Update CONTENTOWNER_OFFER set OFFER_STATUS = 'Canceled', PUBLISH_STATUS = 'Retired', UPDATEDUSER_ID = :UPDATEDUSER_ID, UPDATEDDATE = :UPDATEDDATE WHERE FROM_CONTENTOWNER_ID= :FROM_CONTENTOWNER_ID AND ORIGINAL_COURSE_ID=:ORIGINAL_COURSE_ID");

			query.setParameter("UPDATEDUSER_ID", offer.getCreatedUserId());
			query.setParameter("UPDATEDDATE", new Date());
			query.setParameter("FROM_CONTENTOWNER_ID", offer.getFromContentownerId());
			query.setParameter("ORIGINAL_COURSE_ID", offer.getOriginalCourseId());

			query.executeUpdate();

		} catch (Exception exp) {
			logger.error(" Error in method::remakeOffer. Message:" + exp.getMessage());
			throw exp;
		}

	}


	@Override
	@Transactional
	public Offer newOffer(Offer offer) throws Exception{
		SPCallingParams sparam1 = LCMS_Util.createSPObject("FROM_CONTENTOWNER_ID",	String.valueOf(offer.getFromContentownerId()), Integer.class, ParameterMode.IN);
		SPCallingParams sparam2 = LCMS_Util.createSPObject("TO_CONTENTOWNER_ID",	String.valueOf(offer.getToContentownerId()), Integer.class, ParameterMode.IN);
		SPCallingParams sparam3 = LCMS_Util.createSPObject("ORIGINAL_COURSE_ID",	String.valueOf(offer.getOriginalCourseId()), Integer.class, ParameterMode.IN);
		SPCallingParams sparam4 = LCMS_Util.createSPObject("LOWEST_PRICE",			String.valueOf(offer.getLowestPrice()), String.class, ParameterMode.IN);
		SPCallingParams sparam5 = LCMS_Util.createSPObject("ROYALTY_AMOUNT",		String.valueOf(offer.getRoyaltyAmount()), String.class, ParameterMode.IN);
		SPCallingParams sparam6 = LCMS_Util.createSPObject("ROYALTY_TYPE",			String.valueOf(offer.getRoyaltyType()), String.class, ParameterMode.IN);
		SPCallingParams sparam7 = LCMS_Util.createSPObject("EXISTING_OFFER_IN_PLACE",	String.valueOf(offer.getExistingOfferInPlace()), Integer.class, ParameterMode.IN);
		SPCallingParams sparam8 = LCMS_Util.createSPObject("CREATEDDATE",			String.valueOf(offer.getCreatedDate()), String.class, ParameterMode.IN);
		SPCallingParams sparam9 = LCMS_Util.createSPObject("CREATEDUSER_ID",		String.valueOf(offer.getCreatedUserId()), Integer.class, ParameterMode.IN);
		SPCallingParams sparam10 = LCMS_Util.createSPObject("OFFER_STATUS",			String.valueOf(offer.getOfferStatus()), String.class, ParameterMode.IN);
		SPCallingParams sparam11 = LCMS_Util.createSPObject("SUGGESTED_RETAIL_PRICE",	String.valueOf(offer.getSuggestedRetailPrice()), String.class, ParameterMode.IN);
		SPCallingParams sparam12 = LCMS_Util.createSPObject("NOTE_TO_DISTRIBUTOR",	String.valueOf(offer.getNoteToDistributor()), String.class, ParameterMode.IN);
		SPCallingParams sparam13 = LCMS_Util.createSPObject("NEWID", String.valueOf("0") , Integer.class, ParameterMode.OUT);
		
		StoredProcedureQuery query = createQueryParameters("INSERT_OFFER", sparam1, sparam2, sparam3, sparam4, sparam5, sparam6, sparam7, 
																					sparam8, sparam9, sparam10, sparam11, sparam12, sparam13);
		query.execute(); 
		
		
		
		SPCallingParams sparam14 = LCMS_Util.createSPObject("AUTHOR_ID",			String.valueOf(offer.getAuthorId()), Integer.class, ParameterMode.IN);
		SPCallingParams sparam15 = LCMS_Util.createSPObject("RETIRED_COURSE_IDS",	String.valueOf("0"), String.class, ParameterMode.OUT);
		SPCallingParams sparam16 = LCMS_Util.createSPObject("RETIRED_COURSE_CONTENTOWNER_IDS",	String.valueOf("0"), String.class, ParameterMode.OUT);
		SPCallingParams sparam17 = LCMS_Util.createSPObject("DISTRIBUTOR_IDS", String.valueOf("0") , String.class, ParameterMode.OUT);
		
		StoredProcedureQuery query2 = createQueryParameters("PUBLISH_OFFER", sparam1, sparam3, sparam14, sparam15, sparam16, sparam17);
		query2.execute(); 


		return offer;
	}
	
	
	@Override
	@Transactional
	public Offer getOffer(Offer offer) throws Exception{
		
		SPCallingParams sparam1 = LCMS_Util.createSPObject("COURSE_ID",	String.valueOf(offer.getOriginalCourseId()), Integer.class, ParameterMode.IN);
		SPCallingParams sparam2 = LCMS_Util.createSPObject("CONTENTOWNER_ID",	String.valueOf(offer.getFromContentownerId()), Integer.class, ParameterMode.IN);
		
		//StoredProcedureQuery query = createQueryParameters("GET_OFFER_BY_COURSE_FOR_SUBCONTENTOWNER", sparam1, sparam2);
		//query.execute(); 
		
		Object[] courseRow;
		Object[] courseRows = callStoredProc("[GET_OFFERS_BY_COURSE]", sparam1,  sparam2).toArray();
		
		
		for (Object course : courseRows) {

			courseRow = (Object[]) course;
			offer.setOfferStatus(StringUtil.ifNullReturnEmpty(courseRow[14].toString()));
			offer.setPublishStatus(StringUtil.ifNullReturnEmpty(courseRow[15].toString()));
			
		}
		return offer;
		
	}
	
	
	
	
	
	
}
