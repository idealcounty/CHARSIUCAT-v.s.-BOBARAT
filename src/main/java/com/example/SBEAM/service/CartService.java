package com.example.SBEAM.service;
import com.example.SBEAM.vo.CartItemVO;
import com.example.SBEAM.vo.CartVO;
import java.util.List;
public interface CartService {
    Boolean createCart(CartVO cartVO);
    CartVO getCart(int userId);
    Boolean judgeCartItemExist(Integer productId,CartVO cartVO);
    Boolean updateCart(int productId,int numberOfProduct,CartVO cartVO);
    Boolean settleAccount(int userId,CartVO cartVO);
    List<CartItemVO> showAllCartItems(int userId);
}
