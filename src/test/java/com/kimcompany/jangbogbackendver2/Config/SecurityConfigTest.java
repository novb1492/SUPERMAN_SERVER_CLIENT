package com.kimcompany.jangbogbackendver2.Config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SecurityConfigTest {
    @Autowired
    private SecurityConfig securityConfig;

    @Test
    public void makePwd(){
        System.out.println(securityConfig.pwdEncoder().encode("1111"));
    }
}