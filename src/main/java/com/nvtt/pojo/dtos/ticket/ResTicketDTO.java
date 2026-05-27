/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.pojo.dtos.ticket;

import java.util.Date;

/**
 *
 * @author lequa
 */
public class ResTicketDTO {
    private Long id;
    private Long orderId;
    private Long eventId;
    private String attendeeEmail;
    private String ticketCode;
    private Date checkInTime;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the orderId
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * @param orderId the orderId to set
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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
     * @return the ticketCode
     */
    public String getTicketCode() {
        return ticketCode;
    }

    /**
     * @param ticketCode the ticketCode to set
     */
    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    /**
     * @return the checkInTime
     */
    public Date getCheckInTime() {
        return checkInTime;
    }

    /**
     * @param checkInTime the checkInTime to set
     */
    public void setCheckInTime(Date checkInTime) {
        this.checkInTime = checkInTime;
    }
    
    public ResTicketDTO(Long id, Long orderId, Long eventId, String attendeeEmail, String ticketCode, Date checkInTime){
        this.id = id;
        this.orderId = orderId;
        this.eventId = eventId;
        this.attendeeEmail = attendeeEmail;
        this.ticketCode = ticketCode;
        this.checkInTime = checkInTime;
    }
    
    public ResTicketDTO(){
        
    }

    /**
     * @return the attendeeEmail
     */
    public String getAttendeeEmail() {
        return attendeeEmail;
    }

    /**
     * @param attendeeEmail the attendeeEmail to set
     */
    public void setAttendeeEmail(String attendeeEmail) {
        this.attendeeEmail = attendeeEmail;
    }
}
