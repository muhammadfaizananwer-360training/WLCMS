package com.softech.ls360.lcms.contentbuilder.dao;


import com.softech.ls360.lcms.contentbuilder.model.*;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public interface ClassInstructorDAO extends GenericDAO<ClassInstructor> {
	public List<ClassInstructor> findByContentOwnerId(Long contentOwnerId);
	public ClassInstructor findByContentOwnerIdAndEmail(Long contentOwnerId,String email);

	List<ClassInstructor> findByContentOwnerIdAndEmails(Long contentOwnerId, Collection<String> emails);
}
