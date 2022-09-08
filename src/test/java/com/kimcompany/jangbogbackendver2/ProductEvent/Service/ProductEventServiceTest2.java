package com.kimcompany.jangbogbackendver2.ProductEvent.Service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ProductEventServiceTest2 {
    @Autowired
    private ProductEventService productEventService;

    @Test
    @DisplayName("상품 이벤트 저장 테스트 동일한 이벤트")
    @WithUserDetails("kim")
    void test(){
        List<Map<String, Object>> events = set("2022-08-15T11:32", "2022-08-17T11:35", "2022-08-15T11:32", "2022-08-16T11:37");
        assertThrows(IllegalArgumentException.class, () ->  productEventService.save(1,events));
    }
    @Test
    @DisplayName("상품 이벤트 저장 테스트 정상")
    @WithUserDetails("kim")
    @Transactional
    void test2(){
        List<Map<String, Object>> events = set("2022-08-15T11:32", "2022-08-17T11:32", "2022-08-16T11:32", "2022-08-16T11:33");
        productEventService.save(1,events);
    }
    @Test
    @DisplayName("상품 이벤트 저장 테스트 날짜 뒤바뀜")
    @WithUserDetails("kim")
    void test3(){
        List<Map<String, Object>> events = set("2022-08-15T11:32", "2022-08-15T11:33", "2022-08-16T11:35", "2022-08-16T11:32");
        assertThrows(IllegalArgumentException.class, () ->  productEventService.save(1,events));
    }
    @Test
    @DisplayName("상품 이벤트 저장 테스트 날짜 같음")
    @WithUserDetails("kim")
    @Transactional
    void test4(){
        List<Map<String, Object>> events = set("2022-08-15T11:32", "2022-08-15T11:32", "2022-08-16T11:31", "2022-08-16T11:32");
        assertThrows(IllegalArgumentException.class, () ->  productEventService.save(1,events));
    }
    private List<Map<String, Object>> set(String startDate, String endDate, String startDate2, String endDate2){
        List<Map<String, Object>> events = new ArrayList<>();
        Map<String,Object>event1=new HashMap<>();
        event1.put("price","13,000");
        event1.put("startDate",startDate);
        event1.put("endDate",endDate);
        event1.put("name","TEST1");
        Map<String,Object>event2=new HashMap<>();
        event2.put("price","15,000");
        event2.put("startDate",startDate2);
        event2.put("endDate",endDate2);
        event2.put("name","test2");
        events.add(event1);
        events.add(event2);
        return events;
    }
}