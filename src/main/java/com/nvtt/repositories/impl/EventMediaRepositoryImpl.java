/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.repositories.impl;

import java.util.Map;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nvtt.pojo.EventMedia;
import com.nvtt.repositories.EventMediaRepository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

/**
 *
 * @author lequa
 */
@Repository
@Transactional
public class EventMediaRepositoryImpl implements EventMediaRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public void deleteEventMedia(EventMedia media) {
        Session session = this.factory.getObject().getCurrentSession();
        session.remove(media);
    }

    @Override
    public EventMedia getEventMedia(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<EventMedia> q = b.createQuery(EventMedia.class);
        Root<EventMedia> root = q.from(EventMedia.class);
        
        if (params != null) {
            if (params.containsKey("mediaUrl")) {
                q.where(b.equal(root.get("mediaUrl"), params.get("mediaUrl")));
            }
            if (params.containsKey("id")) {
                q.where(b.equal(root.get("id"), Long.parseLong(params.get("id"))));
            }
        }
        return session.createQuery(q).getSingleResult();

    }
}
