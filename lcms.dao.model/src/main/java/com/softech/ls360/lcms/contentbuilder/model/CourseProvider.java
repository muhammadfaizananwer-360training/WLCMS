package com.softech.ls360.lcms.contentbuilder.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;


@Entity
@Table(name = "COURSEPROVIDER")
public class CourseProvider extends AbstractEntity implements Serializable{
	
	
	private static final long serialVersionUID = -4175335948065016579L;
	
	private Long id;
	private CourseDTO course;
	private Long lastUpdatedUser;
	private Long createdUser;
	private String name;
	private String email;
	private String phoneNo;
	
	@TableGenerator(
	        name="seqCourseProviderClass", 
	        table="VU360_SEQ",
	        pkColumnName="TABLE_NAME", 
	        valueColumnName="NEXT_ID", 
	        pkColumnValue="COURSEPROVIDER",
	        allocationSize = 1)
	    @Id
	    @GeneratedValue(strategy=GenerationType.TABLE, generator="seqCourseProviderClass") 
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="COURSE_ID", nullable = false)
	public CourseDTO getCourse() {
		return course;
	}
	public void setCourse(CourseDTO course) {
		this.course = course;
	}
	@Column(name="LASTUPDATEUSERID")
	public Long getLastUpdatedUser() {
		return lastUpdatedUser;
	}
	public void setLastUpdatedUser(Long lastUpdatedUser) {
		this.lastUpdatedUser = lastUpdatedUser;
	}
	@Column(name="CREATEUSERID")
	public Long getCreatedUser() {
		return createdUser;
	}
	public void setCreatedUser(Long createdUser) {
		this.createdUser = createdUser;
	}
	@Column(name="PHONENUMBER")
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	@Column(name="NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name="email")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
	
	
	
	
	
	

}
