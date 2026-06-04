package com.nvtt.pojo.dtos.event_statistic;

import com.nvtt.pojo.dtos.event.ResEventBasicInfoDTO;
import java.math.BigDecimal;
import java.util.Date;

public class ResEventStatisticDTO {

    private Long eventId;
    private int totalTicketsSold;
    private BigDecimal totalRevenue;
    private int totalViews;
    private Date lastUpdated;
    private Date createdAt;
    private ResEventBasicInfoDTO event;

    public ResEventStatisticDTO(Long eventId, int totalTicketsSold, BigDecimal totalRevenue, int totalViews, ResEventBasicInfoDTO event) {
        this.eventId = eventId;
        this.totalTicketsSold = totalTicketsSold;
        this.totalRevenue = totalRevenue;
        this.totalViews = totalViews;
        this.event = event;
    }

    public ResEventStatisticDTO() {

    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public int getTotalTicketsSold() {
        return totalTicketsSold;
    }

    public void setTotalTicketsSold(int totalTicketsSold) {
        this.totalTicketsSold = totalTicketsSold;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public int getTotalViews() {
        return totalViews;
    }

    public void setTotalViews(int totalViews) {
        this.totalViews = totalViews;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public ResEventBasicInfoDTO getEvent() {
        return event;
    }

    public void setEvent(ResEventBasicInfoDTO event) {
        this.event = event;
    }

}
