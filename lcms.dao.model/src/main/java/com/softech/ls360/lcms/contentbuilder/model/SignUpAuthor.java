package com.softech.ls360.lcms.contentbuilder.model;

import java.util.Date;



/**
 * 
 * @author haider.ali
 *
 */
public class SignUpAuthor {

	
	private Long id;
	public String firstName;
	public String lastName;
	public String email;
	public String reenterEmail;
	public Integer useMyemailTF;
	public String loginName;
	public String passwrod;
	public String reenterPasswrod;	
	public String activationCode;
	public String urlSource;
	public Date creationDate;
	public Date activationDate;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getReenterEmail() {
		return reenterEmail;
	}
	public void setReenterEmail(String reenterEmail) {
		this.reenterEmail = reenterEmail;
	}
	public Integer getUseMyemailTF() {
		return useMyemailTF;
	}
	public void setUseMyemailTF(Integer useMyemailTF) {
		this.useMyemailTF = useMyemailTF;
	}

	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getPasswrod() {
		return passwrod;
	}
	public void setPasswrod(String passwrod) {
		this.passwrod = passwrod;
	}
	public String getReenterPasswrod() {
		return reenterPasswrod;
	}
	public void setReenterPasswrod(String reenterPasswrod) {
		this.reenterPasswrod = reenterPasswrod;
	}

	public String getActivationCode() {
		return activationCode;
	}
	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}
	public String getUrlSource() {
		return urlSource;
	}
	public void setUrlSource(String urlSource) {
		this.urlSource = urlSource;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Date getActivationDate() {
		return activationDate;
	}
	public void setActivationDate(Date activationDate) {
		this.activationDate = activationDate;
	}
	
}
