/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.repositories.impl;

import com.nvtt.pojo.Category;
import com.nvtt.pojo.Event;
import com.nvtt.pojo.EventStatistic;
import com.nvtt.repositories.EventStatisticRepository;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<EventStatistic> getEventStatistics(Map<String, String> params){
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<EventStatistic> q = b.createQuery(EventStatistic.class);
        Root<EventStatistic> root = q.from(EventStatistic.class);
        
        if (params != null) {
            if (params.containsKey("eventId")) {
                q.where(b.equal(root.get("eventId"), Long.parseLong(params.get("eventId"))));
            }
            if (params.containsKey("fromRevenue")) {
                q.where(b.greaterThanOrEqualTo(root.get("totalRevenue"), new BigDecimal(params.get("fromRevenue"))));
            }
            if (params.containsKey("toRevenue")) {
                q.where(b.lessThanOrEqualTo(root.get("totalRevenue"), new BigDecimal(params.get("toRevenue"))));
            }
            if (params.containsKey("fromViews")) {
                q.where(b.greaterThanOrEqualTo(root.get("totalViews"), new BigDecimal(params.get("fromViews"))));
            }
            if (params.containsKey("toViews")) {
                q.where(b.lessThanOrEqualTo(root.get("totalViews"), new BigDecimal(params.get("toViews"))));
            }
        }
        return session.createQuery(q).getResultList();
    }
    
    @Override
    public EventStatistic getEventStatisticByEventId(Long eventId){
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<EventStatistic> q = b.createQuery(EventStatistic.class);
        Root<Event> root = q.from(Event.class);
        q.where(b.equal(root.get("eventId"), eventId));
        return session.createQuery(q).getSingleResult();
    }
}
