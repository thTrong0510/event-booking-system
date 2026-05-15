/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.repositories;

import java.util.Map;

import com.nvtt.pojo.EventStatus;

/**
 *
 * @author lequa
 */
public interface EventStatusRepository {
    EventStatus getStatusByName(String name);
    EventStatus addEventStatus(EventStatus eventStatus); // add and update
}
