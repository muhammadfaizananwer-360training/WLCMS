package com.softech.ls360.lcms.contentbuilder.webservice.asset.upload;

public class LMSServicesSoapProxy implements com.softech.ls360.lcms.contentbuilder.webservice.asset.upload.LMSServicesSoap {
  private String _endpoint = null;
  private com.softech.ls360.lcms.contentbuilder.webservice.asset.upload.LMSServicesSoap lMSServicesSoap = null;
  
  public LMSServicesSoapProxy() {
    _initLMSServicesSoapProxy();
  }
  
  public LMSServicesSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initLMSServicesSoapProxy();
  }
  
  private void _initLMSServicesSoapProxy() {
    try {
      lMSServicesSoap = (new com.softech.ls360.lcms.contentbuilder.webservice.asset.upload.LMSServicesLocator()).getLMSServicesSoap();
      if (lMSServicesSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)lMSServicesSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)lMSServicesSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (lMSServicesSoap != null)
      ((javax.xml.rpc.Stub)lMSServicesSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.softech.ls360.lcms.contentbuilder.webservice.asset.upload.LMSServicesSoap getLMSServicesSoap() {
    if (lMSServicesSoap == null)
      _initLMSServicesSoapProxy();
    return lMSServicesSoap;
  }
  
  public boolean uploadAssetInChunk(java.lang.String fileName, org.apache.axis.types.UnsignedByte[] buffer, long offset) throws java.rmi.RemoteException{
    if (lMSServicesSoap == null)
      _initLMSServicesSoapProxy();
    return lMSServicesSoap.uploadAssetInChunk(fileName, buffer, offset);
  }
  
  public int saveAsset(java.lang.String assetType, long assetId, java.lang.String name, java.lang.String description, java.lang.String keywords, java.lang.String fileName, int itemsPerPage, java.lang.String content, java.lang.String displayText1, java.lang.String displayText2, java.lang.String displayText3, java.lang.String affidavitType, int affidavitTemplateID, int contentOwnerId, int languageId, boolean fileUploaded, int loggedInUserID) throws java.rmi.RemoteException{
    if (lMSServicesSoap == null)
      _initLMSServicesSoapProxy();
    return lMSServicesSoap.saveAsset(assetType, assetId, name, description, keywords, fileName, itemsPerPage, content, displayText1, displayText2, displayText3, affidavitType, affidavitTemplateID, contentOwnerId, languageId, fileUploaded, loggedInUserID);
  }
  
  public int saveAsset_VSC(java.lang.String assetType, int assetId, java.lang.String name, java.lang.String description, java.lang.String keywords, java.lang.String fileName, int contentOwnerId, int languageId, boolean fileUploaded, int loggedInUserID, int sceneID) throws java.rmi.RemoteException{
    if (lMSServicesSoap == null)
      _initLMSServicesSoapProxy();
    return lMSServicesSoap.saveAsset_VSC(assetType, assetId, name, description, keywords, fileName, contentOwnerId, languageId, fileUploaded, loggedInUserID, sceneID);
  }
  
  public int updateAssetStatus(long[] assetIds, boolean active, int loggedInUserID) throws java.rmi.RemoteException{
    if (lMSServicesSoap == null)
      _initLMSServicesSoapProxy();
    return lMSServicesSoap.updateAssetStatus(assetIds, active, loggedInUserID);
  }
  
  public org.apache.axis.types.UnsignedByte[] downloadAssetInChunk(java.lang.String filePath, long offset, int bufferSize) throws java.rmi.RemoteException{
    if (lMSServicesSoap == null)
      _initLMSServicesSoapProxy();
    return lMSServicesSoap.downloadAssetInChunk(filePath, offset, bufferSize);
  }
  
  public java.lang.String[] getAssetFileInfo(long assetId) throws java.rmi.RemoteException{
    if (lMSServicesSoap == null)
      _initLMSServicesSoapProxy();
    return lMSServicesSoap.getAssetFileInfo(assetId);
  }
  
  public boolean publishCourseForLCMS_Web_QuickBuild(long courseId, java.lang.String publishMethod, long loggedInUserId) throws java.rmi.RemoteException{
    if (lMSServicesSoap == null)
      _initLMSServicesSoapProxy();
    return lMSServicesSoap.publishCourseForLCMS_Web_QuickBuild(courseId, publishMethod, loggedInUserId);
  }
  
  
}