package com.softech.ls360.lcms.contentbuilder.dao.impl;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.persistence.StoredProcedureQuery;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.transaction.annotation.Transactional;
import com.softech.ls360.lcms.contentbuilder.dao.GenericDAOImpl;
import com.softech.ls360.lcms.contentbuilder.dao.ReportDAO;

import com.softech.ls360.lcms.contentbuilder.model.SignUpAuthor;




public class ReportDAOImpl extends GenericDAOImpl<SignUpAuthor> implements ReportDAO{
	
	private static Logger logger = Logger.getLogger(ReportDAOImpl.class);
	//@Autowired
	//private JdbcTemplate jdbcTemplate;



	@Transactional
	@Override
	public List<?> getSignupAuthorData() {
		StoredProcedureQuery qr = createQueryParameters("WLCMS_GetAuthorSignupData");
		List<?> courseRows = (List<?>) qr.getResultList();
		return courseRows;
/*

		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
				.withProcedureName("WLCMS_GetAuthorSignupData").returningResultSet("rs1",new RowMapper<Object[]>()
						{
							@Override
							public Object[] mapRow(ResultSet rs, int rowNum) throws SQLException
							{
								return new Object[] { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4) , rs.getDate(5), rs.getString(6), rs.getDate(7), rs.getString(8) , rs.getString(9), rs.getString(10), rs.getString(11)};
							}
						}

				);


		*//*Map<String, Object> inParamMap = new HashMap<String, Object>();
		inParamMap.put("id", 1);
		SqlParameterSource in = new MapSqlParameterSource(inParamMap);*//*
		Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute();
		List<?> lst = (List<?>)simpleJdbcCallResult.get("rs1");
		return  lst;
	}*/
	}
}
