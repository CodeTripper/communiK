package com.goniyo.notification.notification;

import reactor.core.publisher.Mono;

public interface Notifier<T extends NotificationMessage> {
    int getTimeout();

    Mono<NotificationStatusResponse> send(T notificationMessage); // FIXME throws NotificationFailedException;
    // boolean isFallback();
}
