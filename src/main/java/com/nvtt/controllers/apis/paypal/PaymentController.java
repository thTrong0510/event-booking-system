/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.controllers.apis.paypal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.nvtt.pojo.dtos.request.PaymentCreateRequest;
import com.nvtt.pojo.dtos.response.PayPalResponseDTO;
import com.nvtt.pojo.dtos.response.PaymentSuccessResponseDTO;
import com.nvtt.services.paypal.PayPalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author vthan
 */
@RestController
@RequestMapping("/api/payment/paypal")
public class PaymentController {
    
    private static final Logger logger = LogManager.getLogger(PaymentController.class);

    @Autowired
    private PayPalService paypalService;

    @PostMapping("/create")
    public ResponseEntity<PayPalResponseDTO> createPayment(@RequestBody PaymentCreateRequest request) {        
        logger.info("start sql createPayment");
        PayPalResponseDTO response = paypalService.createPayPalPayment(request);
        logger.info("end sql");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/success")
    public ResponseEntity<PaymentSuccessResponseDTO> successPayment(
            @RequestParam("token") String token,
            @RequestParam("PayerID") String payerId,
            @RequestParam("orderId") Long orderId,
            @RequestParam("paymentId") Long paymentId) {
        logger.info("start sql successPayment");
        PaymentSuccessResponseDTO result = paypalService.executeSuccessPayment(token, payerId, orderId, paymentId);
        logger.info("end sql");
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/cancel")
    public ResponseEntity<String> cancelPayment(
            @RequestParam("orderId") Long orderId,
            @RequestParam("paymentId") Long paymentId) {
        logger.info("start sql cancelPayment");
        paypalService.executeCancelPayment(orderId, paymentId);
        logger.info("end sql");
        return ResponseEntity.ok("Giao dịch thanh toán bằng tài khoản PayPal đã bị hủy bỏ bởi người dùng.");
    }
}
