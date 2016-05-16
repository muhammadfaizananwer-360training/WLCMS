package com.softech.ls360.lcms.contentbuilder.web.api.dto;

import java.util.Collection;
import java.util.Date;

/**
 * Created by mujeeb.tariq on 5/2/2016.
 */
public class SyncClassAPIDTO {

    private Long maximumClassSize;
    private String timeZoneText;
    private Date enrollmentCloseDate;
    private String className;
    private String locationName;
    private Collection<SyncSessionAPIDTO> sessions;

    public Collection<SyncSessionAPIDTO> getSessions() {
        return sessions;
    }

    public void setSessions(Collection<SyncSessionAPIDTO> sessions) {
        this.sessions = sessions;
    }

    public Long getMaximumClassSize() {
        return maximumClassSize;
    }

    public void setMaximumClassSize(Long maximumClassSize) {
        this.maximumClassSize = maximumClassSize;
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

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
