package com.softech.ls360.lcms.contentbuilder.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;



@Entity
public class LearningObject implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	int Id;
	String Name;
	String Description;
	
	
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	
	public LearningObject ()
	{
		
	}
	
	public LearningObject (int id,	String name,	String description)
	{
		this.Id = id;
		this.Name = name;
		this.Description = description;
	}
}
