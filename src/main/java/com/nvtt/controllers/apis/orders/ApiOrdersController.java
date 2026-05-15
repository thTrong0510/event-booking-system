/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.controllers.apis.orders;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nvtt.pojo.Orders;
import com.nvtt.pojo.dtos.order.ResOrderInfoDTO;
import com.nvtt.services.OrderService;
import com.nvtt.utils.OrderUtils.OrderUtils;

/**
 *
 * @author lequa
 */
@RestController
@RequestMapping("/api")
public class ApiOrdersController {

    @Autowired
    private OrderUtils orderUtils;

    @Autowired
    private OrderService orderService;
    
    @GetMapping("secure/orders")
    public ResponseEntity<List<ResOrderInfoDTO>> getOrders(@RequestParam Map<String, String> params) {
        try {
            List<Orders> orders = this.orderService.getOrders(params);
            List<ResOrderInfoDTO> dtos = this.orderUtils.convertToResOrderInfoDTOList(orders);
            return ResponseEntity.status(HttpStatus.OK).body(dtos);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("secure/my-orders")
    public ResponseEntity<List<ResOrderInfoDTO>> getMyOrders() {
        try {
            List<Orders> orders = this.orderService.getMyOrders();
            List<ResOrderInfoDTO> dtos = this.orderUtils.convertToResOrderInfoDTOList(orders);
            return ResponseEntity.status(HttpStatus.OK).body(dtos);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}