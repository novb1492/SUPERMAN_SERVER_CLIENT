package com.kimcompany.jangbogbackendver2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Jangbogbackendver2Application {

    public static void main(String[] args) {
        SpringApplication.run(Jangbogbackendver2Application.class, args);
    }

}
