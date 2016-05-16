package com.softech.ls360.lcms.contentbuilder.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Lesson implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String title;
	private String description;
	private ContentObject contentObject;
	
	private List<Slide> slideList = null;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<Slide> getSlideList() {
		return slideList;
	}
	public void setSlideList(List<Slide> slideList) {
		this.slideList = slideList;
	}
	
	public boolean addSlide(Slide slide){
		
		if(slide == null){
			return false;
		}
		
		if(slideList == null || slideList.size() == 0){
			this.slideList = new ArrayList<Slide>();
		}
		return this.slideList.add(slide);
	}
	public ContentObject getContentObject() {
		return contentObject;
	}
	public void setContentObject(ContentObject contentObject) {
		this.contentObject = contentObject;
	}
	
}
