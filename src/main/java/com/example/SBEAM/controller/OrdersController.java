package com.example.SBEAM.controller;

import com.example.SBEAM.service.OrdersService;
import com.example.SBEAM.vo.OrdersVO;
import com.example.SBEAM.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import com.example.SBEAM.enums.OrderStatus;
@RestController
@RequestMapping("/api")
public class OrdersController {
    @Autowired
    OrdersService ordersService;

    @PostMapping("/charge")//这里对应充值
    public ResultVO<Boolean> createOrder(OrdersVO ordersVO) {
        return ResultVO.buildSuccess(ordersService.createOrders(ordersVO));
    }

    @DeleteMapping("/user/{userId}/orders/{orderId}")//暂时可能用不到
    public ResultVO<Boolean> deleteOrders(@PathVariable int orderId) {
        return ResultVO.buildSuccess(ordersService.deleteOrdersByOrderId(orderId));
    }

    @GetMapping("/orders/{ordersId}")
    public ResultVO<OrderStatus> getOrderStatus(@PathVariable int ordersId) {
        return ResultVO.buildSuccess(ordersService.getOrderStatus(ordersId));
    }

}
