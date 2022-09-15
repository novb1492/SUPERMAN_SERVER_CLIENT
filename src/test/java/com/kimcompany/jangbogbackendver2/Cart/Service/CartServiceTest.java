package com.kimcompany.jangbogbackendver2.Cart.Service;

import com.kimcompany.jangbogbackendver2.Cart.Dto.TryPaymentDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
class CartServiceTest {

    @Autowired
    private CartService cartService;

    @Test
    @DisplayName("정상 테스트")
    void test(){
        TryPaymentDto tryPaymentDto = new TryPaymentDto();
        List<Map<String, Object>> strings = new ArrayList<>();
        Map<String, Object> stringObjectMap = new HashMap<>();
        stringObjectMap.put("cartId", 1);
        stringObjectMap.put("count",7);
        strings.add(stringObjectMap);
        tryPaymentDto.setPayments(strings);
        cartService.makePaymentInfo(tryPaymentDto);
    }
    @Test
    @DisplayName("정상 테스트 여러개")
    void test2(){
        TryPaymentDto tryPaymentDto = new TryPaymentDto();
        List<Map<String, Object>> strings = new ArrayList<>();
        Map<String, Object> stringObjectMap = new HashMap<>();
        stringObjectMap.put("cartId", 1);
        stringObjectMap.put("count",7);
        strings.add(stringObjectMap);
        Map<String, Object> stringObjectMap2 = new HashMap<>();
        stringObjectMap2.put("cartId", 5);
        stringObjectMap2.put("count",7);
        strings.add(stringObjectMap2);
        Map<String, Object> stringObjectMap3 = new HashMap<>();
        stringObjectMap3.put("cartId", 6);
        stringObjectMap3.put("count",10);
        strings.add(stringObjectMap3);
        tryPaymentDto.setPayments(strings);
        tryPaymentDto.setAddress("서울어딘가");
        tryPaymentDto.setPostCode("0000");
        tryPaymentDto.setDetailAddress("fdf");
        cartService.makePaymentInfo(tryPaymentDto);
    }
}