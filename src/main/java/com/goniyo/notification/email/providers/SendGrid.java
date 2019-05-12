package com.goniyo.notification.email.providers;

import com.goniyo.notification.email.Email;
import com.goniyo.notification.email.EmailNotifier;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class SendGrid implements EmailNotifier<Email> {
    @Override
    public String send(Email email) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost("smtp.gmail.com");
        sender.setPort(587);
        sender.setUsername("my.gmail@gmail.com");
        sender.setPassword("password");

        Properties props = sender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        MimeMessage message = sender.createMimeMessage();
// use the true flag to indicate you need a multipart message
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setTo("test@host.com");
            helper.setSubject("subject");
            helper.setText("Check out this image!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        // let's attach the infamous windows Sample file (this time copied to c:/)
        //FileSystemResource file = new FileSystemResource(new File("c:/Sample.jpg"));
        //helper.addAttachment("CoolImage.jpg", file);
        // TODO catch error 550
        sender.send(message);
        return "SENDGRID";
    }


}
