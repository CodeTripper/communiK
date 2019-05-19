package com.goniyo.notification.repository.mongo;

import com.goniyo.notification.notification.*;
import com.goniyo.notification.repository.NotificationPersistence;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.beans.PropertyChangeEvent;

@Service
@Slf4j
public class MongoDbPersistenceHandler implements NotificationPersistence {
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private MongoRepository mongoRepository;
    @Autowired
    private NotificationMapper notificationMapper;

    public MongoDbPersistenceHandler(MongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Mono<NotificationMessage> store(NotificationMessage notificationMessage) {
        log.debug("I am storing it nowmin" + notificationMessage);
        NotificationMessageDto notificationMessageDto = notificationMapper.mapMessageToDto(notificationMessage);
        return mongoRepository.insert(notificationMessageDto).map(te ->
                notificationMapper.mapDtoToMessage(te)).doOnSuccess((message -> {
            log.debug("#########");
            this.publisher.publishEvent(new NotificationEvent<>(message, true));
        })).doOnError((message -> {
            log.debug("ERROR#########");
            this.publisher.publishEvent(new NotificationEvent<>(message, false));
        }));

    }

    @PostConstruct
    public void init() {
        System.out.println("repo from @service");
    }

    @Override
    public NotificationStorageResponse update(NotificationMessage notificationMessage) {
        // TODO add hystrix here
        // WARN DO NOT UPDATE STATUS OF notificationMessage HERE
        log.debug("updated");
        NotificationMessageDto notificationMessageDto = notificationMapper.mapMessageToDto(notificationMessage);
        mongoRepository.save(notificationMessageDto);
        //notificationMessage.setStatus("UPDATED");
        return new NotificationStorageResponse();
    }

    @Override
    public NotificationStorageResponse status(String id) {
        // TODO add hystrix here
        mongoRepository.findById(id);
        return null;
    }

    @Override
    public Flux<NotificationMessageDto> getAll() {
        return mongoRepository.findAll();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        log.debug("MongoDb: " + evt.getOldValue());
        if (evt.getOldValue().equals(Status.NOTIFICATION_NEW)) {
            this.store((NotificationMessage) evt.getNewValue());
        } else {
            this.update((NotificationMessage) evt.getNewValue());
        }
    }
}
