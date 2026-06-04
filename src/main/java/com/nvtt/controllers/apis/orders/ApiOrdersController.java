package com.nvtt.controllers.apis.orders;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

@RestController
@RequestMapping("/api/v1")
public class ApiOrdersController {

    private static final Logger logger = LogManager.getLogger(ApiOrdersController.class);

    @Autowired
    private OrderUtils orderUtils;

    @Autowired
    private OrderService orderService;

    @GetMapping("/me/orders")
    public ResponseEntity<List<ResOrderInfoDTO>> getOrders(@RequestParam Map<String, String> params) {
        logger.info("start sql getOrders");
        List<Orders> orders = this.orderService.getOrders(params);
        List<ResOrderInfoDTO> dtos = this.orderUtils.convertToResOrderInfoDTOList(orders);
        logger.info("end sql");
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }

    @GetMapping("/me/my-orders")
    public ResponseEntity<List<ResOrderInfoDTO>> getMyOrders() {
        logger.info("start sql getMyOrders");
        List<Orders> orders = this.orderService.getMyOrders();
        List<ResOrderInfoDTO> dtos = this.orderUtils.convertToResOrderInfoDTOList(orders);
        logger.info("end sql");
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }
}
