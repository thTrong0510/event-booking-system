/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.repositories;

import com.nvtt.pojo.Event;
import com.nvtt.pojo.Orders;
import com.nvtt.pojo.Payment;
import com.nvtt.pojo.PaymentTransaction;
import com.nvtt.pojo.Ticket;
import com.nvtt.utils.constants.OrderStatus;
import com.nvtt.utils.constants.PaymentStatus;
import java.math.BigDecimal;

/**
 *
 * @author vthan
 */
public interface PaymentRepository {

    Event findEventById(Long eventId);

    Long saveOrder(Orders order);

    Long savePayment(Payment payment);

    void savePaymentTransaction(PaymentTransaction transaction);

    void saveTicket(Ticket ticket);

    void updateOrderStatus(Long orderId, OrderStatus status);

    void updatePaymentStatus(Long paymentId, PaymentStatus status);

    void deductAvailableTickets(Long eventId, int quantity);

    void updateEventStatistics(Event event, int quantity, BigDecimal amount);

    Orders findOrderById(Long orderId);
}
