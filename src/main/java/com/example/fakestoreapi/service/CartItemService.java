package com.example.fakestoreapi.service;

import com.example.fakestoreapi.domain.Cart;
import com.example.fakestoreapi.domain.CartItem;
import com.example.fakestoreapi.dto.AddCartItemDto;
import com.example.fakestoreapi.repository.CartItemRepository;
import com.example.fakestoreapi.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;

    @Transactional
    public CartItem addCartItem(AddCartItemDto addCartItemDto) {
        Cart cart = cartRepository.findById(addCartItemDto.getCartId()).orElseThrow();

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setQuantity(addCartItemDto.getQuantity());
        cartItem.setProductId(addCartItemDto.getProductId());
        cartItem.setProductPrice(addCartItemDto.getProductPrice());
        cartItem.setProductTitle(addCartItemDto.getProductTitle());
        cartItem.setProductDescription(addCartItemDto.getProductDescription());

        return cartItemRepository.save(cartItem);
    }

    @Transactional(readOnly = true)
    public boolean isCartItemExist(Long memberId, Long cartId, Long productId) {
        boolean check = cartItemRepository.existsByCart_memberIdAndCart_idAndProductId(memberId, cartId, productId);
        return check;
    }

    @Transactional(readOnly = true)
    public CartItem getCartItem(Long memberId, Long cartId, Long productId) {
        // 쿼리 두 개 실행됨
        return cartItemRepository.findByCart_memberIdAndCart_idAndProductId(memberId, cartId, productId).orElseThrow();
    }

    // save()를 호출하지 않아도 이 어노테이션이 있어서 setQuantity()만 해도 db에 반영되나봄
    @Transactional
    public CartItem updateCartItem(CartItem cartItem) {
        CartItem findCartItem = cartItemRepository.findById(cartItem.getId()).orElseThrow();
        findCartItem.setQuantity(cartItem.getQuantity());
        return findCartItem;
    }
}
