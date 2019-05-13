package com.goniyo.notification.email.providers;

import com.goniyo.notification.email.Email;
import com.goniyo.notification.email.EmailConfiguration;
import com.goniyo.notification.email.EmailSender;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class MailGun extends EmailSender {

    @Override
    protected EmailConfiguration getMailConfiguration() {
        // TODO get from Configuration
        EmailConfiguration emailConfiguration = new EmailConfiguration();
        emailConfiguration.setHost("smtp.gmail.com");
        emailConfiguration.setUsername("hkdisonline@gmail.com");
        emailConfiguration.setPort(587);
        return emailConfiguration;

    }

    @Override
    protected Properties getMailProperties() {
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
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
