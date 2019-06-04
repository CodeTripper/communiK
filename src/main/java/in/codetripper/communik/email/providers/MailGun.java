package in.codetripper.communik.email.providers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import in.codetripper.communik.email.Email;
import in.codetripper.communik.email.EmailNotifier;
import in.codetripper.communik.exceptions.NotificationSendFailedException;
import in.codetripper.communik.notification.NotificationStatusResponse;
import in.codetripper.communik.provider.Provider;
import in.codetripper.communik.provider.ProviderService;
import in.codetripper.communik.trace.WebClientDecorator;
import io.opentracing.Tracer;
import io.opentracing.contrib.spring.web.client.TracingExchangeFilterFunction;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.LocalDateTime;
import java.util.Collections;

import static in.codetripper.communik.email.Constants.MAILGUN;
import static io.netty.util.CharsetUtil.UTF_8;


@Service
@Slf4j
@Qualifier(MAILGUN)
@RequiredArgsConstructor
public class MailGun implements EmailNotifier<Email> {
    private String className = DummyMailer.class.getSimpleName();
    private final ProviderService providerService;
    protected String providerId = "11001";
    private boolean logRequestResponse = false;
    private final Tracer tracer;

    @Override
    public Mono<NotificationStatusResponse> send(Email email) throws NotificationSendFailedException {
        Mono<NotificationStatusResponse> response = null;

        Provider provider = providerService.getProvider(providerId);
        if (provider.getType().equalsIgnoreCase("EMAIL")) {
            log.debug("Sending email via provider: {}", provider);
            MultiValueMap<String, Object> formMap = getStringObjectMultiValueMap(email, provider);
            log.debug("Sending email with data : {}", formMap);
            try {
                WebClient client = WebClient.builder()
                        .filter(new TracingExchangeFilterFunction(tracer, Collections.singletonList(new WebClientDecorator("email.send", className))))
                        .clientConnector(new ReactorClientHttpConnector(
                                HttpClient.create().wiretap(logRequestResponse)
                        ))
                        .baseUrl(provider.getEndpoints().getBase())
                        .build();
                response = client.post()
                        .uri(provider.getEndpoints().getSendUri()).
                                header("Authorization", "Basic " + Base64Utils
                                        .encodeToString((provider.getBasicAuthentication().getUserId() + ":" + provider.getBasicAuthentication().getPassword()).getBytes(UTF_8)))

                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .syncBody(formMap)
                        .retrieve().bodyToMono(MailGunResponse.class).map(mailgunResponse -> {
                            log.debug("mailgunResponse {}", mailgunResponse);
                            NotificationStatusResponse notificationStatusResponse = new NotificationStatusResponse();
                            notificationStatusResponse.setStatus(200);
                            notificationStatusResponse.setTimestamp(LocalDateTime.now());
                            notificationStatusResponse.setProviderResponseId(mailgunResponse.getId());
                            notificationStatusResponse.setProviderResponseMessage(mailgunResponse.getMessage());
                            return notificationStatusResponse;
                        }).doOnSuccess((message -> log.debug("sent email via MailGun successfully"))).doOnError((error -> {
                            log.error("email via MailGun failed ", error);
                        }));
            } catch (WebClientException webClientException) {
                log.error("webClientException");
                throw new NotificationSendFailedException("webClientException received", webClientException);
            }
        } else {
            log.warn("Wrong providerid {} configured for {} ", providerId, DummyMailer.class);
        }
        return response;
    }

    private MultiValueMap<String, Object> getStringObjectMultiValueMap(Email email, Provider provider) {
        MultiValueMap<String, Object> formMap = new LinkedMultiValueMap<>();
        formMap.add("from", provider.getFrom());
        if (email.getCc() != null) {
            formMap.add("cc", email.getCc());
        }
        if (email.getBcc() != null) {
            formMap.add("bcc", email.getBcc());
        }

        formMap.add("subject", email.getSubject());
        formMap.add("to", email.getTo());
        formMap.add("html", email.getBodyTobeSent());
        //formMap.add("attachment", email.getA());
        return formMap;
    }

    @Override
    public boolean isDefault() {
        Provider provider = providerService.getProvider(providerId);
        if (provider != null) {
            return providerService.getProvider(providerId).isPrimary();
        } else {
            return false;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class MailGunResponse {
        private boolean status;
        private String id;
        private String message;
    }


}
