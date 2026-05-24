/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

/**
 *
 * @author lequa
 */
@Repository
@Transactional
public class EventStatusRepositoryImpl implements EventStatusRepository{


    @Autowired
    private LocalSessionFactoryBean factory;

    
    private Session getCurrentSession() {
        return sessionFactory.getObject().getCurrentSession();
    }
    
    @Override
    public EventStatus getStatusByName(String name) {
        Session session = this.factory.getObject().getCurrentSession();
        EventStatus eventStatus = session.createNamedQuery("EventStatus.findByName", EventStatus.class)
                .setParameter("name", name)
                .getSingleResult();
        return eventStatus;
    }
    
    @Override
    public EventStatus getStatusById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        EventStatus eventStatus = session.createNamedQuery("EventStatus.findById", EventStatus.class)
                .setParameter("id", id)
                .getSingleResult();
        return eventStatus;
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
}
