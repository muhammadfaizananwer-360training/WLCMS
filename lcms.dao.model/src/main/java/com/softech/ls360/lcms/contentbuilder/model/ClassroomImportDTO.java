/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softech.ls360.lcms.contentbuilder.model;

import com.softech.common.parsing.TabularDataException;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author abdul.wahid
 */
public class ClassroomImportDTO {
     private Collection<CourseVO> courses = new ArrayList<>();
     private Collection<LocationDTO> locations = new ArrayList<>();
    private Collection<ClassInstructorDTO> instructors = new ArrayList<>();
     Collection<TabularDataException.ExceptionDetail> errorList;

    public Collection<CourseVO> getCourses() {
        return courses;
    }

    public void setCourses(Collection<CourseVO> courses) {
        this.courses = courses;
    }

    public Collection<LocationDTO> getLocations() {
        return locations;
    }

    public void setLocations(Collection<LocationDTO> locations) {
        this.locations = locations;
    }

    public Collection<TabularDataException.ExceptionDetail> getErrorList() {
        return errorList;
    }

    public void setErrorList(Collection<TabularDataException.ExceptionDetail> errorList) {
        this.errorList = errorList;
    }

    public Collection<ClassInstructorDTO> getInstructors() {
        return instructors;
    }

    public void setInstructors(Collection<ClassInstructorDTO> instructors) {
        this.instructors = instructors;
    }
}
