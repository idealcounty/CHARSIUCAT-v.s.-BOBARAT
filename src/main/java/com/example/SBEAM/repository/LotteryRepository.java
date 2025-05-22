package com.example.SBEAM.repository;

import com.example.SBEAM.po.Lottery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LotteryRepository extends JpaRepository<Lottery,Integer> {
    Lottery findByLotteryId(int lotteryId);
}
