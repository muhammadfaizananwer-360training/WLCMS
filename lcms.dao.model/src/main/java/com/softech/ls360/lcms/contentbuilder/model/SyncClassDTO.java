package com.softech.ls360.lcms.contentbuilder.model;
import com.softech.ls360.lcms.contentbuilder.model.validator.annotation.NotEmpty;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SyncClassDTO implements ControllableNode {

    private Long id;
    private Long maximumClassSize;
    private Long locId;
    private Integer timeZoneId;

    @NotEmpty
    private String timeZoneText;

    @NotEmpty
    private Date enrollmentCloseDate;

    @NotEmpty
    private String className;

    @NotEmpty
    private String locationName;

    @NotEmpty
    private String instructorEmail;
    private CourseVO course;
    private Collection<SyncSessionDTO> sessions;
    private Map<String,SyncSessionDTO> sessionsMap;
    private String action;
    private boolean error;

    public SyncClassDTO() {
        this.sessionsMap = new HashMap<>();
    }

    public Long getMaximumClassSize() {
        return (maximumClassSize == null) ? Long.MAX_VALUE : maximumClassSize;
    }

    public void setMaximumClassSize(Long maximumClassSize) {
        this.maximumClassSize = maximumClassSize;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLocId() {
        return locId;
    }

    public void setLocId(Long locId) {
        this.locId = locId;
    }

    public Integer getTimeZoneId() {
        return timeZoneId;
    }

    public void setTimeZoneId(Integer timeZoneId) {
        this.timeZoneId = timeZoneId;
    }

    public String getTimeZoneText() {
        return timeZoneText;
    }

    public void setTimeZoneText(String timeZoneText) {
        this.timeZoneText = timeZoneText;
    }
    
    

    public Date getEnrollmentCloseDate() {
        return enrollmentCloseDate;
    }

    public void setEnrollmentCloseDate(Date enrollmentCloseDate) {
        this.enrollmentCloseDate = enrollmentCloseDate;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public CourseVO getCourse() {
        return course;
    }

    public void setCourse(CourseVO course) {
        this.course = course;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getInstructorEmail() {
        return instructorEmail;
    }

    public void setInstructorEmail(String instructorEmail) {
        this.instructorEmail = instructorEmail;
    }

    public Collection<SyncSessionDTO> getSessions() {
        if(sessions != null) {
            return sessions;
        } else if(sessionsMap != null)  {
            return sessionsMap.values();
        }
        return null;
    }

    public void setSessions(Collection<SyncSessionDTO> sessions) {
        this.sessions = sessions;
    }

    public Map<String, SyncSessionDTO> getSessionsMap() {
        return sessionsMap;
    }

    public void setSessionsMap(Map<String, SyncSessionDTO> sessionsMap) {
        this.sessionsMap = sessionsMap;
    }
    
    
    
    public String getAction() {
        return action;
    }

    public void setAction(String tag) {
        this.action = tag;
    }

     public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    @Override
    public ControllableNode getParent() {
        return getCourse();
    }

    @Override
    public void setParent(ControllableNode parent) {
        setCourse((CourseVO) parent);
    }

    @Override
    public String getNodeKey() {
        return getClassName();
    }

    @Override
    public Map<String, ? extends ControllableNode> getChildren() {
        return getSessionsMap();
    }
    
    
}
