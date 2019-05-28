package in.codetripper.communik.notification;

import in.codetripper.communik.repository.mongo.NotificationMessageRepoDto;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
public class NoPersistenceAdapter {
    public Mono<NotificationStorageResponse> store(NotificationMessage notificationMessage) {
        NotificationStorageResponse notificationStorageResponse = new NotificationStorageResponse();
        notificationStorageResponse.setStatus(true);
        notificationStorageResponse.setId(UUID.randomUUID().toString());
        return Mono.just(notificationStorageResponse);
    }

    public Mono<NotificationMessage> update(NotificationMessage notificationMessage) {
        return Mono.empty();
    }

    public Mono<NotificationMessageRepoDto> status(String id) {
        return Mono.empty();
    }

    public Flux<NotificationMessageRepoDto> getAll() {
        return Flux.empty();
    }
}
