package com.example.fakestoreapi.controller;

import com.example.fakestoreapi.domain.Cart;
import com.example.fakestoreapi.domain.CartItem;
import com.example.fakestoreapi.security.jwt.util.LoginUserDto;
import com.example.fakestoreapi.service.CartItemService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartItemController.class) // 해당 클래스만 테스트하는 Web MVC 환경 설정 / 컨트롤러 단독 테스트
class CartItemControllerTest {
    @Autowired
    // 컨트롤러를 HTTP 요청처럼 모의하여 테스트할 수 있게 해주는 객체
    // perform(): 실제 요청 시뮬레이션 & 결과 검증
    private MockMvc mockMvc;

    // CartItemService를 mock 객체로 만들어서,
    // 실제 서비스 호출 없이 가짜 데이터 반환
    @MockBean
    private CartItemService cartItemService;

    @Autowired
    private ObjectMapper objectMapper;  // JSON 직렬화를 위한 객체

//    @MockBean
//    private LoginUserDto loginUserDto; // 로그인 사용자 정보 모킹

    @Test
    public void testGetCartItemsWithoutCartId() throws Exception {
        // Given: Mocked CartItem list
        Cart mockCart = new Cart();
        // 단순히 CartItem과의 관계만 확인할 때, ID만 설정해도 충분
        mockCart.setId(1L);

        CartItem cartItem1 = new CartItem();
        cartItem1.setId(1L);
        cartItem1.setCart(mockCart);
        cartItem1.setProductId(101L);
        cartItem1.setProductTitle("Product 1");
        cartItem1.setProductPrice(10.0);
        cartItem1.setProductDescription("Description for Product 1");
        cartItem1.setQuantity(2);

        CartItem cartItem2 = new CartItem();
        cartItem2.setId(2L);
        cartItem2.setCart(mockCart);
        cartItem2.setProductId(102L);
        cartItem2.setProductTitle("Product 2");
        cartItem2.setProductPrice(20.0);
        cartItem2.setProductDescription("Description for Product 2");
        cartItem2.setQuantity(1);

        List<CartItem> mockCartItems = Arrays.asList(cartItem1, cartItem2);

        // Capture the argument passed to the service method
        ArgumentCaptor<Long> memberIdCaptor = ArgumentCaptor.forClass(Long.class);

        Mockito.when(cartItemService.getCartItems(anyLong())).thenReturn(mockCartItems);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/cartItems")
                        .header("X-USER-ID", "1"))  // 필요없는 코드/ HTTP 요청의 헤더에 "X-USER-ID"라는 커스텀 헤더를 추가
                .andExpect(status().isOk())
                .andReturn();

        // Then: Verify the result
        String jsonResponse = result.getResponse().getContentAsString();
        // JSON 응답 역직렬화
        List<CartItem> actualResponse = objectMapper.readValue(jsonResponse, List.class);
        // List<CartItem>이라는 구체적인 제네릭 타입으로 JSON을 역직렬화
//        List<CartItem> actualResponse = objectMapper.readValue(jsonResponse, new TypeReference<List<CartItem>>() {});

        assert(actualResponse.size() == 2);  // CartItem이 2개임을 확인
        assert(actualResponse.get(0).getProductTitle().equals("Product 1"));
        assert(actualResponse.get(1).getProductTitle().equals("Product 2"));
        assert(actualResponse.get(0).getQuantity() == 2);
    }

    @Test
    public void testGetCartItemsWithCartId() throws Exception {
        Cart mockCart = new Cart();
        mockCart.setId(1L);

        CartItem cartItem1 = new CartItem();
        cartItem1.setId(1L);
        cartItem1.setCart(mockCart);
        cartItem1.setProductId(101L);
        cartItem1.setProductTitle("Product 1");
        cartItem1.setProductPrice(10.0);
        cartItem1.setProductDescription("Description for Product 1");
        cartItem1.setQuantity(2);

        CartItem cartItem2 = new CartItem();
        cartItem2.setId(2L);
        cartItem2.setCart(mockCart);
        cartItem2.setProductId(102L);
        cartItem2.setProductTitle("Product 2");
        cartItem2.setProductPrice(20.0);
        cartItem2.setProductDescription("Description for Product 2");
        cartItem2.setQuantity(1);

        Cart mockCart2 = new Cart();
        mockCart2.setId(2L);

        CartItem cartItem3 = new CartItem();
        cartItem3.setId(1L);
        cartItem3.setCart(mockCart2);
        cartItem3.setProductId(101L);
        cartItem3.setProductTitle("Product 1");
        cartItem3.setProductPrice(10.0);
        cartItem3.setProductDescription("Description for Product 1");
        cartItem3.setQuantity(2);

        List<CartItem> mockCartItems = Arrays.asList(cartItem1, cartItem2, cartItem3);

        // eq : 매개변수의 값이 정확히 1L일 때만 일치하도록
//        Mockito.when(cartItemService.getCartItems(anyLong(), eq(1L))).thenReturn(mockCartItems);

        Mockito.doAnswer(new Answer<List<CartItem>>() {
            @Override
            public List<CartItem> answer(InvocationOnMock invocation) {
                Long memberId = invocation.getArgument(0);
                Long cartId = invocation.getArgument(1);
                // Return only items with the matching cartId
                return mockCartItems.stream()
                        .filter(cartItem -> cartItem.getCart().getId().equals(cartId))
                        .collect(Collectors.toList());
            }
        }).when(cartItemService).getCartItems(anyLong(), eq(1L));


        // When: Perform the request with cartId
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/cartItems")
                        .header("X-USER-ID", "1")
                        .param("cartId", "1"))
                .andExpect(status().isOk())
                .andReturn();

        // Then: Verify the result
        String jsonResponse = result.getResponse().getContentAsString();
//        List<CartItem> actualResponse2 = objectMapper.readValue(jsonResponse, List.class); // ?? 왜 위에는 되는데 여기는 안됨
//        Iterator<CartItem> iterator = actualResponse2.iterator();
//        while(iterator.hasNext())
//            System.out.println(iterator.next());
//        System.out.println();
//        System.out.println(actualResponse2.toString());
//        System.out.println();

        List<CartItem> actualResponse = objectMapper.readValue(jsonResponse, new TypeReference<List<CartItem>>() {});
//        Iterator<CartItem> iterator2 = actualResponse.iterator();
//        while(iterator2.hasNext())
//            System.out.println(iterator2.next());
//        System.out.println(actualResponse.toString());

        System.out.println(actualResponse.size());
        assertEquals(2, actualResponse.size());
        assertEquals("Product 1", actualResponse.get(0).getProductTitle());
        assertEquals("Product 2", actualResponse.get(1).getProductTitle());
        assertEquals(2, actualResponse.get(0).getQuantity());
    }
}