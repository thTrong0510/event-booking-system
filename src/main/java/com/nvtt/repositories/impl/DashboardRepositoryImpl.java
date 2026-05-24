/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.repositories.impl;

import com.nvtt.pojo.Category;
import com.nvtt.pojo.Event;
import com.nvtt.pojo.Orders;
import com.nvtt.pojo.User;
import com.nvtt.repositories.DashboardRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
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

    private String getPrefixLabel(String type) {
        if ("QUARTER".equalsIgnoreCase(type)) {
            return "Quý ";
        }
        if ("YEAR".equalsIgnoreCase(type)) {
            return "Năm ";
        }
        return "Tháng ";
    }

    private Expression<Integer> getTimeExpression(CriteriaBuilder cb, Expression<?> dateField, String type) {
        if ("QUARTER".equalsIgnoreCase(type)) {
            return cb.function("QUARTER", Integer.class, dateField);
        }
        if ("YEAR".equalsIgnoreCase(type)) {
            return cb.function("YEAR", Integer.class, dateField);
        }
        return cb.function("MONTH", Integer.class, dateField);
    }

    private List<Map<String, Object>> executeAndFormatResult(CriteriaQuery<Object[]> query, String prefix) {
        List<Object[]> results = getCurrentSession().createQuery(query).getResultList();
        List<Map<String, Object>> list = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("label", prefix + row[0]); // Đổi sang 'label' để đồng bộ bóc tách ở JS Frontend
            map.put("value", row[1] != null ? row[1] : 0);
            list.add(map);
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> getEventsCountByTime(String type, int year) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<Event> root = query.from(Event.class);

        Expression<Integer> timeExpr = getTimeExpression(cb, root.get("startTime"), type);
        query.multiselect(timeExpr, cb.count(root));

        List<Predicate> predicates = new ArrayList<>();
        if (!"YEAR".equalsIgnoreCase(type)) {
            predicates.add(cb.equal(cb.function("YEAR", Integer.class, root.get("startTime")), year));
        }
        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[0]));
        }

        query.groupBy(timeExpr);
        query.orderBy(cb.asc(timeExpr));

        return executeAndFormatResult(query, getPrefixLabel(type));
    }

    @Override
    public List<Map<String, Object>> getTicketSalesOverTime(String type, int year) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<Orders> root = query.from(Orders.class);

        Expression<Integer> timeExpr = getTimeExpression(cb, root.get("createdAt"), type);
        // Sum số lượng vé bán được trong khoảng thời gian đó
        query.multiselect(timeExpr, cb.sum(root.get("quantity")));

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("status"), "CONFIRMED")); // Chỉ tính đơn thành công

        if (!"YEAR".equalsIgnoreCase(type)) {
            predicates.add(cb.equal(cb.function("YEAR", Integer.class, root.get("createdAt")), year));
        }
        query.where(predicates.toArray(new Predicate[0]));

        query.groupBy(timeExpr);
        query.orderBy(cb.asc(timeExpr));

        return executeAndFormatResult(query, getPrefixLabel(type));
    }

    // 3. BIỂU ĐỒ: TỶ LỆ DOANH THU THEO LĨNH VỰC (Lọc theo Thời gian đã chọn)
    @Override
    public List<Map<String, Object>> getStatisticsByCategory(String type, int year) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);

        Root<Orders> root = query.from(Orders.class);
        Join<Orders, Event> eventJoin = root.join("event");
        Join<Event, Category> categoryJoin = eventJoin.join("category");

        query.multiselect(categoryJoin.get("name"), cb.sum(root.get("totalAmount")));

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("status"), "CONFIRMED"));

        if (!"YEAR".equalsIgnoreCase(type)) {
            Expression<Integer> orderYear = cb.function("YEAR", Integer.class, root.get("createdAt"));
            Expression<Integer> orderTime = getTimeExpression(cb, root.get("createdAt"), type);
            predicates.add(cb.equal(orderYear, year));
        }
        query.where(predicates.toArray(new Predicate[0]));
        query.groupBy(categoryJoin.get("name"));

        List<Object[]> results = getCurrentSession().createQuery(query).getResultList();
        List<Map<String, Object>> list = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("category", row[0]);
            map.put("revenue", row[1] != null ? row[1] : 0);
            list.add(map);
        }
        return list;
    }

    // 4. BIỂU ĐỒ: TOP DOANH THU THEO NHÀ TỔ CHỨC (Lọc theo Thời gian đã chọn)
    @Override
    public List<Map<String, Object>> getRevenueByOrganizer(String type, int year) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);

        Root<Orders> root = query.from(Orders.class);
        Join<Orders, Event> eventJoin = root.join("event");
        Join<Event, User> organizerJoin = eventJoin.join("organizer"); // organizer_id liên kết bảng users

        query.multiselect(organizerJoin.get("fullName"), cb.sum(root.get("totalAmount")));

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("status"), "CONFIRMED"));

        if (!"YEAR".equalsIgnoreCase(type)) {
            predicates.add(cb.equal(cb.function("YEAR", Integer.class, root.get("createdAt")), year));
        }
        query.where(predicates.toArray(new Predicate[0]));
        query.groupBy(organizerJoin.get("fullName"));

        // Sắp xếp nhà tổ chức kiếm nhiều tiền nhất lên đầu
        query.orderBy(cb.desc(cb.sum(root.get("totalAmount"))));

        List<Object[]> results = getCurrentSession().createQuery(query).setMaxResults(5).getResultList(); // Lấy Top 5
        List<Map<String, Object>> list = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("organizer", row[0]);
            map.put("revenue", row[1] != null ? row[1] : 0);
            list.add(map);
        }
        return list;
    }
}
