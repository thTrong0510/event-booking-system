/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.repositories;

import com.nvtt.pojo.SystemStatisticsDaily;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author vthan
 */
public interface SystemStatisticsRepository {

    Map<String, Object> getOrderStatsByPeriod(Date start, Date end);

    Long getCreatedEventsCountByPeriod(Date start, Date end);

    void save(SystemStatisticsDaily statistics);
}
