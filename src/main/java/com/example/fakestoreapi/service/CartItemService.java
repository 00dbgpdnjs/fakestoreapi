package com.example.fakestoreapi.service;

import com.example.fakestoreapi.domain.Cart;
import com.example.fakestoreapi.domain.CartItem;
import com.example.fakestoreapi.dto.AddCartItemDto;
import com.example.fakestoreapi.repository.CartItemRepository;
import com.example.fakestoreapi.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public boolean isCartItemExist(Long memberId, Long cartItemId) {
        return cartItemRepository.existsByCart_memberIdAndId(memberId, cartItemId);
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

    /* ?? 왜 아래와 달리 똑같은 select from cart 가 두번 실행되지?
        - findByCart_memberId(memberId)는 cart가 두 번 조회하는 이유
            1. 먼저 Cart 엔티티를 로드
                CartItem에는 Cart의 외래 키만 저장되어 있으므로,
                이 외래 키를 사용해 Cart 테이블에서
                memberId에 맞는 해당 Cart 객체를 가져옴
            2. CartItem과 연결된 Cart 조회
                Cart와 연결된 CartItem들을 조회
        - findByCart_memberIdAndCart_id(memberId, cartId)는 한 번만 조회
            cartId를 사용하여 한 번의 조인만으로 Cart와 CartItem을 모두 가져올 수 있습니다.
     */
    @Transactional(readOnly = true)
    public List<CartItem> getCartItems(Long memberId) {
        return cartItemRepository.findByCart_memberId(memberId);
    }

    @Transactional(readOnly = true)
    public List<CartItem> getCartItems(Long memberId, Long cartId) {
        return cartItemRepository.findByCart_memberIdAndCart_id(memberId, cartId);
    }

    @Transactional
    public void deleteCartItem(Long memberId, Long cartItemId) {
        /* (existsByCart_memberIdAndId 와 달리) cart 테이블을 조회하는 이유
         - deleteBy는 데이터 삭제 시 연관된 엔티티의 상태나 무결성을 확인하기 위해 Cart 조회
         - JPA는 삭제할 때 관련된 부모 엔티티 상태 확인
         */
        cartItemRepository.deleteByCart_memberIdAndId(memberId, cartItemId);
    }
}
