package com.softech.ls360.lcms.contentbuilder.service.test;


import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.softech.ls360.lcms.contentbuilder.model.ContentObject;
import com.softech.ls360.lcms.contentbuilder.service.ICourseService;


@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, TestsImpl.class})
@ContextConfiguration(locations = { "classpath:app-context.xml" })
public class TestsImpl extends AbstractTestExecutionListener {
    
	//@Autowired
   // protected CourseDAO courseDao;
	
	@Autowired
    protected ICourseService courseService;
	
    // @Autowired
    //protected ContactService contactService;
   @Test
    public void CourseMoveUp(){
    	try {
    		System.out.println("start......CourseMoveUp()............");
    		courseService.setCourseDisplayOrder(114571, 58607, "ContentObject", 1,11);
    		System.out.println("End.........CourseMoveUp().........");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
     
    }
    
    @Test
    public void CourseMoveDown(){
    	try {
    		System.out.println("start......CourseMoveDown()............");
    		//courseService.setCourseDisplayOrder(114571, 58606, "ContentObject", 0,11);
    		courseService.setCourseDisplayOrder(216457, 66937, "ContentObject", 0,11);
    		System.out.println("End.........CourseMoveDown().........");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
     
    }

    
    @Test
    public void CourseMoveDownBoundry(){
    	try {
    		System.out.println("start......CourseMoveDownBoundry()............");
    		courseService.setCourseDisplayOrder(114571, 58607, "ContentObject", 0,11);
    		System.out.println("End.........CourseMoveDownBoundry().........");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
     
    }    
    /*
    
    @Test
    public void updateLesson(){
    	try {
    		System.out.println("start......updateLesson()............");
    		
    		String varContendOjectId = "55842";
    		String varName = "Test case Lesson2";
    		String varDescription = "Test case Lesson2";
    		String varlearningObjective = "Test case Lesson2";
    		
    		ContentObject dto = new ContentObject();
    		dto.setID(Integer.parseInt(varContendOjectId));
    		dto.setName(varName);
    		dto.setDescription(varDescription);
    		dto.setLearningObjective(varlearningObjective);
    		
    		//courseService.updateContentObject(dto);
    		
    		Assert.assertNotNull(dto);
    		
    		System.out.println("End........updateLesson()..........");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
    }
    
    
    @Test
    public void addCourse(){
    	try {
    		System.out.println("start......addCourse()............");
    		
    		Course crs = new Course();
    		Date date = new Date(0);
			crs.setCoursestatus("Not Started");
			crs.setBranding_id(Integer.valueOf(1));
			crs.setName("Test Case course");
			
			crs.setCeus(new BigDecimal(1));
				
				
			crs.setDescription("Test Case course");
			crs.setKeywords("Test Case course");
			crs.setLanguage_id(1);

			crs.setContentowner_id((int)1);
			crs.setCreateUserId(11l);
			
			crs.setCoursetype("Self Paced Course");
			crs.setCode("YHZJ2K3M");

			// Generate GUID set default as example
			crs.setGuid(UUID.randomUUID().toString().replaceAll("-", ""));
			crs.setProductprice(BigDecimal.ONE);
			crs.setCurrency("USD");
			crs.setBusinessunit_id(1);
			crs.setCreatedDate(date);
			crs.setLastUpdatedDate(date);
			crs.setBusinessunit_name("All");
			crs.setCourseconfigurationtemplate_id(23483);
			crs.setQuick_build_coursetype(1);
			
			//courseService.addCourse( crs );
			
			//Assert.assertNotNull();
    		
    		System.out.println("End........addCourse()..........");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
    }
    
    
    
    @Test
    public void updateCourse(){
    	try {
    		System.out.println("start......updateCourse()............");
    		Course crs = new Course();
    		crs.setContentowner_id(1);
			crs.setCreateUserId(11l);

			// Setting default values
			crs.setId(111738);
			crs.setName ("Test Case course 2");
			crs.setCeus(new BigDecimal(1));
			crs.setDescription("Test Case course 2");
			crs.setKeywords("Test Case course 2");
			crs.setLanguage_id(1);			
			crs.setLastUpdateUser(11l);
			crs.setContentowner_id(1);
			
			//courseService.updateCourse(crs);
    		
    		System.out.println("End........updateCourse()..........");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
    }
    */
}