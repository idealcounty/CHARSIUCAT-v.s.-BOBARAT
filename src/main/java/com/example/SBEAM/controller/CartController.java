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
    @PostMapping("/product/createCart")//这个不太清楚会不会用到
    public ResultVO<Boolean> createCart(@RequestBody CartVO cartVO) {
        return ResultVO.buildSuccess(cartService.createCart(cartVO));
    }
    @PutMapping("/product/productDetail/{productId}/cart")
    public ResultVO<Boolean> updateCart(@PathVariable("productId") int productId,@RequestParam int numberOfProduct, @RequestBody CartVO cartVO) {
        return ResultVO.buildSuccess(cartService.updateCart(productId,numberOfProduct,cartVO));
    }
    @GetMapping("/users/{userId}/cart")
    public List<CartItemVO> showAllCartItem(@PathVariable("userId") int userId) {
        return ResultVO.buildSuccess(cartService.showAllCartItems(userId)).getResult();
    }

    @PutMapping("/users/{userId}/cart")//可能有问题，暂时别用
    public ResultVO<Boolean> settleAccount(@PathVariable("userId") int userId,@RequestBody CartVO cartVO){
        return ResultVO.buildSuccess(cartService.settleAccount(userId,cartVO));
    }

}
