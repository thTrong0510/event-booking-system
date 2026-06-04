package com.nvtt.controllers.apis.paypal;

import com.nvtt.pojo.Event;
import com.nvtt.pojo.Orders;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.nvtt.pojo.dtos.request.PaymentCreateRequest;
import com.nvtt.pojo.dtos.response.PayPalResponseDTO;
import com.nvtt.pojo.dtos.response.PaymentSuccessResponseDTO;
import com.nvtt.services.OrderService;
import com.nvtt.services.paypal.PayPalService;
import com.nvtt.utils.exceptions.IdInvalidException;
import jakarta.servlet.http.HttpServletResponse;
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

@RestController
@PropertySource("classpath:configs.properties")
@RequestMapping("/api/v1")
public class PaymentController {

    private static final Logger logger = LogManager.getLogger(PaymentController.class);

    @Autowired
    private Environment env;

    @Autowired
    private PayPalService paypalService;

    @Autowired
    private OrderService orderService;

    @PostMapping("/me/payment/paypal/create")
    public ResponseEntity<PayPalResponseDTO> createPayment(@RequestBody PaymentCreateRequest request) throws IdInvalidException {
        logger.info("start sql createPayment");
        PayPalResponseDTO response = paypalService.createPayPalPayment(request);
        logger.info("end sql");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/public/payment/paypal/success")
    public ResponseEntity<PaymentSuccessResponseDTO> successPayment(
            @RequestParam("token") String token,
            @RequestParam("PayerID") String payerId,
            @RequestParam("orderId") Long orderId,
            @RequestParam("paymentId") Long paymentId,
            @RequestParam("quantityTickets") int quantityTickets,
            HttpServletResponse response) throws IdInvalidException {
        try {
            Orders order = this.orderService.getOrderById(orderId);
            Event event = order.getEvent();
            logger.info("start sql successPayment");
            PaymentSuccessResponseDTO result = paypalService.executeSuccessPayment(token, payerId, orderId, paymentId, quantityTickets, event.getId());
            logger.info("end sql");

            String frontendBaseUrl = this.env.getProperty("frontend.url", String.class);
            String redirectUrl;

            if (result != null && result.isSuccess()) {
                redirectUrl = frontendBaseUrl
                        + "/payment/success?orderId=" + result.getOrderId()
                        + "&eventId=" + event.getId();
            } else {
                redirectUrl = frontendBaseUrl + "/payment/fail?orderId=" + result.getOrderId()
                        + "&eventId=" + event.getId();
            }

            response.sendRedirect(redirectUrl);
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            logger.error("error: redirect from paypal - case success payment");
            throw new IdInvalidException("error: redirect from paypal");
        }
    }

    @GetMapping("/public/payment/paypal/cancel")
    public ResponseEntity<String> cancelPayment(
            @RequestParam("orderId") Long orderId,
            @RequestParam("paymentId") Long paymentId,
            @RequestParam("eventId") Long eventId,
            @RequestParam("quantityTickets") int quantityTickets,
            HttpServletResponse response) throws IdInvalidException {
        logger.info("start sql cancelPayment");
        paypalService.executeCancelPayment(orderId, paymentId, quantityTickets, eventId);
        logger.info("end sql");
        String frontendBaseUrl = this.env.getProperty("frontend.url", String.class);
        String redirectUrl = frontendBaseUrl
                + "/payment/fail?orderId=" + orderId
                + "&eventId=" + eventId;
        try {
            response.sendRedirect(redirectUrl);
        } catch (Exception ex) {
            logger.error("error: redirect from paypal - case cancel payment");
            throw new IdInvalidException("error: redirect from paypal");
        }
        return ResponseEntity.ok("Giao dịch thanh toán bằng tài khoản PayPal đã bị hủy bỏ bởi người dùng.");
    }
}
