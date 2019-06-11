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

import com.google.common.base.Strings;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import in.codetripper.communik.domain.template.NotificationTemplate;
import in.codetripper.communik.domain.template.NotificationTemplateMapper;
import in.codetripper.communik.domain.template.NotificationTemplatePersistence;
import in.codetripper.communik.repository.mongo.NotificationTemplateRepoDto;
import in.codetripper.communik.repository.mongo.NotificationTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(value = "notification.template.location", havingValue = "mongo",
    matchIfMissing = true)
public class NotificationTemplatePersistenceAdaptor implements NotificationTemplatePersistence {

  private final NotificationTemplateRepository templateRepository;
  private final NotificationTemplateMapper templateMapper;

  @Value("${notification.template.default.enabled:false}")
  private Boolean isDefaultTemplateEnabled;

  @Value("${notification.template.default.name}")
  private String defaultTemplateName;

  @Override
  @HystrixCommand()
  public Mono<NotificationTemplate> create(NotificationTemplate notificationTemplate) {
    log.debug("notificationTemplate NotificationTemplateServiceImpl:{}", notificationTemplate);
    NotificationTemplateRepoDto templateRepoDto =
        templateMapper.templateToTemplateRepoDto(notificationTemplate);
    return this.templateRepository.insert(templateRepoDto)
        .map(templateMapper::templateRepoDtotoTemplate);
  }

  @Override
  @HystrixCommand()
  public Mono<NotificationTemplate> update(NotificationTemplate notificationTemplate) {
    log.debug("notificationTemplate NotificationTemplateServiceImpl:{}", notificationTemplate);
    NotificationTemplateRepoDto templateRepoDto =
        templateMapper.templateToTemplateRepoDto(notificationTemplate);
    return this.templateRepository.save(templateRepoDto)
        .map(templateMapper::templateRepoDtotoTemplate);
  }

  @Override
  @HystrixCommand()
  public Flux<NotificationTemplate> getAll() {
    return this.templateRepository.findAll().map(templateMapper::templateRepoDtotoTemplate);
  }

  @Override
  @HystrixCommand()
  public Mono<NotificationTemplate> get(String id) {
    if (Strings.isNullOrEmpty(id) && isDefaultTemplateEnabled) {
      id = getDefaultTemplate();
    }
    return this.templateRepository.findById(id).map(templateMapper::templateRepoDtotoTemplate);
  }

  @Override
  @HystrixCommand()
  public Mono<Void> delete(String id) {
    return this.templateRepository.deleteById(id);
  }

  @Override
  public String getDefaultTemplate() {
    return defaultTemplateName;
  }

}
