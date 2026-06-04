package com.nvtt.pojo.dtos.admin;

import java.util.List;
import java.util.Map;

public class AdminDashboardDTO {

    private List<Map<String, Object>> eventsByTime;

    private List<Map<String, Object>> ticketSalesTrend;

    private List<Map<String, Object>> revenueByOrganizer;

    private List<Map<String, Object>> statisticsByCategory;

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
