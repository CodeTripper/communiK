package com.goniyo.notification.messagegenerator;


public interface MessageGenerator<T> {
    String generateMessage(String template, T notificationMessage) throws MessageGenerationException;
}
