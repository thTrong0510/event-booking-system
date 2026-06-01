/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.controllers.apis.paypal;

import com.nvtt.pojo.Orders;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.nvtt.pojo.dtos.request.PaymentCreateRequest;
import com.nvtt.pojo.dtos.response.PayPalResponseDTO;
import com.nvtt.pojo.dtos.response.PaymentSuccessResponseDTO;
import com.nvtt.services.OrderService;
import com.nvtt.services.paypal.PayPalService;
import com.nvtt.utils.Utilities;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
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
@PropertySource("classpath:configs.properties")
@RequestMapping("/api/v1/me/payment/paypal")
public class PaymentController {

    private static final Logger logger = LogManager.getLogger(PaymentController.class);

    @Autowired
    private Environment env;

    @Autowired
    private PayPalService paypalService;

    @Autowired
    private OrderService orderService;

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
            @RequestParam("paymentId") Long paymentId,
            HttpServletResponse response) {
        try {
            logger.info("start sql successPayment");
            PaymentSuccessResponseDTO result = paypalService.executeSuccessPayment(token, payerId, orderId, paymentId);
            logger.info("end sql");

            String frontendBaseUrl = this.env.getProperty("frontend.url", String.class);
            String redirectUrl;

            Orders order = this.orderService.getOrderById(orderId);

            if (result != null && result.isSuccess()) {
                redirectUrl = frontendBaseUrl
                        + "/payment/success?orderId=" + result.getOrderId()
                        + "&eventId=" + order.getEvent().getId();
            } else {
                redirectUrl = frontendBaseUrl + "/payment/fail?orderId=" + result.getOrderId()
                        + "&eventId=" + order.getEvent().getId();
            }

            response.sendRedirect(redirectUrl);
            return ResponseEntity.ok(result);
        } catch (IOException ex) {
            logger.error("error: redirect from paypal");
        }
        return ResponseEntity.ok(null);
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
