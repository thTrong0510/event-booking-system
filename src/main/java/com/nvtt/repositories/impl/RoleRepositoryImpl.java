/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.repositories.impl;

import com.nvtt.pojo.Role;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.nvtt.repositories.RoleRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vthan
 */
@Repository
@Transactional
public class RoleRepositoryImpl implements RoleRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    private Session getSession() {
        return factory.getObject().getCurrentSession();
    }

    @Override
    public Role getRoleById(Long id) {
        Query q = this.getSession().createNamedQuery("Role.findById", Role.class);
        q.setParameter("id", id);

        Role role = (Role) q.getSingleResult();

        return role;
    }

    @Override
    public Role getRoleByName(String name) {
        Query q = this.getSession().createNamedQuery("Role.findByName", Role.class);
        q.setParameter("name", name);

        Role role = (Role) q.getSingleResult();

        return role;
    }

    @Override
    public Role save(Role role) {
        if (this.existsByName(role.getName())) {
            this.getSession().persist(role);
        } else {
            this.getSession().merge(role);
        }
        return role;
    }

    @Override
    public void delete(Long id) {
        String hql = "delete from Role r where r.id = :id";
        getSession().createQuery(hql)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public boolean existsByName(String name) {
        String hql = """
        select 1
        from Role r
        where lower(r.name) = :name
    """;

        return !getSession()
                .createQuery(hql, Integer.class)
                .setParameter("name", name.toLowerCase())
                .setMaxResults(1)
                .getResultList()
                .isEmpty();
    }

    @Override
    public List<Role> findAll(String search, Boolean isActive, int offset, int limit) {
        CriteriaBuilder cb = getSession().getCriteriaBuilder();
        CriteriaQuery<Role> cq = cb.createQuery(Role.class);

        Root<Role> root = cq.from(Role.class);

        List<Predicate> predicates = new ArrayList<>();

        if (search != null && !search.isBlank()) {
            String pattern = "%" + search.toLowerCase().trim() + "%";
            predicates.add(
                    cb.like(cb.lower(root.get("name")), pattern)
            );
        }

        if (isActive != null) {
            predicates.add(
                    cb.equal(root.get("isActive"), isActive)
            );
        }

        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        cq.orderBy(cb.asc(root.get("name")));

        return getSession()
                .createQuery(cq)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public List<Role> findAll() {

        String hql = "from Role r order by r.name asc";

        return getSession()
                .createQuery(hql, Role.class)
                .getResultList();
    }

}
