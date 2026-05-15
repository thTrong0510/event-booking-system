/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nvtt.pojo.Event;
import com.nvtt.pojo.Orders;
import com.nvtt.repositories.EventRepository;
import com.nvtt.repositories.OrderRepository;
import com.nvtt.services.OrderService;
import com.nvtt.utils.EventUtils.EventUtils;
import com.nvtt.utils.UserUtils.UserUtils;
import com.nvtt.pojo.User;

/**
 *
 * @author lequa
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private EventUtils eventUtils;
    
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EventRepository eventRepository;

    @Override
    public List<Orders> getOrders(Map<String, String> params) {
        User currentUser = userUtils.getCurrentUser();
        if (params.get("eventId") != null ) {
            if (currentUser == null) {
                throw new RuntimeException("User not authenticated");
            }
            Event event = eventRepository.getEventById(Long.parseLong(params.get("eventId")));
            if (eventUtils.isOwner(event, currentUser.getId())) {
                return this.orderRepository.getOrders(params);
            } else {
                throw new RuntimeException("User not authorized to view orders for this event");
            }
        } else {
            throw new RuntimeException("Event ID is required to view orders");
        }
    }

    @Override
    public List<Orders> getMyOrders() {
        User currentUser = userUtils.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("User not authenticated");
        }
        return this.orderRepository.getOrders(Map.of("userId", currentUser.getId().toString()));
    }
}
