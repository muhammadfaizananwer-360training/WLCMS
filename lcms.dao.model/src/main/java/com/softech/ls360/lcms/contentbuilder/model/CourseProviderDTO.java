package com.softech.ls360.lcms.contentbuilder.model;

import com.softech.common.parsing.ExpressionConstant;
import com.softech.ls360.lcms.contentbuilder.model.validator.annotation.NotEmpty;

import javax.validation.constraints.Pattern;

public class CourseProviderDTO {
	private CourseVO course;

    @NotEmpty
    private String name;

    @NotEmpty @Pattern(regexp = ExpressionConstant.EMAIL, message = "invalid")
    private String email;

    @NotEmpty
    private String phoneNo;

    @NotEmpty
    private String instructorBackground;
    public CourseVO getCourse() {
        return course;
    }

    public void setCourse(CourseVO course) {
        this.course = course;
    }

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
