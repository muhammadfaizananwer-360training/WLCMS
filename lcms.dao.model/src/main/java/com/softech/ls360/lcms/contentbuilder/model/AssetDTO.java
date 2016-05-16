package com.softech.ls360.lcms.contentbuilder.model;

import java.util.Date;

public class AssetDTO {
	private long id;
	private String name;
	private Date updatedOn;
	private String fileExtension;
	private Long sizeInBytes;
	private String videoLocation;
	private long contentOwnerId;
	private Long versionId;
	private String assetType;
	private Boolean isLinked;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	public String getFileExtension() {
		return fileExtension;
	}
	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}
	public Long getSizeInBytes() {
		return sizeInBytes;
	}
	public void setSizeInBytes(Long sizeInBytes) {
		this.sizeInBytes = sizeInBytes;
	}
	public String getVideoLocation() {
		return videoLocation;
	}
	public void setVideoLocation(String videoLocation) {
		this.videoLocation = videoLocation;
	}
	public long getContentOwnerId() {
		return contentOwnerId;
	}
	public void setContentOwnerId(long contentOwnerId) {
		this.contentOwnerId = contentOwnerId;
	}
	public Long getVersionId() {
		return versionId;
	}
	public void setVersionId(Long versionId) {
		this.versionId = versionId;
	}
	public String getAssetType() {
		return assetType;
	}
	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}
	public Boolean getIsLinked() {
		return isLinked;
	}
	public void setIsLinked(Boolean isLinked) {
		this.isLinked = isLinked;
	}
}
