package com.nvtt.pojo.dtos.order;

import java.math.BigDecimal;

import com.nvtt.pojo.dtos.event.ResEventInfoDTO;
import com.nvtt.pojo.dtos.user.ResUserInfoDTO;

public class ResOrderInfoDTO {

    private ResUserInfoDTO user;
    private ResEventInfoDTO event;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalAmount;
    private String status;

    public ResUserInfoDTO getUser() {
        return user;
    }

    public void setUser(ResUserInfoDTO user) {
        this.user = user;
    }

    public ResEventInfoDTO getEvent() {
        return event;
    }

    public void setEvent(ResEventInfoDTO event) {
        this.event = event;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
