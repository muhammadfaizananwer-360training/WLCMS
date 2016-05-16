package com.softech.ls360.lcms.contentbuilder.model;

public class Offer {


	private long fromContentownerId; 
	private String toContentownerId ;
	private String originalCourseId ;
	private String lowestPrice;
	private String royaltyAmount;
	private String royaltyType ;
	private int existingOfferInPlace;
	private String createdDate;
	private long createdUserId ;
	private String offerStatus;
	private String suggestedRetailPrice;
	private String noteToDistributor;
	private long authorId;
	private String publishStatus;
	
	
	public long getFromContentownerId() {
		return fromContentownerId;
	}
	public void setFromContentownerId(long fromContentownerId) {
		this.fromContentownerId = fromContentownerId;
	}
	public String getToContentownerId() {
		return toContentownerId;
	}
	public void setToContentownerId(String toContentownerId) {
		this.toContentownerId = toContentownerId;
	}
	public String getOriginalCourseId() {
		return originalCourseId;
	}
	public void setOriginalCourseId(String originalCourseId) {
		this.originalCourseId = originalCourseId;
	}
	public String getLowestPrice() {
		return lowestPrice;
	}
	public void setLowestPrice(String lowestPrice) {
		this.lowestPrice = lowestPrice;
	}

	
	public String getRoyaltyAmount() {
		return royaltyAmount;
	}
	public void setRoyaltyAmount(String royaltyAmount) {
		this.royaltyAmount = royaltyAmount;
	}
	public String getRoyaltyType() {
		return royaltyType;
	}
	public void setRoyaltyType(String royaltyType) {
		this.royaltyType = royaltyType;
	}
	public int getExistingOfferInPlace() {
		return existingOfferInPlace;
	}
	public void setExistingOfferInPlace(int existingOfferInPlace) {
		this.existingOfferInPlace = existingOfferInPlace;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public long getCreatedUserId() {
		return createdUserId;
	}
	public void setCreatedUserId(long createdUserId) {
		this.createdUserId = createdUserId;
	}
	public String getOfferStatus() {
		return offerStatus;
	}
	public void setOfferStatus(String offerStatus) {
		this.offerStatus = offerStatus;
	}
	public String getSuggestedRetailPrice() {
		return suggestedRetailPrice;
	}
	public void setSuggestedRetailPrice(String suggestedRetailPrice) {
		this.suggestedRetailPrice = suggestedRetailPrice;
	}
	public String getNoteToDistributor() {
		return noteToDistributor;
	}
	public void setNoteToDistributor(String noteToDistributor) {
		this.noteToDistributor = noteToDistributor;
	}
	public long getAuthorId() {
		return authorId;
	}
	public void setAuthorId(long authorId) {
		this.authorId = authorId;
	}
	public String getPublishStatus() {
		return publishStatus;
	}
	public void setPublishStatus(String publishStatus) {
		this.publishStatus = publishStatus;
	}

	
}
