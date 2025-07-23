package com.todai.todai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TodaiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodaiApplication.class, args);
    }

}
