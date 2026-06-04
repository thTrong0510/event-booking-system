package com.nvtt.pojo.dtos.event;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

public class ResEventInfoDTO {

    private Long id;
    private String name;
    private String description;
    private Set<ResEventMediaDTO> eventMedias;
    private Date startTime;
    private Date endTime;
    private String location;
    private int totalTickets;
    private Date createdAt;
    private Date updatedAt;
    private BigDecimal ticketPrice;
    private int availableTickets;
    private String status;
    private String category;
    private int views;

    public ResEventInfoDTO(Long id, String name, String description, Set<ResEventMediaDTO> eventMedias,
            Date startTime, Date endTime, String location, int totalTickets,
            Date createdAt, Date updatedAt, BigDecimal ticketPrice, int availableTickets, String status, String category, int views) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.eventMedias = eventMedias;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.totalTickets = totalTickets;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.ticketPrice = ticketPrice;
        this.availableTickets = availableTickets;
        this.status = status;
        this.category = category;
        this.views = views;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<ResEventMediaDTO> getEventMedias() {
        return eventMedias;
    }

    public void setEventMedias(Set<ResEventMediaDTO> eventMedias) {
        this.eventMedias = eventMedias;
    }

    public BigDecimal getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(BigDecimal ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public int getAvailableTickets() {
        return availableTickets;
    }

    public void setAvailableTickets(int availableTickets) {
        this.availableTickets = availableTickets;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

}
