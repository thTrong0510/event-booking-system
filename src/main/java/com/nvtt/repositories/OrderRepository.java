package com.nvtt.repositories;

import java.util.List;
import java.util.Map;

import com.nvtt.pojo.Orders;

public interface OrderRepository {

    public List<Orders> getOrders(Map<String, String> params);

    public Orders getOrderById(Long id);
}
