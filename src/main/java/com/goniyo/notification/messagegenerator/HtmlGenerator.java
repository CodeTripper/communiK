package com.goniyo.notification.messagegenerator;

public interface HtmlGenerator<T> {
    String generateHtml(String templateId, T notificationMessage) throws MessageGenerationException;
}
