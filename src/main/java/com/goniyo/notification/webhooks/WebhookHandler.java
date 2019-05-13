package com.goniyo.notification.webhooks;

import com.goniyo.notification.notification.NotificationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Observable;

@Component
public class WebhookHandler implements NotificationObserver {
    private static final Logger logger = LoggerFactory.getLogger(WebhookHandler.class);

    @Override
    public void update(Observable o, Object status) {
        // TODO List of webhook listeners, stored as well as runtime
        logger.debug("Webhook: " + status);
    }
}
