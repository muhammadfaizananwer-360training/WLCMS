package com.softech.ls360.lcms.contentbuilder.service;

import com.softech.ls360.lcms.contentbuilder.model.ClassInstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by muhammad.imran on 4/21/2016.
 */
public interface IClassInstructorService {

    public ClassInstructor save(ClassInstructor obj);
    public List<ClassInstructor> getAllClassInstructors();
    public ClassInstructor findByContentOwnerIdAndEmail(Long contentOwnerId,String email);
    public List<ClassInstructor> findByContentOwnerId(Long contentOwnerId);
    public int deleteInstructors(String commasepareteIds)throws Exception;
    public ClassInstructor findById(Long instructorId);
    public boolean emailAlreadyExist(String email,Long id);
}
