package in.codetripper.communik.email.providers;

import in.codetripper.communik.email.Email;
import in.codetripper.communik.email.EmailConfiguration;
import in.codetripper.communik.email.SmtpEmailSender;
import in.codetripper.communik.provider.Provider;
import in.codetripper.communik.provider.ProviderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Properties;

import static in.codetripper.communik.email.Constants.GMAIL;

@Service
@Slf4j
@Qualifier(GMAIL)
@RequiredArgsConstructor()
public class Gmail extends SmtpEmailSender {
    private final ProviderService providerService;
    String providerId = "11003";
    @Override
    protected EmailConfiguration getMailConfiguration() {
        Provider provider = providerService.getProvider(providerId);
        EmailConfiguration emailConfiguration = new EmailConfiguration();
        emailConfiguration.setHost(provider.getServer().getHost());
        emailConfiguration.setUsername(provider.getBasicAuthentication().getUserId());
        emailConfiguration.setPassword(provider.getBasicAuthentication().getPassword());
        emailConfiguration.setPort(provider.getServer().getPort());
        return emailConfiguration;

    }

    @Override
    protected Properties getMailProperties() {
        Provider provider = providerService.getProvider(providerId);
        Properties props = new Properties();
        props.put("mail.transport.protocol", provider.getServer().getProtocol());
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", provider.getServer().isTls());
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


    @Override
    public boolean isDefault() {
        return false;
    }
}
