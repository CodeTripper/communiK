package in.codetripper.communik.notification;

import in.codetripper.communik.repository.mongo.NotificationMessageRepoDto;
import in.codetripper.communik.repository.mongo.NotificationMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

@Service
@Slf4j
@Primary
public class NotificationPersistenceAdapter implements NotificationPersistence {
    @Autowired
    private NotificationMessageRepository notificationRepository;
    @Autowired
    private NotificationMapper notificationMapper;

    public NotificationPersistenceAdapter(NotificationMessageRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

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
                .onErrorMap(error -> new NotificationPersistenceException("Unable to save notification", error))
                .doOnSuccess((message -> {
                    log.debug("Saved message to Mongo with data {}", message);

                })).doOnError((message -> {
                    log.debug("could not save message to Mongo with data {}", message);
                }));

    }

    private Mono<NotificationStorageResponse> getFailure() {
        NotificationStorageResponse notificationStatusResponse = new NotificationStorageResponse();
        notificationStatusResponse.setStatus(false);
        return Mono.just(notificationStatusResponse);
    }

    @PostConstruct
    public void init() {
        System.out.println("repo from @service");
    }

    @Override
    public Mono<NotificationStorageResponse> update(NotificationMessage notificationMessage) {
        // TODO add hystrix here
        // WARN DO NOT UPDATE STATUS OF notificationMessage HERE
        if (notificationMessage.getId().isBlank()) {
            throw new RuntimeException("Id is required");
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
                .onErrorMap(error -> new NotificationPersistenceException("Unable to update notification", error))
                .doOnSuccess((message -> {
                    log.debug("Updated message to Mongo with data {}", message);

                })).doOnError((message -> {
                    log.debug("could not update message to Mongo with data {}", message);
                }));
    }

    @Override
    public Mono<NotificationMessageRepoDto> status(String id) {
        // TODO add hystrix here
        return notificationRepository.findById(id);

    }

    @Override
    public Flux<NotificationMessageRepoDto> getAll() {
        return notificationRepository.findAll();
    }

}
