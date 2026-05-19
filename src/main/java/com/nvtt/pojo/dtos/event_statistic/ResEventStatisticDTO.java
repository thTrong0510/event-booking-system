/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.pojo.dtos.event_statistic;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author lequa
 */
public class ResEventStatisticDTO {
    private Long eventId;
    private int totalTicketsSold;
    private BigDecimal totalRevenue;
    private int totalViews;
    private Date lastUpdated;
    private Date createdAt;
    
    public ResEventStatisticDTO(Long eventId, int totalTicketsSold, BigDecimal totalRevenue, int totalViews){
        this.eventId = eventId;
        this.totalTicketsSold = totalTicketsSold;
        this.totalRevenue = totalRevenue;
        this.totalViews = totalViews;
    }
    
    public ResEventStatisticDTO(){
        
    }

    /**
     * @return the eventId
     */
    public Long getEventId() {
        return eventId;
    }

    /**
     * @param eventId the eventId to set
     */
    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    /**
     * @return the totalTicketsSold
     */
    public int getTotalTicketsSold() {
        return totalTicketsSold;
    }

    /**
     * @param totalTicketsSold the totalTicketsSold to set
     */
    public void setTotalTicketsSold(int totalTicketsSold) {
        this.totalTicketsSold = totalTicketsSold;
    }

    /**
     * @return the totalRevenue
     */
    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    /**
     * @param totalRevenue the totalRevenue to set
     */
    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    /**
     * @return the totalViews
     */
    public int getTotalViews() {
        return totalViews;
    }

    /**
     * @param totalViews the totalViews to set
     */
    public void setTotalViews(int totalViews) {
        this.totalViews = totalViews;
    }

    /**
     * @return the lastUpdated
     */
    public Date getLastUpdated() {
        return lastUpdated;
    }

    /**
     * @param lastUpdated the lastUpdated to set
     */
    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
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
}
