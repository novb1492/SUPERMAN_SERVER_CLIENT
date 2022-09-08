package com.kimcompany.jangbogbackendver2.Aws;

import com.kimcompany.jangbogbackendver2.Text.PropertiesText;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class SqsServiceTest {
    @Autowired
    private  QueueMessagingTemplate queueMessagingTemplate;

    @Test
    @DisplayName("sqs 연동 테스트")
    public void test(){
        queueMessagingTemplate.send(PropertiesText.sqsPhoneEndPoint, MessageBuilder.withPayload(makeTitleAndText("title","text","emailorphone")).build());
    }
    private JSONObject makeTitleAndText(String title, String text, String val) {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("title", title);
        jsonObject.put("text", text);
        jsonObject.put("val", val);
        return jsonObject;
    }
}