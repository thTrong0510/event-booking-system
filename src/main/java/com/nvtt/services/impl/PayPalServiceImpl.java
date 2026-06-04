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
import com.nvtt.utils.exceptions.IdInvalidException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
public class PayPalServiceImpl implements PayPalService {

    @Value("${paypal.cancel-url}")
    private String cancelUrl;

    @Value("${paypal.return-url}")
    private String returnUrl;

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
    public PayPalResponseDTO createPayPalPayment(PaymentCreateRequest request) throws IdInvalidException {
        Event event = this.eventRepository.findById(request.getEventId());
        if (event == null || event.getAvailableTickets() < request.getQuantity()) {
            throw new IdInvalidException("Số lượng vé còn lại không đủ để thực hiện đăng ký!");
        }

        int updatedTickets = event.getAvailableTickets() - request.getQuantity();
        event.setAvailableTickets(updatedTickets);
        this.eventRepository.addEvent(event);

        BigDecimal unitPrice = event.getTicketPrice();
        BigDecimal totalAmount = unitPrice.multiply(new BigDecimal(request.getQuantity()));

        User user = this.userRepository.getUserById(request.getAttendeeId());

        Orders order = new Orders();
        order.setUser(user);
        order.setEvent(event);
        order.setQuantity(request.getQuantity());
        order.setUnitPrice(unitPrice);
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.PENDING);
        Long orderId = paymentRepository.saveOrder(order);

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMethod("PAYPAL");
        payment.setAmount(totalAmount);
        payment.setStatus(PaymentStatus.INITIATED);
        Long paymentId = paymentRepository.savePayment(payment);

        String accessToken = payPalConfig.getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        Map<String, Object> orderRequest = new HashMap<>();
        orderRequest.put("intent", "CAPTURE");

        BigDecimal usdAmount = totalAmount.divide(new BigDecimal("26000"), 2, RoundingMode.UP);

        Map<String, Object> purchaseUnit = new HashMap<>();
        Map<String, String> amountMap = new HashMap<>();
        amountMap.put("currency_code", "USD");
        amountMap.put("value", usdAmount.toString());
        purchaseUnit.put("amount", amountMap);
        orderRequest.put("purchase_units", Collections.singletonList(purchaseUnit));

        Map<String, String> applicationContext = new HashMap<>();
        applicationContext.put("return_url", returnUrl + "?orderId=" + orderId + "&paymentId=" + paymentId + "&quantityTickets=" + request.getQuantity());
        applicationContext.put("cancel_url", cancelUrl + "?orderId=" + orderId + "&paymentId=" + paymentId + "&eventId=" + event.getId() + "&quantityTickets=" + request.getQuantity());
        orderRequest.put("application_context", applicationContext);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(orderRequest, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    payPalConfig.getBaseUrl() + "/v2/checkout/orders", entity, Map.class);

            Map<String, Object> responseBody = response.getBody();
            String approvalUrl = "";

            List<Map<String, String>> links = (List<Map<String, String>>) responseBody.get("links");
            for (Map<String, String> link : links) {
                if ("approve".equals(link.get("rel"))) {
                    approvalUrl = link.get("href");
                    break;
                }
            }

            return new PayPalResponseDTO(approvalUrl, orderId, paymentId);

        } catch (Exception e) {
            throw new IdInvalidException("Gặp lỗi trong quá trình khởi tạo cổng thanh toán PayPal: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentSuccessResponseDTO executeSuccessPayment(String token, String payerId, Long orderId, Long paymentId, int quantityTickets, Long eventId) throws IdInvalidException {
        try {
            ObjectMapper mapper = new ObjectMapper();

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

                Map<String, Object> payerInfo = (Map<String, Object>) responseBody.get("payer");
                String payerEmail = (String) payerInfo.get("email_address");
                Payment payment = new Payment();
                payment.setId(paymentId);

                PaymentTransaction transaction = new PaymentTransaction();
                transaction.setPayment(payment);
                transaction.setProvider("PAYPAL");
                transaction.setProviderTransactionId(token);
                transaction.setPayerEmail(payerEmail);
                transaction.setPayerId(payerId);
                transaction.setProviderStatus(ProviderStatus.SUCCESS);

                transaction.setRawResponse(mapper.writeValueAsString(responseBody));
                paymentRepository.savePaymentTransaction(transaction);

                paymentRepository.updatePaymentStatus(paymentId, PaymentStatus.SUCCESS);
                paymentRepository.updateOrderStatus(orderId, OrderStatus.CONFIRMED);

                Orders order = paymentRepository.findOrderById(orderId);

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

                paymentRepository.updateEventStatistics(order.getEvent(), order.getQuantity(), order.getTotalAmount());

                return new PaymentSuccessResponseDTO(true, orderId, generatedTicketCodes);
            } else {
                handleFailure(orderId, paymentId, quantityTickets, eventId);
                return new PaymentSuccessResponseDTO(false, orderId, null);
            }
        } catch (Exception e) {
            handleFailure(orderId, paymentId, quantityTickets, eventId);
            throw new IdInvalidException("Giao dịch thất bại tại hệ thống hoặc lỗi từ phía nhà cung cấp: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void executeCancelPayment(Long orderId, Long paymentId, int quantityTickets, Long eventId) {
        handleFailure(orderId, paymentId, quantityTickets, eventId);
    }

    private void handleFailure(Long orderId, Long paymentId, int quantityTickets, Long eventId) {
        paymentRepository.updateOrderStatus(orderId, OrderStatus.CANCELLED);
        paymentRepository.updatePaymentStatus(paymentId, PaymentStatus.FAILED);
        Event event = this.eventRepository.findById(eventId);
        int updatedTickets = event.getAvailableTickets() + quantityTickets;
        event.setAvailableTickets(updatedTickets);
        this.eventRepository.addEvent(event);
    }
}
