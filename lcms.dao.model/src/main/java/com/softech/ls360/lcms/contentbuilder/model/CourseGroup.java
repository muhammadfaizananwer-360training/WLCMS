package com.softech.ls360.lcms.contentbuilder.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "COURSEGROUP")
public class CourseGroup implements Serializable{
	
	private static final long serialVersionUID = -5175335948065016579L;
	


	 private long id;
	

	 private String name;
	
	
	 private String description;
	
	
	 private String keyword;
	
	
	 private long contentOwnerId;
	
	
	 private String courseGroupGuid;
	
	
	
	
	 private Date createdDate;
	
	
	 private long createdUserId;

	
	//private long course_Id;
	
	@Column(name = "ID")
	@TableGenerator(
        name="seqCourseGroupId", 
        table="VU360_SEQ",
        pkColumnName="TABLE_NAME", 
        valueColumnName="NEXT_ID", 
        pkColumnValue="COURSEGROUP",
        allocationSize = 1)
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE, generator="seqCourseGroupId") 
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "KEYWORDS")
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	@Column(name = "CONTENTOWNER_ID")
	public long getContentOwnerId() {
		return contentOwnerId;
	}

	public void setContentOwnerId(long contentOwnerId) {
		this.contentOwnerId = contentOwnerId;
	}

	@Column(name = "COURSEGROUP_GUID")
	public String getCourseGroupGuid() {
		return courseGroupGuid;
	}

	public void setCourseGroupGuid(String courseGroupGuid) {
		this.courseGroupGuid = courseGroupGuid;
	}

	@Column(name = "CreatedDate")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "CreateUserId")
	public long getCreatedUserId() {
		return createdUserId;
	}

	public void setCreatedUserId(long createdUserId) {
		this.createdUserId = createdUserId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	private Set<CourseDTO> course = new HashSet<CourseDTO>();

	
	//@ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.REFRESH)
	//@JoinColumn(name="COURSE_ID", nullable = false) 
	
	@ManyToMany(mappedBy="courseGroup")
	
	public Set<CourseDTO> getCourse() {
		return course;
	}

	public void setCourse(Set<CourseDTO> course) {
		this.course = course;
	}


	
	 
}
