package com.kimcompany.jangbogbackendver2.Deliver.Service;

import com.kimcompany.jangbogbackendver2.Deliver.Dto.ChangeDetailDto;
import com.kimcompany.jangbogbackendver2.Deliver.Dto.TryInsertDto;
import com.kimcompany.jangbogbackendver2.Member.Model.MemberEntity;
import com.kimcompany.jangbogbackendver2.Member.Model.PrincipalDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DeliverServiceTest {
    @Autowired
    private DeliverService deliverService;
    @BeforeEach
    void setup() throws IllegalAccessException {
        PrincipalDetails principalDetails=new PrincipalDetails(MemberEntity.builder().id(1L).role("ADMIN").build());
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(principalDetails, principalDetails.getPassword(), principalDetails.getAuthorities()));
    }

    @Test
    @DisplayName("배달방 및 배달 디테일 생성 테스트")
    @Transactional
    void test(){
        deliverService.save(new TryInsertDto());
        assertThrows(IllegalArgumentException.class, () ->   deliverService.save(new TryInsertDto()));
        assertThrows(IllegalArgumentException.class, () ->  deliverService.save(new TryInsertDto()));
    }
    @Test
    @DisplayName("one to many")
    void test2(){
        ChangeDetailDto changeDetailDto = new ChangeDetailDto();
        changeDetailDto.setDeliverDetailId(5L);
        changeDetailDto.setDeliverId(5L);
        changeDetailDto.setState(100);
        changeDetailDto.setStoreId(1L);
        deliverService.updateDeliverAndDeliverDetailAndOrderState(changeDetailDto);
    }
}