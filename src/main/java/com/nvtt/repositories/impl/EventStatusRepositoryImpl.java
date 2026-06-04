package com.nvtt.repositories.impl;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nvtt.pojo.EventStatus;
import com.nvtt.repositories.EventStatusRepository;
import jakarta.persistence.NoResultException;
import java.util.List;
import java.util.Optional;
import org.hibernate.query.Query;

@Repository
@Transactional
public class EventStatusRepositoryImpl implements EventStatusRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    private Session getCurrentSession() {
        return factory.getObject().getCurrentSession();
    }

    @Override
    public EventStatus getStatusByName(String name) {
        Query query = this.getCurrentSession().createNamedQuery("EventStatus.findByName", EventStatus.class);
        query.setParameter("name", name);

        Optional<EventStatus> optionalStatus;

        try {
            EventStatus status = (EventStatus) query.getSingleResult();
            optionalStatus = Optional.of(status);
        } catch (NoResultException e) {
            optionalStatus = Optional.empty();
        }

        if (optionalStatus.isEmpty()) {
            return null;
        }

        return optionalStatus.get();
    }

    @Override
    public EventStatus getStatusById(Long id) {
        Query query = this.getCurrentSession().createNamedQuery("EventStatus.findById", EventStatus.class);
        query.setParameter("id", id);

        Optional<EventStatus> optionalStatus;

        try {
            EventStatus status = (EventStatus) query.getSingleResult();
            optionalStatus = Optional.of(status);
        } catch (NoResultException e) {
            optionalStatus = Optional.empty();
        }

        if (optionalStatus.isEmpty()) {
            return null;
        }

        return optionalStatus.get();
    }

    @Override
    public EventStatus addEventStatus(EventStatus eventStatus) {
        try {
            Session s = factory.getObject().getCurrentSession();
            if (eventStatus.getId() != null) {
                s.merge(eventStatus);
            } else {
                s.persist(eventStatus);
            }
            return eventStatus;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<EventStatus> findAll() {
        String hql = "FROM EventStatus es ORDER BY es.id ASC";
        return getCurrentSession().createQuery(hql, EventStatus.class).getResultList();
    }

    @Override
    public EventStatus findByName(String name) {
        Query query = this.getCurrentSession().createNamedQuery("EventStatus.findByName", EventStatus.class);
        query.setParameter("name", name);

        Optional<EventStatus> optionalStatus;

        try {
            EventStatus status = (EventStatus) query.getSingleResult();
            optionalStatus = Optional.of(status);
        } catch (NoResultException e) {
            optionalStatus = Optional.empty();
        }

        if (optionalStatus.isEmpty()) {
            return null;
        }

        return optionalStatus.get();
    }

    @Override
    public List<EventStatus> findByNameIn(List<String> names) {
        String hql = "FROM EventStatus es WHERE es.name IN (:names) ORDER BY es.id ASC";

        return getCurrentSession()
                .createQuery(hql, EventStatus.class)
                .setParameter("names", names)
                .getResultList();
    }
}
