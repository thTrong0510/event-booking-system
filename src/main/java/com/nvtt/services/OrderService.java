package com.nvtt.services;

import java.util.List;
import java.util.Map;

import com.nvtt.pojo.Orders;

public interface OrderService {

    List<Orders> getOrders(Map<String, String> params);

    List<Orders> getMyOrders();

    Orders getOrderById(Long Id);
}
