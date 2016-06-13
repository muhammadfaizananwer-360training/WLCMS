package com.softech.ls360.lcms.contentbuilder.web.api.dto;

import java.util.Date;

/**
 * Created by mujeeb.tariq on 5/2/2016.
 */
public class SyncSessionAPIDTO {

    private Date startDateTime;
    private Date endDateTime;
    private String sessionKey;

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }
}
