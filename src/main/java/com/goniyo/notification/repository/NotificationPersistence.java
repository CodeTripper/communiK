package com.goniyo.notification.repository;

import com.goniyo.notification.notification.NotificationMessage;
import com.goniyo.notification.notification.NotificationStorageResponse;
import com.goniyo.notification.repository.mongo.NotificationMessageDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NotificationPersistence<T extends NotificationMessage> {
    Mono<NotificationMessage> store(T notificationMessage);

    NotificationStorageResponse update(T notificationMessage);

    Mono<NotificationMessageDto> status(String id);

    Flux<NotificationMessageDto> getAll();
}
