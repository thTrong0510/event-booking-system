package com.nvtt.repositories.impl;

import java.util.Map;

import org.hibernate.Session;
import jakarta.persistence.NoResultException;
import java.util.Optional;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nvtt.pojo.EventMedia;
import com.nvtt.repositories.EventMediaRepository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

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
        Query<EventMedia> hQuery = session.createQuery(q);

        Optional<EventMedia> optionalMedia;
        try {
            EventMedia media = hQuery.getSingleResult();
            optionalMedia = Optional.of(media);
        } catch (NoResultException e) {
            optionalMedia = Optional.empty();
        }

        if (optionalMedia.isEmpty()) {
            return null;
        }

        return optionalMedia.get();

    }
}
