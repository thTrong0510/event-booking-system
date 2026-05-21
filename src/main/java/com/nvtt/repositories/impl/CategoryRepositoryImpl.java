/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.repositories.impl;

import com.nvtt.pojo.Category;
import com.nvtt.repositories.CategoryRepository;
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
public class CategoryRepositoryImpl implements CategoryRepository {

    @Autowired
    private LocalSessionFactoryBean sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getObject().getCurrentSession();
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
