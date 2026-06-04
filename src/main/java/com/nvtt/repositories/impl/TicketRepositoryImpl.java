package com.nvtt.repositories.impl;

import com.nvtt.pojo.Event;
import com.nvtt.pojo.Orders;
import com.nvtt.pojo.Ticket;
import com.nvtt.pojo.User;
import com.nvtt.pojo.dtos.ticket.ResTicketDTO;
import com.nvtt.repositories.TicketRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class TicketRepositoryImpl implements TicketRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    private Session getSession() {
        return factory.getObject().getCurrentSession();
    }

    @Override
    public List<Ticket> getTickets(Map<String, String> params) {
        Session session = getSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Ticket> q = b.createQuery(Ticket.class);
        Root<Ticket> root = q.from(Ticket.class);

        root.fetch("event", JoinType.LEFT);
        root.fetch("order", JoinType.LEFT);
        root.fetch("attendee", JoinType.LEFT);

        if (params != null) {
            if (params.containsKey("orderId")) {
                q.where(b.equal(root.get("order").get("id"), params.get("orderId")));
            }
            if (params.containsKey("eventId")) {
                q.where(b.equal(root.get("event").get("id"), Long.parseLong(params.get("eventId"))));
            }
            if (params.containsKey("attendeeId")) {
                q.where(b.equal(root.get("attendee").get("id"), Long.parseLong(params.get("attendeeId"))));
            }
            if (params.containsKey("fromCheckInTime")) {
                q.where(b.lessThanOrEqualTo(root.get("fromCheckInTime"), new Date(Long.parseLong(params.get("checkInTime")))));
            }
            if (params.containsKey("toCheckInTime")) {
                q.where(b.greaterThanOrEqualTo(root.get("toCheckInTime"), new Date(Long.parseLong(params.get("checkInTime")))));
            }
        }
        q.distinct(true);
        return session.createQuery(q).getResultList();
    }

    @Override
    public List<ResTicketDTO> getTicketDTOs(Map<String, String> params) {
        Session session = getSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<ResTicketDTO> q = b.createQuery(ResTicketDTO.class);
        Root<Ticket> root = q.from(Ticket.class);

        Join<Ticket, Orders> orderJoin = root.join("order", JoinType.LEFT);
        Join<Ticket, Event> eventJoin = root.join("event", JoinType.LEFT);
        Join<Ticket, User> userJoin = root.join("attendee", JoinType.LEFT);

        q.select(b.construct(
                ResTicketDTO.class,
                root.get("id"),
                orderJoin.get("id"),
                eventJoin.get("id"),
                userJoin.get("email"),
                root.get("ticketCode"),
                root.get("checkInTime")
        ));

        if (params != null) {
            if (params.containsKey("orderId")) {
                q.where(b.equal(root.get("order").get("id"), params.get("orderId")));
            }
            if (params.containsKey("eventId")) {
                q.where(b.equal(root.get("event").get("id"), Long.parseLong(params.get("eventId"))));
            }
            if (params.containsKey("attendeeId")) {
                q.where(b.equal(root.get("attendee").get("id"), Long.parseLong(params.get("attendeeId"))));
            }
            if (params.containsKey("fromCheckInTime")) {
                q.where(b.lessThanOrEqualTo(root.get("fromCheckInTime"), new Date(Long.parseLong(params.get("checkInTime")))));
            }
            if (params.containsKey("toCheckInTime")) {
                q.where(b.greaterThanOrEqualTo(root.get("toCheckInTime"), new Date(Long.parseLong(params.get("checkInTime")))));
            }
        }
        q.distinct(true);
        return session.createQuery(q).getResultList();
    }
}
