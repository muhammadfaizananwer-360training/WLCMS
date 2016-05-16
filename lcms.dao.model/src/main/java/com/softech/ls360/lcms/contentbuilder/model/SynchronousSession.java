package com.softech.ls360.lcms.contentbuilder.model;

import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "SYNCHRONOUSSESSION")
@Where(clause = "status in ('U','A','D')")
public class SynchronousSession extends AbstractEntity implements Serializable{

	private static final long serialVersionUID = -5175335948065016579L;
	
	
	private Long id;
	

	private Date startDateTime;
	
	
	private Date endDateTime;
	
	
	private Long locationId;
	
	private String status;
	
	SynchronousClass syncClass;
	
	
	public SynchronousSession() {
	}
	
	
	
	
	
	@Column(name = "ID")
	@TableGenerator(
        name="seqSyncSession", 
        table="VU360_SEQ",
        pkColumnName="TABLE_NAME", 
        valueColumnName="NEXT_ID", 
        pkColumnValue="SYNCHRONOUSSESSION",
        allocationSize = 1)
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE, generator="seqSyncSession") 
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "STARTDATETIME")
	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}
	
	@Column(name = "ENDDATETIME")
	public Date getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(Date endDateTime) {
		this.endDateTime = endDateTime;
	}

	@Column(name = "LOCATION_ID")
	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	@ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="SYNCHRONOUSCLASS_ID", nullable = false) 
	public SynchronousClass getSyncClass() {
		return syncClass;
	}

	public void setSyncClass(SynchronousClass syncClass) {
		this.syncClass = syncClass;
	}

	@Column(name = "STATUS",insertable=false,updatable=true)
	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}

	
	
}
