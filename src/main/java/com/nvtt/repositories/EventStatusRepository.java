/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.repositories;

import java.util.Map;

import com.nvtt.pojo.EventStatus;
import java.util.List;

/**
 *
 * @author lequa
 */
public interface EventStatusRepository {

    EventStatus getStatusByName(String name);

    EventStatus addEventStatus(EventStatus eventStatus); // add and update

    EventStatus getStatusById(Long id);

    List<EventStatus> findAll();

    EventStatus findByName(String name);
}
