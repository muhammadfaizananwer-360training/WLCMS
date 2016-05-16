package com.softech.ls360.lcms.contentbuilder.web.vo;

import com.softech.ls360.lcms.contentbuilder.model.ClassInstructor;

import java.util.List;

/**
 * Created by muhammad.imran on 4/27/2016.
 */
public class ClassInstructorVO {

    int iTotalRecords;

    int iTotalDisplayRecords;

    String sEcho;

    String sColumns;

    List<ClassInstructor> aaData;

    public int getiTotalRecords() {
        return iTotalRecords;
    }

    public void setiTotalRecords(int iTotalRecords) {
        this.iTotalRecords = iTotalRecords;
    }

    public int getiTotalDisplayRecords() {
        return iTotalDisplayRecords;
    }

    public void setiTotalDisplayRecords(int iTotalDisplayRecords) {
        this.iTotalDisplayRecords = iTotalDisplayRecords;
    }

    public String getsEcho() {
        return sEcho;
    }

    public void setsEcho(String sEcho) {
        this.sEcho = sEcho;
    }

    public String getsColumns() {
        return sColumns;
    }

    public void setsColumns(String sColumns) {
        this.sColumns = sColumns;
    }

    public List<ClassInstructor> getAaData() {
        return aaData;
    }

    public void setAaData(List<ClassInstructor> aaData) {
        this.aaData = aaData;
    }
}


