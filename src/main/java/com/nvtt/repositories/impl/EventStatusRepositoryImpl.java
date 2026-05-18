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

/**
 *
 * @author lequa
 */
@Repository
@Transactional
public class EventStatusRepositoryImpl implements EventStatusRepository{


    @Autowired
    private LocalSessionFactoryBean factory;
    
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

}
