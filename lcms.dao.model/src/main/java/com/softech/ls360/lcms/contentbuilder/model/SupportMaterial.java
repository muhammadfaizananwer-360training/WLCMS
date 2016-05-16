package com.softech.ls360.lcms.contentbuilder.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;



@Entity
@Table(name = "COURSEMATERIALS")
public class SupportMaterial {
	
	@Id
	@Column(name = "ID")
	private Integer id;
	
	@Column(name = "CONTENTOBJECT_ID")
	private Integer contentObjectId;
	
	@Column(name = "COURSE_ID")
	private Integer courseId;
	
	@Column(name = "TYPE")
	private String type;
	
	@Column(name = "CreateUserId")
	private Long createdUserId;
	
	@Column(name = "CreatedDate")
	private Date createdDate;
	
	@Column(name = "ASSET_ID")
	private Long assetId;
	
	@Column(name = "SEQUENCE")
	private Integer sequence;
	
	@Transient
	private String assetName;
	
	@Transient
	private String Keywords;
	
	@Transient
	private String description;
	
	@Transient
	private String fileName;
	
	@Transient
	private String location;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getContentObjectId() {
		return contentObjectId;
	}
	public void setContentObjectId(Integer contentObjectId) {
		this.contentObjectId = contentObjectId;
	}
	public Integer getCourseId() {
		return courseId;
	}
	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getCreatedUserId() {
		return createdUserId;
	}
	public void setCreatedUserId(Long createdUserId) {
		this.createdUserId = createdUserId;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Long getAssetId() {
		return assetId;
	}
	public void setAssetId(Long assetId) {
		this.assetId = assetId;
	}
	public Integer getSequence() {
		return sequence;
	}
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
	public String getAssetName() {
		return assetName;
	}
	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}
	public String getKeywords() {
		return Keywords;
	}
	public void setKeywords(String keywords) {
		Keywords = keywords;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
}
