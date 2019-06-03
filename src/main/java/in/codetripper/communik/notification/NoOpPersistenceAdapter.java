package in.codetripper.communik.notification;

import in.codetripper.communik.repository.mongo.NotificationMessageRepoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Repository
@ConditionalOnProperty(value = "notification.persistence", havingValue = "noop")
public class NoOpPersistenceAdapter implements NotificationPersistence {
    public Mono<NotificationStorageResponse> store(NotificationMessage notificationMessage) {
        NotificationStorageResponse notificationStorageResponse = new NotificationStorageResponse();
        notificationStorageResponse.setStatus(true);
        notificationStorageResponse.setId(UUID.randomUUID().toString());
        return Mono.just(notificationStorageResponse);
    }

    public Mono<NotificationStorageResponse> update(NotificationMessage notificationMessage) {
        NotificationStorageResponse notificationStorageResponse = new NotificationStorageResponse();
        notificationStorageResponse.setId(notificationMessage.getId());
        notificationStorageResponse.setStatus(true);
        return Mono.just(notificationStorageResponse);
    }

    public Mono<NotificationMessageRepoDto> status(String id) {
        return Mono.empty();
    }

    public Flux<NotificationMessageRepoDto> getAll() {
        return Flux.empty();
    }
}
