package com.kimcompany.jangbogbackendver2.Api;

import com.kimcompany.jangbogbackendver2.Text.PropertiesText;
import org.assertj.core.api.Assertions;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static com.kimcompany.jangbogbackendver2.Text.PropertiesText.kakaoRestKey;
import static org.junit.jupiter.api.Assertions.*;

class KakaoMapServiceTest {
    private Logger logger = LoggerFactory.getLogger(KakaoMapServiceTest.class);

    @BeforeEach
    void setup() throws IllegalAccessException {
        ReflectionTestUtils.setField(PropertiesText.class, "kakaoRestKey", "kakao키");

    }
    @Test
    @DisplayName("정상적인 테스트")
    public void suc() {
        JSONObject jsonObject=pro("서울 송파구 올림픽로 240");
        Assertions.assertThat(getTotalCount((LinkedHashMap<String, Object>) jsonObject.get("meta"))).isEqualTo(1);
    }
    @Test
    @DisplayName("비정상적인 테스트")
    public void fail() {
        JSONObject jsonObject= pro("ㄹㅇ");
        Assertions.assertThat(getTotalCount((LinkedHashMap<String, Object>) jsonObject.get("meta"))).isEqualTo(0);
    }
    private int getTotalCount(LinkedHashMap<String, Object> linkedHashMap){
        return Integer.parseInt(linkedHashMap.get("total_count").toString());
    }
    private JSONObject pro(String address){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "KakaoAK " + kakaoRestKey);
        return RequestTo.requestGet(null, "https://dapi.kakao.com/v2/local/search/address.json?query=" + address, httpHeaders);
    }
}