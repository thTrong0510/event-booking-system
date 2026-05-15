/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.repositories.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nvtt.pojo.OrganizerVerification;
import com.nvtt.repositories.OrganizerVerificationRepository;

import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

/**
 *
 * @author lequa
 */
@Repository
@Transactional
public class OrganizerVerificationRepositoryImpl implements OrganizerVerificationRepository {

    @Autowired
    private LocalSessionFactoryBean factory;
    
    @Override
    public OrganizerVerification addOrganizerVerification(OrganizerVerification organizerVerification) {
        try {
            Session s = factory.getObject().getCurrentSession();
            s.persist(organizerVerification);
            return organizerVerification;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<OrganizerVerification> getOrganizerVerifications(Map<String, String> params){
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<OrganizerVerification> q = b.createQuery(OrganizerVerification.class);
        Root<OrganizerVerification> root = q.from(OrganizerVerification.class);
        
        if (params != null) {
            if (params.containsKey("status")) {
                q.where(b.equal(root.get("status"), params.get("status")));
            }
            if (params.containsKey("id")) {
                q.where(b.equal(root.get("id"), Long.parseLong(params.get("id"))));
            }
        }
        return session.createQuery(q).getResultList();
    }

    @Override
    public OrganizerVerification getOrganizerVerificationById(Long id){
        Session session = factory.getObject().getCurrentSession();
        
        Query q = session.createNamedQuery("OrganizerVerification.findById", OrganizerVerification.class);
        q.setParameter("id", id);
        
        OrganizerVerification organizerVerification = (OrganizerVerification) q.getSingleResult();
        
        return organizerVerification;
    }
}
