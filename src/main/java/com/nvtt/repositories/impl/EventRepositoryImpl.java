package com.nvtt.repositories.impl;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Date;
import java.math.BigDecimal;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nvtt.pojo.Event;
import com.nvtt.pojo.EventStatus;
import com.nvtt.repositories.EventRepository;

import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.JoinType;

/**
 *
 * @author lequa
 */
@Repository
@Transactional
public class EventRepositoryImpl implements EventRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Autowired
    private Environment env;

    @Override
    public Event getEventById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Event> q = b.createQuery(Event.class);
        Root<Event> root = q.from(Event.class);
        root.fetch("eventMedias", JoinType.LEFT);
        q.select(root).distinct(true);
        q.where(b.equal(root.get("id"), id));
        return session.createQuery(q).getSingleResult();
    }

    @Override
    public Event getEventById(Long id, List<EventStatus> statuses) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Event> q = b.createQuery(Event.class);
        Root<Event> root = q.from(Event.class);
        root.fetch("eventMedias", JoinType.LEFT);
        q.select(root).distinct(true);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(b.equal(root.get("id"), id));
        if (statuses != null) {
            predicates.add(statuses.isEmpty() ? b.disjunction() : root.get("status").in(statuses));
        }

        q.where(predicates.toArray(new Predicate[0]));
        return session.createQuery(q).getSingleResult();
    }

    @Override
    public Event getOwnEventById(Long id, Long organizerId){
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Event> q = b.createQuery(Event.class);
        Root<Event> root = q.from(Event.class);
        root.fetch("eventMedias", JoinType.LEFT);
        q.select(root).distinct(true);
        q.where(
            b.and(
                b.equal(root.get("id"), id),
                b.equal(root.get("organizer").get("id"), organizerId)
            )
        );
        return session.createQuery(q).getSingleResult();
    }

    @Override
    public List<Event> getEvents(Map<String, String> params, List<EventStatus> statuses) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Event> q = b.createQuery(Event.class);
        Root<Event> root = q.from(Event.class);
        root.fetch("eventMedias", JoinType.LEFT);
        q.select(root).distinct(true);

        List<Predicate> predicates = new ArrayList<>();
        
        if (statuses != null) {
            predicates.add(statuses.isEmpty() ? b.disjunction() : root.get("status").in(statuses));
        }

        if (params != null) {
            String id = params.get("id");
            if (id != null && !id.isEmpty()) {
                predicates.add(b.equal(root.get("id"), Long.parseLong(id)));
            }

            String kw = params.get("name");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(root.get("name"), String.format("%%%s%%", kw)));
            }

            String fromPrice = params.get("fromPrice");
            if (fromPrice != null && !fromPrice.isEmpty()) {
                predicates.add(b.greaterThanOrEqualTo(root.get("ticketPrice"), new BigDecimal(fromPrice)));
            }

            String toPrice = params.get("toPrice");
            if (toPrice != null && !toPrice.isEmpty()) {
                predicates.add(b.lessThanOrEqualTo(root.get("ticketPrice"), new BigDecimal(toPrice)));
            }

            String cateId = params.get("cateId");
            if (cateId != null && !cateId.isEmpty()) {
                predicates.add(b.equal(root.get("category").get("id"), Integer.parseInt(cateId)));
            }

            String statusId = params.get("statusId");
            if (statusId != null && !statusId.isEmpty()) {
                predicates.add(b.equal(root.get("status").get("id"), Integer.parseInt(statusId)));
            }

            String startTime = params.get("startTime");
            if (startTime != null && !startTime.isEmpty()) {
                predicates.add(b.greaterThanOrEqualTo(root.get("startTime"), new Date(Long.parseLong(startTime))));
            }

            String endTime = params.get("endTime");
            if (endTime != null && !endTime.isEmpty()) {
                predicates.add(b.lessThanOrEqualTo(root.get("startTime"), new Date(Long.parseLong(endTime))));
            }

            String location = params.get("location");
            if (location != null && !location.isEmpty()) {
                predicates.add(b.like(root.get("location"), String.format("%%%s%%", location)));
            }
        }

        q.where(predicates.toArray(new Predicate[0]));
        q.orderBy(b.desc(root.get("id")));

        Query query = session.createQuery(q);

        // xu ly phan trang
        if (params != null) {
            int pageSize = this.env.getProperty("events.page_size", Integer.class, 10);
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page - 1) * pageSize;

            query.setMaxResults(pageSize);
            query.setFirstResult(start);
        }

        return query.getResultList();
    }

    @Override
    public Event addEvent(Event event) {
        Session s = factory.getObject().getCurrentSession();
        if (event.getId() == null) {
            System.out.println("Media: " + event.getEventMedias().size());
            s.persist(event);
        } else {
            s.merge(event);
        }
        return event;
    }

    @Override
    public boolean deleteEvent(Event event) {
        Session s = factory.getObject().getCurrentSession();
        s.remove(event);
        return true;
    }

    @Override
    public List<Event> getOrganizerEvents(Long organizerId, Map<String, String> params) {
        Session s = factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Event> q = b.createQuery(Event.class);
        Root<Event> root = q.from(Event.class);
        root.fetch("eventMedias", JoinType.LEFT);
        q.select(root).distinct(true);
        
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(b.equal(root.get("organizer").get("id"), organizerId));

        if (params != null) {
            String id = params.get("id");
            if (id != null && !id.isEmpty()) {
                predicates.add(b.equal(root.get("id"), Long.parseLong(id)));
            }

            String kw = params.get("name");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(root.get("name"), String.format("%%%s%%", kw)));
            }

            String fromPrice = params.get("fromPrice");
            if (fromPrice != null && !fromPrice.isEmpty()) {
                predicates.add(b.greaterThanOrEqualTo(root.get("ticketPrice"), new BigDecimal(fromPrice)));
            }

            String toPrice = params.get("toPrice");
            if (toPrice != null && !toPrice.isEmpty()) {
                predicates.add(b.lessThanOrEqualTo(root.get("ticketPrice"), new BigDecimal(toPrice)));
            }

            String cateId = params.get("cateId");
            if (cateId != null && !cateId.isEmpty()) {
                predicates.add(b.equal(root.get("category").get("id"), Integer.parseInt(cateId)));
            }

            String statusId = params.get("statusId");
            if (statusId != null && !statusId.isEmpty()) {
                predicates.add(b.equal(root.get("status").get("id"), Integer.parseInt(statusId)));
            }

            String startTime = params.get("startTime");
            if (startTime != null && !startTime.isEmpty()) {
                predicates.add(b.greaterThanOrEqualTo(root.get("startTime"), new Date(Long.parseLong(startTime))));
            }

            String endTime = params.get("endTime");
            if (endTime != null && !endTime.isEmpty()) {
                predicates.add(b.lessThanOrEqualTo(root.get("startTime"), new Date(Long.parseLong(endTime))));
            }

            String location = params.get("location");
            if (location != null && !location.isEmpty()) {
                predicates.add(b.like(root.get("location"), String.format("%%%s%%", location)));
            }
        }
        
        q.where(predicates.toArray(new Predicate[0]));
        q.orderBy(b.desc(root.get("id")));

        Query query = s.createQuery(q);

        // xu ly phan trang
        if (params != null) {
            int pageSize = this.env.getProperty("events.page_size", Integer.class, 10);
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page - 1) * pageSize;

            query.setMaxResults(pageSize);
            query.setFirstResult(start);
        }

        return query.getResultList();
    }
}
