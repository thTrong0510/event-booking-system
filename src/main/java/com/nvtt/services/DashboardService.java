package com.nvtt.services;

import com.nvtt.pojo.dtos.admin.AdminDashboardDTO;

public interface DashboardService {

    AdminDashboardDTO getDashboardData(String timeFilter, int year);
}
