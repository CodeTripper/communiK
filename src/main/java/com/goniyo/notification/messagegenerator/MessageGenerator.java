package com.goniyo.notification.messagegenerator;


public interface MessageGenerator<T> {
    String generateMessage(String templateId, T notificationMessage);
}
