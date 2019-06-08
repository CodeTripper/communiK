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
package in.codetripper.communik.sms;

import static in.codetripper.communik.Constants.DB_READ_TIMEOUT;
import static in.codetripper.communik.exceptions.ExceptionConstants.INVALID_REQUEST_TEMPLATE_MISMATCH;
import static in.codetripper.communik.exceptions.ExceptionConstants.INVALID_REQUEST_TEMPLATE_NOT_FOUND;
import static in.codetripper.communik.exceptions.ExceptionConstants.NOTIFICATION_PERSISTENCE_DB_TIMED_OUT;
import static in.codetripper.communik.exceptions.ExceptionConstants.NO_PRIMARY_PROVIDER;

import com.google.common.base.Strings;
import in.codetripper.communik.exceptions.InvalidRequestException;
import in.codetripper.communik.exceptions.NotificationPersistenceException;
import in.codetripper.communik.exceptions.NotificationSendFailedException;
import in.codetripper.communik.messagegenerator.MessageGenerator;
import in.codetripper.communik.notification.Notification;
import in.codetripper.communik.notification.NotificationMessage;
import in.codetripper.communik.notification.NotificationStatusResponse;
import in.codetripper.communik.template.NotificationTemplate;
import in.codetripper.communik.template.NotificationTemplateService;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
class SmsServiceImpl implements SmsService {

  private final NotificationTemplateService templateService;
  private final Notification<Sms> notificationHandler;
  private final MessageGenerator<SmsDto> messageGenerator;
  private final Map<String, SmsNotifier<Sms>> providers;
  private final SmsMapper smsMapper;

  @Override
  public Mono<NotificationStatusResponse> sendSms(SmsDto smsDto) {
    // validate all data present in SMS dto?
    return templateService.get(smsDto.getTemplateId()).timeout(Duration.ofMillis(DB_READ_TIMEOUT))
        .single().map(this::validateTemplate).onErrorMap(original -> {
          if (original instanceof TimeoutException) {
            return new NotificationPersistenceException(NOTIFICATION_PERSISTENCE_DB_TIMED_OUT);
          } else {
            return new InvalidRequestException(INVALID_REQUEST_TEMPLATE_NOT_FOUND);
          }
        })
        .onErrorMap(original -> new NotificationSendFailedException(original.getMessage()))
        .map(t -> prepareSms(t, smsDto)).flatMap(notificationHandler::sendNotification)
        .doOnError(err -> log.error("Error while sending Sms", err))
        .onErrorMap(original -> new NotificationSendFailedException(original.getMessage()));
  }

  private Sms prepareSms(NotificationTemplate template, SmsDto smsDto) {
    log.debug("sms called with provider {} for providers {}", smsDto.getProviderName(),
        providers);
    String message = generateMessage(template, smsDto);
    Sms sms = smsMapper.smsDtoToSms(smsDto);

    if (Strings.isNullOrEmpty(sms.getFrom())) {
      sms.setFrom(template.getFrom());
    }

    sms.setBodyTobeSent(message);
    NotificationMessage.Notifiers<Sms> notifiers = new NotificationMessage.Notifiers<>();
    // TODO do not need to call all for default
    var defaultProvider = providers.entrySet().stream()
        .filter(provider -> provider.getValue().isPrimary()).findFirst();

    var requestedProvider = providers.get(smsDto.getProviderName());
    if (requestedProvider == null) {
      if (defaultProvider.isPresent()) {
        requestedProvider = defaultProvider.get().getValue();
      } else {
        throw new RuntimeException(NO_PRIMARY_PROVIDER);
      }
    }
    var backups = providers.entrySet().stream()
        .filter(provider -> defaultProvider
            .map(notifierEntry -> provider.getKey().equalsIgnoreCase(notifierEntry.getKey()))
            .orElse(true))
        .map(Entry::getValue).collect(Collectors.toList());

    notifiers.setPrimary(requestedProvider);
    notifiers.setBackup(backups);
    sms.setNotifiers(notifiers);
    NotificationMessage.Meta meta = new NotificationMessage.Meta();
    meta.setSenderIp(smsDto.getIpAddress());
    meta.setType(getType());
    meta.setCategory(template.getCategory());
    meta.setLob(template.getLob());
    meta.setCreated(LocalDateTime.now());
    sms.setMeta(meta);
    return sms;
  }

  private NotificationTemplate validateTemplate(NotificationTemplate template) {
    log.debug("validating template {}", template);
    if (template == null) {
      throw new InvalidRequestException(INVALID_REQUEST_TEMPLATE_NOT_FOUND);
    } else if (!template.getType().equals(getType())) {
      throw new InvalidRequestException(INVALID_REQUEST_TEMPLATE_MISMATCH);
    }
    return template;
  }

  @Override
  public NotificationStatusResponse getSmsStatus(String id) {
    // notificationHandler.getNotificationStatus(id);
    return null;
  }

  private String generateMessage(NotificationTemplate template, SmsDto smsDto) {

    Locale userLocale = Strings.isNullOrEmpty(smsDto.getLocale()) ? Locale.getDefault()
        : Locale.forLanguageTag(smsDto.getLocale());
    String message = Strings.isNullOrEmpty(smsDto.getTemplateId()) ? smsDto.getBody().getMessage()
        : messageGenerator.generateMessage(template.getBody(), smsDto, userLocale);
    log.debug("generated message {}", message);
    return message;

  }


}
