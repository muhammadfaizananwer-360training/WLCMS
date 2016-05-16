package com.softech.ls360.lcms.contentbuilder.repository;

import com.softech.ls360.lcms.contentbuilder.model.ClassInstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by muhammad.imran on 4/21/2016.
 */

public interface ClassInstructorRepository extends CrudRepository<ClassInstructor, Long>{//, JpaRepository<ClassInstructor, Integer> {//,PagingAndSortingRepository<ClassInstructor, Integer> {

    public List<ClassInstructor> findByContentOwnerId(Long contentOwnerId);
    public List<ClassInstructor> findByContentOwnerIdAndEmail(Long contentOwnerId,String email);
    @Modifying
    @Transactional
    @Query("update ClassInstructor u set u.isActive=false where u.id in (:syncIds)")
    public int updateByIdIn(@Param("syncIds") List<Long> ids);
    public ClassInstructor findByEmail(String email);

    //public Page<ClassInstructor> findAll(Pageable pageable);
}
