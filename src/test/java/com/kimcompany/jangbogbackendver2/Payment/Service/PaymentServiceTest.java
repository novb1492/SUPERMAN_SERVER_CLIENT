package com.kimcompany.jangbogbackendver2.Payment.Service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class PaymentServiceTest {
    @Autowired
    private PaymentService paymentService;

    @Test
    void test(){
        paymentService.selectForPeriod(1L, 2022, 8);
    }
}