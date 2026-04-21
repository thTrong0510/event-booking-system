/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

/**
 *
 * @author vthan
 */
@Entity
@Table(name = "payment_transactions")
@NamedQueries({
    @NamedQuery(name = "PaymentTransaction.findAll", query = "SELECT p FROM PaymentTransaction p"),
    @NamedQuery(name = "PaymentTransaction.findById", query = "SELECT p FROM PaymentTransaction p WHERE p.id = :id"),
    @NamedQuery(name = "PaymentTransaction.findByProvider", query = "SELECT p FROM PaymentTransaction p WHERE p.provider = :provider"),
    @NamedQuery(name = "PaymentTransaction.findByProviderTransactionId", query = "SELECT p FROM PaymentTransaction p WHERE p.providerTransactionId = :providerTransactionId"),
    @NamedQuery(name = "PaymentTransaction.findByPayerEmail", query = "SELECT p FROM PaymentTransaction p WHERE p.payerEmail = :payerEmail"),
    @NamedQuery(name = "PaymentTransaction.findByPayerId", query = "SELECT p FROM PaymentTransaction p WHERE p.payerId = :payerId"),
    @NamedQuery(name = "PaymentTransaction.findByProviderStatus", query = "SELECT p FROM PaymentTransaction p WHERE p.providerStatus = :providerStatus"),
    @NamedQuery(name = "PaymentTransaction.findByCreatedAt", query = "SELECT p FROM PaymentTransaction p WHERE p.createdAt = :createdAt")})
public class PaymentTransaction implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 50)
    @Column(name = "provider")
    private String provider;
    @Size(max = 255)
    @Column(name = "provider_transaction_id")
    private String providerTransactionId;
    @Size(max = 255)
    @Column(name = "payer_email")
    private String payerEmail;
    @Size(max = 255)
    @Column(name = "payer_id")
    private String payerId;
    @Size(max = 100)
    @Column(name = "provider_status")
    private String providerStatus;
    @Lob
    @Size(max = 1073741824)
    @Column(name = "raw_response")
    private String rawResponse;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @JoinColumn(name = "payment_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Payment payment;

    public PaymentTransaction() {
    }

    public PaymentTransaction(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getProviderTransactionId() {
        return providerTransactionId;
    }

    public void setProviderTransactionId(String providerTransactionId) {
        this.providerTransactionId = providerTransactionId;
    }

    public String getPayerEmail() {
        return payerEmail;
    }

    public void setPayerEmail(String payerEmail) {
        this.payerEmail = payerEmail;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public String getProviderStatus() {
        return providerStatus;
    }

    public void setProviderStatus(String providerStatus) {
        this.providerStatus = providerStatus;
    }

    public String getRawResponse() {
        return rawResponse;
    }

    public void setRawResponse(String rawResponse) {
        this.rawResponse = rawResponse;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
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
        if (!(object instanceof PaymentTransaction)) {
            return false;
        }
        PaymentTransaction other = (PaymentTransaction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.nvtt.pojo.PaymentTransaction[ id=" + id + " ]";
    }

    @PrePersist
    public void beforeSave() {
        this.createdAt = Date.from(Instant.now());
    }

}
