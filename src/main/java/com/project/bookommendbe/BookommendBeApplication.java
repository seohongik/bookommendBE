package com.project.bookommendbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class BookommendBeApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookommendBeApplication.class, args);
    }
}
