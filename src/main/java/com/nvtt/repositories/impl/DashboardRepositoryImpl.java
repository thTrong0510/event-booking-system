/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.repositories.impl;

import com.nvtt.pojo.SystemStatisticsDaily;
import com.nvtt.repositories.DashboardRepository;
import com.nvtt.utils.constants.OrderStatus;
import java.math.BigDecimal;
import java.util.Date;
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
public class DashboardRepositoryImpl implements DashboardRepository {

    @Autowired
    private LocalSessionFactoryBean sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getObject().getCurrentSession();
    }

    @Override
    public Long countActiveEvents() {
        // Lấy các sự kiện đang mở thông qua trạng thái active (ví dụ id trạng thái đang mở = 1)
        // Bạn có thể chỉnh sửa lại điều kiện logic theo bảng event_status của bạn
        String hql = "SELECT COUNT(e.id) FROM Event e WHERE e.status.name = 'OPEN' OR e.status.id = 1";
        return getCurrentSession().createQuery(hql, Long.class).uniqueResult();
    }

    @Override
    public BigDecimal sumTotalRevenue() {
        String hql = """
        SELECT COALESCE(SUM(o.totalAmount), 0)
        FROM Orders o
        WHERE o.status = :status
        """;

        return getCurrentSession()
                .createQuery(hql, BigDecimal.class)
                .setParameter("status", OrderStatus.CONFIRMED)
                .uniqueResult();
    }

    @Override
    public Long countTotalTicketsSold() {
        // Tần suất bán vé: Tính tổng tất cả số vé đã được phát hành trong hệ thống
        String hql = "SELECT COUNT(t.id) FROM Ticket t";
        return getCurrentSession().createQuery(hql, Long.class).uniqueResult();
    }

    @Override
    public Long countPendingOrganizers() {
        // Số lượng đối tác nhà tổ chức đang nằm trong hàng đợi chờ duyệt PENDING
        String hql = "SELECT COUNT(ov.id) FROM OrganizerVerification ov WHERE ov.status = 'PENDING'";
        return getCurrentSession().createQuery(hql, Long.class).uniqueResult();
    }

    @Override
    public List<SystemStatisticsDaily> getDailyStatistics(Date startDate, Date endDate) {
        // Truy vấn dữ liệu lịch sử từ bảng tổng hợp báo cáo ngày theo dải thời gian lựa chọn
        String hql = "FROM SystemStatisticsDaily ssd WHERE ssd.statDate BETWEEN :startDate AND :endDate ORDER BY ssd.statDate ASC";
        return getCurrentSession().createQuery(hql, SystemStatisticsDaily.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }
}
