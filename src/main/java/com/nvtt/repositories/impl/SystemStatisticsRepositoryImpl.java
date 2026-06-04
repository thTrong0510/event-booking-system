package com.nvtt.repositories.impl;

import com.nvtt.pojo.SystemStatisticsDaily;
import com.nvtt.repositories.SystemStatisticsRepository;
import com.nvtt.utils.Utilities;
import jakarta.persistence.NoResultException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;

@Repository
public class SystemStatisticsRepositoryImpl implements SystemStatisticsRepository {

    @Autowired
    private LocalSessionFactoryBean sessionFactory;

    @Override
    public Map<String, Object> getOrderStatsByPeriod(Date start, Date end) {
        Session session = sessionFactory.getObject().getCurrentSession();

        String hql = "SELECT COUNT(o.id), "
                + "COALESCE(SUM(CASE WHEN o.status = 'CONFIRMED' THEN o.totalAmount ELSE 0 END), 0) "
                + "FROM Orders o WHERE o.createdAt >= :start AND o.createdAt <= :end";

        Optional<Object[]> optionalResult;

        try {
            Object[] result = session.createQuery(hql, Object[].class)
                    .setParameter("start", start)
                    .setParameter("end", end)
                    .getSingleResult();
            optionalResult = Optional.of(result);
        } catch (NoResultException e) {
            optionalResult = Optional.empty();
        }

        if (optionalResult.isEmpty()) {
            return new HashMap<>();
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalOrders", optionalResult.get()[0] != null ? ((Long) optionalResult.get()[0]).intValue() : 0);
        stats.put("totalRevenue", optionalResult.get()[1] != null ? (BigDecimal) optionalResult.get()[1] : BigDecimal.ZERO);
        return stats;
    }

    @Override
    public Long getCreatedEventsCountByPeriod(Date start, Date end) {
        Session session = sessionFactory.getObject().getCurrentSession();
        String hql = "SELECT COUNT(e.id) FROM Event e WHERE e.createdAt >= :start AND e.createdAt <= :end";

        return session.createQuery(hql, Long.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getSingleResult();
    }

    @Override
    public void save(SystemStatisticsDaily statistics) {
        sessionFactory.getObject().getCurrentSession().persist(statistics);
    }

    @Override
    public SystemStatisticsDaily getHistoricalSummary(Date startDate, Date endDate) {
        String hql = "SELECT COALESCE(SUM(s.totalOrders), 0), "
                + "COALESCE(SUM(s.totalRevenue), 0), "
                + "COALESCE(SUM(s.totalEvents), 0) "
                + "FROM SystemStatisticsDaily s WHERE 1=1 ";

        if (startDate != null) {
            hql += "AND s.statDate >= :startDate ";
        }
        if (endDate != null) {
            hql += "AND s.statDate <= :endDate ";
        }

        var query = sessionFactory.getObject().getCurrentSession().createQuery(hql);
        if (startDate != null) {
            query.setParameter("startDate", startDate);
        }
        if (endDate != null) {
            query.setParameter("endDate", endDate);
        }

        Object[] result = (Object[]) query.getSingleResult();

        SystemStatisticsDaily summary = new SystemStatisticsDaily();
        summary.setTotalOrders(((Long) result[0]).intValue());
        summary.setTotalRevenue((BigDecimal) result[1]);
        summary.setTotalEvents(((Long) result[2]).intValue());
        return summary;
    }

    @Override
    public Map<String, Object> getLiveOrderSummaryToday(Date startToday, Date endToday) {
        String hql = "SELECT COUNT(o.id), "
                + "COALESCE(SUM(CASE WHEN o.status = 'CONFIRMED' THEN o.totalAmount ELSE 0 END), 0) "
                + "FROM Orders o WHERE o.createdAt >= :start AND o.createdAt <= :end";

        Object[] result = sessionFactory.getObject().getCurrentSession().createQuery(hql, Object[].class)
                .setParameter("start", startToday)
                .setParameter("end", endToday)
                .getSingleResult();

        Map<String, Object> map = new HashMap<>();
        map.put("totalOrders", result[0] != null ? ((Long) result[0]).intValue() : 0);
        map.put("totalRevenue", result[1] != null ? (BigDecimal) result[1] : BigDecimal.ZERO);
        return map;
    }

    @Override
    public Long getLiveEventCountToday(Date startToday, Date endToday) {
        String hql = "SELECT COUNT(e.id) FROM Event e WHERE e.createdAt >= :start AND e.createdAt <= :end";
        return sessionFactory.getObject().getCurrentSession().createQuery(hql, Long.class)
                .setParameter("start", startToday)
                .setParameter("end", endToday)
                .getSingleResult();
    }
}
