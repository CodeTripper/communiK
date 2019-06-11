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
package in.codetripper.communik.domain.sms;

import static in.codetripper.communik.Constants.DB_READ_TIMEOUT;
import static in.codetripper.communik.exceptions.ExceptionConstants.INVALID_REQUEST_TEMPLATE_MISMATCH;
import static in.codetripper.communik.exceptions.ExceptionConstants.INVALID_REQUEST_TEMPLATE_NOT_FOUND;
import static in.codetripper.communik.exceptions.ExceptionConstants.NOTIFICATION_PERSISTENCE_DB_TIMED_OUT;
import static in.codetripper.communik.exceptions.ExceptionConstants.NO_PRIMARY_PROVIDER;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.google.common.base.Strings;
import in.codetripper.communik.domain.notification.Notification;
import in.codetripper.communik.domain.notification.NotificationMessage;
import in.codetripper.communik.domain.notification.NotificationPersistence;
import in.codetripper.communik.domain.notification.NotificationStatusResponse;
import in.codetripper.communik.domain.template.NotificationTemplate;
import in.codetripper.communik.domain.template.NotificationTemplateService;
import in.codetripper.communik.exceptions.InvalidRequestException;
import in.codetripper.communik.exceptions.NotificationPersistenceException;
import in.codetripper.communik.messagegenerator.AttachmentHandler;
import in.codetripper.communik.messagegenerator.MessageGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
class SmsServiceImpl implements SmsService {

  private final Notification<NotificationMessage> notificationHandler;
  private final MessageGenerator<SmsDto.Container, String> messageGenerator;
  private final Map<String, SmsNotifier<SmsId>> providers;
  private final NotificationPersistence<NotificationMessage<SmsId>> notificationPersistence; // TODO
  // here?
  private final SmsMapper smsMapper;
  private final NotificationTemplateService templateService;
  private final Map<String, AttachmentHandler<SmsDto.Container, byte[]>> attachmentHandlers;

  @Override
  public Mono<NotificationStatusResponse> sendSms(SmsDto smsDto) {
    log.debug("smsDTO {}", smsDto);
    return templateService.get(smsDto.getTemplateId()).timeout(Duration.ofMillis(DB_READ_TIMEOUT))
        .single().map(this::validateTemplate).onErrorMap(this::getThrowable)
        .map(template -> new SmsRequest(template, smsDto, smsMapper.smsDtoToSms(smsDto)))
        .flatMap(this::generateBody).map(this::prepareSms)
        .flatMap(notificationHandler::sendNotification)
        .doOnError(error -> log.error("Error while sending Sms", error));
  }

  private Throwable getThrowable(Throwable error) {
    log.error("error while processing template", error);
    if (error instanceof TimeoutException) {
      return new NotificationPersistenceException(NOTIFICATION_PERSISTENCE_DB_TIMED_OUT);
    } else {
      return new InvalidRequestException(INVALID_REQUEST_TEMPLATE_NOT_FOUND);
    }
  }


  private NotificationMessage prepareSms(SmsRequest smsRequest) {
    SmsDto smsDto = smsRequest.getSmsDto();
    NotificationMessage<SmsId> sms = smsRequest.getSms();
    NotificationTemplate<SmsId> notificationTemplate = smsRequest.getNotificationTemplate();
    log.debug("sms called with provider {} from provider list {}", smsDto.getProviderName(),
        providers);
    if (sms.getFrom() != null) {
      sms.setFrom(notificationTemplate.getFrom());
    }
    NotificationMessage.Notifiers<SmsId> notifiers = new NotificationMessage.Notifiers<>();
    // TODO do not need to call all for default
    var primaryProvider = providers.entrySet().stream()
        .filter(provider -> provider.getValue().isPrimary()).findFirst();
    var requestedProvider = providers.get(smsDto.getProviderName());
    if (requestedProvider == null) {

      if (primaryProvider.isPresent()) {
        requestedProvider = primaryProvider.get().getValue();
      } else {
        throw new RuntimeException(NO_PRIMARY_PROVIDER);
      }
    }
    var backups = providers.entrySet().stream()
        .filter(provider -> primaryProvider
            .map(notifierEntry -> provider.getKey().equalsIgnoreCase(notifierEntry.getKey()))
            .orElse(true))
        .map(Entry::getValue).collect(Collectors.toList());

    notifiers.setPrimary(requestedProvider);
    notifiers.setBackup(backups);
    sms.setNotifiers(notifiers);
    NotificationMessage.Meta meta = new NotificationMessage.Meta();
    meta.setSenderIp(smsDto.getIpAddress());
    meta.setType(getType());
    meta.setCategory(notificationTemplate.getCategory());
    meta.setLob(notificationTemplate.getLob());
    meta.setCreated(LocalDateTime.now());
    sms.setMeta(meta);
    return sms;
  }

  private NotificationTemplate validateTemplate(NotificationTemplate template) {
    log.debug("validating template {}", template);
    if (template == null) {
      throw new InvalidRequestException(INVALID_REQUEST_TEMPLATE_NOT_FOUND);
    }
    if (!template.getType().equals(getType())) {
      throw new InvalidRequestException(INVALID_REQUEST_TEMPLATE_MISMATCH);
    }
    return template;
  }


  private Mono<SmsRequest> generateBody(SmsRequest smsRequest) {
    SmsDto smsDto = smsRequest.getSmsDto();
    NotificationMessage sms = smsRequest.getSms();
    return getMessage(smsDto).switchIfEmpty(Mono.defer(
        () -> messageGenerator.generateMessage(smsRequest.getNotificationTemplate().getBody(),
            smsDto.getBody(), smsDto.getLocale())))
        .map(t -> {
          sms.setBodyTobeSent(t);
          smsRequest.setSms(sms);
          return smsRequest;
        });

  }

  private Mono<String> getMessage(SmsDto smsDto) {
    Mono<String> message = Mono.empty();
    if (Strings.isNullOrEmpty(smsDto.getTemplateId())) {
      message = Mono.just(smsDto.getBody().getMessage());
    }
    return message;
  }

  @Override
  public NotificationStatusResponse getSmsStatus(String id) {
    return null;
  }


  @Data
  @AllArgsConstructor
  public static class SmsRequest {

    private NotificationTemplate<SmsId> notificationTemplate;
    private SmsDto smsDto;
    private NotificationMessage<SmsId> sms;

  }
}
