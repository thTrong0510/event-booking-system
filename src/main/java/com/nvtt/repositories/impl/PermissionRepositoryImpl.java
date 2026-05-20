/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.repositories.impl;

import com.nvtt.pojo.Permission;
import com.nvtt.repositories.PermissionRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author vthan
 */
@Repository
@Transactional
public class PermissionRepositoryImpl implements PermissionRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    private Session getSession() {
        return factory.getObject().getCurrentSession();
    }

    @Override
    public Permission save(Permission permission) {
        if (this.exists(permission.getApiPath(), permission.getApiMethod())) {
            this.getSession().persist(permission);
        } else {
            this.getSession().merge(permission);
        }

        return permission;
    }

    @Override
    public void delete(Long id) {
        String hql = "delete from Permission p where p.id = :id";

        getSession().createQuery(hql)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public Permission findById(Long id) {
        String hql = """
        select p
        from Permission p
        where p.id = :id
    """;

        return getSession()
                .createQuery(hql, Permission.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    public List<Permission> findAll(String search, String module, String apiMethod, int offset, int limit) {
        CriteriaBuilder cb = getSession().getCriteriaBuilder();
        CriteriaQuery<Permission> cq = cb.createQuery(Permission.class);

        Root<Permission> root = cq.from(Permission.class);

        List<Predicate> predicates = new ArrayList<>();

        // Search theo name hoặc apiPath
        if (search != null && !search.isBlank()) {
            String pattern = "%" + search.toLowerCase().trim() + "%";

            predicates.add(
                    cb.or(
                            cb.like(cb.lower(root.get("name")), pattern),
                            cb.like(cb.lower(root.get("apiPath")), pattern)
                    )
            );
        }

        if (module != null && !module.isBlank()) {
            predicates.add(
                    cb.equal(root.get("module"), module)
            );
        }

        if (apiMethod != null && !apiMethod.isBlank()) {
            predicates.add(
                    cb.equal(root.get("apiMethod"), apiMethod)
            );
        }

        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        cq.orderBy(cb.asc(root.get("module")),
                cb.asc(root.get("name")));

        return getSession()
                .createQuery(cq)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public long countAll(String search, String module, String apiMethod) {
        return 0;
    }

    @Override
    public boolean exists(String apiPath, String apiMethod) {
        String hql = """
        select 1
        from Permission p
        where p.apiPath = :path
        and p.apiMethod = :method
    """;

        return !getSession()
                .createQuery(hql, Integer.class)
                .setParameter("path", apiPath)
                .setParameter("method", apiMethod)
                .setMaxResults(1)
                .getResultList()
                .isEmpty();
    }

    @Override
    public List<Permission> findByRoleId(Long roleId) {
        // Câu lệnh liên kết bảng trung gian qua quan hệ n-n (ManyToMany) trên Entity
        String hql = "SELECT p FROM Permission p JOIN p.roles r WHERE r.id = :roleId";
        return this.getSession().createQuery(hql, Permission.class)
                .setParameter("roleId", roleId)
                .getResultList();
    }

    @Override
    public List<Permission> findAll() {
        String hql = "from Permission p order by p.name asc";

        return getSession().createQuery(hql, Permission.class).getResultList();
    }

}
