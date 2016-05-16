package com.softech.ls360.lcms.contentbuilder.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;


@Entity
@Table(name = "ADDRESS")
public class Address  implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@javax.persistence.TableGenerator(name = "ADDRESS_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "ADDRESS", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "ADDRESS_ID")
	private int id;
	
	 
	 public int getId() {
	  return id;
	 }
	
	
	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getStreetAddress2() {
		return streetAddress2;
	}

	public void setStreetAddress2(String streetAddress2) {
		this.streetAddress2 = streetAddress2;
	}

	public String getStreetAddress3() {
		return streetAddress3;
	}

	public void setStreetAddress3(String streetAddress3) {
		this.streetAddress3 = streetAddress3;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}
	@Column(name="STREETADDRESS")
	private String streetAddress = null;
	
	@Column(name="STREETADDRESS2")
	private String streetAddress2 = null;
	
	@Column(name="STREETADDRESS3")
	private String streetAddress3 = null;
	
	@Column(name="CITY")
	private String city = null;
	
	@Column(name="STATE")
	private String state = null;
	
	@Column(name="ZIPCODE")
	private String zipcode = null;
	
	@Column(name="COUNTRY")
	private String country = null;
	
	@Column(name="PROVINCE")
	private String province = null;
	
}
