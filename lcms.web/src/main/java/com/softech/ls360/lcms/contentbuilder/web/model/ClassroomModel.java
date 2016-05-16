package com.softech.ls360.lcms.contentbuilder.web.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;


public class ClassroomModel {
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ClassUpdate {
		private Long id;
		private Long maximumClassSize;
		private Long locId;
		private Long classInsId;
		
		//following field will be used in reflection only (by modal mapper). So no need to define accessors for them.
		public Integer timeZoneId;
		public Date enrollmentCloseDate;
		public String className;
		
		
		public Long getMaximumClassSize() {
			return (maximumClassSize == null)? new Long(Long.MAX_VALUE): maximumClassSize;
		}
		public void setMaximumClassSize(Long maximumClassSize) {
			this.maximumClassSize = maximumClassSize;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public Long getLocId() {
			return locId;
		}
		public void setLocId(Long locId) {
			this.locId = locId;
		}

		public Long getClassInsId() {
			return classInsId;
		}

		public void setClassInsId(Long classInsId) {
			this.classInsId = classInsId;
		}
	}
	}
