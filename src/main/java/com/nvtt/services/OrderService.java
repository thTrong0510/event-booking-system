/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services;

import java.util.List;
import java.util.Map;

import com.nvtt.pojo.Orders;

/**
 *
 * @author lequa
 */
public interface OrderService {
    List<Orders> getOrders(Map<String, String> params);
    List<Orders> getMyOrders();
}
