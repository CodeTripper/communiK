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
package in.codetripper.communik.domain.notification;

import static in.codetripper.communik.exceptions.ExceptionConstants.NOTIFICATION_PERSISTENCE_ID_NOT_PRESENT;
import static in.codetripper.communik.exceptions.ExceptionConstants.NOTIFICATION_PERSISTENCE_UNABLE_TO_SAVE;
import static in.codetripper.communik.exceptions.ExceptionConstants.NOTIFICATION_PERSISTENCE_UNABLE_TO_UPDATE;

import com.google.common.base.Strings;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
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
public class NotificationPersistenceAdapter<T> implements NotificationPersistence<T> {

  private final NotificationMessageRepository<T> notificationRepository;
  private final NotificationMapper<T> notificationMapper;


  @Override
  @HystrixCommand
  public Mono<NotificationStorageResponse> store(NotificationMessage<T> notificationMessage) {

    NotificationMessageRepoDto<T> notificationMessageDto =
        notificationMapper.mapMessageToDto(notificationMessage);
    return notificationRepository.insert(notificationMessageDto).map(this::createResponse)
        .onErrorMap(error -> new NotificationPersistenceException(
            NOTIFICATION_PERSISTENCE_UNABLE_TO_SAVE, error))
        .doOnSuccess(message -> log.debug("Saved message to Mongo with data {}", message))
        .doOnError(error -> log.error("could not save message to Mongo with data ", error));

  }


  @Override
  @HystrixCommand
  public Mono<NotificationStorageResponse> update(NotificationMessage<T> notificationMessage) {
    log.debug("Received for updation to Mongo pre mapped data {}", notificationMessage);
    if (Strings.isNullOrEmpty(notificationMessage.getId())) {
      throw new NotificationPersistenceException(NOTIFICATION_PERSISTENCE_ID_NOT_PRESENT);
    }
    NotificationMessageRepoDto<T> notificationMessageDto =
        notificationMapper.mapMessageToDto(notificationMessage);
    log.debug("Received for updation to Mongo post mapped data {}", notificationMessageDto);
    return notificationRepository.save(notificationMessageDto).map(this::createResponse)
        .onErrorMap(error -> new NotificationPersistenceException(
            NOTIFICATION_PERSISTENCE_UNABLE_TO_UPDATE, error))
        .doOnSuccess(message -> log.debug("Updated message to Mongo with data {}", message))
        .doOnError((error -> log.error("could not update message to Mongo with data ", error)));
  }

  private NotificationStorageResponse createResponse(NotificationMessageRepoDto<T> message) {
    NotificationStorageResponse notificationStorageResponse = new NotificationStorageResponse();
    notificationStorageResponse.setId(message.getId());
    notificationStorageResponse.setStatus(true);
    return notificationStorageResponse;
  }

  @Override
  @HystrixCommand
  public Mono<NotificationMessage<T>> status(String id) {
    // NotificationMessageRepoDto notificationMessageRepoDto = notificationRepository.findById(id);
    return null; // FIXME

  }

  @Override
  @HystrixCommand
  public Flux<NotificationMessage<T>> getAll() {
    // return notificationRepository.findAll();
    return null; // FIXME
  }

}
