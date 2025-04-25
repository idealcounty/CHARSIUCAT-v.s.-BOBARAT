package com.example.SBEAM.repository;

import com.example.SBEAM.po.Orders;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {
    List<Orders> findAll();
    Orders findByOrdersId(Integer OrderId);
    Orders findByOutTradeNo(String outTradeNo);
}
