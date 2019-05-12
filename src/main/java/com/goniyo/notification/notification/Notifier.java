package com.goniyo.notification.notification;

public interface Notifier<T extends NotificationMessage> {
    String send(T notificationMessage);
    // boolean isFallback();
}
