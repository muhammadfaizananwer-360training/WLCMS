package com.softech.ls360.lcms.contentbuilder.service.impl;

import com.softech.ls360.lcms.contentbuilder.dao.MarketingDAO;
import com.softech.ls360.lcms.contentbuilder.dao.SynchronousClassDAO;
import com.softech.ls360.lcms.contentbuilder.model.ClassInstructor;
import com.softech.ls360.lcms.contentbuilder.repository.ClassInstructorRepository;
import com.softech.ls360.lcms.contentbuilder.service.IClassInstructorService;
import com.softech.ls360.lcms.contentbuilder.utils.TypeConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muhammad.imran on 4/21/2016.
 */
public class ClassInstructorServiceImpl implements IClassInstructorService{

    @Autowired
    ClassInstructorRepository classInstructorRepository;
    @Autowired
    SynchronousClassDAO synchronousClassDAO;
    @Autowired
    MarketingDAO marketingDAO;

    @Override
    @Transactional
    public ClassInstructor save(ClassInstructor obj) {

        ClassInstructor emailVerifyObj = classInstructorRepository.findByEmail(obj.getEmail());

        if(emailVerifyObj!=null && ((obj.getId()!=null && !emailVerifyObj.getId().equals(obj.getId())) || obj.getId()==null)) {
            obj.setEmail("Email exist");
            return obj;
        }
        if(obj.getId()!=null && !obj.getId().equals(0L)){
            ClassInstructor persistObj = classInstructorRepository.findOne(obj.getId());
            persistObj.setPhoneNo(obj.getPhoneNo());
            persistObj.setFirstName(obj.getFirstName());
            persistObj.setLastName(obj.getLastName());
            persistObj.setEmail(obj.getEmail());
            return classInstructorRepository.save(persistObj);
        }
        return classInstructorRepository.save(obj);

    }

    @Override
    public boolean emailAlreadyExist(String email, Long id) {
        ClassInstructor emailVerifyObj = classInstructorRepository.findByEmail(email);

        if(emailVerifyObj!=null && ((id!=null && !emailVerifyObj.getId().equals(id)) || id==null)) {
            return true;
        }

        return false;
    }

    @Override
    public List<ClassInstructor> getAllClassInstructors() {
        return (List<ClassInstructor>)classInstructorRepository.findAll();
    }

    @Override
    public ClassInstructor findByContentOwnerIdAndEmail(Long contentOwnerId, String email) {
        return null;//classInstructorRepository.getClassInstructorbyEmailandContentOwnerId(email, contentOwnerId);
    }

    @Override
    public List<ClassInstructor> findByContentOwnerId(Long contentOwnerId) {
        return classInstructorRepository.findByContentOwnerId(contentOwnerId);
    }



    @Override
    public int deleteInstructors(String commasepareteIds)throws Exception {

        List<Long> ids = new ArrayList<>();
        String[] strArray = commasepareteIds.split(",");
        for(int i=0; i<strArray.length; i++){
            ids.add(TypeConvertor.AnyToLong(strArray[i]));
        }
        if(synchronousClassDAO.IsInstructorExist(ids)) {
            throw new Exception("Error in classroom");
        }
        if(marketingDAO.IsInstructorExist(ids)) {
            throw new Exception("Error in course");
        }
       return classInstructorRepository.updateByIdIn(ids);


    }

    @Override
    public ClassInstructor findById(Long instructorId) {
        return classInstructorRepository.findOne(instructorId);

    }
}
