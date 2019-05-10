package com.goniyo.notification.messagegenerator;

import com.goniyo.notification.notification.NotificationMessage;
import org.springframework.stereotype.Service;

@Service
public class FreeMarkerMessageGenerator implements MessageGenerator {
    @Override
    public String generateMessage(String templateId, NotificationMessage notificationMessage) {
        return null;
    }
}
