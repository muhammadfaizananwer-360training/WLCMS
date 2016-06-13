package com.softech.ls360.lcms.contentbuilder.dataimport.test;

import com.softech.common.parsing.TabularDataException;
import com.softech.ls360.lcms.contentbuilder.dao.LocationDAO;
import com.softech.ls360.lcms.contentbuilder.dataimport.CRClassParsingSubHndlr;
import com.softech.ls360.lcms.contentbuilder.dataimport.CRCourseParsingSubHndlr;
import com.softech.ls360.lcms.contentbuilder.dataimport.CRLocationParsingSubHndlr;
import com.softech.ls360.lcms.contentbuilder.dataimport.CRSessionParsingSubHndlr;
import com.softech.ls360.lcms.contentbuilder.dataimport.ClassroomParsingHndlr;
import com.softech.ls360.lcms.contentbuilder.exception.BulkUplaodCourseException;
import com.softech.ls360.lcms.contentbuilder.model.VU360UserDetail;
import com.softech.ls360.lcms.contentbuilder.service.IClassroomCourseService;
import com.softech.ls360.lcms.contentbuilder.service.VU360UserService;
import com.softech.ls360.lcms.contentbuilder.service.impl.ClassroomCourseServiceImpl;
import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.softech.ls360.lcms.contentbuilder.utils.IOConvertor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.softech.ls360.lcms.contentbuilder.test.AbstractLcmsTest;

public class ClassRoomImportTest extends AbstractLcmsTest {
    
    @Autowired
    private ClassroomParsingHndlr classroomHandler;
    
    @Autowired
    private CRCourseParsingSubHndlr courseSubHandler;
    
    @Autowired
    private CRClassParsingSubHndlr classSubHandler;
    
    @Autowired
    private CRSessionParsingSubHndlr sessionSubHandler;
    
    @Autowired
    private CRLocationParsingSubHndlr locationSubHandler;
    

    @Autowired
    private IClassroomCourseService classroomService;
    
    @Autowired
    private VU360UserService userService;


    @Test(expected=TabularDataException.class)
    public void importExcelFile() throws IOException, TabularDataException {
        
        String testFilePath = "excels/classroom2.xlsx";
        VU360UserDetail user = (VU360UserDetail) userService.loadUserByUsername("admin.manager@360training.com");
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(testFilePath)) {
            assertNotNull("classpath:" + testFilePath + " file not available for testing.", is);
            byte[] fileData = IOConvertor.convertStreamToDataBytes(is);
            classroomService.importCourses(user, fileData,true);
        }
    }
    
    private void initializeHandlers(){
        classroomHandler.addSubHandlers(classSubHandler,courseSubHandler,sessionSubHandler,locationSubHandler);
        classSubHandler.setCourseHandler(courseSubHandler);
        classSubHandler.setLocationHandler(locationSubHandler);
        sessionSubHandler.setClassHandler(classSubHandler);
    }
            
}
