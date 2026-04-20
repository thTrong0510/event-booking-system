/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author vthan
 */
@Entity
@Table(name = "system_statistics_daily")
@NamedQueries({
    @NamedQuery(name = "SystemStatisticsDaily.findAll", query = "SELECT s FROM SystemStatisticsDaily s"),
    @NamedQuery(name = "SystemStatisticsDaily.findByStatDate", query = "SELECT s FROM SystemStatisticsDaily s WHERE s.statDate = :statDate"),
    @NamedQuery(name = "SystemStatisticsDaily.findByTotalOrders", query = "SELECT s FROM SystemStatisticsDaily s WHERE s.totalOrders = :totalOrders"),
    @NamedQuery(name = "SystemStatisticsDaily.findByTotalRevenue", query = "SELECT s FROM SystemStatisticsDaily s WHERE s.totalRevenue = :totalRevenue"),
    @NamedQuery(name = "SystemStatisticsDaily.findByTotalEvents", query = "SELECT s FROM SystemStatisticsDaily s WHERE s.totalEvents = :totalEvents")})
public class SystemStatisticsDaily implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "stat_date")
    @Temporal(TemporalType.DATE)
    private Date statDate;
    @Column(name = "total_orders")
    private Integer totalOrders;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total_revenue")
    private BigDecimal totalRevenue;
    @Column(name = "total_events")
    private Integer totalEvents;

    public SystemStatisticsDaily() {
    }

    public SystemStatisticsDaily(Date statDate) {
        this.statDate = statDate;
    }

    public Date getStatDate() {
        return statDate;
    }

    public void setStatDate(Date statDate) {
        this.statDate = statDate;
    }

    public Integer getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Integer totalOrders) {
        this.totalOrders = totalOrders;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Integer getTotalEvents() {
        return totalEvents;
    }

    public void setTotalEvents(Integer totalEvents) {
        this.totalEvents = totalEvents;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (statDate != null ? statDate.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SystemStatisticsDaily)) {
            return false;
        }
        SystemStatisticsDaily other = (SystemStatisticsDaily) object;
        if ((this.statDate == null && other.statDate != null) || (this.statDate != null && !this.statDate.equals(other.statDate))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.nvtt.pojo.SystemStatisticsDaily[ statDate=" + statDate + " ]";
    }
    
}
