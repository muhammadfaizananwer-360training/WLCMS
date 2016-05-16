package com.softech.ls360.lcms.contentbuilder.interfaces;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;

import com.softech.ls360.lcms.contentbuilder.exception.BulkUplaodCourseException;
import com.softech.ls360.lcms.contentbuilder.model.Course;
import com.softech.ls360.lcms.contentbuilder.model.Lesson;

public interface ICourseParser {
	public Object parseCourse(String fileName, int streamingtemplateId) throws IOException, BulkUplaodCourseException;
}
