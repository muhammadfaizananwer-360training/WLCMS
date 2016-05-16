package com.softech.ls360.lcms.contentbuilder.model;

import java.util.Map;

/**
 * Created by abdul.wahid on 4/19/2016.
 */
public class ClassInstructorDTO implements ControllableNode {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNo;
    private String background;
    private String action;
    private boolean error;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    @Override
    public String getAction() {
        return action;
    }

    @Override
    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public boolean isError() {
        return error;
    }

    @Override
    public void setError(boolean error) {
        this.error = error;
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
        return getEmail();
    }

    @Override
    public Map<String, ? extends ControllableNode> getChildren() {
        return null;
    }
}
