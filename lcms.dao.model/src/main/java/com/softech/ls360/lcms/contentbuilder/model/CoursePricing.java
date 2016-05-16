package com.softech.ls360.lcms.contentbuilder.model;


import javax.persistence.Entity;
import javax.persistence.Id;
 
@Entity 
public class CoursePricing  implements java.io.Serializable { 
 
	private static final long serialVersionUID = 4399549077058319833L;
	@Id 
	private long id;
    private String name = null;
    private String businessKey = null;
    private String mSRP = null;
    private String lowestSalePrice = null;
    private String updatedBy = null;
    private double offerprice = 0.0f;
    private boolean manageSFPrice = false;
    
    public CoursePricing(long id, String name, String businessKey,
    		String mSRP, String lowestSalePrice, String updatedBy , double offerprice,
     boolean manageSFPrice) {
		super();
		this.id = id;
		this.name = name;
		this.businessKey = businessKey;
		this.mSRP = mSRP;
		this.lowestSalePrice = lowestSalePrice;
		this.updatedBy = updatedBy;
		this.setOfferprice(offerprice);
		this.setManageSFPrice(manageSFPrice);
	}
    
    public CoursePricing(){
    	super();
    }
 
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public String getmSRP() {
		return mSRP;
	}

	public void setmSRP(String mSRP) {
		this.mSRP = mSRP;
	}

	public String getLowestSalePrice() {
		return lowestSalePrice;
	}

	public void setLowestSalePrice(String lowestSalePrice) {
		this.lowestSalePrice = lowestSalePrice;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public double getOfferprice() {
		return offerprice;
	}

	public void setOfferprice(double offerprice) {
		this.offerprice = offerprice;
	}

	public boolean isManageSFPrice() {
		return manageSFPrice;
	}

	public void setManageSFPrice(boolean manageSFPrice) {
		this.manageSFPrice = manageSFPrice;
	}   
}
