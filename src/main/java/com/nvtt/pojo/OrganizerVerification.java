/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.pojo;

import com.nvtt.utils.SecurityUtil;
import com.nvtt.utils.constants.OrganizerVerificationStatus;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author vthan
 */
@Entity
@Table(name = "organizer_verifications")
@NamedQueries({
    @NamedQuery(name = "OrganizerVerification.findAll", query = "SELECT o FROM OrganizerVerification o"),
    @NamedQuery(name = "OrganizerVerification.findById", query = "SELECT o FROM OrganizerVerification o WHERE o.id = :id"),
    @NamedQuery(name = "OrganizerVerification.findByStatus", query = "SELECT o FROM OrganizerVerification o WHERE o.status = :status"),
    @NamedQuery(name = "OrganizerVerification.findByApprovedAt", query = "SELECT o FROM OrganizerVerification o WHERE o.approvedAt = :approvedAt"),
    @NamedQuery(name = "OrganizerVerification.findByCreatedAt", query = "SELECT o FROM OrganizerVerification o WHERE o.createdAt = :createdAt")})
public class OrganizerVerification implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrganizerVerificationStatus status;
    @Column(name = "approved_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approvedAt;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @OneToOne(optional = false)
    private User user;
    @JoinColumn(name = "approved_by", referencedColumnName = "id")
    @ManyToOne
    private User approvedBy;

    public OrganizerVerification() {
    }

    public OrganizerVerification(Long id) {
        this.id = id;
    }

    public OrganizerVerification(Long id, OrganizerVerificationStatus status) {
        this.id = id;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrganizerVerificationStatus getStatus() {
        return status;
    }

    public void setStatus(OrganizerVerificationStatus status) {
        this.status = status;
    }

    public Date getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(Date approvedAt) {
        this.approvedAt = approvedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrganizerVerification)) {
            return false;
        }
        OrganizerVerification other = (OrganizerVerification) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.nvtt.pojo.OrganizerVerification[ id=" + id + " ]";
    }

    @PrePersist
    public void beforeSave() {
        this.createdAt = Date.from(Instant.now());
    }

    @PreUpdate
    public void beforeUpdate() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : null;
        if (!Objects.isNull(email)) {
            User user = new User();
            user.setEmail(email);
            this.approvedBy = user;
        }
        this.approvedAt = Date.from(Instant.now());
    }
}