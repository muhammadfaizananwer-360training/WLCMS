package com.softech.ls360.lcms.contentbuilder.model;

import com.softech.ls360.lcms.contentbuilder.model.validator.annotation.NotEmpty;
import com.softech.ls360.lcms.contentbuilder.model.validator.annotation.NotFalse;
import com.softech.ls360.lcms.contentbuilder.model.validator.annotation.NotPastDate;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;


@NotFalse(
        messages = {"End Date Before Start Date"} ,
        properties={"endDateTime"},
        verifiers = {"validDateRange"})
public class SyncSessionDTO implements ControllableNode, Serializable {
    private SyncClassDTO syncClass;


    @NotEmpty @NotPastDate
    private Date startDateTime;

    @NotEmpty
    private Date endDateTime;
    private String action;
    private boolean error;
    private Long id;

    @NotEmpty
    private String sessionKey;

    public SyncClassDTO getSyncClass() {
        return syncClass;
    }

    public void setSyncClass(SyncClassDTO syncClass) {
        this.syncClass = syncClass;
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

    public String getAction() {
        return action;
    }

    public void setAction(String tag) {
        this.action = tag;
    }

    public String getKey() {
       return getSessionKey();
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean isError() {
        return error;
    }

    @Override
    public void setError(boolean error) {
        this.error = error;
    }

    @Override
    public ControllableNode getParent() {
        return getSyncClass();
    }

    @Override
    public void setParent(ControllableNode parent) {
        setSyncClass((SyncClassDTO) parent);
    }

    @Override
    public String getNodeKey() {
        return getKey();
    }

    @Override
    public Map<String, ControllableNode> getChildren() {
        return null;
    }

    public Boolean getValidDateRange(){
        if(startDateTime != null && endDateTime != null) {
            return startDateTime.getTime() <= endDateTime.getTime();
        }

        return null;
    }
}
