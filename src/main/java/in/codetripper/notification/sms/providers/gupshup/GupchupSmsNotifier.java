package in.codetripper.notification.sms.providers.gupshup;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import in.codetripper.notification.notification.NotificationStatusResponse;
import in.codetripper.notification.notification.Type;
import in.codetripper.notification.provider.Provider;
import in.codetripper.notification.provider.ProviderService;
import in.codetripper.notification.sms.Sms;
import in.codetripper.notification.sms.SmsNotifier;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;


@Service
@Slf4j
@Primary
public class GupchupSmsNotifier implements SmsNotifier<Sms> {
    @Autowired
    private ProviderService providerService;
    private String providerId = "12001"; // TODO Move to configuration

    @Override
    public Mono<NotificationStatusResponse> send(Sms sms) {
        Mono<NotificationStatusResponse> response = null;
        Provider provider = providerService.getProvider(providerId);
        GupchupRequest gupchupRequest = new GupchupRequest();
        gupchupRequest.setBody(sms.getBodyTobeSent());
        gupchupRequest.setTo(sms.getTo());
        // gupchupRequest.setRequestId(sms.get); from where?
        if (provider.getType().equalsIgnoreCase(Type.SMS.toString())) {
            log.debug("Sending sms via provider: {} with data {}", provider, sms);
            try {
                WebClient client = WebClient.create(provider.getEndpoints().getBase());
                response = client.post()
                        .uri(provider.getEndpoints().getSendUri())
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromObject(gupchupRequest)).retrieve().bodyToMono(GupchupResponse.class).map(gupchupResponse -> {
                            NotificationStatusResponse notificationStatusResponse = new NotificationStatusResponse();
                            notificationStatusResponse.setStatus(gupchupResponse.isStatus());
                            notificationStatusResponse.setResponseReceivedAt(LocalDateTime.now());
                            return notificationStatusResponse;
                        }).doOnSuccess((message -> {
                            log.debug("sent sms successfully");
                        })).doOnError((message -> {
                            log.debug("sms sending failed {}", message);
                            NotificationStatusResponse notificationStatusResponse = new NotificationStatusResponse();
                            notificationStatusResponse.setStatus(false);
                            notificationStatusResponse.setResponseReceivedAt(LocalDateTime.now());
                        }));
            } catch (WebClientException webClientException) {
                log.error("webClientException");
            }
        } else {
            log.warn("Wrong providerid {} configured for {} ", providerId, GupchupSmsNotifier.class);
        }
        return response;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class GupchupResponse {
        private boolean status;
        private String responseId;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class GupchupRequest {
        private String to;
        private String body;
        private String requestId;
    }


}
