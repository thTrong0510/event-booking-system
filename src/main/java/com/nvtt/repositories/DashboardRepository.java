package com.nvtt.repositories;

import java.util.List;
import java.util.Map;

public interface DashboardRepository {

    List<Map<String, Object>> getEventsCountByTime(String type, int year);

    List<Map<String, Object>> getTicketSalesOverTime(String type, int year);

    List<Map<String, Object>> getStatisticsByCategory(String type, int year);

    List<Map<String, Object>> getRevenueByOrganizer(String type, int year);
}
