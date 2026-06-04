package com.nvtt.pojo.dtos.ticket;

import java.util.Date;

public class ResTicketDTO {

    private Long id;
    private Long orderId;
    private Long eventId;
    private String attendeeEmail;
    private String ticketCode;
    private Date checkInTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public Date getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(Date checkInTime) {
        this.checkInTime = checkInTime;
    }

    public ResTicketDTO(Long id, Long orderId, Long eventId, String attendeeEmail, String ticketCode, Date checkInTime) {
        this.id = id;
        this.orderId = orderId;
        this.eventId = eventId;
        this.attendeeEmail = attendeeEmail;
        this.ticketCode = ticketCode;
        this.checkInTime = checkInTime;
    }

    public ResTicketDTO() {

    }

    public String getAttendeeEmail() {
        return attendeeEmail;
    }

    public void setAttendeeEmail(String attendeeEmail) {
        this.attendeeEmail = attendeeEmail;
    }
}
