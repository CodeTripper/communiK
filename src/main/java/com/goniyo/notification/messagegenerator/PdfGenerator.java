package com.goniyo.notification.messagegenerator;

public interface PdfGenerator<T> {
    String generatePdf(String templateId, T notificationMessage) throws MessageGenerationException;
}
