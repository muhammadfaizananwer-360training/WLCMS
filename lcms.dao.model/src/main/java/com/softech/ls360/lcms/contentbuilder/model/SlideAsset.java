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
/*
@SqlResultSetMapping(name="LCMS_WEB_SEARCH_ASSET", classes = {
	    @ConstructorResult(targetClass = SlideAsset.class, 
	    columns = {
	    	@ColumnResult(name="ID"), 
	   		@ColumnResult(name="NAME")
	    , @ColumnResult(name="ASSETTYPE")
	    , @ColumnResult(name="HEIGHT")
	    , @ColumnResult(name="VERSION")
	    , @ColumnResult(name="AssetVersion_ID")
	    , @ColumnResult(name="DURATION")
	    , @ColumnResult(name="LOCATION")
	    , @ColumnResult(name="Description")
	    })
	})
*/

//This stored procedure returns a result set and has one input parameter.
/*@NamedStoredProcedureQuery(
 name = "LCMS_WEB_SEARCH_ASSET",
 resultClasses = SlideAsset.class,
 procedureName = "LCMS_WEB_SEARCH_ASSET",
 parameters = {
     @StoredProcedureParameter(mode=IN, name="NAME", type=String.class),
     @StoredProcedureParameter(mode=IN, name="CONTENTOWNER_ID", type=Integer.class),
     @StoredProcedureParameter(mode=IN, name="ASSETTYPE", type=Integer.class)
 }
)
*/
public class SlideAsset  implements java.io.Serializable { 

	/**
	 * 
	 */
	private static final long serialVersionUID = 4399549077058319833L;
	@Id
	private long id;
    private String name ;
    private String keywords = null;
    private String assettype = null;
	private String content = null;
	private String orientationkey = null;
	private int assetversion_id;
	private int scene_id;
	private String width;
	private String height;
	private String size;
	private String version;
	private String description;
	private int VersionId;
	private String streamingServerPath;


	private int duration;
	private String location;
	private String MC_SCENE_XML;
	
	@Transient
	private int createUser_id;
	@Transient
	Long LastUpdateUser;
    
    public Long getLastUpdateUser() {
		return LastUpdateUser;
	}


	public void setLastUpdateUser(Long lastUpdateUser) {
		LastUpdateUser = lastUpdateUser;
	}


	public SlideAsset(long id, String name, String keywords, String assettype, String content, String orientationkey,
			//Integer assetversion_id, 
			Integer scene_id) {//, long current_assetversion_id) {
		super();
		this.id = id;
		this.name = name;
		this.keywords = keywords;
		this.assettype = assettype;
		this.content = content;		
		this.orientationkey = orientationkey;
		//this.assetversion_id = assetversion_id;
		this.scene_id = scene_id;
		//this.current_assetversion_id = current_assetversion_id;
		
	
	}   
    
    
    public SlideAsset(long id, String name,  String assettype, String height, String width, String version, 
 			Integer assetversion_id, String description) {//, long current_assetversion_id) {
 		super();
 		this.id = id;
 		this.name = name; 		
 		this.assettype = assettype;
 		this.height = height;
 		this.width  = width;
 		this.version = version;
 		this.assetversion_id = assetversion_id;
 		this.description = description;
 	}   
    
    
    // 	search assets
    public SlideAsset(long id, String name,  String assettype, String height, String width, String version, 
 			Integer assetversion_id, int duration, String location, String description) {
 		super();
 		this.id = id;
 		this.name = name; 		
 		this.assettype = assettype;
 		this.height = height;
 		this.width  = width;
 		this.version = version;
 		this.assetversion_id = assetversion_id;
 		this.duration = duration;
 		this.location = location;
 		this.description = description;
 	}   
    
    public SlideAsset(){
    	super();
    }
	public SlideAsset(long id, String name) {
		super();
		this.id = id;
		this.name = name;
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
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getAssettype() {
		return assettype;
	}
	public void setAssettype(String assettype) {
		this.assettype = assettype;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getOrientationkey() {
		return orientationkey;
	}
	public void setOrientationkey(String orientationkey) {
		this.orientationkey = orientationkey;
	}
	


	/*public Integer getAssetversion_id() {
		return assetversion_id;
	}

	public void setAssetversion_id(Integer assetversion_id) {
		this.assetversion_id = assetversion_id;
	}*/


	public Integer getScene_id() {
		return scene_id;
	}
	public void setScene_id(Integer scene_id) {
		this.scene_id = scene_id;
	}


	public String getWidth() {
		return width;
	}


	public void setWidth(String width) {
		this.width = width;
	}


	public String getHeight() {
		return height;
	}


	public void setHeight(String height) {
		this.height = height;
	}


	public String getSize() {
		return size;
	}


	public void setSize(String size) {
		this.size = size;
	}


	public String getVersion() {
		return version;
	}


	public void setVersion(String version) {
		this.version = version;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public int getAssetversion_id() {
		return assetversion_id;
	}


	public void setAssetversion_id(int assetversion_id) {
		this.assetversion_id = assetversion_id;
	}


	public int getCreateUser_id() {
		return createUser_id;
	}


	public void setCreateUser_id(int createUser_id) {
		this.createUser_id = createUser_id;
	}
	
	
	public int getDuration() {
		return duration;
	}


	public void setDuration(int duration) {
		this.duration = duration;
	}


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}


	public void setScene_id(int scene_id) {
		this.scene_id = scene_id;
	}


	public int getVersionId() {
		return VersionId;
	}


	public void setVersionId(int versionId) {
		VersionId = versionId;
	}


	public String getStreamingServerPath() {
		return streamingServerPath;
	}


	public void setStreamingServerPath(String streamingServerPath) {
		this.streamingServerPath = streamingServerPath;
	}


	public String getMC_SCENE_XML() {
		return MC_SCENE_XML;
	}


	public void setMC_SCENE_XML(String mC_SCENE_XML) {
		MC_SCENE_XML = mC_SCENE_XML;
	}


   
}
