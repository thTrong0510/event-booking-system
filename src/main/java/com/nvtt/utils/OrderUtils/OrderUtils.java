/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.utils.OrderUtils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nvtt.pojo.Orders;
import com.nvtt.pojo.User;
import com.nvtt.pojo.dtos.order.ResOrderInfoDTO;
import com.nvtt.utils.EventUtils.EventUtils;
import com.nvtt.utils.UserUtils.UserUtils;

/**
 *
 * @author lequa
 */
@Component
public class OrderUtils {

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private EventUtils eventUtils;

    public ResOrderInfoDTO convertToResOrderInfoDTO(Orders order) {
        ResOrderInfoDTO dto = new ResOrderInfoDTO();
        User user = order.getUser();
        dto.setUser(userUtils.convertToResUserInfoDTO(user));
        dto.setEvent(eventUtils.convertToResEventInfoDTO(order.getEvent()));
        dto.setQuantity(order.getQuantity());
        dto.setUnitPrice(order.getUnitPrice());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus().name());
        return dto;
    }

    public List<ResOrderInfoDTO> convertToResOrderInfoDTOList(List<Orders> orders) {
        List<ResOrderInfoDTO> dtos = new ArrayList<>();
        for (Orders order : orders) {
            dtos.add(this.convertToResOrderInfoDTO(order));
        }
        return dtos;
    }
}
