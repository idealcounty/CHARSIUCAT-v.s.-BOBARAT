package com.example.SBEAM.controller;
import com.example.SBEAM.service.CartService;
import com.example.SBEAM.vo.*;
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
    @GetMapping("/users/{userId}")
    public ResultVO<CartVO> getCart(@PathVariable int userId) {
        return ResultVO.buildSuccess(cartService.getCart(userId));
    }

    @PostMapping("/product/productDetail/{productId}/{cartId}")
    public boolean judgeCartItemExist(@PathVariable("productId") int productId, @PathVariable("cartId") int cartId) {
        return cartService.judgeCartItemExist(productId,cartId);
    }

    @PutMapping("/product/productDetail/{productId}/cart")
    public ResultVO<Boolean> updateCart(@PathVariable("productId") int productId,@RequestParam int numberOfProduct, @RequestBody CartVO cartVO) {
        return ResultVO.buildSuccess(cartService.updateCart(productId,numberOfProduct,cartVO));
    }
    @GetMapping("/users/{userId}/cart")
    public List<CartItemVO> showAllCartItem(@PathVariable("userId") int userId) {
        return ResultVO.buildSuccess(cartService.showAllCartItems(userId)).getResult();
    }

    @PutMapping("/users/{userId}/cart")
    public ResultVO<OrdersVO> settleAccount(@PathVariable("userId") int userId, @RequestBody CartVO cartVO){
        return ResultVO.buildSuccess(cartService.settleAccount(userId,cartVO));
    }

}
