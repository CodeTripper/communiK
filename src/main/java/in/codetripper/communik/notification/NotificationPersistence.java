package in.codetripper.communik.notification;

import in.codetripper.communik.repository.mongo.NotificationMessageRepoDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NotificationPersistence<T extends NotificationMessage> {
    Mono<NotificationStorageResponse> store(T notificationMessage);

    Mono<NotificationMessage> update(T notificationMessage);

    Mono<NotificationMessageRepoDto> status(String id);

    Flux<NotificationMessageRepoDto> getAll();
}
