package com.softech.ls360.lcms.contentbuilder.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "COURSE")
public class CourseDTO implements Serializable{

	private static final long serialVersionUID = -5175335948065016579L;
	
	
	private long id;

    @Column(name = "COURSESTATUS")
    private String courseStatus = "Not Started";    
    
    @Column(name = "COURSEID")
    private String courseId = null;//bussinessKey    
    
    @Column(name = "NAME")
    private String name = null;    
    
    @Column(name = "DESCRIPTION")
    private String description;
    
    @Column(name = "LANGUAGE_ID")
    private Integer language_id;
    
    @Column(name = "CEUS")
    private BigDecimal ceus;

	private BigDecimal msrp;
	private BigDecimal lowestPrice;
    
    @Column(name = "KEYWORDS")
    private String keywords;
    
    @Column(name = "ADDITIONALMATERIALS")
    private String AdditionalMaterials;
    
    @Column(name = "COURSETYPE")
    private String courseType;
	
	@Column(name="BRANDING_ID")
	private Integer  brandingId;
	
	@Column(name="CONTENTOWNER_ID")
	private Integer  contentownerId;
	
	@Column(name="CreateUserId")
	private Long createUserId;
	
	@Column(name="CODE")
	private String code;
	
	@Column(name="GUID")
	private String guid;
	
	@Column(name="CURRENCY")
	private String currency;
	
	@Column(name="BUSINESSUNIT_ID")
	private Integer  businessunitId;
	
	@Column(name="CreatedDate")
	private Date createdDate;
	
	@Column(name="LastUpdatedDate")
	private Date lastUpdatedDate;
	
	@Column(name="BUSINESSUNIT_NAME")
	private String businessunitName;
	
    @Column(name = "BUSSINESSKEY")
    private String bussinesskey;
    
    @Column(name = "productPrice")
    private BigDecimal productPrice;
    
    @Column(name = "LastUpdateUser")
    private Long lastUpdateUser;
    
    @Column(name = "RETIREDTF")
    private boolean retiredTF;
    
    @Column(name = "APPROVALNUMBER")
    private String approvalNumber = "1";
    
    @Column(name = "VERSION")
    private String version = "1.0";
    
	@Column(name="LASTPUBLISHEDDATE")
	private Date lastPublishedDate;
        
    @Column(name = "TOPICSCOVERED")
    private String topicsCovered;

    @Column(name = "LEARNINGOBJECTIVES")
    private String learningObjectives;

    @Column(name = "COURSEPRE_REQ")
    private String coursePrereq;
    
	private Integer deliveryMethodId;
	
	private Long courseVideoAssetId;
	
	private String duration;
	
	@Column(name = "SOURCE_NAME")
	private String source;
    
    private Long courseThumbnailId;

	private Long classInstructorId;

    @Transient
    private Boolean ratingTF;
    
    @Transient
    private Double rating;
    
	@Transient
	private Integer  quickBuildCoursetype;
	
	@Transient
    private SlideAsset courseImageThumbnail;
   
	@Transient
	private String lastModifiedDate = null;
	
	@Transient
	private String courseRatingStr = null;
	
	@Transient
	private String courseRating;
	
	@Transient
	private String coursePublishStatus;
	
	@Transient
	private String courseGroupName;
	
	@Transient
    private String coursePreReq;
	
	@Transient
    private String EndOfCourseInstructions;
	
	@Column(name="SUBJECTMATTEREXPERTINFO")
    private String AuthorBackground;
	
	@Column(name="INTENDED_AUDIENCE")
    private String IntendedAudience;
	
	@Transient
    private Integer CourseAuthorImageId;
	
	@Transient
    private Integer ImageOfCourse;
	
	@Transient
    private SlideAsset CourseImage;
	
	@Transient
    private SlideAsset AuthorImage;
	
	@Transient
    private long totalRows;
	
	@Transient
    private String courseGUID = null;
	
	@Transient
    private String creationDate = null;
        
    @Column(name = "PROPVALUE")
    private String propValue;


	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "seqCourseId")
	@GenericGenerator(name = "seqCourseId", strategy = "com.softech.ls360.lcms.contentbuilder.model.PrimaryKeyGenerator", parameters = {
	        @Parameter(name = "table_name", value = "VU360_SEQ"),
	        @Parameter(name = "value_column_name", value = "NEXT_ID"),
	        @Parameter(name = "segment_column_name", value = "TABLE_NAME"),
	        @Parameter(name = "segment_value", value = "COURSE") })
	@Column(name = "ID")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public CourseDTO(){
	
	}
	
	public CourseDTO(long id, String name, String courseStatus) {
	     this.id = id;
	     this.name = name;
		 this.courseStatus = courseStatus;
	}
	
	
    public Integer getLanguage_id() {
		return language_id;
	}

	public void setLanguage_id(Integer language_id) {
		this.language_id = language_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Transient
	public String getCourseGUID() {
		return courseGUID;
	}

	public void setCourseGUID(String courseGUID) {
		this.courseGUID = courseGUID;
	}

	public String getCourseStatus() {
		return courseStatus;
	}

	public void setCourseStatus(String courseStatus) {
		this.courseStatus = courseStatus;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Transient
	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	@Transient
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Transient
	public String getCourseRatingStr() {
		return courseRatingStr;
	}

	public void setCourseRatingStr(String courseRating) {
		this.courseRatingStr = courseRating;
	}

	@Transient
	public String getCourseRating() {
		return courseRating;
	}

	public void setCourseRating(String courseRating) {
		this.courseRating = courseRating;
	}

	@Transient
	public String getCoursePublishStatus() {
		return coursePublishStatus;
	}

	public void setCoursePublishStatus(String coursePublishStatus) {
		this.coursePublishStatus = coursePublishStatus;
	}

	@Transient
	public long getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(long totalRows) {
		this.totalRows = totalRows;
	}

	@Transient
	 public String getCourseGroupName() {
		return courseGroupName;
	}

	public void setCourseGroupName(String courseGroupName) {
		this.courseGroupName = courseGroupName;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	@Transient
	public Boolean getRatingTF() {
		return ratingTF;
	}

	public void setRatingTF(Boolean ratingTF) {
		this.ratingTF = ratingTF;
	}

	@Transient
	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	
	public BigDecimal getCeus() {
		return ceus;
	}

	public void setCeus(BigDecimal ceus) {
		this.ceus = ceus;
	}

	public String getBussinesskey() {
		return bussinesskey;
	}

	public void setBussinesskey(String bussinesskey) {
		this.bussinesskey = bussinesskey;
	}

	public String getAdditionalMaterials() {
		return AdditionalMaterials;
	}

	public void setAdditionalMaterials(String additionalMaterials) {
		AdditionalMaterials = additionalMaterials;
	}

	public String getCourseType() {
		return courseType;
	}

	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}

	@Transient
	public Integer getBrandingId() {
		return brandingId;
	}

	public void setBrandingId(Integer brandingId) {
		this.brandingId = brandingId;
	}

	@Column(name="CONTENTOWNER_ID")
	public Integer getContentownerId() {
		return contentownerId;
	}

	
	public void setContentownerId(Integer contentownerId) {
		this.contentownerId = contentownerId;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Transient
	public Integer getBusinessunitId() {
		return businessunitId;
	}

	public void setBusinessunitId(Integer businessunitId) {
		this.businessunitId = businessunitId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	@Column(name="BUSINESSUNIT_NAME")
	public String getBusinessunitName() {
		return businessunitName;
	}

	public void setBusinessunitName(String businessunitName) {
		this.businessunitName = businessunitName;
	}

	@Transient
	public Integer getQuickBuildCoursetype() {
		return quickBuildCoursetype;
	}

	public void setQuickBuildCoursetype(Integer quickBuildCoursetype) {
		this.quickBuildCoursetype = quickBuildCoursetype;
	}

	@Transient
	public String getCoursePreReq() {
		return coursePreReq;
	}

	public void setCoursePreReq(String coursePreReq) {
		this.coursePreReq = coursePreReq;
	}

	public String getEndOfCourseInstructions() {
		return EndOfCourseInstructions;
	}

	public void setEndOfCourseInstructions(String endOfCourseInstructions) {
		EndOfCourseInstructions = endOfCourseInstructions;
	}

	@Column(name="SUBJECTMATTEREXPERTINFO")
	public String getAuthorBackground() {
		return AuthorBackground;
	}

	public void setAuthorBackground(String authorBackground) {
		AuthorBackground = authorBackground;
	}

	@Column(name="INTENDED_AUDIENCE")
	public String getIntendedAudience() {
		return IntendedAudience;
	}

	public void setIntendedAudience(String intendedAudience) {
		IntendedAudience = intendedAudience;
	}

	@Transient
	public Integer getCourseAuthorImageId() {
		return CourseAuthorImageId;
	}

	public void setCourseAuthorImageId(Integer courseAuthorImageId) {
		CourseAuthorImageId = courseAuthorImageId;
	}

	public Integer getImageOfCourse() {
		return ImageOfCourse;
	}

	public void setImageOfCourse(Integer imageOfCourse) {
		ImageOfCourse = imageOfCourse;
	}

	@Transient
	public SlideAsset getCourseImage() {
		return CourseImage;
	}

	public void setCourseImage(SlideAsset courseImage) {
		CourseImage = courseImage;
	}

	@Transient
	public SlideAsset getAuthorImage() {
		return AuthorImage;
	}

	public void setAuthorImage(SlideAsset authorImage) {
		AuthorImage = authorImage;
	}

	public BigDecimal getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
	}
	
	private Set<SynchronousClass> syncClass ;//= new HashSet<SynchronousClass>();
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="course" ,fetch=FetchType.LAZY)
	public Set<SynchronousClass> getSyncClass() {
		return syncClass;
	}

	public void setSyncClass(Set<SynchronousClass> syncClass) {
		this.syncClass = syncClass;
	}

	public Long getLastUpdateUser() {
		return lastUpdateUser;
	}

	public void setLastUpdateUser(Long lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}
	

	private Set<CourseGroup> courseGroup = new HashSet<CourseGroup>();

	@ManyToMany(cascade = {CascadeType.ALL},fetch=FetchType.LAZY)
    @JoinTable(name="COURSE_COURSEGROUP",
                joinColumns={@JoinColumn(name="COURSE_ID",  referencedColumnName="ID")},
                inverseJoinColumns={@JoinColumn(name="COURSEGROUP_ID", referencedColumnName="ID")})
	public Set<CourseGroup> getCourseGroup() {
		return courseGroup;
	}

	public void setCourseGroup(Set<CourseGroup> courseGroup) {
		this.courseGroup = courseGroup;
	}

	public boolean isRetiredTF() {
		return retiredTF;
	}

	public void setRetiredTF(boolean retiredTF) {
		this.retiredTF = retiredTF;
	}

	public String getApprovalNumber() {
		return approvalNumber;
	}

	public void setApprovalNumber(String approvalNumber) {
		this.approvalNumber = approvalNumber;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Date getLastPublishedDate() {
		return lastPublishedDate;
	}

	public void setLastPublishedDate(Date lastPublishedDate) {
		this.lastPublishedDate = lastPublishedDate;
	}

	@Column(name="DELIVERYMETHOD_ID")
	public Integer getDeliveryMethodId() {
		return deliveryMethodId;
	}

	public void setDeliveryMethodId(Integer deliveryMethodId) {
		this.deliveryMethodId = deliveryMethodId;
	}
	
	@Column(name="COURSEVIDEOASSET_ID")
	public Long getCourseVideoAssetId() {
		return courseVideoAssetId;
	}

	public void setCourseVideoAssetId(Long courseVideoAssetId) {
		this.courseVideoAssetId = courseVideoAssetId;
	}

    public String getTopicsCovered() {
        return topicsCovered;
    }

    public void setTopicsCovered(String topicsCovered) {
        this.topicsCovered = topicsCovered;
    }

    public String getLearningObjectives() {
        return learningObjectives;
    }

    public void setLearningObjectives(String learningObjectives) {
        this.learningObjectives = learningObjectives;
    }

    @Column(name = "COURSEPRE_REQ")
    public String getCoursePrereq() {
        return coursePrereq;
    }

    public void setCoursePrereq(String coursePrereq) {
        this.coursePrereq = coursePrereq;
    }

    @Column(name = "PROPVALUE")
    public String getPropValue() {
        return propValue;
    }

    public void setPropValue(String propValue) {
        this.propValue = propValue;
    }
    
    @Column(name = "COURSETHUMBNAIL_ID")
	public Long getCourseThumbnailId() {
		return courseThumbnailId;
	}

	public void setCourseThumbnailId(Long courseThumbnailId) {
		this.courseThumbnailId = courseThumbnailId;
	}

	@Transient
	public SlideAsset getCourseImageThumbnail() {
		return courseImageThumbnail;
	}
	
	
	public void setCourseImageThumbnail(SlideAsset courseImageThumbnail) {
		this.courseImageThumbnail = courseImageThumbnail;
	}
	
	@Column(name = "DURATION")
	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}
    
	@Column(name = "SOURCE_NAME")
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
    @Column(name="CLASSINSTRUCTOR_ID")
    public Long getClassInstructorId() {
        return classInstructorId;
    }

    public void setClassInstructorId(Long classInstructorId) {
        this.classInstructorId = classInstructorId;
    }

	@Column(name="MSRP")
	public BigDecimal getMsrp() {
		return msrp;
	}

	public void setMsrp(BigDecimal msrp) {
		this.msrp = msrp;
	}

	@Column(name="LOWESTPRICE")
	public BigDecimal getLowestPrice() {
		return lowestPrice;
	}

	public void setLowestPrice(BigDecimal lowestPrice) {
		this.lowestPrice = lowestPrice;
	}
}