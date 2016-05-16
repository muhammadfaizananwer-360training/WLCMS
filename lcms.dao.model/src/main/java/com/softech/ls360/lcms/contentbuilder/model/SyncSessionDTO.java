package com.softech.ls360.lcms.contentbuilder.model;

import com.softech.ls360.lcms.contentbuilder.utils.TypeConvertor;
import java.util.Date;
import java.util.Map;


public class SyncSessionDTO implements ControllableNode {
    private SyncClassDTO syncClass;
    private Date startDateTime;
    private Date endDateTime;
    private String action;
    private boolean error;
    private Long id;

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
        String start = "";
        String end = "";
        if(startDateTime != null) {
            start = TypeConvertor.DateTimeToString(startDateTime);
        }
        if(endDateTime != null) {
            end = TypeConvertor.DateTimeToString(endDateTime);
        }
        return start + " - " + end;
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
    
    
    
    
}
