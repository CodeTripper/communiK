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

import static in.codetripper.communik.Constants.DB_WRITE_TIMEOUT;
import static in.codetripper.communik.Constants.PROVIDER_TIMEOUT;

import in.codetripper.communik.exceptions.NotificationPersistenceException;
import in.codetripper.communik.exceptions.NotificationSendFailedException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * The core domain class. Contains all the business logic to store, forward and retry notifications
 *
 * @author CodeTripper
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class Notification<T extends NotificationMessage> {

  private final NotificationPersistence<T> notificationPersistence;

  public Mono<NotificationStatusResponse> sendNotification(@NonNull T message) {
    log.info("About to persist notification for {}", message.getTo());
    log.debug("About to persist notification for {}", message);
    // TODO how not to save reruns? Use existing id to bypass
    // Schedulers.parallel()
    message.setStatus(Status.NOTIFICATION_NEW);
    return notificationPersistence.store(message).timeout(Duration.ofMillis(DB_WRITE_TIMEOUT))
        .map(status -> {
          message.setStatus(Status.NOTIFICATION_STORED);
          message.setId(status.getId());
          return message;
        }).flatMap(m -> {
          NotificationMessage.Notifiers<T> noti = message.getNotifiers();
          return noti.getPrimary().send(message).timeout(Duration.ofMillis(PROVIDER_TIMEOUT));
        }).onErrorResume(error -> {
          log.info("primary provider failed to send notification ", error);
          if (error instanceof NotificationPersistenceException) {
            return Mono.error(error);
          } else {
            log.warn("retrying again as primary provider failed to send notification");
            NotificationMessage.Notifiers<T> noti = message.getNotifiers();
            Optional<? extends Notifier<T>> backup = noti.getBackup().stream().findFirst();
            if (backup.isPresent()) {
              return backup.get().send(message).timeout(Duration.ofMillis(PROVIDER_TIMEOUT));
            } else {
              return Mono.error(new NotificationSendFailedException(error.getMessage()));
            }
          }

        }).flatMap(status -> update(message)).timeout(Duration.ofMillis(DB_WRITE_TIMEOUT))
        .flatMap(this::createResponse).onErrorResume(
            error -> Mono.error(new NotificationSendFailedException(error.getMessage())));

  }

  private Mono<NotificationStatusResponse> createResponse(NotificationStorageResponse status) {
    NotificationStatusResponse notificationStatusResponse = new NotificationStatusResponse();
    notificationStatusResponse.setResponseId(status.getId());
    notificationStatusResponse.setStatus(200);
    notificationStatusResponse.setMessage("SUCCESS");
    notificationStatusResponse.setTimestamp(LocalDateTime.now());
    return Mono.just(notificationStatusResponse);
  }

  private Mono<NotificationStorageResponse> update(@NonNull T notificationMessage) {
    notificationMessage.setStatus(Status.NOTIFICATION_SUCCESS);
    notificationMessage.setAttempts(1);
    NotificationMessage.Action<T> action = new NotificationMessage.Action<>();
    action.setEnded(LocalDateTime.now());
    action.setStatus(false);
    NotificationMessage.Notifiers<T> primary = notificationMessage.getNotifiers();
    Notifier<T> test = primary.getPrimary();
    action.setNotifier(test.getClass().getName());
    notificationMessage.setActions(List.of(action));
    return notificationPersistence.update(notificationMessage);
  }

}
