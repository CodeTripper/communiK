package in.codetripper.communik.repository;

import in.codetripper.communik.notification.NotificationMessage;
import in.codetripper.communik.notification.NotificationStorageResponse;
import in.codetripper.communik.repository.mongo.NotificationMessageDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NotificationPersistence<T extends NotificationMessage> {
    Mono<NotificationStorageResponse> store(T notificationMessage);

    Mono<NotificationMessage> update(T notificationMessage);

    Mono<NotificationMessageDto> status(String id);

    Flux<NotificationMessageDto> getAll();
}
