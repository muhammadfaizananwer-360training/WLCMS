package com.softech.ls360.lcms.contentbuilder.service;


import com.softech.ls360.lcms.contentbuilder.model.Offer;

public interface IOfferService {

	public void remakeOffer (Offer offer) throws Exception ;
	public void cancelOffer (Offer offer) throws Exception ;
	public Offer newOffer (Offer offer) throws Exception ;
	public Offer getOffer (Offer offer) throws Exception ;	
	
}
