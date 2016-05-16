package com.softech.ls360.lcms.contentbuilder.web.api.dto;

/**
 * Created by mujeeb.tariq on 5/2/2016.
 */
public class ClassInstructorAPIDTO {
    
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNo;
    private String background;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }
}
