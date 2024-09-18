package com.example.fakestoreapi.controller;

import com.example.fakestoreapi.domain.Cart;
import com.example.fakestoreapi.security.jwt.util.IfLogin;
import com.example.fakestoreapi.security.jwt.util.LoginUserDto;
import com.example.fakestoreapi.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartApiController {
    private final CartService cartService;

    @PostMapping
    public Cart addCart(@IfLogin LoginUserDto loginUserDto) {
//    public Cart addCart(@RequestBody LoginUserDto loginUserDto) {
        LocalDate localDate = LocalDate.now();
        String date = String.valueOf(localDate.getYear()) + (localDate.getMonthValue() < 10 ? "0" :"") + String.valueOf(localDate.getMonthValue()) + (localDate.getDayOfMonth() < 10 ? "0" :"") +String.valueOf(localDate.getDayOfMonth());
        Cart cart = cartService.addCart(loginUserDto.getMemberId(), date);
        return cart;
    }

}
