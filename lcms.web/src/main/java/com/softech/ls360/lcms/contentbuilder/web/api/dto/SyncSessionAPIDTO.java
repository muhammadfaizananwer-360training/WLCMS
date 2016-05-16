package com.softech.ls360.lcms.contentbuilder.web.api.dto;

import java.util.Date;

/**
 * Created by mujeeb.tariq on 5/2/2016.
 */
public class SyncSessionAPIDTO {

    private Long id;
    private Date startDateTime;
    private Date endDateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
