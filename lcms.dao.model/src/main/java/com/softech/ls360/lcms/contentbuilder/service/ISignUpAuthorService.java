package com.softech.ls360.lcms.contentbuilder.service;

import com.softech.ls360.lcms.contentbuilder.model.SignUpAuthor;

public interface ISignUpAuthorService {

	
	public SignUpAuthor save(SignUpAuthor signUpAuthor) ;
	public SignUpAuthor getSignUpUserByActivationCode(SignUpAuthor signUpAuthor) ;
	public void activateAuthor(Long id,String marketoXml);
	public boolean insertDefaultyRoyaltySettings(SignUpAuthor author);
}
