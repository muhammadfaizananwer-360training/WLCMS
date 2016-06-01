package com.softech.ls360.lcms.contentbuilder.model;

import com.softech.ls360.lcms.contentbuilder.model.validator.annotation.NotEmpty;

import java.util.Map;

public class LocationDTO implements ControllableNode {

    private String description = null;

    @NotEmpty
    private String locationName = null;

    @NotEmpty
    private String city = null;

    @NotEmpty
    private String state = null;

    @NotEmpty
    private String zip = null;

    @NotEmpty
    private String country = null;

    @NotEmpty
    private String address = null;

    private String phone = null;
    private String action;
    private boolean error;
    private Long id;
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationname) {
        this.locationName = locationname;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStreetAddress() {
        return address;
    }

    public void setStreetAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String tag) {
        this.action = tag;
    }

     public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public ControllableNode getParent() {
        return null;
    }

    @Override
    public void setParent(ControllableNode parent) {
    }

    @Override
    public String getNodeKey() {
        return getLocationName();
    }

    @Override
    public Map<String, ? extends ControllableNode> getChildren() {
        return null;
    }



}
