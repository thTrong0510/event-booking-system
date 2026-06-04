package com.nvtt.pojo.dtos.response;

import java.util.List;

public class PaymentSuccessResponseDTO {

    private boolean success;
    private Long orderId;
    private List<String> ticketCodes;

    public PaymentSuccessResponseDTO(boolean success, Long orderId, List<String> ticketCodes) {
        this.success = success;
        this.orderId = orderId;
        this.ticketCodes = ticketCodes;
    }

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
