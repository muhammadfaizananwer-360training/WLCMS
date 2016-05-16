package com.softech.ls360.lcms.contentbuilder.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.softech.ls360.lcms.contentbuilder.dao.ContentOwnerRoyaltySettingsDAO;
import com.softech.ls360.lcms.contentbuilder.dao.SignUpAuthorDAO;
import com.softech.ls360.lcms.contentbuilder.model.ContentOwnerRoyaltySettings;
import com.softech.ls360.lcms.contentbuilder.model.SignUpAuthor;
import com.softech.ls360.lcms.contentbuilder.service.ISignUpAuthorService;
import com.softech.ls360.lcms.contentbuilder.utils.LCMSProperties;


/**
 * 
 * @author haider.ali
 *
 */
public class SignUpAuthorServiceImpl implements ISignUpAuthorService{

	
	@Autowired
	public SignUpAuthorDAO signUpAuthorDAO;
	
	@Autowired
	ContentOwnerRoyaltySettingsDAO royaltySettingsDAO;
	
	public SignUpAuthorDAO getSignUpAuthorDAO() {
		return signUpAuthorDAO;
	}

	public void setSignUpAuthorDAO(SignUpAuthorDAO signUpAuthorDAO) {
		this.signUpAuthorDAO = signUpAuthorDAO;
	}

	@Override
	public SignUpAuthor save(SignUpAuthor signUpAuthor) {
		return signUpAuthorDAO.save(signUpAuthor);
	}

	@Override
	public SignUpAuthor getSignUpUserByActivationCode(SignUpAuthor signUpAuthor) {
			return signUpAuthorDAO.getSignUpUserByActivationCode(signUpAuthor);
	}
	
	@Override
	public void activateAuthor(Long id,String marketoXml){
		 signUpAuthorDAO.activateAuthor(id,marketoXml);
	}
	
	@Override
	public boolean insertDefaultyRoyaltySettings(SignUpAuthor author){
		
		ContentOwnerRoyaltySettings settings  = royaltySettingsDAO.getRoyaltySettingsByUserName(author.getLoginName());
		
		if(settings == null){
			return false;
		}
		float onlineRoyalty = Float.parseFloat(LCMSProperties.getLCMSProperty("roaylty.settings.default.online.royalty.percentage"));
		float classroomRoyalty = Float.parseFloat(LCMSProperties.getLCMSProperty("roaylty.settings.default.classroom.royalty.percentage"));
		float webinarRoyalty = Float.parseFloat(LCMSProperties.getLCMSProperty("roaylty.settings.default.webinar.royalty.percentage"));
		
		return royaltySettingsDAO.updateRoyaltSettings(settings.getContentOwnerId(), onlineRoyalty, classroomRoyalty, webinarRoyalty);
	}
}
