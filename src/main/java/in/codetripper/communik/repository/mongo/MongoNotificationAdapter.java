package in.codetripper.communik.repository.mongo;

import in.codetripper.communik.notification.NotificationMapper;
import in.codetripper.communik.notification.NotificationMessage;
import in.codetripper.communik.notification.NotificationStorageResponse;
import in.codetripper.communik.repository.NotificationPersistence;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class MongoNotificationAdapter implements NotificationPersistence {
    @Autowired
    private MongoNotificationRepository mongoNotificationRepository;
    @Autowired
    private NotificationMapper notificationMapper;

    public MongoNotificationAdapter(MongoNotificationRepository mongoNotificationRepository) {
        this.mongoNotificationRepository = mongoNotificationRepository;
    }

    @Override
    public Mono<NotificationStorageResponse> store(NotificationMessage notificationMessage) {
        NotificationMessageDto notificationMessageDto = notificationMapper.mapMessageToDto(notificationMessage);
        return mongoNotificationRepository.insert(notificationMessageDto).
                map(message -> {
                    NotificationStorageResponse notificationStorageResponse = new NotificationStorageResponse();
                    notificationStorageResponse.setId(message.getId());
                    notificationStorageResponse.setStatus(true);
                    return notificationStorageResponse;
                })
                .doOnSuccess((message -> {
                    log.debug("Saved message to Mongo with data {}", message);

                })).doOnError((message -> {
                    log.debug("could not save message to Mongo with data {}", message);
                }));
        // TODO unable to return error

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
            throw new RuntimeException("Id cant be null");
        }
        log.debug("Received for updation to Mongo data {}", notificationMessage);
        NotificationMessageDto notificationMessageDto = notificationMapper.mapMessageToDto(notificationMessage);
        return mongoNotificationRepository.save(notificationMessageDto).
                map(message -> {
                    NotificationStorageResponse notificationStorageResponse = new NotificationStorageResponse();
                    notificationStorageResponse.setId(message.getId());
                    notificationStorageResponse.setStatus(true);
                    return notificationStorageResponse;
                })
                .doOnSuccess((message -> {
                    log.debug("Updated message to Mongo with data {}", message);

                })).doOnError((message -> {
                    log.debug("could not update message to Mongo with data {}", message);
                }));
    }

    @Override
    public Mono<NotificationMessageDto> status(String id) {
        // TODO add hystrix here
        return mongoNotificationRepository.findById(id);

    }

    @Override
    public Flux<NotificationMessageDto> getAll() {
        return mongoNotificationRepository.findAll();
    }

}
