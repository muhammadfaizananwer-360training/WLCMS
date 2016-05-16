package com.softech.ls360.lcms.contentbuilder.model;

public class CourseDisplayOrder {

	private int Id;
	private int Item_Id;
	private String Item_Type;
	private int DisplayOrder;
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public int getItem_Id() {
		return Item_Id;
	}
	public void setItem_Id(int item_Id) {
		Item_Id = item_Id;
	}
	public String getItem_Type() {
		return Item_Type;
	}
	public void setItem_Type(String item_Type) {
		Item_Type = item_Type;
	}
	public int getDisplayOrder() {
		return DisplayOrder;
	}
	public void setDisplayOrder(int displayOrder) {
		DisplayOrder = displayOrder;
	}
}
