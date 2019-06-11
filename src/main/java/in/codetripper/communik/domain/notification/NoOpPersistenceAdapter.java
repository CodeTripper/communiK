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

import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import in.codetripper.communik.repository.mongo.NotificationMessageRepoDto;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@ConditionalOnProperty(value = "notification.persistence", havingValue = "noop")
public class NoOpPersistenceAdapter implements NotificationPersistence {

  public Mono<NotificationStorageResponse> store(NotificationMessage notificationMessage) {
    NotificationStorageResponse notificationStorageResponse = new NotificationStorageResponse();
    notificationStorageResponse.setStatus(true);
    notificationStorageResponse.setId(UUID.randomUUID().toString());
    return Mono.just(notificationStorageResponse);
  }

  public Mono<NotificationStorageResponse> update(NotificationMessage notificationMessage) {
    NotificationStorageResponse notificationStorageResponse = new NotificationStorageResponse();
    notificationStorageResponse.setId(notificationMessage.getId());
    notificationStorageResponse.setStatus(true);
    return Mono.just(notificationStorageResponse);
  }

  public Mono<NotificationMessageRepoDto> status(String id) {
    return Mono.empty();
  }

  public Flux<NotificationMessageRepoDto> getAll() {
    return Flux.empty();
  }
}
