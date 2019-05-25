package in.codetripper.notification.repository;

import in.codetripper.notification.notification.NotificationMessage;
import in.codetripper.notification.notification.NotificationStorageResponse;
import in.codetripper.notification.repository.mongo.NotificationMessageDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NotificationPersistence<T extends NotificationMessage> {
    Mono<NotificationStorageResponse> store(T notificationMessage);

    Mono<NotificationMessage> update(T notificationMessage);

    Mono<NotificationMessageDto> status(String id);

    Flux<NotificationMessageDto> getAll();
}
