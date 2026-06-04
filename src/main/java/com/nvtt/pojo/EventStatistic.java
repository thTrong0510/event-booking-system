package com.nvtt.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "event_statistics")
@NamedQueries({
    @NamedQuery(name = "EventStatistic.findAll", query = "SELECT e FROM EventStatistic e"),
    @NamedQuery(name = "EventStatistic.findByEventId", query = "SELECT e FROM EventStatistic e WHERE e.eventId = :eventId"),
    @NamedQuery(name = "EventStatistic.findByTotalTicketsSold", query = "SELECT e FROM EventStatistic e WHERE e.totalTicketsSold = :totalTicketsSold"),
    @NamedQuery(name = "EventStatistic.findByTotalRevenue", query = "SELECT e FROM EventStatistic e WHERE e.totalRevenue = :totalRevenue"),
    @NamedQuery(name = "EventStatistic.findByTotalViews", query = "SELECT e FROM EventStatistic e WHERE e.totalViews = :totalViews"),
    @NamedQuery(name = "EventStatistic.findByLastUpdated", query = "SELECT e FROM EventStatistic e WHERE e.lastUpdated = :lastUpdated")})
public class EventStatistic implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "event_id")
    private Long eventId;
    @Column(name = "total_tickets_sold")
    private Integer totalTicketsSold;
    @Column(name = "total_revenue")
    private BigDecimal totalRevenue;
    @Column(name = "total_views")
    private Integer totalViews;
    @Column(name = "last_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public EventStatistic() {
    }

    public EventStatistic(Long eventId) {
        this.eventId = eventId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Integer getTotalTicketsSold() {
        return totalTicketsSold;
    }

    public void setTotalTicketsSold(Integer totalTicketsSold) {
        this.totalTicketsSold = totalTicketsSold;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Integer getTotalViews() {
        return totalViews;
    }

    public void setTotalViews(Integer totalViews) {
        this.totalViews = totalViews;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (eventId != null ? eventId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EventStatistic)) {
            return false;
        }
        EventStatistic other = (EventStatistic) object;
        if ((this.eventId == null && other.eventId != null) || (this.eventId != null && !this.eventId.equals(other.eventId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.nvtt.pojo.EventStatistic[ eventId=" + eventId + " ]";
    }

    @PrePersist
    public void beforeSave() {
        this.lastUpdated = Date.from(Instant.now());
    }

    @PreUpdate
    public void beforeUpdate() {
        this.lastUpdated = Date.from(Instant.now());
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

}
