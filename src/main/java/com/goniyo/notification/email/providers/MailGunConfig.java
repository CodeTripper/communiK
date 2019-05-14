package com.goniyo.notification.email.providers;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@ConfigurationProperties(prefix = "mailgun")
@Configuration("mailGunConfig")
@PropertySource("classpath:email.properties")
public class MailGunConfig {
    private String username;
    private String password;
    private String host;
    private int port;
}
