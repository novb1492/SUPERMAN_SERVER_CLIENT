package com.kimcompany.jangbogbackendver2.Store;

import com.kimcompany.jangbogbackendver2.Member.Model.MemberEntity;
import com.kimcompany.jangbogbackendver2.Member.Model.PrincipalDetails;
import com.kimcompany.jangbogbackendver2.Store.Dto.TryInsertDto;
import com.kimcompany.jangbogbackendver2.Text.PropertiesText;
import org.json.simple.parser.ParseException;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class StoreServiceTest {

    @Autowired
    private StoreService storeService;
    @BeforeEach
    void setup() throws IllegalAccessException {
        PrincipalDetails principalDetails=new PrincipalDetails(MemberEntity.builder().id(1L).role("ADMIN").build());
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(principalDetails, principalDetails.getPassword(), principalDetails.getAuthorities()));
    }

    @Test
    @DisplayName("정상적인 테스트")
    @Transactional
    void suc() throws ParseException {
        TryInsertDto tryInsertDto = set("test", "서울 송파구 올림픽로 240");
        storeService.save(tryInsertDto);
    }
    @Test
    @DisplayName("중복매장 테스트")
    void same(){
        TryInsertDto tryInsertDto = set("한국2", "서울 송파구 올림픽로 240");
        assertThrows(IllegalArgumentException.class, () -> storeService.save(tryInsertDto));
    }
    private TryInsertDto set(String storeName,String storeAddress){
        TryInsertDto tryInsertDto=new TryInsertDto();
        tryInsertDto.setAddress(storeAddress);
        tryInsertDto.setCloseTime("12:00");
        tryInsertDto.setDetailAddress("");
        tryInsertDto.setMinPrice(1);
        tryInsertDto.setName(storeName);
        tryInsertDto.setPostcode("12345");
        tryInsertDto.setRadius("0.1");
        tryInsertDto.setTel("02");
        tryInsertDto.setText(null);
        tryInsertDto.setOpenTime("03:01");
        tryInsertDto.setThumbnail("imagepath");
        return tryInsertDto;
    }

}