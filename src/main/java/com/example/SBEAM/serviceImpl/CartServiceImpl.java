package com.example.SBEAM.serviceImpl;
import com.example.SBEAM.exception.SBEAMException;
import com.example.SBEAM.po.*;
import com.example.SBEAM.repository.CartRepository;
import com.example.SBEAM.repository.ProductRepository;
import com.example.SBEAM.repository.UserRepository;
import com.example.SBEAM.service.CartService;
import com.example.SBEAM.service.CartItemService;
import com.example.SBEAM.vo.CartVO;
import com.example.SBEAM.vo.CartItemVO;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class CartServiceImpl implements CartService{
    @Autowired
    CartRepository cartRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;
    @Override
    public Boolean createCart(CartVO cartVO){
        Cart cart = cartRepository.findByCartId(cartVO.getCartId());
        Cart newCart = cartVO.toPO();
        cartRepository.save(newCart);
        return true;
    }

    @Override
    public Boolean updateCart(int productId,int numberOfProduct,CartVO cartVO){
        Cart cart = cartRepository.findByCartId(cartVO.getCartId());
        Product product = productRepository.findByProductId(productId);

        if(product.getProductAmount()<numberOfProduct){
            throw SBEAMException.productNotEnough();
        }
        Optional<CartItem> optional = cart.getCartItems().stream().filter(item -> item.getProductId().equals(productId)).findFirst();
        if (optional.isPresent()) {
            CartItem existing = optional.get();
            existing.setProductQuantity(numberOfProduct);
        }
        else {
            CartItem cartItem = new CartItem(cart, productId, numberOfProduct, product.getProductPrice());
            cart.getCartItems().add(cartItem);
        }
        cartRepository.save(cart);
        return true;
    }

    @Override
    public Boolean settleAccount(int userId,CartVO cartVO){
        Cart cart = cartRepository.findByCartId(cartVO.getCartId());
        User user = userRepository.findById(userId).get();
        double price=0;
        for (CartItem cartItem : cart.getCartItems()) {
            price+=cartItem.getProductQuantity()*cartItem.getProductPrice();
        }
        if(price>user.getBalance()){
            throw SBEAMException.priceNotEnough();
        }
        user.setBalance(user.getBalance()-price);
        userRepository.save(user);

        cart.getCartItems().clear();
        cartRepository.save(cart);

        return true;
    }

    @Override
    public List<CartItemVO>showAllCartItems(int userId){
        Cart cart = cartRepository.findByCartId(userId);
        List<CartItem> cartItems = cart.getCartItems();
        return cartItems.stream().map(CartItem::toVO).collect(Collectors.toList());
    }
}
