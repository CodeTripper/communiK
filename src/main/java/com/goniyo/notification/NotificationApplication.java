package com.goniyo.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.mapping.event.LoggingEventListener;

@SpringBootApplication
//ComponentScan("com.goniyo.notification.repository.mongo")

public class NotificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class, args);
    }

    @Bean
    public LoggingEventListener mongoEventListener() {
        return new LoggingEventListener();
    }

}
