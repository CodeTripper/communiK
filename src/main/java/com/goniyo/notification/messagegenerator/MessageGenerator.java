package com.goniyo.notification.messagegenerator;

import com.goniyo.notification.notification.NotificationMessage;

public interface MessageGenerator {
    String generateMessage(String templateId, NotificationMessage notificationMessage);
}
