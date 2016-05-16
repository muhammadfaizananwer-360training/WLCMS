/**
 * LMSServicesLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.softech.ls360.lcms.contentbuilder.webservice.asset.upload;

public class LMSServicesLocator extends org.apache.axis.client.Service implements com.softech.ls360.lcms.contentbuilder.webservice.asset.upload.LMSServices {

    public LMSServicesLocator() {
    }


    public LMSServicesLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public LMSServicesLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for LMSServicesSoap
    private java.lang.String LMSServicesSoap_address = "http://10.0.100.179/LCMSSErver/LMSWebService/LMSServices.asmx";

    public java.lang.String getLMSServicesSoapAddress() {
        return LMSServicesSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String LMSServicesSoapWSDDServiceName = "LMSServicesSoap";

    public java.lang.String getLMSServicesSoapWSDDServiceName() {
        return LMSServicesSoapWSDDServiceName;
    }

    public void setLMSServicesSoapWSDDServiceName(java.lang.String name) {
        LMSServicesSoapWSDDServiceName = name;
    }

    public com.softech.ls360.lcms.contentbuilder.webservice.asset.upload.LMSServicesSoap getLMSServicesSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(LMSServicesSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getLMSServicesSoap(endpoint);
    }

    public com.softech.ls360.lcms.contentbuilder.webservice.asset.upload.LMSServicesSoap getLMSServicesSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
        	com.softech.ls360.lcms.contentbuilder.webservice.asset.upload.LMSServicesSoapStub _stub = new com.softech.ls360.lcms.contentbuilder.webservice.asset.upload.LMSServicesSoapStub(portAddress, this);
            _stub.setPortName(getLMSServicesSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setLMSServicesSoapEndpointAddress(java.lang.String address) {
        LMSServicesSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.softech.ls360.lcms.contentbuilder.webservice.asset.upload.LMSServicesSoap.class.isAssignableFrom(serviceEndpointInterface)) {
            	com.softech.ls360.lcms.contentbuilder.webservice.asset.upload.LMSServicesSoapStub _stub = new com.softech.ls360.lcms.contentbuilder.webservice.asset.upload.LMSServicesSoapStub(new java.net.URL(LMSServicesSoap_address), this);
                _stub.setPortName(getLMSServicesSoapWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("LMSServicesSoap".equals(inputPortName)) {
            return getLMSServicesSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://360training.com/", "LMSServices");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://360training.com/", "LMSServicesSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("LMSServicesSoap".equals(portName)) {
            setLMSServicesSoapEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
