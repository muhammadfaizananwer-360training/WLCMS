package com.softech.ls360.lcms.contentbuilder.dao;

import java.math.BigInteger;
import com.softech.ls360.lcms.contentbuilder.model.LMSUserRole;
import java.util.List;


public interface LMSUserRoleDAO {
	public List<LMSUserRole> getLMSUserRoleByUserId(BigInteger UserID);
}
