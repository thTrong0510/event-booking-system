/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.utils.constants;

/**
 *
 * @author vthan
 */
public enum OrderStatus {

    PENDING,      // mới tạo
    CONFIRMED,    // thanh toán thành công
    CANCELLED,    // bị hủy
    EXPIRED,      // quá hạn thanh toán
    REFUNDED      // đã hoàn tiền

}