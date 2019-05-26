package in.codetripper.communik.notification;

import in.codetripper.communik.repository.mongo.NotificationMessageRepoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Service
public class NoPersistenceAdapter implements NotificationPersistence {
    @Override
    public Mono<NotificationStorageResponse> store(NotificationMessage notificationMessage) {
        NotificationStorageResponse notificationStorageResponse = new NotificationStorageResponse();
        notificationStorageResponse.setStatus(true);
        notificationStorageResponse.setId(UUID.randomUUID().toString());
        return Mono.just(notificationStorageResponse);
    }

    @Override
    public Mono<NotificationMessage> update(NotificationMessage notificationMessage) {
        return Mono.empty();
    }

    @Override
    public Mono<NotificationMessageRepoDto> status(String id) {
        return Mono.empty();
    }

    @Override
    public Flux<NotificationMessageRepoDto> getAll() {
        return Flux.empty();
    }
}
