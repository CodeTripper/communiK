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
package in.codetripper.communik.domain.template;

import static in.codetripper.communik.Constants.CACHE_TEMPLATE;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/*
 * All domain logic to reside here , no reference of DTos here.
 */
@Service
@Slf4j
@RequiredArgsConstructor

public class NotificationTemplateServiceImpl implements NotificationTemplateService {

  private final NotificationTemplatePersistence templatePersistence;

  // TODO add validation
  @Override
  public Mono<NotificationTemplate> create(NotificationTemplate notificationTemplate) {
    notificationTemplate.setCreated(LocalDateTime.now());
    return templatePersistence.create(notificationTemplate);
  }

  @Override
  public Mono<NotificationTemplate> update(NotificationTemplate notificationTemplate) {
    notificationTemplate.setUpdated(LocalDateTime.now());
    notificationTemplate.setActive(true);
    return templatePersistence.update(notificationTemplate);
  }

  @Override
  public Flux<NotificationTemplate> getAll() {
    return templatePersistence.getAll();
  }

  @Override
  @Cacheable(cacheNames = CACHE_TEMPLATE)
  public Mono<NotificationTemplate> get(String id) {
    log.debug("Getting template for id {}", id);
    return templatePersistence.get(id);
  }

  @Override
  public Mono<Void> delete(String id) {
    return templatePersistence.delete(id);
  }

}
