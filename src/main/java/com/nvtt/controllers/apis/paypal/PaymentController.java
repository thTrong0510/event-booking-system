/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.controllers.apis.paypal;

import com.nvtt.pojo.dtos.request.PaymentCreateRequest;
import com.nvtt.pojo.dtos.response.PayPalResponseDTO;
import com.nvtt.pojo.dtos.response.PaymentSuccessResponseDTO;
import com.nvtt.services.paypal.PayPalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Autowired
    private PayPalService paypalService;

    // Tiếp nhận yêu cầu thanh toán ban đầu từ Client (Bước 1)
    @PostMapping("/create")
    public ResponseEntity<PayPalResponseDTO> createPayment(@RequestBody PaymentCreateRequest request) {
        // Giả sử lấy ID người dùng đăng nhập hiện tại gán vào DTO
        // request.setAttendeeId(currentLoggedInUserId); 
        
        PayPalResponseDTO response = paypalService.createPayPalPayment(request);
        return ResponseEntity.ok(response);
    }

    // Nhận Callback chuyển hướng thành công trả từ Server PayPal (Bước 5 & Bước 6)
    @GetMapping("/success")
    public ResponseEntity<PaymentSuccessResponseDTO> successPayment(
            @RequestParam("token") String token,
            @RequestParam("PayerID") String payerId,
            @RequestParam("orderId") Long orderId,
            @RequestParam("paymentId") Long paymentId) {

        PaymentSuccessResponseDTO result = paypalService.executeSuccessPayment(token, payerId, orderId, paymentId);
        return ResponseEntity.ok(result);
    }

    // Nhận điều hướng trong tình huống người dùng bấm hủy thao tác ngay trên giao diện PayPal
    @GetMapping("/cancel")
    public ResponseEntity<String> cancelPayment(
            @RequestParam("orderId") Long orderId,
            @RequestParam("paymentId") Long paymentId) {
            
        paypalService.executeCancelPayment(orderId, paymentId);
        return ResponseEntity.ok("Giao dịch thanh toán bằng tài khoản PayPal đã bị hủy bỏ bởi người dùng.");
    }
}
