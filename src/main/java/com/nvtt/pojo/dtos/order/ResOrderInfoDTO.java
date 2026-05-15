/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.pojo.dtos.order;

import java.math.BigDecimal;

import com.nvtt.pojo.dtos.event.ResEventInfoDTO;
import com.nvtt.pojo.dtos.user.ResUserInfoDTO;

/**
 *
 * @author lequa
 */
public class ResOrderInfoDTO {
    private ResUserInfoDTO user;
    private ResEventInfoDTO event;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalAmount;
    private String status;

    /**
     * @return the user
     */
    public ResUserInfoDTO getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(ResUserInfoDTO user) {
        this.user = user;
    }

    /**
     * @return the event
     */
    public ResEventInfoDTO getEvent() {
        return event;
    }

    /**
     * @param event the event to set
     */
    public void setEvent(ResEventInfoDTO event) {
        this.event = event;
    }

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the unitPrice
     */
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    /**
     * @param unitPrice the unitPrice to set
     */
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * @return the totalAmount
     */
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    /**
     * @param totalAmount the totalAmount to set
     */
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
