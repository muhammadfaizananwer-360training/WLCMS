package com.softech.ls360.lcms.contentbuilder.service.test;

import com.softech.ls360.lcms.contentbuilder.model.ClassInstructor;
import com.softech.ls360.lcms.contentbuilder.repository.ClassInstructorRepository;
import com.softech.ls360.lcms.contentbuilder.service.impl.ClassInstructorServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.List;

/**
 * Created by muhammad.imran on 4/21/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, SynchronousClassServiceTest.class})
@ContextConfiguration(locations = { "classpath:app-context.xml" })
public class ClassInstructorTest extends AbstractTestExecutionListener {

    @Autowired
    public ClassInstructorServiceImpl classInstructorService;

    @Autowired
    public ClassInstructorRepository classInstructorRepository;

    //   @Test
   /* public void save(){

        ClassInstructor obj = new ClassInstructor();
        obj.setName("Test");
        obj.setIsActive(true);
        classInstructorService.save(obj);

    }*/

    //@Test
    /*public void getAuthorsByContentOwner(){
        List<ClassInstructor> lst = classInstructorService.findByContentOwnerId(2L);
        System.out.print(lst.size());
    }*/

    @Test
    public void getAuthorsByContentOwnerAndEmail(){
        //ClassInstructor lst = classInstructorService.findByContentOwnerIdAndEmail(1L, "Abdus@360training.com");
        //Page<ClassInstructor> lst = classInstructorRepository.findAll(new PageRequest(0, 10));

        /*for(ClassInstructor obj : lst.getContent()){
            System.out.println("ID : " + obj.getId());
        }*/
       // System.out.print(lst.getBackground());
    }


}