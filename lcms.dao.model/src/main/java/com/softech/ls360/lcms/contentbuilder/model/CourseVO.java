package com.softech.ls360.lcms.contentbuilder.model;

import com.softech.ls360.lcms.contentbuilder.model.validator.annotation.NotEmpty;

import javax.validation.Valid;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class CourseVO implements Serializable,ControllableNode {


    private Long id;
    private String courseStatus = "Not Started";

    @NotEmpty
    private String courseId = null;//bussinessKey

    @NotEmpty
    private String name = null;

    @NotEmpty
    private String description;

    @NotEmpty
    private String keywords;
    private String courseType;
    private Long createUserId;
    private String code;
    private String guid;
    private String currency;

    @NotEmpty @Valid
    private CourseProviderDTO courseProvider;
    private String additionalMaterials;
    private String intendedAudience;
    private String coursePrereq;
    private Map<String, SyncClassDTO> syncClassesMap;
    private Collection<SyncClassDTO> syncClasses;

    @NotEmpty
    private String instructorEmail;
    private String action;

    @NotEmpty
    private String businessunitName;
    private String topicsCovered;
    private String learningObjectives;
    private String propValue;
    private boolean error;
    private Double courseDuration;
    private Double coursePrice;
    
    public CourseVO() {
        this.syncClassesMap = new LinkedHashMap<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
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

    public CourseProviderDTO getCourseProvider() {
        return courseProvider;
    }

    public void setCourseProvider(CourseProviderDTO courseProvider) {
        this.courseProvider = courseProvider;
    }

    public Map<String, SyncClassDTO> getSyncClassesMap() {
        return syncClassesMap;
    }

    public void setSyncClassesMap(Map<String, SyncClassDTO> syncClasses) {
        this.syncClassesMap = syncClasses;
    }

    public Collection<SyncClassDTO> getSyncClasses() {
        if(syncClasses != null) {
            return syncClasses;
        } else if(syncClassesMap != null)  {
            return syncClassesMap.values();
        }
        return null;
    }

    public String getInstructorEmail() {
        return instructorEmail;
    }

    public void setInstructorEmail(String instructorEmail) {
        this.instructorEmail = instructorEmail;
    }

    public void setSyncClasses(Collection<SyncClassDTO> syncClasses) {
        this.syncClasses = syncClasses;
    }
    
    public String getAction() {
        return action;
    }

    public void setAction(String tag) {
        this.action = tag;
    }

    public String getBusinessunitName() {
        return businessunitName;
    }

    public void setBusinessunitName(String businessunitName) {
        this.businessunitName = businessunitName;
    }

    public String getIntendedAudience() {
        return intendedAudience;
    }

    public void setIntendedAudience(String intendedAudience) {
        this.intendedAudience = intendedAudience;
    }

    public String getAdditionalMaterials() {
        return additionalMaterials;
    }

    public void setAdditionalMaterials(String additionalMaterials) {
        this.additionalMaterials = additionalMaterials;
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

    public String getCoursePrereq() {
        return coursePrereq;
    }

    public void setCoursePrereq(String coursePrereq) {
        this.coursePrereq = coursePrereq;
    }

    public String getPropValue() {
        return propValue;
    }

    public void setPropValue(String propValue) {
        this.propValue = propValue;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    @Override
    public ControllableNode getParent() {
        return null;
    }

    @Override
    public void setParent(ControllableNode parent) {
    }

    @Override
    public String getNodeKey() {
        return getCourseId();
    }

    @Override
    public Map<String, ? extends ControllableNode> getChildren() {
        return getSyncClassesMap();
    }


    public Double getCourseDuration() {
        return courseDuration;
    }

    public void setCourseDuration(Double courseDuration) {
        this.courseDuration = courseDuration;
    }

    public Double getCoursePrice() {
        return coursePrice;
    }

    public void setCoursePrice(Double coursePrice) {
        this.coursePrice = coursePrice;
    }
}
