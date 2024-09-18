package com.example.fakestoreapi.controller;

import com.example.fakestoreapi.domain.CartItem;
import com.example.fakestoreapi.dto.AddCartItemDto;
import com.example.fakestoreapi.security.jwt.util.IfLogin;
import com.example.fakestoreapi.security.jwt.util.LoginUserDto;
import com.example.fakestoreapi.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/cartItems")
@RestController
@RequiredArgsConstructor
public class CartItemController {
    private final CartItemService cartItemService;

    @PostMapping
    public CartItem addCartItem(@IfLogin LoginUserDto loginUserDto, @RequestBody AddCartItemDto addCartItemDto){
//    public CartItem addCartItem(@RequestBody LoginUserDto loginUserDto, @RequestBody AddCartItemDto addCartItemDto){
        // 같은 cart에 같은 product가 있으면 quantity를 더해줘야함
//        System.out.println(loginUserDto);
//        System.out.println(loginUserDto.getMemberId());
        if(cartItemService.isCartItemExist(loginUserDto.getMemberId(), addCartItemDto.getCartId(), addCartItemDto.getProductId())){
//            System.out.println("같은 cart에 같은 product가 있으므로 quantity를 더해줄것임");
            CartItem cartItem = cartItemService.getCartItem(loginUserDto.getMemberId(), addCartItemDto.getCartId(), addCartItemDto.getProductId());
            cartItem.setQuantity(cartItem.getQuantity() + addCartItemDto.getQuantity());
            return cartItemService.updateCartItem(cartItem);
        }
        return cartItemService.addCartItem(addCartItemDto);
    }
}
