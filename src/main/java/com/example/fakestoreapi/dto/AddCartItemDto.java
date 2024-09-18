package com.example.fakestoreapi.dto;

import lombok.Data;

@Data
public class AddCartItemDto {
    private Long cartId;
    private Long productId;
    private String productTitle;
    private Double productPrice;
    private String productDescription;
    private int quantity;
}

/*
{
    "cartId": "user@example.com",
    "name": "John Doe",
    "memberId": 123,
    "roles": ["ROLE_USER", "ROLE_ADMIN"]
}
{
    "cartId": 2,
    "productId": 3,
    "productTitle": "셔츠",
    "productPrice": 20000,
    "productDescription": "린넨 셔츠",
    "quantity": 2
}

private Long cartId;
private Long productId;
private String productTitle;
private Double productPrice;
private String productDescription;
private int quantity;
 */