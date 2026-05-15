/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.pojo.dtos.organizer_verification;

import com.nvtt.utils.constants.OrganizerVerificationStatus;

/**
 *
 * @author lequa
 */
public class ResOrganizerApplicationDTO {
    private Long userId;
    private OrganizerVerificationStatus status;

    /**
     * @return the userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * @return the status
     */
    public OrganizerVerificationStatus getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(OrganizerVerificationStatus status) {
        this.status = status;
    }
}
