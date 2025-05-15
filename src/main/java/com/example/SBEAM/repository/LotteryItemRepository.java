package com.example.SBEAM.repository;

import com.example.SBEAM.po.LotteryItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LotteryItemRepository extends JpaRepository<LotteryItem, Integer> {
    LotteryItem findByLotteryItemId(int lotteryItemId);
}
