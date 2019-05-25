package in.codetripper.communik.email.providers;

import in.codetripper.communik.email.Email;
import in.codetripper.communik.email.EmailConfiguration;
import in.codetripper.communik.email.SmtpEmailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
@Slf4j
public class Gmail extends SmtpEmailSender {
    @Override
    protected EmailConfiguration getMailConfiguration() {
        EmailConfiguration emailConfiguration = new EmailConfiguration();
  /*      emailConfiguration.setHost(mailGunConfig.getHost());
        emailConfiguration.setUsername(mailGunConfig.getUsername());
        emailConfiguration.setPassword(mailGunConfig.getPassword());
        emailConfiguration.setPort(mailGunConfig.getPort());*/
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
