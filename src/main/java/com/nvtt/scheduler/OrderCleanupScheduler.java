package com.nvtt.scheduler;

import com.nvtt.pojo.Event;
import com.nvtt.pojo.Orders;
import com.nvtt.pojo.Payment;
import com.nvtt.repositories.EventRepository;
import com.nvtt.repositories.PaymentRepository;
import com.nvtt.utils.constants.OrderStatus;
import com.nvtt.utils.constants.PaymentStatus;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderCleanupScheduler {

    private static final Logger logger = LogManager.getLogger(OrderCleanupScheduler.class);

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private EventRepository eventRepository;

    @Transactional(rollbackFor = Exception.class)
    @Scheduled(fixedRate = 60000)
    public void cancelExpiredPendingOrders() {

        Date fiveMinutesAgo = new Date(System.currentTimeMillis() - 15 * 60 * 1000);
        logger.info("start sql cancelExpiredPendingOrders");
        List<Orders> expiredOrders = paymentRepository.findPendingOrdersBefore(fiveMinutesAgo);

        if (expiredOrders != null && !expiredOrders.isEmpty()) {
            logger.info("Phát hiện {} đơn hàng PENDING quá hạn 5 phút. Bắt đầu hủy...", expiredOrders.size());

            for (Orders order : expiredOrders) {
                Payment payment = paymentRepository.findPaymentByOrderId(order.getId());
                Long paymentId = (payment != null) ? payment.getId() : null;

                handleFailure(order.getId(), paymentId, order.getQuantity(), order.getEvent().getId());

                logger.info("Đã tự động hủy đơn hàng rác ID: {}", order.getId());
            }
        }
        logger.info("end sql");
    }

    private void handleFailure(Long orderId, Long paymentId, int quantityTickets, Long eventId) {
        paymentRepository.updateOrderStatus(orderId, OrderStatus.CANCELLED);

        if (paymentId != null) {
            paymentRepository.updatePaymentStatus(paymentId, PaymentStatus.FAILED);
        }

        Event event = this.eventRepository.findById(eventId);
        int updatedTickets = event.getAvailableTickets() + quantityTickets;
        event.setAvailableTickets(updatedTickets);
        this.eventRepository.addEvent(event);
    }
}
