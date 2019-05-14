package com.goniyo.notification.email.providers;

import com.goniyo.notification.email.Email;
import com.goniyo.notification.email.EmailConfiguration;
import com.goniyo.notification.email.EmailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
@Slf4j
public class SendGrid extends EmailSender {
    @Autowired
    private SendGridConfig sendGridConfig;
    @Override
    protected EmailConfiguration getMailConfiguration() {
        EmailConfiguration emailConfiguration = new EmailConfiguration();
        emailConfiguration.setHost(sendGridConfig.getHost());
        emailConfiguration.setUsername(sendGridConfig.getUsername());
        emailConfiguration.setPassword(sendGridConfig.getPassword());
        emailConfiguration.setPort(sendGridConfig.getPort());
        return emailConfiguration;

    }
    @Override
    protected Properties getMailProperties() {
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "false");
        return props;
    }

    @Override
    protected Properties preProcess(Email email) {
        return null;
    }

    @Override
    protected Properties postProcess(Email email) {
        return null;
    }


}
