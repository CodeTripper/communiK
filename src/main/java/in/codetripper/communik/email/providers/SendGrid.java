package in.codetripper.communik.email.providers;

import in.codetripper.communik.email.Email;
import in.codetripper.communik.email.EmailNotifier;
import in.codetripper.communik.notification.NotificationStatusResponse;
import in.codetripper.communik.provider.Provider;
import in.codetripper.communik.provider.ProviderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static in.codetripper.communik.email.Constants.SENDGRID;


@Service
@Slf4j
@Qualifier(SENDGRID)
public class SendGrid implements EmailNotifier<Email> {
    @Autowired
    private ProviderService providerService;
    String providerId = "11002";
    @Override
    public Mono<NotificationStatusResponse> send(Email email) {
        Mono<NotificationStatusResponse> response = null;

        Provider provider = providerService.getProvider(providerId);
        if (provider.getType().equalsIgnoreCase("EMAIL")) {
            log.debug("Sending email via provider: {}", provider);
            try {
                WebClient client = WebClient.create(provider.getEndpoints().getBase());
                response = client.post()
                        .uri(provider.getEndpoints().getSendUri())
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromObject(email)).retrieve().bodyToMono(SendGridResponse.class).map(sendGridResponse -> {
                            NotificationStatusResponse notificationStatusResponse = new NotificationStatusResponse();
                            notificationStatusResponse.setStatus(sendGridResponse.isStatus());
                            notificationStatusResponse.setResponseReceivedAt(LocalDateTime.now());
                            return notificationStatusResponse;
                        }).doOnSuccess((message -> log.debug("sent email via DummyMailer successfully"))).doOnError((message -> {
                            log.debug("email via DummyMailer failed {0}", message);
                            NotificationStatusResponse notificationStatusResponse = new NotificationStatusResponse();
                            notificationStatusResponse.setStatus(false);
                            notificationStatusResponse.setResponseReceivedAt(LocalDateTime.now());
                        }));
            } catch (WebClientException webClientException) {
                log.error("webClientException");
            }
        } else {
            log.warn("Wrong providerid {} configured for {} ", providerId, DummyMailer.class);
        }
        return response;
    }

    @Override
    public boolean isDefault() {
        return providerService.getProvider(providerId).isDefaultProvider();
    }


}
