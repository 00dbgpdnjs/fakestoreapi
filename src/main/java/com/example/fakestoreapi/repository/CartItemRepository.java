package com.example.fakestoreapi.repository;

import com.example.fakestoreapi.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    // 다음 조건을 만족하는 CartItem이 데이터베이스에 존재하는지
    //   주어진 memberId와 일치하는 Cart의 소유자.
    //   주어진 cartId와 일치하는 Cart.
    //   주어진 productId와 일치하는 Product.
    boolean existsByCart_memberIdAndCart_idAndProductId(Long memberId, Long cartId, Long productId);

    Optional<CartItem> findByCart_memberIdAndCart_idAndProductId(Long memberId, Long cartId, Long productId);

}