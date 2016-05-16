package com.softech.ls360.lcms.contentbuilder.dao;

import com.softech.ls360.lcms.contentbuilder.model.Offer;

public interface OfferDAO extends GenericDAO<Offer>{
	
	
	public Offer newOffer(Offer offer) throws Exception;
	public Offer getOffer(Offer offer) throws Exception;
	
}
