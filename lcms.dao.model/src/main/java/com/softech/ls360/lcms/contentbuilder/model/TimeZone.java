package com.softech.ls360.lcms.contentbuilder.model;

 
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@NamedNativeQueries({
	@NamedNativeQuery(
        name="Timezone.getAllTimeZone",
		query=" SELECT id,zone,code,hours,minutes FROM TimeZone",
		resultClass=com.softech.ls360.lcms.contentbuilder.model.TimeZone.class
	),
	@NamedNativeQuery(
	        name="Timezone.getTimeZoneById",
			query=" SELECT id,zone,code,hours,minutes FROM TimeZone WHERE id = :ID",
			resultClass=com.softech.ls360.lcms.contentbuilder.model.TimeZone.class
		)
})
 
@Table(name = "TIMEZONE")
public class TimeZone  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;
	
	@Column(name = "CODE")
	private String code;
	
	@Column(name = "ZONE")
	private String zone;
	
	@Column(name = "HOURS")
	private int hours;
	
	@Column(name = "MINUTES")
	private int minutes;
	
	@Column(name = "ID")
	@TableGenerator(
        name="seqTimezoneClass", 
        table="VU360_SEQ",
        pkColumnName="TABLE_NAME", 
        valueColumnName="NEXT_ID", 
        pkColumnValue="TIMEZONE",
        allocationSize = 1)
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE, generator="seqTimezoneClass") 
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}
	
	

}
