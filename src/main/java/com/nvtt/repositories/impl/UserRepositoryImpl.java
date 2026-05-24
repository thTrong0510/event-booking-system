/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.repositories.impl;

import com.nvtt.pojo.Role;
import com.nvtt.pojo.User;
import com.nvtt.repositories.UserRepository;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author huu-thanhduong
 */
@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User getUserByEmail(String email) {
        Session s = factory.getObject().getCurrentSession();

        Query query = s.createNamedQuery("User.findByEmail", User.class);
        query.setParameter("email", email);

        Optional<User> optionalUser;
        try {
            User user = (User) query.getSingleResult();
            optionalUser = Optional.of(user);
        } catch (NoResultException ex) {
            optionalUser = Optional.empty();
        }

        if (optionalUser.isEmpty()) {
            return null;
        }

        return optionalUser.get();
    }

    @Override
    public User getUserById(Long id) {
        Session s = factory.getObject().getCurrentSession();

        Query query = s.createNamedQuery("User.findById", User.class);
        query.setParameter("id", id);

        Optional<User> optionalUser;

        try {
            User user = (User) query.getSingleResult();
            optionalUser = Optional.of(user);
        } catch (NoResultException e) {
            optionalUser = Optional.empty();
        }

        if (optionalUser.isEmpty()) {
            return null;
        }

        return optionalUser.get();
    }

    @Override
    public User toggleStatus(Long userId) {

        Session s = factory.getObject().getCurrentSession();

        User user = s.get(User.class, userId);

        if (user == null) {
            return null;
        }

        user.setIsActive(!user.getIsActive());

        s.update(user);

        return user;
    }

    public void addUser(User user) {
        Session s = factory.getObject().getCurrentSession();
        if (user.getId() == null) {
            s.persist(user);
        } else {
            s.merge(user);
        }
    }

    @Override
    public void updateRole(Long userId, Long roleId) {
        Session s = factory.getObject().getCurrentSession();
        // Gom câu lệnh HQL tác động database về đúng vị trí hạ tầng Repository
        String hql = "UPDATE User u SET u.role.id = :roleId, u.updatedAt = CURRENT_TIMESTAMP WHERE u.id = :userId";
        s.createQuery(hql)
                .setParameter("roleId", roleId)
                .setParameter("userId", userId)
                .executeUpdate();
    }

    @Override
    public long countAll(String search, Long roleId) {
        Session s = factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        Root<User> root = cq.from(User.class);

        // select count(*)
        cq.select(cb.count(root));

        List<Predicate> predicates = new ArrayList<>();

        // search theo username hoặc email
        if (search != null && !search.trim().isEmpty()) {
            String pattern = "%" + search.toLowerCase() + "%";

            Predicate usernameLike
                    = cb.like(cb.lower(root.get("username")), pattern);

            Predicate emailLike
                    = cb.like(cb.lower(root.get("email")), pattern);

            predicates.add(cb.or(usernameLike, emailLike));
        }

        // filter theo roleId
        if (roleId != null) {
            Join<User, Role> roleJoin = root.join("role");
            predicates.add(cb.equal(roleJoin.get("id"), roleId));
        }

        // where
        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        return s.createQuery(cq).getSingleResult();
    }

    @Override
    public boolean authenticate(String email, String password) {
        User u = this.getUserByEmail(email);
        return this.passwordEncoder.matches(password, u.getPassword());
    }

    @Override
    public List<User> findAll(String search, Long roleId, int offset, int limit) {
        Session s = factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);

        Root<User> root = cq.from(User.class);

        // Fetch join để tránh N+1
        root.fetch("role", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        // Search theo username hoặc email
        if (search != null && !search.trim().isEmpty()) {
            String pattern = "%" + search.trim().toLowerCase() + "%";

            Predicate usernameLike
                    = cb.like(cb.lower(root.get("username")), pattern);

            Predicate emailLike
                    = cb.like(cb.lower(root.get("email")), pattern);

            predicates.add(cb.or(usernameLike, emailLike));
        }

        // Filter theo roleId
        if (roleId != null) {
            predicates.add(
                    cb.equal(root.get("role").get("id"), roleId)
            );
        }

        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        // Sắp xếp mới nhất trước
        cq.orderBy(cb.desc(root.get("createdAt")));

        return s.createQuery(cq)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();

    }

    public boolean checkExistEmail(String email) {
        Session s = factory.getObject().getCurrentSession();

        String hql = "select count(u.id) from User u where lower(u.email) = :email";

        Long count = s.createQuery(hql, Long.class)
                .setParameter("email", email.toLowerCase())
                .getSingleResult();

        return count > 0;
    }

    @Override
    public List<User> findByRoleName(String roleName) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);

        // Gốc truy vấn từ bảng User
        Root<User> root = cq.from(User.class);

        // Sử dụng FETCH JOIN để nạp luôn thông tin Role (Tránh lỗi N+1 và tối ưu Performance)
        Fetch<User, Role> roleFetch = root.fetch("role", JoinType.INNER);
        Join<User, Role> roleJoin = (Join<User, Role>) roleFetch;

        // Xử lý điều kiện LIKE tương đối: %roleName%
        // Ép cả 2 đầu về chữ thường (LOWER) để tìm kiếm không phân biệt hoa thường
        String pattern = "%" + roleName.trim().toLowerCase() + "%";
        Predicate likePredicate = cb.like(cb.lower(roleJoin.get("name")), pattern);

        cq.where(likePredicate);
        cq.orderBy(cb.asc(root.get("fullName"))); // Sắp xếp theo bảng chữ cái từ A-Z

        return session.createQuery(cq).getResultList();
    }

    @Override
    public boolean checkActiveAccount(String email) {
        User user = this.getUserByEmail(email);

        return user.getIsActive();
    }
}
