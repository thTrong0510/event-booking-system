/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services;

import java.util.Date;

/**
 *
 * @author vthan
 */
public interface SystemStatisticsService {

    void calculateAndSaveDailyStatistics(Date targetDate);
}
