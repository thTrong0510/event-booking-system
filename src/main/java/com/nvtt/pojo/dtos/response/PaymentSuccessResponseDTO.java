/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.pojo.dtos.response;

import java.util.List;

/**
 *
 * @author vthan
 */
public class PaymentSuccessResponseDTO {

    private boolean success;
    private Long orderId;
    private List<String> ticketCodes;

    public PaymentSuccessResponseDTO(boolean success, Long orderId, List<String> ticketCodes) {
        this.success = success;
        this.orderId = orderId;
        this.ticketCodes = ticketCodes;
    }

    // Getters và Setters
    public boolean isSuccess() {
        return success;
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<String> getTicketCodes() {
        return ticketCodes;
    }
}
