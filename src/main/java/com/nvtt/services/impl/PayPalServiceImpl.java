/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nvtt.configs.PayPalConfig;
import com.nvtt.pojo.Event;
import com.nvtt.pojo.Orders;
import com.nvtt.pojo.Payment;
import com.nvtt.pojo.PaymentTransaction;
import com.nvtt.pojo.Ticket;
import com.nvtt.pojo.User;
import com.nvtt.pojo.dtos.request.PaymentCreateRequest;
import com.nvtt.pojo.dtos.response.PayPalResponseDTO;
import com.nvtt.pojo.dtos.response.PaymentSuccessResponseDTO;
import com.nvtt.repositories.EventRepository;
import com.nvtt.repositories.PaymentRepository;
import com.nvtt.repositories.UserRepository;
import com.nvtt.services.paypal.PayPalService;
import com.nvtt.utils.constants.OrderStatus;
import com.nvtt.utils.constants.PaymentStatus;
import com.nvtt.utils.constants.ProviderStatus;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author vthan
 */
@Service
public class PayPalServiceImpl implements PayPalService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PayPalConfig payPalConfig;

    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private EventRepository eventRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PayPalResponseDTO createPayPalPayment(PaymentCreateRequest request) {
        // BƯỚC 2: Backend validate & tạo Order -> DB
        Event event = this.eventRepository.findById(request.getEventId());
        if (event == null || event.getAvailableTickets() < request.getQuantity()) {
            throw new RuntimeException("Số lượng vé còn lại không đủ để thực hiện đăng ký!");
        }
        
        int updatedTickets = event.getAvailableTickets() - request.getQuantity();
        event.setAvailableTickets(updatedTickets);
        this.eventRepository.addEvent(event);
        
        BigDecimal unitPrice = event.getTicketPrice();
        BigDecimal totalAmount = unitPrice.multiply(new BigDecimal(request.getQuantity()));

        // Lưu bản ghi Order ở trạng thái PENDING
        User user = this.userRepository.getUserById(request.getAttendeeId());
        
        Orders order = new Orders();
        order.setUser(user);
        order.setEvent(event);
        order.setQuantity(request.getQuantity());
        order.setUnitPrice(unitPrice);
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.PENDING);
        Long orderId = paymentRepository.saveOrder(order);

        // Lưu bản ghi Payment ở trạng thái INITIATED
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMethod("PAYPAL");
        payment.setAmount(totalAmount);
        payment.setStatus(PaymentStatus.INITIATED);
        Long paymentId = paymentRepository.savePayment(payment);

        // BƯỚC 3: Gọi PayPal API tạo order v2
        String accessToken = payPalConfig.getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        // Xây dựng Body Request gửi lên PayPal API v2
        Map<String, Object> orderRequest = new HashMap<>();
        orderRequest.put("intent", "CAPTURE");

        Map<String, Object> purchaseUnit = new HashMap<>();
        Map<String, String> amountMap = new HashMap<>();
        amountMap.put("currency_code", "USD"); // Chuyển đổi currency tùy cấu hình hệ thống
        amountMap.put("value", totalAmount.toString());
        purchaseUnit.put("amount", amountMap);
        orderRequest.put("purchase_units", Collections.singletonList(purchaseUnit));

        // Nhúng ngược orderId và paymentId vào URL callback trả về phục vụ Bước 5 & 6
        Map<String, String> applicationContext = new HashMap<>();
        applicationContext.put("return_url", "http://localhost:8080/nvtt_lqv/api/payment/paypal/success?orderId=" + orderId + "&paymentId=" + paymentId);
        applicationContext.put("cancel_url", "http://localhost:8080/nvtt_lqv/api/payment/paypal/cancel?orderId=" + orderId + "&paymentId=" + paymentId);
        orderRequest.put("application_context", applicationContext);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(orderRequest, headers);
        
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    payPalConfig.getBaseUrl() + "/v2/checkout/orders", entity, Map.class);
            
            Map<String, Object> responseBody = response.getBody();
            String approvalUrl = "";
            
            // Tìm kiếm link approvalUrl có rel = "approve" từ mảng link phản hồi của PayPal
            List<Map<String, String>> links = (List<Map<String, String>>) responseBody.get("links");
            for (Map<String, String> link : links) {
                if ("approve".equals(link.get("rel"))) {
                    approvalUrl = link.get("href");
                    break;
                }
            }

            // BƯỚC 4: Trả dữ liệu DTO điều hướng về Controller
            return new PayPalResponseDTO(approvalUrl, orderId, paymentId);

        } catch (Exception e) {
            throw new RuntimeException("Gặp lỗi trong quá trình khởi tạo cổng thanh toán PayPal: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentSuccessResponseDTO executeSuccessPayment(String token, String payerId, Long orderId, Long paymentId) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            // BƯỚC 6: Backend gửi lệnh Capture thanh toán sang PayPal với token nhận được
            String accessToken = payPalConfig.getAccessToken();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            HttpEntity<String> entity = new HttpEntity<>("{}", headers);
            String captureUrl = payPalConfig.getBaseUrl() + "/v2/checkout/orders/" + token + "/capture";
            
            ResponseEntity<Map> response = restTemplate.postForEntity(captureUrl, entity, Map.class);
            Map<String, Object> responseBody = response.getBody();

            String paypalStatus = (String) responseBody.get("status");

            if ("COMPLETED".equals(paypalStatus)) {
                // Đọc thông tin người thanh toán từ phản hồi
                Map<String, Object> payerInfo = (Map<String, Object>) responseBody.get("payer");
                String payerEmail = (String) payerInfo.get("email_address");
                Payment payment = new Payment();
                payment.setId(paymentId);

                // Thêm bản ghi chi tiết vào bảng payment_transactions
                PaymentTransaction transaction = new PaymentTransaction();
                transaction.setPayment(payment);
                transaction.setProvider("PAYPAL");
                transaction.setProviderTransactionId(token); // Paypal Order ID đóng vai trò token định danh
                transaction.setPayerEmail(payerEmail);
                transaction.setPayerId(payerId);
                transaction.setProviderStatus(ProviderStatus.SUCCESS);
                // Giả định dùng thư viện Gson hoặc Jackson chuyển đổi Map response sang JSON String để lưu trường raw_response
                transaction.setRawResponse(mapper.writeValueAsString(responseBody)); 
                paymentRepository.savePaymentTransaction(transaction);

                // Cập nhật trạng thái các bảng thông tin tài chính
                paymentRepository.updatePaymentStatus(paymentId, PaymentStatus.SUCCESS);
                paymentRepository.updateOrderStatus(orderId, OrderStatus.CONFIRMED);

                // Lấy thông tin đơn hàng để xử lý trừ số lượng vé và sinh mã vé chi tiết
                Orders order = paymentRepository.findOrderById(orderId);
//                paymentRepository.deductAvailableTickets(order.getEvent().getId(), order.getQuantity());

                // BƯỚC 7: Sinh chuỗi mã vé bảo mật (UUID) tương ứng với số lượng & Cập nhật thống kê
                List<String> generatedTicketCodes = new ArrayList<>();
                for (int i = 0; i < order.getQuantity(); i++) {
                    String uniqueTicketCode = UUID.randomUUID().toString().replace("-", "").toUpperCase();
                    
                    Ticket ticket = new Ticket();
                    ticket.setOrder(order);
                    ticket.setEvent(order.getEvent());
                    ticket.setAttendee(order.getUser());
                    ticket.setTicketCode(uniqueTicketCode);
                    
                    paymentRepository.saveTicket(ticket);
                    generatedTicketCodes.add(uniqueTicketCode);
                }

                // Cập nhật tăng doanh thu trực tiếp cho EventStatistics
                paymentRepository.updateEventStatistics(order.getEvent(), order.getQuantity(), order.getTotalAmount());

                // BƯỚC 8: Trả kết quả chuẩn xác về dạng DTO
                return new PaymentSuccessResponseDTO(true, orderId, generatedTicketCodes);
            } else {
                handleFailure(orderId, paymentId);
                return new PaymentSuccessResponseDTO(false, orderId, null);
            }
        } catch (Exception e) {
            handleFailure(orderId, paymentId);
            throw new RuntimeException("Giao dịch thất bại tại hệ thống hoặc lỗi từ phía nhà cung cấp: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void executeCancelPayment(Long orderId, Long paymentId) {
        handleFailure(orderId, paymentId);
    }

    private void handleFailure(Long orderId, Long paymentId) {
        // TRƯỜNG HỢP HỦY / THẤT BẠI: Đưa về trạng thái hủy giao dịch và không cấp phát vé
        paymentRepository.updateOrderStatus(orderId, OrderStatus.CANCELLED);
        paymentRepository.updatePaymentStatus(paymentId, PaymentStatus.FAILED);
    }
}
