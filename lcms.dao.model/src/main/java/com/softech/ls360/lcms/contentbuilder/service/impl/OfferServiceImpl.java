package com.softech.ls360.lcms.contentbuilder.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.softech.ls360.lcms.contentbuilder.dao.OfferDAO;
import com.softech.ls360.lcms.contentbuilder.model.CoursePricing;
import com.softech.ls360.lcms.contentbuilder.model.Offer;
import com.softech.ls360.lcms.contentbuilder.service.IOfferService;

public class OfferServiceImpl implements IOfferService{

	@Autowired
	OfferDAO offerDAO;
	
	@Override
	public Offer newOffer(Offer objOffer) throws Exception {
		return offerDAO.newOffer(objOffer);
	}

	@Override
	public void remakeOffer(Offer objOffer) throws Exception {
		offerDAO.remakeOffer(objOffer);
	}

	@Override
	public void cancelOffer(Offer objOffer) throws Exception {
		offerDAO.cancelOffer(objOffer);
	}
	
	public Offer getOffer (Offer offer) throws Exception {
		return offerDAO.getOffer(offer);
	}

	public OfferDAO getOfferDAO() {
		return offerDAO;
	}

	public void setOfferDAO(OfferDAO offerDAO) {
		this.offerDAO = offerDAO;
	}
	
	

}
