package com.nvtt.services.paypal;

import com.nvtt.pojo.dtos.request.PaymentCreateRequest;
import com.nvtt.pojo.dtos.response.PayPalResponseDTO;
import com.nvtt.pojo.dtos.response.PaymentSuccessResponseDTO;
import com.nvtt.utils.exceptions.IdInvalidException;
import org.springframework.stereotype.Service;

@Service
public interface PayPalService {

    PayPalResponseDTO createPayPalPayment(PaymentCreateRequest request) throws IdInvalidException;

    PaymentSuccessResponseDTO executeSuccessPayment(String token, String payerId, Long orderId, Long paymentId, int quantityTickets, Long eventId) throws IdInvalidException;

    void executeCancelPayment(Long orderId, Long paymentId, int quantityTickets, Long eventId);
}
