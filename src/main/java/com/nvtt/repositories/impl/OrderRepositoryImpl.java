package com.nvtt.repositories.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nvtt.pojo.Orders;
import com.nvtt.repositories.OrderRepository;
import jakarta.persistence.NoResultException;

import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.Optional;

@Transactional
@Repository
public class OrderRepositoryImpl implements OrderRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Autowired
    private Environment env;

    @Override
    public List<Orders> getOrders(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Orders> q = b.createQuery(Orders.class);
        Root<Orders> root = q.from(Orders.class);
        root.fetch("event", JoinType.LEFT);
        root.fetch("user", JoinType.LEFT);
        q.select(root).distinct(true);

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            String id = params.get("id");
            if (id != null && !id.isEmpty()) {
                predicates.add(b.equal(root.get("id"), Long.parseLong(id)));
            }

            String eventId = params.get("eventId");
            if (eventId != null && !eventId.isEmpty()) {
                predicates.add(b.equal(root.get("event").get("id"), Long.parseLong(eventId)));
            }

            String userId = params.get("userId");
            if (userId != null && !userId.isEmpty()) {
                predicates.add(b.equal(root.get("user").get("id"), Long.parseLong(userId)));
            }

            String status = params.get("status");
            if (status != null && !status.isEmpty()) {
                predicates.add(b.equal(root.get("status"), status));
            }

            q.where(predicates.toArray(new Predicate[0]));
        }

        q.orderBy(b.desc(root.get("id")));

        Query query = session.createQuery(q);

        if (params != null) {
            int pageSize = this.env.getProperty("orders.pageSize", Integer.class, 10);
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page - 1) * pageSize;

            query.setMaxResults(pageSize);
            query.setFirstResult(start);
        }

        return query.getResultList();
    }

    @Override
    public Orders getOrderById(Long id) {
        Session s = factory.getObject().getCurrentSession();

        Query query = s.createNamedQuery("Orders.findById", Orders.class);
        query.setParameter("id", id);

        Optional<Orders> optionalOrder;

        try {
            Orders order = (Orders) query.getSingleResult();
            optionalOrder = Optional.of(order);
        } catch (NoResultException e) {
            optionalOrder = Optional.empty();
        }

        if (optionalOrder.isEmpty()) {
            return null;
        }

        return optionalOrder.get();
    }
}
