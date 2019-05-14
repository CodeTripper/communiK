package com.goniyo.notification.webhooks;

import com.goniyo.notification.notification.NotificationObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
        // TODO List of webhook listeners, stored as well as runtime
        // iterate through webhookClients and check active compare their interests with o and call the webhook non blocking
    }
}
