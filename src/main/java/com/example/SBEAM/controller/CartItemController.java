package com.example.SBEAM.controller;
import com.example.SBEAM.service.CartItemService;
import com.example.SBEAM.vo.ResultVO;
import com.example.SBEAM.vo.CartItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CartItemController {
    @Autowired
    CartItemService cartItemService;
    @PostMapping("/product/createProduct2")//可能用不到
    public ResultVO<Boolean> createCart(@RequestBody CartItemVO cartItemVO) {
        return ResultVO.buildSuccess(cartItemService.createCartItem(cartItemVO));
    }
}
