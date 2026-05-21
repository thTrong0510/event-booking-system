/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services;

import com.nvtt.pojo.EventStatus;
import java.util.List;

/**
 *
 * @author vthan
 */
public interface EventStatusService {
    List<EventStatus> getAllStatuses();
    EventStatus getByName(String name);
}
