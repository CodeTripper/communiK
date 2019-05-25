package in.codetripper.notification.email;

import in.codetripper.notification.notification.NotificationFailedException;
import in.codetripper.notification.notification.NotificationStatusResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import reactor.core.publisher.Mono;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Properties;

@Slf4j
public abstract class SmtpEmailSender implements EmailNotifier<Email> {
    private final JavaMailSenderImpl sender;

    public SmtpEmailSender() {
        sender = new JavaMailSenderImpl();
        //props = sender.getJavaMailProperties();
    }

    @Override
    public final Mono<NotificationStatusResponse> send(Email email) throws NotificationFailedException {
        log.debug("starting email sender");
        try {
            preProcess(email);
            process(email);
            postProcess(email);
        } catch (MessagingException | MailException e) {
            throw new NotificationFailedException("Unable to send Email", e);
        }
        return null;
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
        helper.setTo(email.getTo());
        helper.setSubject(email.getSubject());
        helper.setText(email.getBody().getMessage());

        if (email.getAttachment() != null) {
            FileSystemResource file = new FileSystemResource(new File(email.getAttachment().getMessage()));
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
