/**
 * LMSServicesSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.softech.ls360.lcms.contentbuilder.webservice.asset.upload;

public interface LMSServicesSoap extends java.rmi.Remote {
    public boolean uploadAssetInChunk(java.lang.String fileName, org.apache.axis.types.UnsignedByte[] buffer, long offset) throws java.rmi.RemoteException;
    public int saveAsset(java.lang.String assetType, long assetId, java.lang.String name, java.lang.String description, java.lang.String keywords, java.lang.String fileName, int itemsPerPage, java.lang.String content, java.lang.String displayText1, java.lang.String displayText2, java.lang.String displayText3, java.lang.String affidavitType, int affidavitTemplateID, int contentOwnerId, int languageId, boolean fileUploaded, int loggedInUserID) throws java.rmi.RemoteException;
    public int saveAsset_VSC(java.lang.String assetType, int assetId, java.lang.String name, java.lang.String description, java.lang.String keywords, java.lang.String fileName, int contentOwnerId, int languageId, boolean fileUploaded, int loggedInUserID, int sceneID) throws java.rmi.RemoteException;
    public int updateAssetStatus(long[] assetIds, boolean active, int loggedInUserID) throws java.rmi.RemoteException;
    public org.apache.axis.types.UnsignedByte[] downloadAssetInChunk(java.lang.String filePath, long offset, int bufferSize) throws java.rmi.RemoteException;
    public java.lang.String[] getAssetFileInfo(long assetId) throws java.rmi.RemoteException;
    public boolean publishCourseForLCMS_Web_QuickBuild(long courseId, java.lang.String publishMethod, long loggedInUserId) throws java.rmi.RemoteException;
}
