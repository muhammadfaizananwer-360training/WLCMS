package com.softech.ls360.lcms.contentbuilder.dao.impl;

import com.softech.ls360.lcms.contentbuilder.dao.ClassInstructorDAO;
import com.softech.ls360.lcms.contentbuilder.dao.GenericDAOImpl;
import com.softech.ls360.lcms.contentbuilder.model.ClassInstructor;
import com.softech.ls360.lcms.contentbuilder.model.SynchronousSession;
import org.springframework.stereotype.Component;

import javax.persistence.Query;
import java.util.Collection;
import java.util.List;

/**
 * Created by abdul.wahid on 4/25/2016.
 */

@Component
public class ClassInstructorDAOImpl extends GenericDAOImpl<ClassInstructor> implements ClassInstructorDAO {
    @Override
    public List<ClassInstructor> findByContentOwnerId(Long contentOwnerId) {
        Query query = entityManager.createQuery(" SELECT ci FROM ClassInstructor ci   "
                + " WHERE ci.contentOwnerId=:contentOwnerId"
                + " and ci.isActive=true");

        query.setParameter("contentOwnerId", contentOwnerId);
        return  (List<ClassInstructor>) query.getResultList();
    }

    @Override
    public ClassInstructor findByContentOwnerIdAndEmail(Long contentOwnerId, String email) {
        Query query = entityManager.createQuery(" SELECT ci FROM ClassInstructor ci   "
                + " WHERE ci.contentOwnerId=:contentOwnerId"
                + " and ci.email=:email"
                + " and ci.isActive=true");

        query.setParameter("contentOwnerId", contentOwnerId);
        query.setParameter("email", email);
        List<ClassInstructor> insts = (List<ClassInstructor>) query.getResultList();

        if (!insts.isEmpty()) {
            return insts.get(0);
        }
        return null;
    }


    @Override
    public List<ClassInstructor> findByContentOwnerIdAndEmails(Long contentOwnerId, Collection<String> emails) {
        Query query = entityManager.createQuery(" SELECT ci FROM ClassInstructor ci   "
                + " WHERE ci.contentOwnerId=:contentOwnerId"
                + " and ci.email in :emails"
                + " and ci.isActive=true");

        query.setParameter("contentOwnerId", contentOwnerId);
        query.setParameter("emails", emails);
        return (List<ClassInstructor>) query.getResultList();
    }
}
