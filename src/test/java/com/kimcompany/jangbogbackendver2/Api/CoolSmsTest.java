package com.kimcompany.jangbogbackendver2.Api;

import com.kimcompany.jangbogbackendver2.Text.PropertiesText;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class CoolSmsTest {
    @BeforeEach
    void setup() throws IllegalAccessException {
        /**
         * @value 값을 mock테스트 에서 사용할때
         */
        ReflectionTestUtils.setField(PropertiesText.class, "companyNum", "01011112222");
        ReflectionTestUtils.setField(PropertiesText.class, "coolSmsKey", "khkhk");
        ReflectionTestUtils.setField(PropertiesText.class, "coolSmsSecret", "sdfsf");
    }
    @Test
    @DisplayName("문자 전송테스트")
    void test(){
        try{
            CoolSms.sendMessage("01011112222", "테스트내용");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}