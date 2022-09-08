package com.kimcompany.jangbogbackendver2.Api;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JungBuTest {
    private Logger logger = LoggerFactory.getLogger(JungBuTest.class);
    @Test
    void test(){
        JSONObject companyNum = JungBu.getCompanyNum("108","ㅀㅇ");
        logger.info("정부 api 결과:{}",companyNum.toString());
    }
}