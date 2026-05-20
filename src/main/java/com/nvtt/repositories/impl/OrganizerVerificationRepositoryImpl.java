/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.repositories.impl;

import com.nvtt.pojo.OrganizerVerification;
import com.nvtt.pojo.User;
import com.nvtt.repositories.OrganizerVerificationRepository;
import com.nvtt.utils.constants.OrganizerVerificationStatus;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;

/**
 *
 * @author vthan
 */
@Repository
public class OrganizerVerificationRepositoryImpl implements OrganizerVerificationRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<OrganizerVerification> findAll(String status, String search, int offset, int limit) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<OrganizerVerification> cq
                = cb.createQuery(OrganizerVerification.class);

        Root<OrganizerVerification> root
                = cq.from(OrganizerVerification.class);

        root.fetch("user", JoinType.INNER);
        root.fetch("approvedBy", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if (status != null && !status.trim().isEmpty()) {
            predicates.add(cb.equal(root.get("status"), status));
        }

        if (search != null && !search.trim().isEmpty()) {

            String searchPattern = "%" + search.trim().toLowerCase() + "%";

            Join<OrganizerVerification, User> userJoin
                    = root.join("user", JoinType.INNER);

            Predicate searchEmail
                    = cb.like(cb.lower(userJoin.get("email")), searchPattern);

            Predicate searchName
                    = cb.like(cb.lower(userJoin.get("fullName")), searchPattern);

            predicates.add(cb.or(searchEmail, searchName));
        }

        cq.select(root)
                .where(predicates.toArray(new Predicate[0]))
                .orderBy(cb.desc(root.get("createdAt")))
                .distinct(true);

        return session.createQuery(cq)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public long countAll(String status, String search) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<OrganizerVerification> root = cq.from(OrganizerVerification.class);

        List<Predicate> predicates = new ArrayList<>();

        if (status != null && !status.trim().isEmpty()) {
            predicates.add(cb.equal(root.get("status"), status));
        }

        if (search != null && !search.trim().isEmpty()) {
            String searchPattern = "%" + search.trim().toLowerCase() + "%";
            Join<OrganizerVerification, User> userJoin = root.join("user", JoinType.INNER);
            Predicate searchEmail = cb.like(cb.lower(userJoin.get("email")), searchPattern);
            Predicate searchName = cb.like(cb.lower(userJoin.get("fullName")), searchPattern);
            predicates.add(cb.or(searchEmail, searchName));
        }

        cq.select(cb.count(root)).where(predicates.toArray(new Predicate[0]));
        return session.createQuery(cq).getSingleResult();
    }

    @Override
    public OrganizerVerification findById(Long id) {
        Session session = factory.getObject().getCurrentSession();
        return session.get(OrganizerVerification.class, id);
    }

    @Override
    public void updateStatus(Long id, String status, Long adminId) {
        Session session = factory.getObject().getCurrentSession();
        
        OrganizerVerificationStatus enumStatus = OrganizerVerificationStatus.APPROVED;
        if(status.equals(OrganizerVerificationStatus.REJECTED.toString())) {
            enumStatus = OrganizerVerificationStatus.REJECTED;
        }
        

        // Cập nhật nhanh bằng HQL, chuẩn hóa thời gian và định danh admin phê duyệt
        String hql = "UPDATE OrganizerVerification ov SET ov.status = :status, "
                + "ov.approvedBy.id = :adminId, ov.approvedAt = CURRENT_TIMESTAMP WHERE ov.id = :id";
        session.createQuery(hql)
                .setParameter("status", enumStatus)
                .setParameter("adminId", adminId)
                .setParameter("id", id)
                .executeUpdate();
    }
}
