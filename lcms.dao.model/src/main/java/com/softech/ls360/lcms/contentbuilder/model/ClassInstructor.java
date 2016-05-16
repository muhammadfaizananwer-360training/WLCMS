package com.softech.ls360.lcms.contentbuilder.model;

import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by muhammad.imran on 4/21/2016.
 */

@Entity
@Where(clause="active=1")
public class ClassInstructor extends AbstractEntity implements Serializable{

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNo;
    private String background;
    private Long lastUpdatedUser;
    private Long createdUser;
    private Boolean isActive = true;
    private Long imageId;
    private Long contentOwnerId;
    private String deleteExceptionMessage;


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name="email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @Column(name="PhoneNumber")
    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
    @Column(name="background")
    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }
    @Column(name="LASTUPDATEUSERID")
    public Long getLastUpdatedUser() {
        return lastUpdatedUser;
    }

    public void setLastUpdatedUser(Long lastUpdatedUser) {
        this.lastUpdatedUser = lastUpdatedUser;
    }
    @Column(name="CREATEUSERID")
    public Long getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(Long createdUser) {
        this.createdUser = createdUser;
    }
    @Column(name="active")
    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    @Column(name="image_id")
    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }
    @Column(name="CONTENTOWNER_ID")
    public Long getContentOwnerId() {
        return contentOwnerId;
    }

    public void setContentOwnerId(Long contentOwnerId) {
        this.contentOwnerId = contentOwnerId;
    }
    @Column(name="firstname")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    @Column(name="lastname")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    @Transient
    public String getDeleteExceptionMessage() {
        return deleteExceptionMessage;
    }

    public void setDeleteExceptionMessage(String deleteExceptionMessage) {
        this.deleteExceptionMessage = deleteExceptionMessage;
    }
}
