package com.example.SBEAM.controller;

import com.example.SBEAM.service.OrderService;
import com.example.SBEAM.vo.ResultVO;
import com.example.SBEAM.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {
    @Autowired
    OrderService orderService;


}
