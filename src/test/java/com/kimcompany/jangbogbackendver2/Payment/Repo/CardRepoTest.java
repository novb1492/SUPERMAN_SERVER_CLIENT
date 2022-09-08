package com.kimcompany.jangbogbackendver2.Payment.Repo;

import com.kimcompany.jangbogbackendver2.Payment.Model.CardEntity;
import com.kimcompany.jangbogbackendver2.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestConfig.class)
@ActiveProfiles("test")
class CardRepoTest {

    @Autowired
    private CardRepo cardRepo;

    @Test
    void test(){
        List<CardEntity> byPeriod = cardRepo.findByPeriod(40, Timestamp.valueOf("2022-08-30 08:40:48.607237").toLocalDateTime(), Timestamp.valueOf("2022-08-30 08:43:48.607237").toLocalDateTime(),1L);
        for(CardEntity c:byPeriod){
            System.out.println(c.getId());
        }
    }
}