/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services;

import com.nvtt.pojo.dtos.admin.AdminDashboardDTO;

/**
 *
 * @author vthan
 */
public interface DashboardService {
    AdminDashboardDTO getDashboardData(String timeFilter, int year);
}
