package com.kimcompany.jangbogbackendver2.Product.Service;

import com.kimcompany.jangbogbackendver2.Member.Model.MemberEntity;
import com.kimcompany.jangbogbackendver2.Member.Model.PrincipalDetails;
import com.kimcompany.jangbogbackendver2.Product.Dto.TryInsertDto;
import com.kimcompany.jangbogbackendver2.Text.BasicText;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("ADMIN 정상 제품등록")
    @WithUserDetails("kim")
    @Transactional
    void test(){
        TryInsertDto tryInsertDto=set("1","13,000","test소고기");
        productService.save(tryInsertDto);
    }
    @Test
    @DisplayName("MANAGE 정상 제품등록")
    @WithUserDetails("kim3")
    @Transactional
    void test2(){
        TryInsertDto tryInsertDto=set("1","13,000","test소고기");
        productService.save(tryInsertDto);
    }
    @Test
    @DisplayName("USER 정상 제품등록")
    @WithUserDetails("kim2")
    @Transactional
    void test3(){
        TryInsertDto tryInsertDto=set("2","13,000","test소고기");
        productService.save(tryInsertDto);
    }
    @Test
    @DisplayName("해당 소유의 매장이 아닐경우")
    @Transactional
    void test4(){
        setUser(3L, BasicText.ROLE_USER);
        TryInsertDto tryInsertDto=set("1","13,000","test소고기");
        assertThrows(IllegalArgumentException.class, () ->  productService.save(tryInsertDto));
        setUser(4L,BasicText.ROLE_MANAGE);
        TryInsertDto tryInsertDto2=set("2","13,000","test소고기");
        assertThrows(IllegalArgumentException.class, () ->  productService.save(tryInsertDto2));
    }
    @Test
    @DisplayName("금액에  오류가 있을경우")
    @WithUserDetails("kim")
    @Transactional
    void test5(){
        TryInsertDto tryInsertDto=set("1","ㅎㅇㅀㅇ","test소고기");
        assertThrows(IllegalArgumentException.class, () ->  productService.save(tryInsertDto));
    }
    @Test
    @DisplayName("중복제품인경우")
    @WithUserDetails("kim")
    @Transactional
    void test6(){
        TryInsertDto tryInsertDto=set("1","13,000","테스트고기");
        assertThrows(IllegalArgumentException.class, () ->  productService.save(tryInsertDto));
    }
    @Test
    @DisplayName("전체롤백 테스트")
    @WithUserDetails("kim")
    void test7(){
        TryInsertDto tryInsertDto=set("1","13,000","테스트고기123789");
        List<Map<String, Object>> events = set("2022-08-15T11:31", "2022-08-17T11:32", "2022-08-18T11:35", "2022-08-16T11:32");
        tryInsertDto.setEvents(events);
        assertThrows(IllegalArgumentException.class, () ->  productService.save(tryInsertDto));
    }
    @Test
    @DisplayName("이벤트까지 저장 테스트")
    @WithUserDetails("kim")
    @Transactional
    void test8(){
        TryInsertDto tryInsertDto=set("1","13,000","테스트고기1234");
        List<Map<String, Object>> events = set("2022-08-15T11:31", "2022-08-17T11:32", "2022-08-16T11:33", "2022-08-16T11:34");
        tryInsertDto.setEvents(events);
        productService.save(tryInsertDto);
    }
    @Test
    @DisplayName("없는 카테고리")
    @WithUserDetails("kim")
    void test9(){
        TryInsertDto tryInsertDto=set("1","13,000","테스트고기12345");
        tryInsertDto.setCategory("100");
        List<Map<String, Object>> events = set("2022-08-15T11:31", "2022-08-17T11:32", "2022-08-16T11:33", "2022-08-16T11:34");
        tryInsertDto.setEvents(events);
        assertThrows(IllegalArgumentException.class, () ->  productService.save(tryInsertDto));
    }
    @Test
    @DisplayName("사용불가 카테고리")
    @WithUserDetails("kim")
    void test10(){
        TryInsertDto tryInsertDto=set("1","13,000","테스트고기12345");
        tryInsertDto.setCategory("2");
        List<Map<String, Object>> events = set("2022-08-15T11:31", "2022-08-17T11:32", "2022-08-16T11:33", "2022-08-16T11:34");
        tryInsertDto.setEvents(events);
        assertThrows(IllegalArgumentException.class, () ->  productService.save(tryInsertDto));
    }
    private void setUser(long id ,String role){
        PrincipalDetails principalDetails=new PrincipalDetails(MemberEntity.builder().id(id).role(role).build());
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(principalDetails, principalDetails.getPassword(), principalDetails.getAuthorities()));
    }
    private  List<Map<String, Object>> set(String startDate,String endDate,String startDate2,String endDate2){
        List<Map<String, Object>> events = new ArrayList<>();
        Map<String,Object>event1=new HashMap<>();
        event1.put("price","13,000");
        event1.put("startDate",startDate);
        event1.put("endDate",endDate);
        Map<String,Object>event2=new HashMap<>();
        event2.put("price","15,000");
        event2.put("startDate",startDate2);
        event2.put("endDate",endDate2);
        events.add(event1);
        events.add(event2);
        return events;
    }
    private TryInsertDto set(String storeId,String price,String name){
        TryInsertDto tryInsertDto=new TryInsertDto();
        tryInsertDto.setCategory("1");
        tryInsertDto.setId(storeId);
        tryInsertDto.setIntroduce("제품소개");
        tryInsertDto.setName(name);
        tryInsertDto.setOrigin("국내산");
        tryInsertDto.setPrice(Integer.parseInt(price));
        tryInsertDto.setProductImgPath("테스트이미지");
        return tryInsertDto;
    }
}