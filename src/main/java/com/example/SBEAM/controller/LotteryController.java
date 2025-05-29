package com.example.SBEAM.controller;

import com.example.SBEAM.po.LotteryItem;
import com.example.SBEAM.po.LotteryOrder;
import com.example.SBEAM.enums.OrderStatus;
import com.example.SBEAM.repository.LotteryOrderRepository;
import com.example.SBEAM.service.LotteryService;
import com.example.SBEAM.vo.LotteryVO;
import com.example.SBEAM.vo.LotteryItemVO;
import com.example.SBEAM.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lottery")
public class LotteryController {
    @Autowired
    LotteryService lotteryService;

    @Autowired
    private LotteryOrderRepository lotteryOrderRepository;

    @PostMapping
    public ResultVO<LotteryVO> createLottery(@RequestParam String lotteryName) {
        return ResultVO.buildSuccess(lotteryService.createLottery(lotteryName));
    }

    @PostMapping("/createLotteryItem")
    public ResultVO<LotteryItemVO> createLotteryItem(@RequestParam int productId, @RequestParam int productQuantity) {
        return ResultVO.buildSuccess(lotteryService.createLotteryItem(productId, productQuantity));
    }

    @PostMapping("/{lotteryId}")
    public ResultVO<Boolean> addLotteryItem(@PathVariable int lotteryId, @RequestBody LotteryItemVO lotteryItemVO) {
        return ResultVO.buildSuccess(lotteryService.addLotteryItem(lotteryId, lotteryItemVO));
    }

    @DeleteMapping("/{lotteryId}")
    public ResultVO<Boolean> deleteLottery(@PathVariable int lotteryId) {
        boolean success = lotteryService.deleteLottery(lotteryId);
        if (success) {
            return ResultVO.buildSuccess(true);
        } else {
            return ResultVO.buildFailure("奖池不存在，删除失败");
        }
    }

    @DeleteMapping("/item/{lotteryItemId}")
    public ResultVO<Boolean> deleteLotteryItem(@PathVariable int lotteryItemId) {
        boolean success = lotteryService.deleteLotteryItem(lotteryItemId);
        if (success) {
            return ResultVO.buildSuccess(true);
        } else {
            return ResultVO.buildFailure("奖品不存在，删除失败");
        }
    }

    @DeleteMapping("/{lotteryId}/item/{lotteryItemId}")
    public ResultVO<Boolean> removeLotteryItemFromLottery(@PathVariable int lotteryId,
            @PathVariable int lotteryItemId) {
        boolean success = lotteryService.removeLotteryItemFromLottery(lotteryId, lotteryItemId);
        if (success) {
            return ResultVO.buildSuccess(true);
        } else {
            return ResultVO.buildFailure("奖池或奖品不存在，移除失败");
        }
    }

    @PutMapping("/{lotteryId}/name")
    public ResultVO<Boolean> updateLotteryName(@PathVariable int lotteryId, @RequestParam String newName) {
        boolean success = lotteryService.updateLotteryName(lotteryId, newName);
        if (success) {
            return ResultVO.buildSuccess(true);
        } else {
            return ResultVO.buildFailure("奖池不存在，更新失败");
        }
    }

    @GetMapping("/{lotteryId}")
    public ResultVO<List<LotteryItemVO>> getLotteryItemList(@PathVariable int lotteryId) {
        return ResultVO.buildSuccess(lotteryService.getLotteryItemList(lotteryId));
    }

    @GetMapping("/{lotteryId}/draw")
    public ResultVO<LotteryItemVO> drawLottery(@PathVariable int lotteryId, @RequestParam int userId) {
        return ResultVO.buildSuccess(lotteryService.drawLottery(lotteryId, userId));
    }

    @GetMapping()
    public ResultVO<List<LotteryVO>> getLotteryList() {
        return ResultVO.buildSuccess(lotteryService.getLottery());
    }

    @PostMapping("/createOrder")
    public ResultVO<Map<String, Object>> createLotteryOrder(@RequestBody Map<String, Object> params) {
        Integer userId = (Integer) params.get("userId");
        Integer lotteryChancesCount = (Integer) params.get("lotteryChancesCount");
        Double totalPrice = ((Number) params.get("totalPrice")).doubleValue();

        LotteryOrder lotteryOrder = new LotteryOrder(userId, lotteryChancesCount, totalPrice);
        lotteryOrder.setOrderStatus(OrderStatus.PENDING);
        lotteryOrder.setCreateTime(new Date());
        lotteryOrder = lotteryOrderRepository.save(lotteryOrder);

        Map<String, Object> result = new java.util.HashMap<>();
        result.put("lotteryOrderId", lotteryOrder.getLotteryOrderId());
        result.put("totalPrice", lotteryOrder.getTotalPrice());
        result.put("lotteryChancesCount", lotteryOrder.getLotteryChancesCount());

        return ResultVO.buildSuccess(result);
    }

    @GetMapping("/orderStatus/{lotteryOrderId}")
    public ResultVO<String> getLotteryOrderStatus(@PathVariable Integer lotteryOrderId) {
        LotteryOrder lotteryOrder = lotteryOrderRepository.findById(lotteryOrderId).orElse(null);
        if (lotteryOrder == null) {
            return ResultVO.buildFailure("订单不存在");
        }
        return ResultVO.buildSuccess(lotteryOrder.getOrderStatus().toString());
    }
}
