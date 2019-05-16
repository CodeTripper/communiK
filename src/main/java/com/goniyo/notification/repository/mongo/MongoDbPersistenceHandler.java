package com.goniyo.notification.repository.mongo;

import com.goniyo.notification.notification.NotificationMessage;
import com.goniyo.notification.notification.NotificationStorageResponse;
import com.goniyo.notification.notification.Status;
import com.goniyo.notification.repository.NotificationPersistence;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.beans.PropertyChangeEvent;

@Service
@Slf4j
public class MongoDbPersistenceHandler implements NotificationPersistence {
    @Autowired
    private MongoRepository mongoRepository;
    @Autowired
    private TempRepo repo;

    public MongoDbPersistenceHandler(MongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }
    @Override
    public NotificationStorageResponse store(NotificationMessage notificationMessage) {
        log.debug("I am storing it nowmin" + notificationMessage);
        NotificationMessageDto notificationMessageDto = NotificationMessageDto.builder().to(notificationMessage.getTo()).id(notificationMessage.getId()).message(notificationMessage.getMessage()).build();
        mongoRepository.insert(notificationMessageDto).doOnSuccess((email1 -> {
            log.debug("#########");
        })).doOnError((email1 -> {
            log.debug("ERROR#########");
        })).block();
        notificationMessage.setStatus(Status.NOTIFICATION_STORED);
        return new NotificationStorageResponse();
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
        NotificationMessageDto notificationMessageDto = NotificationMessageDto.builder().to(notificationMessage.getTo()).id(notificationMessage.getId()).message(notificationMessage.getMessage()).build();
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
