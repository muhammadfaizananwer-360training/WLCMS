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

    String presenterFirstName;

    String presenterLastName;

    String presenterEmail;

    String presenterPhone;

    Long id;

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

    public String getPresenterFirstName() {
        return presenterFirstName;
    }

    public void setPresenterFirstName(String presenterFirstName) {
        this.presenterFirstName = presenterFirstName;
    }

    public String getPresenterLastName() {
        return presenterLastName;
    }

    public void setPresenterLastName(String presenterLastName) {
        this.presenterLastName = presenterLastName;
    }

    public String getPresenterEmail() {
        return presenterEmail;
    }

    public void setPresenterEmail(String presenterEmail) {
        this.presenterEmail = presenterEmail;
    }

    public String getPresenterPhone() {
        return presenterPhone;
    }

    public void setPresenterPhone(String presenterPhone) {
        this.presenterPhone = presenterPhone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}


