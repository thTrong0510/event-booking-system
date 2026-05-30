/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.pojo.dtos.organizer_verification;

import java.util.Date;

/**
 *
 * @author vthan
 */
public class VerificationDTO {

    private Long id;

    private String organizerName;
    private String organizerEmail;
    private String organizerAvatar;

    private String approvedByName;

    private Date createdAt;
    private Date approvedAt;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the organizerName
     */
    public String getOrganizerName() {
        return organizerName;
    }

    /**
     * @param organizerName the organizerName to set
     */
    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    /**
     * @return the organizerEmail
     */
    public String getOrganizerEmail() {
        return organizerEmail;
    }

    /**
     * @param organizerEmail the organizerEmail to set
     */
    public void setOrganizerEmail(String organizerEmail) {
        this.organizerEmail = organizerEmail;
    }

    /**
     * @return the organizerAvatar
     */
    public String getOrganizerAvatar() {
        return organizerAvatar;
    }

    /**
     * @param organizerAvatar the organizerAvatar to set
     */
    public void setOrganizerAvatar(String organizerAvatar) {
        this.organizerAvatar = organizerAvatar;
    }

    /**
     * @return the approvedByName
     */
    public String getApprovedByName() {
        return approvedByName;
    }

    /**
     * @param approvedByName the approvedByName to set
     */
    public void setApprovedByName(String approvedByName) {
        this.approvedByName = approvedByName;
    }

    /**
     * @return the createdAt
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt the createdAt to set
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return the approvedAt
     */
    public Date getApprovedAt() {
        return approvedAt;
    }

    /**
     * @param approvedAt the approvedAt to set
     */
    public void setApprovedAt(Date approvedAt) {
        this.approvedAt = approvedAt;
    }

    
}
