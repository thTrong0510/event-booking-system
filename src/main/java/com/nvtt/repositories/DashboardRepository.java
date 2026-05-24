/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.repositories;

import java.util.List;
import java.util.Map;

/**
 *
 * @author vthan
 */
public interface DashboardRepository {

    List<Map<String, Object>> getEventsCountByTime(String type, int year); // type: MONTH, QUARTER, YEAR

    List<Map<String, Object>> getTicketSalesOverTime(String type, int year);

    List<Map<String, Object>> getStatisticsByCategory(String type, int year);

    List<Map<String, Object>> getRevenueByOrganizer(String type, int year);
}
