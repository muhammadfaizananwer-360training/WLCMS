package com.softech.ls360.lcms.contentbuilder.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * table name [SCENE]
 * @author muhammad.rehan
 *
 */
@Entity 
public class Slide  implements java.io.Serializable { 

	/**
	 * 
	 */
	private static final long serialVersionUID = 4399549077058319833L;
	@Id 
	private long id;
    private String name = null;
    private String sceneGUID = null;
    private String orientationKey = null;
	private long duration;
	private long TemplateID;
	private String TemplateName;
	private long Scene_Id;
	private int contentObject_id;
	private int contentObjectScene_id;
	private String slideTemplateURL;
	private String templateType;
	private String embedCode;
	private Boolean isEmbedCode;
	
    @Transient
    private Integer contentOwner_ID;
    @Transient
    private Integer course_ID;
	@Transient
    private Integer asset_ID;
	@Transient
	private Integer CreateUserId;
	
	@Transient
	private Boolean nameVisbileTF;
    
	@Transient
	private Boolean displayStandardTF;
	
	@Transient
	private Boolean displayWideScreenTF;
	
	@Transient
	Long LastUpdateUser;
    
    public Slide(long id, String name, String sceneGUID,
			Integer contentOwner_ID, Integer course_ID, Integer asset_ID, String orientationKey, long duration, boolean displayStandardTF ,boolean displayWideScreenTF) {
		super();
		this.id = id;
		this.name = name;
		this.sceneGUID = sceneGUID;
		this.contentOwner_ID = contentOwner_ID;
		this.course_ID = course_ID;
		this.asset_ID = asset_ID;
		this.orientationKey = orientationKey;
		this.duration = duration;
		nameVisbileTF = true;
		this.displayStandardTF = displayStandardTF;
		this.displayWideScreenTF = displayWideScreenTF;
	}   
    
    
    public Slide(){
    	super();
    	nameVisbileTF = true;
    }
	public Slide(long id, String name) {
		super();
		this.id = id;
		this.name = name;
		nameVisbileTF = true;
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
	public Integer getAsset_ID() {
		return asset_ID;
	}
	public void setAsset_ID(Integer asset_ID) {
		this.asset_ID = asset_ID;
	}
	public Integer getCourse_ID() {
		return course_ID;
	}
	public void setCourse_ID(Integer course_ID) {
		this.course_ID = course_ID;
	}
	public Integer getContentOwner_ID() {
		return contentOwner_ID;
	}
	public void setContentOwner_ID(Integer contentOwner_ID) {
		this.contentOwner_ID = contentOwner_ID;
	}
	public String getSceneGUID() {
		return sceneGUID;
	}
	public void setSceneGUID(String sceneGUID) {
		this.sceneGUID = sceneGUID;
	}


	public String getOrientationKey() {
		return orientationKey;
	}


	public void setOrientationKey(String orientationKey) {
		this.orientationKey = orientationKey;
	}


	public Integer getCreateUserId() {
		return CreateUserId;
	}


	public void setCreateUserId(Integer createUserId) {
		CreateUserId = createUserId;
	}


	public long getDuration() {
		return duration;
	}


	public void setDuration(long duration) {
		this.duration = duration;
	}
    
    
	public long getTemplateID() {
		return TemplateID;
	}


	public void setTemplateID(long templateID) {
		TemplateID = templateID;
	}


	public long getScene_Id() {
		return Scene_Id;
	}


	public void setScene_Id(long scene_Id) {
		Scene_Id = scene_Id;
	}


	public int getContentObject_id() {
		return contentObject_id;
	}


	public void setContentObject_id(int contentObject_id) {
		this.contentObject_id = contentObject_id;
	}


	public String getTemplateName() {
		return TemplateName;
	}


	public void setTemplateName(String templateName) {
		TemplateName = templateName;
	}


	public String getSlideTemplateURL() {
		return slideTemplateURL;
	}


	public void setSlideTemplateURL(String slideTemplateURL) {
		this.slideTemplateURL = slideTemplateURL;
	}


	public int getContentObjectScene_id() {
		return contentObjectScene_id;
	}


	public void setContentObjectScene_id(int contentObjectScene_id) {
		this.contentObjectScene_id = contentObjectScene_id;
	}


	public Boolean getNameVisbileTF() {
		return nameVisbileTF;
	}

	public void setNameVisbileTF(Boolean nameVisbileTF) {
		this.nameVisbileTF = nameVisbileTF;
	}
	

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}
	
	
	public Boolean getDisplayStandardTF() {
		return displayStandardTF;
	}


	public void setDisplayStandardTF(Boolean displayStandardTF) {
		this.displayStandardTF = displayStandardTF;
	}

		
	public Boolean getDisplayWideScreenTF() {
		return displayWideScreenTF;
	}


	public void setDisplayWideScreenTF(Boolean displayWideScreenTF) {
		this.displayWideScreenTF = displayWideScreenTF;
	}


	public Long getLastUpdateUser() {
		return LastUpdateUser;
	}


	public void setLastUpdateUser(Long lastUpdateUser) {
		LastUpdateUser = lastUpdateUser;
	}


	public String getEmbedCode() {
		return embedCode;
	}


	public void setEmbedCode(String embedCode) {
		this.embedCode = embedCode;
	}

	
	public Boolean getIsEmbedCode() {
		if(isEmbedCode == null)
			isEmbedCode=Boolean.FALSE;
		return isEmbedCode;
	}


	public void setIsEmbedCode(Boolean isEmbedCode) {
		this.isEmbedCode = isEmbedCode;
	}
	
	
	

}
