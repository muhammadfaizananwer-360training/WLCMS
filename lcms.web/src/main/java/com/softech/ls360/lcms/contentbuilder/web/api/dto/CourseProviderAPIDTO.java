package com.softech.ls360.lcms.contentbuilder.web.api.dto;

/**
 * Created by mujeeb.tariq on 5/3/2016.
 */
public class CourseProviderAPIDTO {

    private String name;
    private String email;
    private String phoneNo;
    private String instructorBackground;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getInstructorBackground() {
        return instructorBackground;
    }

    public void setInstructorBackground(String instructorBackground) {
        this.instructorBackground = instructorBackground;
    }
}
