package com.nvtt.pojo.dtos.response;

import com.nvtt.utils.constants.OrganizerVerificationStatus;

public class OrganizerVerificationResponseDTO {

    private Long id;
    private Long userId;
    private String organizerName;
    private String organizerEmail;
    private String organizerAvatar;
    private OrganizerVerificationStatus status;
    private String approvedByName;
    private String approvedAt;
    private String createdAt;
    private String company;

    public OrganizerVerificationResponseDTO() {
    }

    public OrganizerVerificationResponseDTO(Long id, Long userId, String organizerName, String organizerEmail,
            String organizerAvatar, OrganizerVerificationStatus status, String approvedByName,
            String approvedAt, String createdAt, String company) {
        this.id = id;
        this.userId = userId;
        this.organizerName = organizerName;
        this.organizerEmail = organizerEmail;
        this.organizerAvatar = organizerAvatar;
        this.status = status;
        this.approvedByName = approvedByName;
        this.approvedAt = approvedAt;
        this.createdAt = createdAt;
        this.company = company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompany() {
        return company;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public String getOrganizerEmail() {
        return organizerEmail;
    }

    public void setOrganizerEmail(String organizerEmail) {
        this.organizerEmail = organizerEmail;
    }

    public String getOrganizerAvatar() {
        return organizerAvatar;
    }

    public void setOrganizerAvatar(String organizerAvatar) {
        this.organizerAvatar = organizerAvatar;
    }

    public OrganizerVerificationStatus getStatus() {
        return status;
    }

    public void setStatus(OrganizerVerificationStatus status) {
        this.status = status;
    }

    public String getApprovedByName() {
        return approvedByName;
    }

    public void setApprovedByName(String approvedByName) {
        this.approvedByName = approvedByName;
    }

    public String getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(String approvedAt) {
        this.approvedAt = approvedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
