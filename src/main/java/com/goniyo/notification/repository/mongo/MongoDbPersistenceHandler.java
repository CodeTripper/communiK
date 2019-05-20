package com.goniyo.notification.repository.mongo;

import com.goniyo.notification.notification.NotificationMapper;
import com.goniyo.notification.notification.NotificationMessage;
import com.goniyo.notification.notification.NotificationStorageResponse;
import com.goniyo.notification.repository.NotificationPersistence;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class MongoDbPersistenceHandler implements NotificationPersistence {
    @Autowired
    private MongoRepository mongoRepository;
    @Autowired
    private NotificationMapper notificationMapper;

    public MongoDbPersistenceHandler(MongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Mono<NotificationMessage> store(NotificationMessage notificationMessage) {
        NotificationMessageDto notificationMessageDto = notificationMapper.mapMessageToDto(notificationMessage);
        return mongoRepository.insert(notificationMessageDto).map(te ->
                notificationMapper.mapDtoToMessage(te)).doOnSuccess((message -> {
            log.debug("Saved message to Mongo with data {}", message);

        })).doOnError((message -> {
            log.debug("MongoDbPersistenceHandler could not save message to Mongo with data {}", message);
        }));

    }

    @PostConstruct
    public void init() {
        System.out.println("repo from @service");
    }

    @Override
    public NotificationStorageResponse update(NotificationMessage notificationMessage) {
        // TODO add hystrix here
        // TODO mono
        // WARN DO NOT UPDATE STATUS OF notificationMessage HERE
        if (notificationMessage.getId().isBlank()) {
            throw new RuntimeException("Id cant be null");
        }
        log.debug("Received for updation to Mongo data {}", notificationMessage);
        NotificationMessageDto notificationMessageDto = notificationMapper.mapMessageToDto(notificationMessage);
        log.debug("Updated message to Mongo with data {}", notificationMessageDto);
        mongoRepository.save(notificationMessageDto);
        //notificationMessage.setStatus("UPDATED");
        return new NotificationStorageResponse();
    }

    @Override
    public Mono<NotificationMessageDto> status(String id) {
        // TODO add hystrix here
        return mongoRepository.findById(id);

    }

    @Override
    public Flux<NotificationMessageDto> getAll() {
        return mongoRepository.findAll();
    }

}
