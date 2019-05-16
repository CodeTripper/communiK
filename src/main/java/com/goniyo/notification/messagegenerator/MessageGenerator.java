package com.goniyo.notification.messagegenerator;

@FunctionalInterface
public interface MessageGenerator<T> {
    String generateMessage(String templateId, T notificationMessage) throws MessageGenerationException;
}
