/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.repositories.impl;

import java.util.Map;

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
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.Query;
import jakarta.persistence.NoResultException;
import java.util.Optional;

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
    public List<OrganizerVerification> getOrganizerVerifications(Map<String, String> params) {
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
    public OrganizerVerification getOrganizerVerificationById(Long id) {
        Session session = factory.getObject().getCurrentSession();

        Query q = session.createNamedQuery("OrganizerVerification.findById", OrganizerVerification.class);
        q.setParameter("id", id);

        Optional<OrganizerVerification> optionalOV;
        try {
            OrganizerVerification ov = (OrganizerVerification) q.getSingleResult();
            optionalOV = Optional.of(ov);
        } catch (NoResultException e) {
            optionalOV = Optional.empty();
        }

        if (optionalOV.isEmpty()) {
            return null;
        }

        return optionalOV.get();
    }

    @Override
    public List<OrganizerVerification> findAll(String status, String search, int offset, int limit) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        
        CriteriaQuery<OrganizerVerification> cq = cb.createQuery(OrganizerVerification.class);
        Root<OrganizerVerification> root = cq.from(OrganizerVerification.class);
        
        Join<OrganizerVerification, User> userJoin = root.join("user", JoinType.INNER);
        Join<OrganizerVerification, User> approvedByJoin = root.join("approvedBy", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if (status != null && !status.trim().isEmpty()) {
            predicates.add(cb.equal(root.get("status"), status));
        }

        if (search != null && !search.trim().isEmpty()) {
            String searchPattern = "%" + search.trim().toLowerCase() + "%";

            // Tái sử dụng biến userJoin ở trên, không tạo thêm bản join mới nữa
            Predicate searchEmail = cb.like(cb.lower(userJoin.get("email")), searchPattern);
            Predicate searchName = cb.like(cb.lower(userJoin.get("fullName")), searchPattern);

            predicates.add(cb.or(searchEmail, searchName));
        }

        // THAY THẾ cq.select(root) thành Constructor Projection chỉ định danh cột cần dùng
        cq.select(cb.construct(
                OrganizerVerification.class,
                root.get("id"),
                root.get("status"),
                root.get("createdAt"),
                root.get("approvedAt"),
                userJoin.get("avatarUrl"),
                userJoin.get("fullName"),
                userJoin.get("email"),
                approvedByJoin.get("fullName") // Trường này lấy từ bảng kết nối LEFT JOIN
        ));

        cq.where(predicates.toArray(new Predicate[0]))
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
        CriteriaQuery<Long> cq = cb.createQuery(Long.class
        );
        Root<OrganizerVerification> root = cq.from(OrganizerVerification.class
        );

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
        if (status.equals(OrganizerVerificationStatus.REJECTED.toString())) {
            enumStatus = OrganizerVerificationStatus.REJECTED;
        }

        // Cập nhật nhanh bằng HQL, chuẩn hóa thời gian và định danh admin phê duyệt
        String hql = "UPDATE OrganizerVerification ov SET ov.status = :status, "
                + "ov.approvedBy.id = :adminId, ov.approvedAt = CURRENT_TIMESTAMP WHERE ov.id = :id";
        session.createMutationQuery(hql)
                .setParameter("status", enumStatus)
                .setParameter("adminId", adminId)
                .setParameter("id", id)
                .executeUpdate();
    }
}
