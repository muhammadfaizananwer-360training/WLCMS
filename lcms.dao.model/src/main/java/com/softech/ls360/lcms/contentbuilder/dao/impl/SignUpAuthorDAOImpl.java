package com.softech.ls360.lcms.contentbuilder.dao.impl;

import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.softech.ls360.lcms.contentbuilder.dao.GenericDAOImpl;
import com.softech.ls360.lcms.contentbuilder.dao.SPCallingParams;
import com.softech.ls360.lcms.contentbuilder.dao.SignUpAuthorDAO;
import com.softech.ls360.lcms.contentbuilder.model.SignUpAuthor;
import com.softech.ls360.lcms.contentbuilder.utils.Encryptor;
import com.softech.ls360.lcms.contentbuilder.utils.LCMS_Util;
import com.softech.ls360.lcms.contentbuilder.utils.StringUtil;





/**
 * 
 * @author haider.ali
 *
 */

public class SignUpAuthorDAOImpl extends GenericDAOImpl<SignUpAuthor> implements SignUpAuthorDAO {

	private static Logger logger = Logger.getLogger(SignUpAuthorDAOImpl.class);

	@Transactional
	@Override
	public SignUpAuthor save(SignUpAuthor signUpAuthor) {

		logger.info("In SignUpAuthorDAOImpl::save Begin");

		try {
			Encryptor enc = new Encryptor();

			SPCallingParams sparam1 = LCMS_Util.createSPObject("FIRSTNAME", String.valueOf(signUpAuthor.getFirstName()) , String.class, ParameterMode.IN);
			SPCallingParams sparam2 = LCMS_Util.createSPObject("LASTNAME", String.valueOf(signUpAuthor.getLastName()) , String.class, ParameterMode.IN);
			SPCallingParams sparam3 = LCMS_Util.createSPObject("EMAIL", signUpAuthor.getEmail(), String.class, ParameterMode.IN);
			SPCallingParams sparam4 = LCMS_Util.createSPObject("USEMYEMAILTF", String.valueOf(signUpAuthor.getUseMyemailTF()) , Integer.class, ParameterMode.IN); 
			SPCallingParams sparam5 = LCMS_Util.createSPObject("LoginName", signUpAuthor.getLoginName() , String.class, ParameterMode.IN);
			SPCallingParams sparam6 = LCMS_Util.createSPObject("USERPASSOWRD", enc.encrypt( signUpAuthor.getPasswrod()) , String.class, ParameterMode.IN);			
			SPCallingParams sparam7 = LCMS_Util.createSPObject("ACTIVATIONCODE", signUpAuthor.getActivationCode(),  String.class, ParameterMode.IN );
			SPCallingParams sparam8 = LCMS_Util.createSPObject("URLSource", signUpAuthor.getUrlSource(),  String.class, ParameterMode.IN );
			
			StoredProcedureQuery storedProcedureQuery = createQueryParameters("INSERT_SIGNUP_AUTHOR", sparam1, sparam2, sparam3, sparam4, sparam5, sparam6,sparam7, sparam8 );
			storedProcedureQuery.execute();
			
		}

		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return signUpAuthor;
	}

	/**
	 * 
	 * Get signUp User by activation code 
	 */
	@Transactional
	@Override
	public SignUpAuthor getSignUpUserByActivationCode(SignUpAuthor signUpAuthor){

		SPCallingParams sparam1 = LCMS_Util.createSPObject("activation_code", String.valueOf(signUpAuthor.getActivationCode()) , String.class, ParameterMode.IN);		
		Object[] courseRows = callStoredProc("[GET_SIGNUP_AUTHOR]", sparam1).toArray();
		
		try {
			Encryptor enc = new Encryptor();
			
			for (Object course : courseRows) {

				Object[] courseRow = (Object[]) course;
				
				signUpAuthor.setId(Long.parseLong(StringUtil.ifNullReturnZero(courseRow[0])));
				signUpAuthor.setFirstName( (String) courseRow[1]);
				signUpAuthor.setLastName( (String) courseRow[2]);
				signUpAuthor.setEmail( (String) courseRow[3]);		
				signUpAuthor.setUseMyemailTF( Integer.parseInt(StringUtil.ifNullReturnZero( courseRow[4])));
				signUpAuthor.setLoginName((String) courseRow[5]);		
				signUpAuthor.setPasswrod(enc.decrypt((String) courseRow[6]));				
				signUpAuthor.setActivationCode((String) courseRow[7]);
				
			}

		} catch (Exception e) {
			logger.error("In SignUpDAOImpl::getSignUpUser ..................Exception..................");
			e.printStackTrace();
		}
		return signUpAuthor;
	}
	
	@Override
	@Transactional
	public void activateAuthor(Long authorId,String marketoXml){
		try{
		Query query = entityManager.createNativeQuery("UPDATE SIGNUP_AUTHOR SET ActivationDate=GETDATE(),MARKETO_XML=:MARKETO_XML WHERE ID=:authorId");
		
		query.setParameter("authorId", authorId);
		query.setParameter("MARKETO_XML", marketoXml);
		query.executeUpdate();
				
		}catch(Exception exp){
			logger.error(" Error in method::activateCourse. Message:" + exp.getMessage());
		}
		
		
	}
	
	


}
