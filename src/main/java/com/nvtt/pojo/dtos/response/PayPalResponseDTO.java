package com.nvtt.pojo.dtos.response;

public class PayPalResponseDTO {

    private String approvalUrl;
    private Long orderId;
    private Long paymentId;

    public PayPalResponseDTO(String approvalUrl, Long orderId, Long paymentId) {
        this.approvalUrl = approvalUrl;
        this.orderId = orderId;
        this.paymentId = paymentId;
    }

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
