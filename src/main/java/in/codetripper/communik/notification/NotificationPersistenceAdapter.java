/*
 * Copyright 2019 CodeTripper
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package in.codetripper.communik.notification;

import static in.codetripper.communik.exceptions.ExceptionConstants.NOTIFICATION_PERSISTENCE_ID_NOT_PRESENT;
import static in.codetripper.communik.exceptions.ExceptionConstants.NOTIFICATION_PERSISTENCE_UNABLE_TO_SAVE;
import static in.codetripper.communik.exceptions.ExceptionConstants.NOTIFICATION_PERSISTENCE_UNABLE_TO_UPDATE;

import in.codetripper.communik.exceptions.NotificationPersistenceException;
import in.codetripper.communik.repository.mongo.NotificationMessageRepoDto;
import in.codetripper.communik.repository.mongo.NotificationMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(value = "notification.persistence", havingValue = "mongo",
        matchIfMissing = true)
public class NotificationPersistenceAdapter implements NotificationPersistence {

    // TODO add hystrix in all methods
    private final NotificationMessageRepository notificationRepository;
    private final NotificationMapper notificationMapper;


    @Override
    public Mono<NotificationStorageResponse> store(NotificationMessage notificationMessage) {

        NotificationMessageRepoDto notificationMessageDto =
                notificationMapper.mapMessageToDto(notificationMessage);
        return notificationRepository.insert(notificationMessageDto).map(message -> {
            NotificationStorageResponse notificationStorageResponse = new NotificationStorageResponse();
            notificationStorageResponse.setId(message.getId());
            notificationStorageResponse.setStatus(true);
            return notificationStorageResponse;
        }).onErrorMap(error -> new NotificationPersistenceException(
                NOTIFICATION_PERSISTENCE_UNABLE_TO_SAVE, error))
                .doOnSuccess((message -> log.debug("Saved message to Mongo with data {}", message)))
                .doOnError(
                        (message -> log
                                .debug("could not save message to Mongo with data {0}", message)));

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
        NotificationMessageRepoDto notificationMessageDto =
                notificationMapper.mapMessageToDto(notificationMessage);
        log.debug("Received for updation to Mongo post mapped data {}", notificationMessageDto);
        return notificationRepository.save(notificationMessageDto).map(message -> {
            NotificationStorageResponse notificationStorageResponse = new NotificationStorageResponse();
            notificationStorageResponse.setId(message.getId());
            notificationStorageResponse.setStatus(true);
            return notificationStorageResponse;
        }).onErrorMap(error -> new NotificationPersistenceException(
                NOTIFICATION_PERSISTENCE_UNABLE_TO_UPDATE, error))
                .doOnSuccess(
                        (message -> log.debug("Updated message to Mongo with data {}", message)))
                .doOnError(
                        (message -> log.debug("could not update message to Mongo with data {0}",
                                message)));
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
