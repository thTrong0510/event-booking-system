package com.nvtt.repositories.impl;

import com.nvtt.pojo.Event;
import com.nvtt.pojo.Orders;
import com.nvtt.pojo.Payment;
import com.nvtt.pojo.PaymentTransaction;
import com.nvtt.pojo.Ticket;
import com.nvtt.repositories.PaymentRepository;
import com.nvtt.utils.constants.OrderStatus;
import com.nvtt.utils.constants.PaymentStatus;
import jakarta.persistence.NoResultException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentRepositoryImpl implements PaymentRepository {

    @Autowired
    private LocalSessionFactoryBean sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getObject().getCurrentSession();
    }

    @Override
    public Event findEventById(Long eventId) {
        String hql = "FROM Event e WHERE e.id = :eventId";
        return getCurrentSession().createQuery(hql, Event.class)
                .setParameter("eventId", eventId)
                .uniqueResult();
    }

    @Override
    public Orders findOrderById(Long orderId) {
        String hql = "FROM Orders o WHERE o.id = :orderId";
        return getCurrentSession().createQuery(hql, Orders.class)
                .setParameter("orderId", orderId)
                .uniqueResult();
    }

    @Override
    public Long saveOrder(Orders order) {
        getCurrentSession().persist(order);

        return order.getId();
    }

    @Override
    public Long savePayment(Payment payment) {
        getCurrentSession().persist(payment);

        return payment.getId();
    }

    @Override
    public void savePaymentTransaction(PaymentTransaction transaction) {
        getCurrentSession().persist(transaction);
    }

    @Override
    public void saveTicket(Ticket ticket) {
        getCurrentSession().persist(ticket);
    }

    @Override
    public void updateOrderStatus(Long orderId, OrderStatus status) {
        String hql = "UPDATE Orders o SET o.status = :status WHERE o.id = :orderId";
        getCurrentSession().createMutationQuery(hql)
                .setParameter("status", status)
                .setParameter("orderId", orderId)
                .executeUpdate();
    }

    @Override
    public void updatePaymentStatus(Long paymentId, PaymentStatus status) {
        String hql = "UPDATE Payment p SET p.status = :status WHERE p.id = :paymentId";
        getCurrentSession().createMutationQuery(hql)
                .setParameter("status", status)
                .setParameter("paymentId", paymentId)
                .executeUpdate();
    }

    @Override
    public void deductAvailableTickets(Long eventId, int quantity) {
        String hql = "UPDATE Event e SET e.availableTickets = e.availableTickets - :quantity "
                + "WHERE e.id = :eventId AND e.availableTickets >= :quantity";
        int updatedRows = getCurrentSession().createMutationQuery(hql)
                .setParameter("quantity", quantity)
                .setParameter("eventId", eventId)
                .executeUpdate();
        if (updatedRows == 0) {
            throw new RuntimeException("Vé đã bị hết hoặc không đủ trong quá trình xử lý giao dịch!");
        }
    }

    @Override
    public void updateEventStatistics(Event event, int quantity, BigDecimal amount) {
        String hql = "UPDATE EventStatistic es SET es.totalTicketsSold = es.totalTicketsSold + :quantity, "
                + "es.totalRevenue = es.totalRevenue + :amount WHERE es.eventId = :eventId";
        getCurrentSession().createMutationQuery(hql)
                .setParameter("quantity", quantity)
                .setParameter("amount", amount)
                .setParameter("eventId", event.getId())
                .executeUpdate();
    }

    @Override
    public List<Orders> findPendingOrdersBefore(Date thresholdTime) {
        String jpql = "SELECT o FROM Orders o WHERE o.status = :status AND o.createdAt <= :thresholdTime";
        return getCurrentSession().createQuery(jpql, Orders.class)
                .setParameter("status", OrderStatus.PENDING)
                .setParameter("thresholdTime", thresholdTime)
                .getResultList();
    }

    @Override
    public Payment findPaymentByOrderId(Long orderId) {
        try {
            String jpql = "SELECT p FROM Payment p WHERE p.order.id = :orderId";
            return getCurrentSession().createQuery(jpql, Payment.class)
                    .setParameter("orderId", orderId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
