/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services;

import com.nvtt.pojo.SystemStatisticsDaily;
import com.nvtt.pojo.dtos.admin.AdminDashboardDTO;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author vthan
 */
public interface DashboardService {
    AdminDashboardDTO getDashboardData(String timeFilter, int year);
}
