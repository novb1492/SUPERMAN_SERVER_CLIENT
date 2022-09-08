package com.kimcompany.jangbogbackendver2.Api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.mail.MessagingException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class EmailSenderTest {

    @Autowired
    private EmailSender emailSender;

    @Test
    @DisplayName("네이버로 전송테스트")
    void test() throws MessagingException {
        emailSender.sendEmail("novb1942@gmail.com","aa","bb");
    }
}