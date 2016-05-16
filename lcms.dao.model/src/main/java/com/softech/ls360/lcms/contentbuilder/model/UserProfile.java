package com.softech.ls360.lcms.contentbuilder.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name="VU360User")
public class UserProfile implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8306135554366046426L;
	@TableGenerator(
        name="seqUserProfile", 
        table="VU360_SEQ",
        pkColumnName="TABLE_NAME", 
        valueColumnName="NEXT_ID", 
        pkColumnValue="vu360user",
        allocationSize = 20)

	@GeneratedValue(strategy=GenerationType.TABLE, generator="seqUserProfile") 
	@Id
	private long Id;
	
	@Column(name="USERNAME")
    private String username = null;
	
	@Column(name="PASSWORD")
    private String password = null;
	
	@Column(name="EMAILADDRESS")
    private String emailAddress = null;
	
	@Column(name="FIRSTNAME")
    private String firstName = null;
	
	@Column(name="LASTNAME")
    private String lastName = null;
	
	@Column(name="LASTUPDATEDDATE")
    private Date lastUpdatedDate = null;
	
	@Column(name="PROFILEIMAGEASSETID")
	private Integer profileImageAssetId;
    
    public UserProfile(){
    }
    
	public long getId() {
		return Id;
	}
	public void setId(long id) {
		Id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
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
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public Integer getProfileImageAssetId() {
		return profileImageAssetId;
	}

	public void setProfileImageAssetId(Integer profileImageAssetId) {
		this.profileImageAssetId = profileImageAssetId;
	}
}