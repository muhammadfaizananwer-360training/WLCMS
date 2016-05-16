package com.softech.ls360.lcms.contentbuilder.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Where;


@Entity
@Table(name = "SYNCHRONOUSCLASS")
@Where(clause = "ClassActiveTF = 1")
public class SynchronousClass extends AbstractEntity implements Serializable{

	private static final long serialVersionUID = -5175335948065016579L;
	
	
	private Long id;
    
	 @Column(name = "CLASSNAME")
	 private String className;
	 
	 
	 private Date classStartDate;
	 
	 private Date classEndDate;
	 
	 private Long maximumClassSize;
	 
	 private Long minimumClassSize;
	 
	 private Date enrollmentCloseDate;
	 
	 private String classStatus;
	 
	 private String presenterFirstName;	
	
	 private String presenterLastName;	
	 
	 private String  presenterEmail;
	 
	 private String  presenterPhone;
	
	 private String meetingURL;	
		
	 private String meetingPassCode;	
	 
	 private String  dialInNumber;
	 
	 private String  additionalInformation;
	 
	 private String  webinarServiceProvider;

	 private Integer timeZoneId;
	 
	 private String meetingId;
	
	 private String guid;
	 
	 private Set<SynchronousSession> syncSession ;
	 
	 private String status;
	 
	 private Boolean deleted;

	 private Long classInstructorId;

	@Column(name = "ID")
	@TableGenerator(
        name="seqSynchronousClass", 
        table="VU360_SEQ",
        pkColumnName="TABLE_NAME", 
        valueColumnName="NEXT_ID", 
        pkColumnValue="SYNCHRONOUSCLASS",
        allocationSize = 1)
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE, generator="seqSynchronousClass") 
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
	public SynchronousClass() {
	}
 
	
	 private CourseDTO course;
	 
   
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	

	
	

	@ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="COURSE_ID", nullable = false) 
	public CourseDTO getCourse() {
		return course;
	}

	public void setCourse(CourseDTO course) {
		this.course = course;
	}

	public Date getClassStartDate() {
		return classStartDate;
	}

	public void setClassStartDate(Date classStartDate) {
		this.classStartDate = classStartDate;
	}

	

	public Date getClassEndDate() {
		return classEndDate;
	}

	public void setClassEndDate(Date classEndDate) {
		this.classEndDate = classEndDate;
	}

	public Long getMaximumClassSize() {
		return maximumClassSize;
	}

	public void setMaximumClassSize(Long maximumClassSize) {
		this.maximumClassSize = maximumClassSize;
	}

	public Long getMinimumClassSize() {
		return minimumClassSize;
	}

	public void setMinimumClassSize(Long minimumClassSize) {
		this.minimumClassSize = minimumClassSize;
	}

	public Date getEnrollmentCloseDate() {
		return enrollmentCloseDate;
	}

	public void setEnrollmentCloseDate(Date enrollmentCloseDate) {
		this.enrollmentCloseDate = enrollmentCloseDate;
	}

	public String getClassStatus() {
		return classStatus;
	}

	public void setClassStatus(String classStatus) {
		this.classStatus = classStatus;
	}

	@Column(name = "PRESENTER_PHONE_NUBER")
	public String getPresenterPhone() {
		return presenterPhone;
	}

	public void setPresenterPhone(String presenterPhone) {
		this.presenterPhone = presenterPhone;
	}
	
	@Column(name = "PRESENTER_FIRST_NAME")
	public String getPresenterFirstName() {
		return presenterFirstName;
	}

	public void setPresenterFirstName(String presenterFirstName) {
		this.presenterFirstName = presenterFirstName;
	}

	@Column(name = "PRESENTER_LAST_NAME")
	public String getPresenterLastName() {
		return presenterLastName;
	}

	public void setPresenterLastName(String presenterLastName) {
		this.presenterLastName = presenterLastName;
	}

	@Column(name = "PRESENTER_EMAIL_ADDRESS")
	public String getPresenterEmail() {
		return presenterEmail;
	}

	public void setPresenterEmail(String presenterEmail) {
		this.presenterEmail = presenterEmail;
	}

	@Column(name = "TIMEZONE_ID")
	public Integer getTimeZoneId() {
		return timeZoneId;
	}

	public void setTimeZoneId(Integer timeZoneId) {
		this.timeZoneId = timeZoneId;
	}
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL, mappedBy="syncClass")
	@Where(clause = "status in ('U','A','D')")
	public Set<SynchronousSession> getSyncSession() {
		return syncSession;
	}

	public void setSyncSession(Set<SynchronousSession> syncSession) {
		this.syncSession = syncSession;
	}

	
	private Location location;

	@OneToOne( cascade = CascadeType.ALL)
	@JoinColumn(name="LOCATION_ID", nullable=true, insertable=true, updatable=true)
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
		
	
	@Column(name = "MEETINGURL")
	public String getMeetingURL() {
		return meetingURL;
	}

	public void setMeetingURL(String meetingURL) {
		this.meetingURL = meetingURL;
	}
	
	@Column(name = "MEETINGPASSCODE")
	public String getMeetingPassCode() {
		return meetingPassCode;
	}

	public void setMeetingPassCode(String meetingPassCode) {
		this.meetingPassCode = meetingPassCode;
	}

	@Column(name = "DIAL_IN_NUMBER")
	public String getDialInNumber() {
		return dialInNumber;
	}

	public void setDialInNumber(String dialInNumber) {
		this.dialInNumber = dialInNumber;
	}

	@Column(name = "ADDITIONAL_INFORMATION")
	public String getAdditionalInformation() {
		return additionalInformation;
	}

	public void setAdditionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}

	@Column(name = "WEBINAR_SERVICE_PROVIDER")
	public String getWebinarServiceProvider() {
		return webinarServiceProvider;
	}

	public void setWebinarServiceProvider(String webinarServiceProvider) {
		this.webinarServiceProvider = webinarServiceProvider;
	}

	@Column(name = "MEETING_ID")
	public String getMeetingId() {
		return meetingId;
	}

	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}

	@Column(name="GUID")
	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	@Column(name="status",insertable=false)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name="ClassActiveTF",insertable=false)
	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	@Column(name="CLASSINSTRUCTOR_ID")
	public Long getClassInstructorId() {
		return classInstructorId;
	}

	public void setClassInstructorId(Long classInstructorId) {
		this.classInstructorId = classInstructorId;
	}
}
