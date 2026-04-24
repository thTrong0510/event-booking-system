/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.utils.constants;

/**
 *
 * @author vthan
 */
public enum PaymentStatus {

    INITIATED,    // tạo payment
    PENDING,      // chờ provider xử lý
    SUCCESS,      // thanh toán thành công
    FAILED,       // thất bại
    CANCELLED,    // user hủy
    REFUNDED      // đã hoàn tiền

}
