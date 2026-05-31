/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.repositories.impl;

import com.nvtt.pojo.Event;
import com.nvtt.pojo.EventStatistic;
import com.nvtt.repositories.EventStatisticRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.persistence.NoResultException;
import java.util.Optional;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author lequa
 */
@Repository
@Transactional
public class EventStatisticRepositoryImpl implements EventStatisticRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public EventStatistic addEventStatistic(EventStatistic eventStatistic) {
        try {
            Session s = factory.getObject().getCurrentSession();
            s.persist(eventStatistic);
            return eventStatistic;
        } catch (Exception e) {
            throw new RuntimeException("Error in add Event Statistic: " + e.getMessage());
        }
    }

    @Override
    public EventStatistic updateEventStatistic(EventStatistic eventStatistic) {
        try {
            Session s = factory.getObject().getCurrentSession();
            s.merge(eventStatistic);
            return eventStatistic;
        } catch (Exception e) {
            throw new RuntimeException("Error in update Event Statistic: " + e.getMessage());
        }
    }

    @Override
    public List<EventStatistic> getEventStatistics(Map<String, String> params) {
        return getEventStatisticsByOrganizerAndCreatedAtRange(null, params, null, null);
    }

    @Override
    public List<EventStatistic> getEventStatisticsByCreatedAtRange(Map<String, String> params, Date fromCreatedAt, Date toCreatedAt) {
        return getEventStatisticsByOrganizerAndCreatedAtRange(null, params, fromCreatedAt, toCreatedAt);
    }

    @Override
    public List<EventStatistic> getEventStatisticsByOrganizerAndCreatedAtRange(Long organizerId, Map<String, String> params, Date fromCreatedAt, Date toCreatedAt) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<EventStatistic> q = b.createQuery(EventStatistic.class);
        Root<EventStatistic> root = q.from(EventStatistic.class);
        List<Predicate> predicates = new ArrayList<>();

        if (organizerId != null) {
            Join<EventStatistic, Event> event = root.join("event");
            predicates.add(b.equal(event.get("organizer").get("id"), organizerId));
        }

        if (params != null) {
            String eventId = params.get("eventId");
            if (eventId != null && !eventId.isEmpty()) {
                predicates.add(b.equal(root.get("eventId"), Long.parseLong(eventId)));
            }

            String fromRevenue = params.get("fromRevenue");
            if (fromRevenue != null && !fromRevenue.isEmpty()) {
                predicates.add(b.greaterThanOrEqualTo(root.<BigDecimal>get("totalRevenue"), new BigDecimal(fromRevenue)));
            }

            String toRevenue = params.get("toRevenue");
            if (toRevenue != null && !toRevenue.isEmpty()) {
                predicates.add(b.lessThanOrEqualTo(root.<BigDecimal>get("totalRevenue"), new BigDecimal(toRevenue)));
            }

            String fromViews = params.get("fromViews");
            if (fromViews != null && !fromViews.isEmpty()) {
                predicates.add(b.greaterThanOrEqualTo(root.<Integer>get("totalViews"), Integer.parseInt(fromViews)));
            }

            String toViews = params.get("toViews");
            if (toViews != null && !toViews.isEmpty()) {
                predicates.add(b.lessThanOrEqualTo(root.<Integer>get("totalViews"), Integer.parseInt(toViews)));
            }
        }

        if (fromCreatedAt != null) {
            predicates.add(b.greaterThanOrEqualTo(root.<Date>get("createdAt"), fromCreatedAt));
        }

        if (toCreatedAt != null) {
            predicates.add(b.lessThan(root.<Date>get("createdAt"), toCreatedAt));
        }

        q.where(predicates.toArray(new Predicate[0]));
        return session.createQuery(q).getResultList();
    }

    @Override
    public EventStatistic getEventStatisticByEventId(Long eventId) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<EventStatistic> q = b.createQuery(EventStatistic.class);
        Root<EventStatistic> root = q.from(EventStatistic.class);
        q.where(b.equal(root.get("eventId"), eventId));
        org.hibernate.query.Query<EventStatistic> hQuery = session.createQuery(q);

        Optional<EventStatistic> optionalStat;
        try {
            EventStatistic stat = hQuery.getSingleResult();
            optionalStat = Optional.of(stat);
        } catch (NoResultException e) {
            optionalStat = Optional.empty();
        }

        if (optionalStat.isEmpty()) {
            return null;
        }

        return optionalStat.get();
    }

    @Override
    public EventStatistic getEventStatisticByEventIdAndOrganizerId(Long eventId, Long organizerId) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<EventStatistic> q = b.createQuery(EventStatistic.class);
        Root<EventStatistic> root = q.from(EventStatistic.class);
        Join<EventStatistic, Event> event = root.join("event");
        q.where(
                b.and(
                        b.equal(root.get("eventId"), eventId),
                        b.equal(event.get("organizer").get("id"), organizerId)
                )
        );
        org.hibernate.query.Query<EventStatistic> hQuery = session.createQuery(q);

        Optional<EventStatistic> optionalStat;
        try {
            EventStatistic stat = hQuery.getSingleResult();
            optionalStat = Optional.of(stat);
        } catch (NoResultException e) {
            optionalStat = Optional.empty();
        }

        if (optionalStat.isEmpty()) {
            return null;
        }

        return optionalStat.get();
    }

    @Override
    public List<EventStatistic> getEventStatisticsByEventIds(List<Long> eventIds) {
        if (eventIds == null || eventIds.isEmpty()) {
            return new ArrayList<>();
        }

        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<EventStatistic> q = b.createQuery(EventStatistic.class);
        Root<EventStatistic> root = q.from(EventStatistic.class);

        // Tạo điều kiện: eventId IN (:eventIds)
        q.where(root.get("eventId").in(eventIds));

        return session.createQuery(q).getResultList();
    }
}
