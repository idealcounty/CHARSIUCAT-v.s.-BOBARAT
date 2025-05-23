package com.example.SBEAM.controller;

import com.example.SBEAM.service.LotteryService;
import com.example.SBEAM.vo.LotteryItemVO;
import com.example.SBEAM.vo.LotteryVO;
import com.example.SBEAM.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lottery")
public class LotteryController {
    @Autowired
    LotteryService lotteryService;
    @PostMapping
    public ResultVO<Boolean>createLottery(@RequestBody String lotteryName) {
        return ResultVO.buildSuccess(lotteryService.createLottery(lotteryName));
    }
    @PostMapping("/{lotteryId}")
    public ResultVO<Boolean>addLotteryItem(@PathVariable int lotteryId,@RequestBody LotteryItemVO lotteryItemVO) {
        return ResultVO.buildSuccess(lotteryService.addLotteryItem(lotteryId,lotteryItemVO));
    }
    @GetMapping("/{lotteryId}")
    public ResultVO<List<LotteryItemVO>> getLotteryItemList(@PathVariable int lotteryId) {
        return ResultVO.buildSuccess(lotteryService.getLotteryItemList(lotteryId));
    }
    @GetMapping("/{lotteryId}/draw")
    public ResultVO<LotteryItemVO> drawLottery(@PathVariable int lotteryId,@RequestParam int userId) {
        return ResultVO.buildSuccess(lotteryService.drawLottery(lotteryId,userId));
    }
    @GetMapping()
    public ResultVO<List<LotteryVO>> getLotteryList() {
        return ResultVO.buildSuccess(lotteryService.getLottery());
    }
}
