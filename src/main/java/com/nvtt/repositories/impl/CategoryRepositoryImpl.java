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

import com.nvtt.pojo.Category;
import com.nvtt.repositories.CategoryRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Map;

/**
 *
 * @author lequa
 */
@Repository
@Transactional
public class CategoryRepositoryImpl implements CategoryRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    private Session getCurrentSession() {
        return factory.getObject().getCurrentSession();
    }

    @Override
    public Category getCategoryByName(String name) {
        Session session = this.factory.getObject().getCurrentSession();
        Category category = session.createNamedQuery("Category.findByName", Category.class)
                .setParameter("name", name)
                .getSingleResult();
        return category;
    }

    @Override
    public Category getCategoryById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        Category category = session.createNamedQuery("Category.findById", Category.class)
                .setParameter("id", id)
                .getSingleResult();
        return category;
    }

    @Override
    public Category addCategory(Category category) {
        try {
            Session s = factory.getObject().getCurrentSession();
            if (category.getId() != null) {
                s.merge(category);
            } else {
                s.persist(category);
            }
            return category;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteCategory(Category category) {
        try {
            Session s = factory.getObject().getCurrentSession();
            s.remove(category);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Category> getCategory(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Category> q = b.createQuery(Category.class);
        Root<Category> root = q.from(Category.class);

        if (params != null) {
            if (params.containsKey("name")) {
                q.where(b.equal(root.get("name"), params.get("name")));
            }
            if (params.containsKey("id")) {
                q.where(b.equal(root.get("id"), Long.parseLong(params.get("id"))));
            }
        }
        return session.createQuery(q).getResultList();
    }

    @Override
    public List<Category> findAll() {
        // Sắp xếp theo tên từ A-Z để hiển thị trên thẻ select mượt mà hơn
        String hql = "FROM Category c ORDER BY c.name ASC";
        return this.getCurrentSession().createQuery(hql, Category.class).getResultList();
    }

    @Override
    public Category findById(Long id) {
        return this.getCurrentSession().get(Category.class, id);
    }
    
}
