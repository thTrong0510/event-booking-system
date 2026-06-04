package com.nvtt.pojo.dtos.organizer_verification;

import com.nvtt.utils.constants.OrganizerVerificationStatus;

public class ResOrganizerApplicationDTO {

    private Long userId;
    private OrganizerVerificationStatus status;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public OrganizerVerificationStatus getStatus() {
        return status;
    }

    public void setStatus(OrganizerVerificationStatus status) {
        this.status = status;
    }
}
