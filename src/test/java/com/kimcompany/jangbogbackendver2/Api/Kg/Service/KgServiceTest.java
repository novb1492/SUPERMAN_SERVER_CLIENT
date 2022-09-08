package com.kimcompany.jangbogbackendver2.Api.Kg.Service;

import com.kimcompany.jangbogbackendver2.Api.Kg.Dto.RequestCancelPartialDto;
import com.kimcompany.jangbogbackendver2.Text.BasicText;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class KgServiceTest {
    @Autowired
    private KgService kgService;

    @Test
    @DisplayName("부분취소 테스트")
    void test() throws NoSuchAlgorithmException {

        RequestCancelPartialDto dto =
                RequestCancelPartialDto.builder().requestCancelDto(
                        RequestCancelPartialDto.setRequestCancelDto("Card", "INIMX_CARDINIpayTest20220823103543585258", "test", BasicText.PartialRefundText))
                        .price("1").confirmPrice("1003").build();
        kgService.cancelAllService(dto);
    }
}