/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.pojo.dtos.response;

import java.math.BigDecimal;

/**
 *
 * @author vthan
 */
public class EventResponseDTO {

    private Long id;
    private String name;
    private String categoryName;
    private String organizerName;
    private String statusName;
    private String startTime;
    private String endTime;
    private String location;
    private int totalTickets;
    private int availableTickets;
    private BigDecimal ticketPrice;

    // Constructors
    public EventResponseDTO() {
    }

    public EventResponseDTO(Long id, String name, String categoryName, String organizerName,
            String statusName, String startTime, String endTime,
            String location, int totalTickets, int availableTickets, BigDecimal ticketPrice) {
        this.id = id;
        this.name = name;
        this.categoryName = categoryName;
        this.organizerName = organizerName;
        this.statusName = statusName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.totalTickets = totalTickets;
        this.availableTickets = availableTickets;
        this.ticketPrice = ticketPrice;
    }

    // Getters and Setters
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
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

    public int getAvailableTickets() {
        return availableTickets;
    }

    public void setAvailableTickets(int availableTickets) {
        this.availableTickets = availableTickets;
    }

    public BigDecimal getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(BigDecimal ticketPrice) {
        this.ticketPrice = ticketPrice;
    }
}
