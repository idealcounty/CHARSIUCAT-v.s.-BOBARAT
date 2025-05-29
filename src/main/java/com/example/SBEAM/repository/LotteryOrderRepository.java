package com.example.SBEAM.repository;

import com.example.SBEAM.po.LotteryOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LotteryOrderRepository extends JpaRepository<LotteryOrder, Integer> {
    LotteryOrder findByOutTradeNo(String outTradeNo);

    Optional<LotteryOrder> findById(Integer id);
}