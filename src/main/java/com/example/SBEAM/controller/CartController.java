package com.example.SBEAM.controller;
import com.example.SBEAM.service.CartService;
import com.example.SBEAM.vo.CartItemVO;
import com.example.SBEAM.vo.ProductVO;
import com.example.SBEAM.vo.ResultVO;
import com.example.SBEAM.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
@RestController
@RequestMapping("/api")
public class CartController {
    @Autowired
    CartService cartService;
    @PostMapping("/product/createProduct")
    public ResultVO<Boolean> createCart(@RequestBody CartVO cartVO) {
        return ResultVO.buildSuccess(cartService.createCart(cartVO));
    }


}
