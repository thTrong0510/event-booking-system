/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.pojo.dtos.admin;

import java.util.List;
import java.util.Map;

/**
 *
 * @author vthan
 */
public class AdminDashboardDTO {

    // 1. Thống kê sự kiện theo thời gian
    private List<Map<String, Object>> eventsByTime;

    // 2. Tần suất bán vé theo thời gian
    private List<Map<String, Object>> ticketSalesTrend;

    // 3. Doanh thu theo nhà tổ chức
    private List<Map<String, Object>> revenueByOrganizer;

    // 4. Thống kê theo lĩnh vực sự kiện
    private List<Map<String, Object>> statisticsByCategory;

    // Getters and Setters
    public List<Map<String, Object>> getEventsByTime() {
        return eventsByTime;
    }

    public void setEventsByTime(List<Map<String, Object>> eventsByTime) {
        this.eventsByTime = eventsByTime;
    }

    public List<Map<String, Object>> getTicketSalesTrend() {
        return ticketSalesTrend;
    }

    public void setTicketSalesTrend(List<Map<String, Object>> ticketSalesTrend) {
        this.ticketSalesTrend = ticketSalesTrend;
    }

    public List<Map<String, Object>> getRevenueByOrganizer() {
        return revenueByOrganizer;
    }

    public void setRevenueByOrganizer(List<Map<String, Object>> revenueByOrganizer) {
        this.revenueByOrganizer = revenueByOrganizer;
    }

    public List<Map<String, Object>> getStatisticsByCategory() {
        return statisticsByCategory;
    }

    public void setStatisticsByCategory(List<Map<String, Object>> statisticsByCategory) {
        this.statisticsByCategory = statisticsByCategory;
    }
}
