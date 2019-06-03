package in.codetripper.communik.email.providers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import in.codetripper.communik.email.Email;
import in.codetripper.communik.email.EmailNotifier;
import in.codetripper.communik.exceptions.NotificationSendFailedException;
import in.codetripper.communik.notification.NotificationStatusResponse;
import in.codetripper.communik.provider.Provider;
import in.codetripper.communik.provider.ProviderService;
import in.codetripper.communik.trace.WebClientDecorator;
import io.opentracing.Tracer;
import io.opentracing.contrib.spring.web.client.TracingExchangeFilterFunction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.LocalDateTime;
import java.util.*;

import static in.codetripper.communik.email.Constants.SENDGRID;


@Service
@Slf4j
@Qualifier(SENDGRID)
@RequiredArgsConstructor
public class SendGrid implements EmailNotifier<Email> {
    private String className = DummyMailer.class.getSimpleName();
    private final ProviderService providerService;
    protected String providerId = "11002";
    private boolean logRequestResponse = false;
    private final Tracer tracer;
    @Override
    public Mono<NotificationStatusResponse> send(Email email) throws NotificationSendFailedException {
        Mono<NotificationStatusResponse> response = null;
        Provider provider = providerService.getProvider(providerId);
        if (provider.getType().equalsIgnoreCase("EMAIL")) {
            log.debug("Sending email via provider: {}", provider);
            SendGridRequest sendGridRequest = createSendGridRequest(provider, email);
            try {
                WebClient client = WebClient.builder()
                        .clientConnector(new ReactorClientHttpConnector(
                                HttpClient.create().wiretap(logRequestResponse)
                        ))
                        .filter(new TracingExchangeFilterFunction(tracer, Collections.singletonList(new WebClientDecorator("email.send", className))))
                        .baseUrl(provider.getEndpoints().getBase())
                        .build();
                response = client.post()
                        .uri(provider.getEndpoints().getSendUri())
                        .header("Authorization", "Bearer " + provider.getBearerAuthentication().getApiKey())
                        .contentType(MediaType.APPLICATION_JSON)
                        // FIXME empty return?
                        .body(BodyInserters.fromObject(sendGridRequest)).retrieve().bodyToMono(SendGridResponse.class).map(sendGridResponse -> {
                            log.debug("getting response from SendGrid {}", sendGridResponse);
                            NotificationStatusResponse notificationStatusResponse = new NotificationStatusResponse();
                            notificationStatusResponse.setStatus(200);
                            notificationStatusResponse.setTimestamp(LocalDateTime.now());
                            return notificationStatusResponse;
                        }).doOnSuccess((message -> log.debug("sent email via SendGrid successfully {}", message))).doOnError((error -> {
                            log.error("email via SendGrid failed ", error);
                        }));
            } catch (WebClientException webClientException) {
                log.error("webClientException");
                throw new NotificationSendFailedException("webClientException received", webClientException);
            }
        } else {
            log.warn("Wrong providerid {} configured for {} ", providerId, SendGrid.class);
        }
        return response;
    }

    private SendGridRequest createSendGridRequest(Provider provider, Email email) {

        SendGridRequest sendGridRequest = new SendGridRequest();
        Personalization personalization = new Personalization();
        Personalization.EmailEntity from = new Personalization.EmailEntity(provider.getFrom());
        Personalization.EmailEntity to = new Personalization.EmailEntity(email.getTo());
        personalization.addTo(to);
        sendGridRequest.setFrom(from);
        sendGridRequest.setSubject(email.getSubject());
        sendGridRequest.setContent(Arrays.asList(Map.of("type", "text/html", "value", email.getBodyTobeSent())));
        sendGridRequest.setPersonalizations(Arrays.asList(personalization));
        return sendGridRequest;
    }

    @Override
    public boolean isDefault() {
        return providerService.getProvider(providerId).isPrimary();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class SendGridResponse {
        private boolean status;
        private String id;
        private String message;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class SendGridRequest {

        private List<Personalization> personalizations;
        private Personalization.EmailEntity from;
        private String subject;
        private List<Map<String, String>> content;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Personalization {
        @JsonProperty("to")
        private List<EmailEntity> tos;

        @Data
        @AllArgsConstructor
        public static class EmailEntity {
            private String email;
            //private String email;
        }

        @Data
        @AllArgsConstructor
        public static class ToHolder {
            private List<EmailEntity> to;
            //private String email;
        }

        @JsonProperty("to")
        public List<EmailEntity> getTos() {
            if (tos == null)
                return Collections.emptyList();
            return tos;
        }

        public void addTo(Personalization.EmailEntity newEmail) {
            if (tos == null) {
                tos = new ArrayList<>();
                tos.add(newEmail);
            } else {
                tos.add(newEmail);
            }
        }
    }

}

