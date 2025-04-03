package com.example.SBEAM.controller;

import com.example.SBEAM.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OrdersController {
    @Autowired
    OrdersService ordersService;


}
