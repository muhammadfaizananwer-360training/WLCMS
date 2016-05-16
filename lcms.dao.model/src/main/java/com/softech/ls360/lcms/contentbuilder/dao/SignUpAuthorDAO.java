package com.softech.ls360.lcms.contentbuilder.dao;


import com.softech.ls360.lcms.contentbuilder.model.SignUpAuthor;

/**
 * 
 * @author haider.ali
 *
 */
public interface SignUpAuthorDAO   extends GenericDAO<SignUpAuthor> { 

	public SignUpAuthor  save(SignUpAuthor signUpAuthor) ;
	public SignUpAuthor  getSignUpUserByActivationCode(SignUpAuthor signUpAuthor) ;
	public void activateAuthor(Long authorId,String marketoXml);
	

}
