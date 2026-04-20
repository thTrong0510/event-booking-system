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

/**
 *
 * @author vthan
 */
@Repository
@Transactional
public class RoleRepositoryImpl implements RoleRepository {

    @Autowired
    private LocalSessionFactoryBean factory;
    
    @Override
    public Role getRoleById(Long id) {
        Session session = factory.getObject().getCurrentSession();
        
        Query q = session.createNamedQuery("Role.findById", Role.class);
        q.setParameter("id", id);
        
        Role role = (Role) q.getSingleResult();
        
        return role;
    }
    
}
