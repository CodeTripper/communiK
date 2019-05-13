package com.goniyo.notification.email;

import com.goniyo.notification.notification.NotificationFailedException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Properties;

public abstract class EmailSender implements EmailNotifier<Email> {
    private JavaMailSenderImpl sender;

    public EmailSender() {
        sender = new JavaMailSenderImpl();
        //props = sender.getJavaMailProperties();
    }

    @Override
    public final String send(Email email) throws NotificationFailedException {
        try {
            preProcess(email);
            process(email);
            postProcess(email);
        } catch (MessagingException | MailException e) {
            throw new NotificationFailedException("Unable to send Email", e);
        }
        return "SENDGRID";
    }

    private boolean process(Email email) throws MailException, MessagingException {
        EmailConfiguration emailConfiguration = getMailConfiguration();
        sender.setHost(emailConfiguration.getHost());
        sender.setPassword(emailConfiguration.getPassword());
        sender.setUsername(emailConfiguration.getUsername());
        sender.setPort(emailConfiguration.getPort());
        sender.setProtocol(emailConfiguration.getProtocol());
        sender.setJavaMailProperties(getMailProperties());
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email.to);
        helper.setSubject(email.getSubject());
        helper.setText(email.message);

        if (!email.getAttachment().isEmpty()) {
            FileSystemResource file = new FileSystemResource(new File(email.getAttachment()));
            helper.addAttachment("CoolImage.jpg", file);
        }
        sender.send(message);
        return true;

    }

    protected abstract EmailConfiguration getMailConfiguration();

    protected abstract Properties getMailProperties();

    protected abstract Properties preProcess(Email email);

    protected abstract Properties postProcess(Email email);

}
