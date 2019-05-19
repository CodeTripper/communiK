package com.goniyo.notification.repository;

import com.goniyo.notification.notification.NotificationMessage;
import com.goniyo.notification.notification.NotificationObserver;
import com.goniyo.notification.notification.NotificationStorageResponse;
import com.goniyo.notification.repository.mongo.NotificationMessageDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NotificationPersistence extends NotificationObserver {
    Mono<NotificationMessage> store(NotificationMessage notificationMessage);

    NotificationStorageResponse update(NotificationMessage notificationMessage);

    NotificationStorageResponse status(String id);

    Flux<NotificationMessageDto> getAll();
}
