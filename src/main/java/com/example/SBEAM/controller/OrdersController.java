package com.example.SBEAM.controller;

import com.example.SBEAM.service.OrdersService;
import com.example.SBEAM.vo.OrdersVO;
import com.example.SBEAM.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OrdersController {
    @Autowired
    OrdersService ordersService;

    @PostMapping("/users/{userId}/cart")
    public ResultVO<Boolean> createOrder(OrdersVO ordersVO) {
        return ResultVO.buildSuccess(ordersService.createOrders(ordersVO));
    }

    @DeleteMapping("/user/{userId}/orders/{orderId}")
    public ResultVO<Boolean> deleteOrders(@PathVariable int orderId) {
        return ResultVO.buildSuccess(ordersService.deleteOrdersByOrderId(orderId));
    }

}
