/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.repositories.impl;

import com.nvtt.pojo.SystemStatisticsDaily;
import com.nvtt.repositories.SystemStatisticsRepository;
import com.nvtt.utils.Utilities;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;

/**
 *
 * @author vthan
 */
@Repository
public class SystemStatisticsRepositoryImpl implements SystemStatisticsRepository {

    @Autowired
    private LocalSessionFactoryBean sessionFactory;

    @Override
    public Map<String, Object> getOrderStatsByPeriod(Date start, Date end) {
        Session session = sessionFactory.getObject().getCurrentSession();
        
        String hql = "SELECT COUNT(o.id), " +
                     "COALESCE(SUM(CASE WHEN o.status = 'CONFIRMED' THEN o.totalAmount ELSE 0 END), 0) " +
                     "FROM Orders o WHERE o.createdAt >= :start AND o.createdAt <= :end";

        Object[] result = Utilities.getSafe(() -> session.createQuery(hql, Object[].class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getSingleResult());
        
        if(Objects.isNull(result)) {
            return new HashMap<>();
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalOrders", result[0] != null ? ((Long) result[0]).intValue() : 0);
        stats.put("totalRevenue", result[1] != null ? (BigDecimal) result[1] : BigDecimal.ZERO);
        return stats;
    }

    @Override
    public Long getCreatedEventsCountByPeriod(Date start, Date end) {
        Session session = sessionFactory.getObject().getCurrentSession();
        String hql = "SELECT COUNT(e.id) FROM Event e WHERE e.createdAt >= :start AND e.createdAt <= :end";
        
        return Utilities.getSafe(() -> session.createQuery(hql, Long.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getSingleResult());
    }

    @Override
    public void save(SystemStatisticsDaily statistics) {
        sessionFactory.getObject().getCurrentSession().persist(statistics);
    }
}