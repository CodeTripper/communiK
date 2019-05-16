package com.goniyo.notification.webhooks;

import com.goniyo.notification.notification.NotificationMessage;
import com.goniyo.notification.notification.NotificationObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.beans.PropertyChangeEvent;
import java.util.List;

@Component
@Slf4j
public class WebhookHandler implements NotificationObserver {
    private List<WebhookClient> webbookClients;

    // initialize webhookClients on startup from DB
    public void addWebhookClient(WebhookClient webhookClient) {
        webbookClients.add(webhookClient);
    }

    public void removeWebhookClient(WebhookClient webhookClient) {
        webbookClients.remove(webhookClient);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        log.debug("Webhook: " + evt.getOldValue());
        hook((NotificationMessage) evt.getNewValue());
        // TODO List of webhook listeners, stored as well as runtime
        // iterate through webhookClients and check active compare their interests with o and call the webhook non blocking

    }

    private boolean hook(NotificationMessage notificationMessage) {
        // TODO Connection refused check
        try {
            WebClient client = WebClient.create("http://localhost:9999");
            String t = client
                    .post()
                    .uri("/api/webhook")
                    .body(BodyInserters.fromObject(notificationMessage)).retrieve().bodyToMono(String.class).block();
        } catch (WebClientException webClientException) {
            log.error("webClientException");
        }
        log.debug("webhook called:" + notificationMessage);
        // create payload
        // call post
        return true;
    }
}
