/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.pojo.dtos.response;

/**
 *
 * @author vthan
 */
public class PayPalResponseDTO {

    private String approvalUrl;
    private Long orderId;
    private Long paymentId;

    public PayPalResponseDTO(String approvalUrl, Long orderId, Long paymentId) {
        this.approvalUrl = approvalUrl;
        this.orderId = orderId;
        this.paymentId = paymentId;
    }

    // Getters và Setters
    public String getApprovalUrl() {
        return approvalUrl;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getPaymentId() {
        return paymentId;
    }
}
