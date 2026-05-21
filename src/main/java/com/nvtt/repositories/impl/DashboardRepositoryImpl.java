/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.repositories.impl;

import com.nvtt.pojo.Category;
import com.nvtt.pojo.Event;
import com.nvtt.pojo.Orders;
import com.nvtt.pojo.SystemStatisticsDaily;
import com.nvtt.pojo.User;
import com.nvtt.repositories.DashboardRepository;
import com.nvtt.utils.constants.OrderStatus;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;

/**
 *
 * @author vthan
 */
@Repository
public class DashboardRepositoryImpl implements DashboardRepository {

    @Autowired
    private LocalSessionFactoryBean sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getObject().getCurrentSession();
    }

    @Override
    public List<Map<String, Object>> getEventsCountByTime(String type, int year) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<Event> root = query.from(Event.class);

        // Tạo điều kiện lọc theo Năm (Mặc định lọc theo năm được truyền từ controller xuống)
        Expression<Integer> yearExpression = cb.function("YEAR", Integer.class, root.get("startTime"));
        Predicate filterByYear = cb.equal(yearExpression, year);

        Expression<Integer> timeExpression;
        String prefixLabel = "";

        if ("QUARTER".equalsIgnoreCase(type)) {
            timeExpression = cb.function("QUARTER", Integer.class, root.get("startTime"));
            prefixLabel = "Quý ";
        } else if ("YEAR".equalsIgnoreCase(type)) {
            timeExpression = cb.function("YEAR", Integer.class, root.get("startTime"));
            prefixLabel = "Năm ";
        } else { // Mặc định là MONTH
            timeExpression = cb.function("MONTH", Integer.class, root.get("startTime"));
            prefixLabel = "Tháng ";
        }

        query.multiselect(timeExpression, cb.count(root));

        // Nếu chọn lọc theo YEAR thì không cần ép điều kiện WHERE YEAR nữa để có cái nhìn tổng quan qua các năm
        if ("YEAR".equalsIgnoreCase(type)) {
            query.groupBy(timeExpression);
        } else {
            query.where(filterByYear);
            query.groupBy(timeExpression);
        }

        query.orderBy(cb.asc(timeExpression)); // Sắp xếp theo thứ tự thời gian tăng dần

        List<Object[]> results = getCurrentSession().createQuery(query).getResultList();
        List<Map<String, Object>> list = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("label", prefixLabel + row[0]);
            map.put("value", row[1]);
            list.add(map);
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> getStatisticsByCategory() {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);

        Root<Event> eventRoot = query.from(Event.class);
        Join<Event, Category> categoryJoin = eventRoot.join("category");
        Join<Event, Orders> orderJoin = eventRoot.join("orders");

        query.multiselect(
                categoryJoin.get("name"),
                cb.countDistinct(eventRoot.get("id")),
                cb.sum(orderJoin.get("totalAmount"))
        );
        query.where(cb.equal(orderJoin.get("status"), "CONFIRMED"));
        query.groupBy(categoryJoin.get("id"), categoryJoin.get("name"));

        List<Object[]> results = getCurrentSession().createQuery(query).getResultList();
        List<Map<String, Object>> list = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("category", row[0]);
            map.put("eventCount", row[1]);
            map.put("revenue", row[2]);
            list.add(map);
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> getTicketSalesOverTime() {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<Orders> root = query.from(Orders.class);

        // Group theo định dạng Date chuỗi: YYYY-MM-DD
        Expression<String> dateString = cb.function("DATE_FORMAT", String.class, root.get("createdAt"), cb.literal("%Y-%m-%d"));

        query.multiselect(dateString, cb.sum(root.get("quantity")));
        query.where(cb.equal(root.get("status"), "CONFIRMED"));
        query.groupBy(dateString);
        query.orderBy(cb.asc(dateString));

        List<Object[]> results = getCurrentSession().createQuery(query).getResultList();
        List<Map<String, Object>> list = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("date", row[0]);
            map.put("quantity", row[1]);
            list.add(map);
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> getRevenueByOrganizer() {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);

        Root<Event> eventRoot = query.from(Event.class);
        Join<Event, User> organizerJoin = eventRoot.join("organizer");
        Join<Event, Orders> orderJoin = eventRoot.join("orders"); // giả định mapping @OneToMany từ Event -> Order

        query.multiselect(organizerJoin.get("fullName"), cb.sum(orderJoin.get("totalAmount")));
        query.where(cb.equal(orderJoin.get("status"), "CONFIRMED"));
        query.groupBy(organizerJoin.get("id"), organizerJoin.get("fullName"));

        List<Object[]> results = getCurrentSession().createQuery(query).getResultList();
        List<Map<String, Object>> list = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("organizer", row[0]);
            map.put("revenue", row[1]);
            list.add(map);
        }
        return list;
    }
}
