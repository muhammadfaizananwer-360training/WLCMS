package com.softech.ls360.lcms.contentbuilder.web.api.dto;

import java.util.Collection;

/**
 * Created by mujeeb.tariq on 5/2/2016.
 */
public class CourseAPIDTO {

    private String courseStatus = "Not Started";
    private String courseId = null;//bussinessKey
    private String name = null;
    private String description;
    private String keywords;
    private String courseType;
    private String currency;
    private CourseProviderAPIDTO courseProvider;
    private String additionalMaterials;
    private String intendedAudience;
    private String coursePrereq;
    private Collection<SyncClassAPIDTO> syncClasses;
    private String businessunitName;
    private String topicsCovered;
    private String learningObjectives;
    private String propValue;
    private String instructorEmail;

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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public CourseProviderAPIDTO getCourseProvider() {
        return courseProvider;
    }

    public void setCourseProvider(CourseProviderAPIDTO courseProvider) {
        this.courseProvider = courseProvider;
    }

    public String getAdditionalMaterials() {
        return additionalMaterials;
    }

    public void setAdditionalMaterials(String additionalMaterials) {
        this.additionalMaterials = additionalMaterials;
    }

    public String getIntendedAudience() {
        return intendedAudience;
    }

    public void setIntendedAudience(String intendedAudience) {
        this.intendedAudience = intendedAudience;
    }

    public String getCoursePrereq() {
        return coursePrereq;
    }

    public void setCoursePrereq(String coursePrereq) {
        this.coursePrereq = coursePrereq;
    }

    public Collection<SyncClassAPIDTO> getSyncClasses() {
        return syncClasses;
    }

    public void setSyncClasses(Collection<SyncClassAPIDTO> syncClasses) {
        this.syncClasses = syncClasses;
    }

    public String getBusinessunitName() {
        return businessunitName;
    }

    public void setBusinessunitName(String businessunitName) {
        this.businessunitName = businessunitName;
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

    public String getPropValue() {
        return propValue;
    }

    public void setPropValue(String propValue) {
        this.propValue = propValue;
    }

    public String getInstructorEmail() {
        return instructorEmail;
    }

    public void setInstructorEmail(String instructorEmail) {
        this.instructorEmail = instructorEmail;
    }
}
