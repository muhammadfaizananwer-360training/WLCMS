package com.softech.ls360.lcms.contentbuilder.model;

import javax.persistence.*;
import java.io.Serializable;



@Entity
@Table(name = "LOCATION")
public class Location implements Serializable{


	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/*@Id
	@javax.persistence.TableGenerator(name = "LOCATION_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "LOCATION", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "LOCATION_ID")*/

	private Long id;
	private String enabledtf = null;
	private String streetAddress;
	/*private Set<SynchronousClass> classrooms = new HashSet<SynchronousClass>(
			0);
	*/






	@Transient
	public String getStreetAddress() {
		return streetAddress;
	}


	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}


	@TableGenerator(
	        name="seqSynchronousClass",
	        table="VU360_SEQ",
	        pkColumnName="TABLE_NAME",
	        valueColumnName="NEXT_ID",
	        pkColumnValue="LOCATION",
	        allocationSize = 1)
	    @Id
	    @GeneratedValue(strategy=GenerationType.TABLE, generator="seqSynchronousClass")
		public Long getId() {
		  return id;
		 }





		@Column(name="ENABLEDTF")
		public String getEnabledtf() {
			return enabledtf;
		}

		public void setEnabledtf(String enabledtf) {
			this.enabledtf = enabledtf;
		}




	@Column(name="DESCRIPTION")
	 public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}


	@Column(name="LOCATIONNAME")
	public String getLocationname() {
		return locationname;
	}



	public void setLocationname(String locationname) {
		this.locationname = locationname;
	}


	@Column(name="ZIP")
	public String getZip() {
		return zip;
	}



	public void setZip(String zip) {
		this.zip = zip;
	}



	@Column(name="PHONE")
	public String getPhone() {
		return phone;
	}



	public void setPhone(String phone) {
		this.phone = phone;
	}


	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="CITY")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name="STATE")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}


	@Column(name="COUNTRY")
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}






	/*@OneToMany(fetch = FetchType.LAZY,mappedBy="location",cascade=CascadeType.ALL)
	public Set<SynchronousClass> getClassrooms() {
		return classrooms;
	}


	public void setClassrooms(Set<SynchronousClass> classrooms) {
		this.classrooms = classrooms;
	}*/







	private String description = null;


	private String locationname = null;


	private String city = null;


	private String state = null;


	private String zip = null;


	private String country = null;


	private String phone = null;


	private Integer contentownerId = null;




	@Column(name="CONTENTOWNER_ID")
	public Integer getContentownerId() {
		return contentownerId;
	}



	public void setContentownerId(Integer contentownerId) {
		this.contentownerId = contentownerId;
	}




	private Address address;


	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="ADDRESS_ID", nullable=true, insertable=true, updatable=true)
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

}
