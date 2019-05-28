package in.codetripper.communik.notification;

import in.codetripper.communik.exceptions.NotificationPersistenceException;
import in.codetripper.communik.repository.mongo.NotificationMessageRepoDto;
import in.codetripper.communik.repository.mongo.NotificationMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static in.codetripper.communik.exceptions.ExceptionConstants.*;

@Service
@Slf4j
@Primary
@RequiredArgsConstructor
public class NotificationPersistenceAdapter implements NotificationPersistence {
    // TODO add hystrix in all methods
    private final NotificationMessageRepository notificationRepository;
    private final NotificationMapper notificationMapper;


    @Override
    public Mono<NotificationStorageResponse> store(NotificationMessage notificationMessage) {
        NotificationMessageRepoDto notificationMessageDto = notificationMapper.mapMessageToDto(notificationMessage);
        return notificationRepository.insert(notificationMessageDto).
                map(message -> {
                    NotificationStorageResponse notificationStorageResponse = new NotificationStorageResponse();
                    notificationStorageResponse.setId(message.getId());
                    notificationStorageResponse.setStatus(true);
                    return notificationStorageResponse;
                })
                .onErrorMap(error -> new NotificationPersistenceException(NOTIFICATION_PERSISTENCE_UNABLE_TO_SAVE, error))
                .doOnSuccess((message -> log.debug("Saved message to Mongo with data {}", message))).doOnError((message -> log.debug("could not save message to Mongo with data {0}", message)));

    }

    private Mono<NotificationStorageResponse> getFailure() {
        NotificationStorageResponse notificationStatusResponse = new NotificationStorageResponse();
        notificationStatusResponse.setStatus(false);
        return Mono.just(notificationStatusResponse);
    }

    @Override
    public Mono<NotificationStorageResponse> update(NotificationMessage notificationMessage) {
        if (notificationMessage.getId().isBlank()) {
            new NotificationPersistenceException(NOTIFICATION_PERSISTENCE_ID_NOT_PRESENT);
        }
        log.debug("Received for updation to Mongo pre mapped data {}", notificationMessage);
        NotificationMessageRepoDto notificationMessageDto = notificationMapper.mapMessageToDto(notificationMessage);
        log.debug("Received for updation to Mongo post mapped data {}", notificationMessageDto);
        return notificationRepository.save(notificationMessageDto).
                map(message -> {
                    NotificationStorageResponse notificationStorageResponse = new NotificationStorageResponse();
                    notificationStorageResponse.setId(message.getId());
                    notificationStorageResponse.setStatus(true);
                    return notificationStorageResponse;
                })
                .onErrorMap(error -> new NotificationPersistenceException(NOTIFICATION_PERSISTENCE_UNABLE_TO_UPDATE, error))
                .doOnSuccess((message -> log.debug("Updated message to Mongo with data {}", message))).doOnError((message -> log.debug("could not update message to Mongo with data {0}", message)));
    }

    @Override
    public Mono<NotificationMessageRepoDto> status(String id) {
        return notificationRepository.findById(id);

    }

    @Override
    public Flux<NotificationMessageRepoDto> getAll() {
        return notificationRepository.findAll();
    }

}
