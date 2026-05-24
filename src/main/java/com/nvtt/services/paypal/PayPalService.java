/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services.paypal;

import com.nvtt.pojo.dtos.request.PaymentCreateRequest;
import com.nvtt.pojo.dtos.response.PayPalResponseDTO;
import com.nvtt.pojo.dtos.response.PaymentSuccessResponseDTO;
import org.springframework.stereotype.Service;


/**
 *
 * @author vthan
 */
@Service
public interface PayPalService {
    PayPalResponseDTO createPayPalPayment(PaymentCreateRequest request);
    PaymentSuccessResponseDTO executeSuccessPayment(String token, String payerId, Long orderId, Long paymentId);
    void executeCancelPayment(Long orderId, Long paymentId);
}
