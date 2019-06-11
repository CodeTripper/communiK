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
package in.codetripper.communik.domain.template.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import in.codetripper.communik.domain.template.NotificationTemplate;
import in.codetripper.communik.domain.template.NotificationTemplatePersistence;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@ConditionalOnProperty(value = "notification.template.location", havingValue = "filesystem")
@RequiredArgsConstructor
@Slf4j
public class FileSystemTemplateAdapter implements NotificationTemplatePersistence {

  private final ResourceLoader resourceLoader;

  @Value("${notification.template.default.enabled:false}")
  private Boolean isDefaultTemplateEnabled;

  @Value("${notification.template.default.name}")
  private String defaultTemplateName;

  @Value("${notification.template.location.filesystem:classpath:templates}")
  private String templatePath;


  @Override
  public Mono<NotificationTemplate> create(NotificationTemplate notificationTemplate) {
    return null;
  }

  @Override
  public Mono<NotificationTemplate> update(NotificationTemplate notificationTemplate) {
    return null;
  }

  @Override
  public Flux<NotificationTemplate> getAll() {
    return null;
  }


  @Override
  public Mono<NotificationTemplate> get(String id) {
    if (Strings.isNullOrEmpty(id) && isDefaultTemplateEnabled) {
      id = getDefaultTemplate();
    }
    Resource resource = resourceLoader.getResource(templatePath + "/" + id + ".json");
    log.debug("template resource {}", id);
    NotificationTemplate notificationTemplate = null;
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      notificationTemplate = objectMapper.readValue(resource.getFile(), NotificationTemplate.class);
    } catch (IOException e) {
      return Mono.error(e);
    }
    log.debug("returning template {}", notificationTemplate);
    return Mono.justOrEmpty(notificationTemplate);
  }

  @Override
  public Mono<Void> delete(String id) {
    return null;
  }

  @Override
  public String getDefaultTemplate() {
    return defaultTemplateName;
  }
}
