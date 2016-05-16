package com.softech.ls360.lcms.contentbuilder.dao.impl;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.softech.ls360.lcms.contentbuilder.dao.GenericDAOImpl;
import com.softech.ls360.lcms.contentbuilder.dao.LMSUserRoleDAO;
import com.softech.ls360.lcms.contentbuilder.model.LMSUserRole;



@Repository("LMSUserRoleDAO") 

/*
@NamedNativeQueries({
	@NamedNativeQuery(
        name="LMSUserRole.getLMSUserRoleByUserId",
		query=" SELECT ROLE_ID,ROLENAME,ROLE_TYPE FROM VU360USER_ROLE UR   "
					+ " INNER JOIN LMSROLE LR ON LR.ID = UR.ROLE_ID "
					+ " WHERE UR.USER_ID = :VU360USER_ID ",
		resultSetMapping="getLMSUserRoleByUserId"
	)
})
*/

public class LMSUserRoleDAOImpl extends GenericDAOImpl<LMSUserRoleDAO> implements LMSUserRoleDAO {


	@Override
	@Transactional
	public List<LMSUserRole> getLMSUserRoleByUserId(BigInteger UserID){
	
		Query query = entityManager.createNamedQuery("LMSUserRole.getLMSUserRoleByUserId",LMSUserRole.class);
		query.setParameter("VU360USER_ID", UserID);
	//	query.setParameter("VU360USER_ID", 200616);
		List<LMSUserRole> resultList = query.getResultList();
		return CollectionUtils.isEmpty(resultList) ? null : resultList;
	}
}
