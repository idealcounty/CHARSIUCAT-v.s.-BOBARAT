package com.example.SBEAM.controller;
import com.example.SBEAM.po.Store;

import com.example.SBEAM.service.StoreService;
import com.example.SBEAM.vo.ResultVO;
import com.example.SBEAM.vo.StoreVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/store")
public class StoreController {
        @Autowired
        StoreService storeService;

    @PostMapping("/createStore")
    public ResultVO<Boolean> createStore(@RequestBody StoreVO storeVO) {
        System.out.println("接收到的 storeVO: " + storeVO);
        return ResultVO.buildSuccess(storeService.createStore(storeVO));
    }

    @GetMapping({"/allStore", "/register"})
    public List<StoreVO> getAllStores() {
        List<Store> stores = storeService.getAllStores();
        return stores.stream()
                     .map(Store::toVO)
                     .collect(Collectors.toList());
    }
    @GetMapping("/{id}")
    public ResultVO<StoreVO> getStore(@PathVariable(value = "id") Integer storeId) {
        return ResultVO.buildSuccess(storeService.getStore(storeId));
    }

}
