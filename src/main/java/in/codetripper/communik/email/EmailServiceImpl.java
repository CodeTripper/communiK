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
package in.codetripper.communik.email;

import static in.codetripper.communik.Constants.DB_READ_TIMEOUT;
import static in.codetripper.communik.exceptions.ExceptionConstants.INVALID_REQUEST_TEMPLATE_MISMATCH;
import static in.codetripper.communik.exceptions.ExceptionConstants.INVALID_REQUEST_TEMPLATE_NOT_FOUND;
import static in.codetripper.communik.exceptions.ExceptionConstants.NOTIFICATION_PERSISTENCE_DB_TIMED_OUT;
import static in.codetripper.communik.exceptions.ExceptionConstants.NO_PRIMARY_PROVIDER;

import com.google.common.base.Strings;
import in.codetripper.communik.exceptions.InvalidRequestException;
import in.codetripper.communik.exceptions.NotificationPersistenceException;
import in.codetripper.communik.exceptions.NotificationSendFailedException;
import in.codetripper.communik.messagegenerator.AttachmentDownloader;
import in.codetripper.communik.messagegenerator.MessageGenerator;
import in.codetripper.communik.notification.Notification;
import in.codetripper.communik.notification.NotificationMessage;
import in.codetripper.communik.notification.NotificationMessage.Attachment;
import in.codetripper.communik.notification.NotificationPersistence;
import in.codetripper.communik.notification.NotificationStatusResponse;
import in.codetripper.communik.repository.mongo.NotificationMessageRepoDto;
import in.codetripper.communik.template.NotificationTemplate;
import in.codetripper.communik.template.NotificationTemplate.Container;
import in.codetripper.communik.template.NotificationTemplateService;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
class EmailServiceImpl implements EmailService {

  private final Notification<Email> notificationHandler;
  private final MessageGenerator<EmailDto> messageGenerator;
  private final Map<String, EmailNotifier<Email>> providers;
  private final NotificationPersistence<Email> notificationPersistence; // TODO here?
  private final EmailMapper emailMapper;
  private final NotificationTemplateService templateService;
  private final AttachmentDownloader attachmentDownloader;

  public Mono<NotificationStatusResponse> sendEmail(EmailDto emailDto) {
    return templateService.get(emailDto.getTemplateId()).timeout(Duration.ofMillis(DB_READ_TIMEOUT))
        .single().map(this::validateTemplate).onErrorMap(this::getThrowable)
        .map(
            template -> new EmailRequest(template, emailDto, emailMapper.emailDtoToEmail(emailDto)))
        .flatMap(this::generateAttachment)
        .map(this::prepareEmail)
        .flatMap(notificationHandler::sendNotification)
        .doOnError(err -> log.error("Error while sending Email", err))
        .onErrorMap(original -> new NotificationSendFailedException(original.getMessage()));
  }

  private Mono<EmailRequest> generateAttachment2(EmailRequest emailRequest) {
    EmailDto emailDto = emailRequest.getEmailDto();
    Email email = emailRequest.getEmail();
    return Mono.just(emailRequest);
  }
  private Throwable getThrowable(Throwable error) {
    log.error("error while processing template", error);
    if (error instanceof TimeoutException) {
      return new NotificationPersistenceException(NOTIFICATION_PERSISTENCE_DB_TIMED_OUT);
    } else {
      return new InvalidRequestException(INVALID_REQUEST_TEMPLATE_NOT_FOUND);
    }
  }

  @Override
  public Flux<NotificationMessageRepoDto> getAllEmails() {
    return notificationPersistence.getAll();
  }

  private Email prepareEmail(EmailRequest emailRequest) {
    EmailDto emailDto = emailRequest.getEmailDto();
    Email email = emailRequest.getEmail();
    NotificationTemplate notificationTemplate = emailRequest.getNotificationTemplate();
    log.debug("email called with provider {} from provider list {}", emailDto.getProviderName(),
        providers);
    String message = generateMessage(notificationTemplate, emailDto);
    //  generateAttachment(notificationTemplate, emailDto);

    if (Strings.isNullOrEmpty(email.getReplyTo())) {
      email.setReplyTo(notificationTemplate.getReplyTo());
    }
    if (Strings.isNullOrEmpty(email.getFrom())) {
      email.setFrom(notificationTemplate.getFrom());
    }
    email.setBodyTobeSent(message);
    NotificationMessage.Notifiers<Email> notifiers = new NotificationMessage.Notifiers<>();
    // TODO do not need to call all for default
    var primaryProvider = providers.entrySet().stream()
        .filter(provider -> provider.getValue().isPrimary()).findFirst();
    var requestedProvider = providers.get(emailDto.getProviderName());
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
    email.setNotifiers(notifiers);
    NotificationMessage.Meta meta = new NotificationMessage.Meta();
    meta.setSenderIp(emailDto.getIpAddress());
    meta.setType(getType());
    meta.setCategory(notificationTemplate.getCategory());
    meta.setLob(notificationTemplate.getLob());
    meta.setCreated(LocalDateTime.now());
    email.setMeta(meta);
    return email;
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


  private String generateMessage(NotificationTemplate notificationTemplate, EmailDto emailDto) {
    Locale userLocale = Strings.isNullOrEmpty(emailDto.getLocale()) ? Locale.getDefault()
        : Locale.forLanguageTag(emailDto.getLocale());
    String message =
        Strings.isNullOrEmpty(emailDto.getTemplateId()) ? emailDto.getBody().getMessage()
            : messageGenerator
                .generateMessage(notificationTemplate.getBody(), emailDto, userLocale);
    log.debug("generated message {}", message);
    return message;

  }

  private Mono<EmailRequest> generateAttachment(EmailRequest emailRequest) {
    EmailDto emailDto = emailRequest.getEmailDto();
    Email email = emailRequest.getEmail();
    NotificationTemplate notificationTemplate = emailRequest.getNotificationTemplate();
    Locale userLocale = Strings.isNullOrEmpty(emailDto.getLocale()) ? Locale.getDefault()
        : Locale.forLanguageTag(emailDto.getLocale());
    //notificationTemplate.getAttachments().stream().forEach(attachement->{
    Container attachmentMetaData = notificationTemplate.getAttachments().get(0); // TODO hard coded
    return attachmentDownloader.download(attachmentMetaData.getSource()).map(path -> {
      try {
        Attachment attachment = new Attachment();
        attachment.setName(attachmentMetaData.getName());
        attachment.setType(attachmentMetaData.getType());
        attachment.setContent(Base64.getEncoder().encodeToString(Files.readAllBytes(path)));
        email.setAttachment(attachment);
        System.out.println("attachment = " + attachment);
        emailRequest.setEmail(email);
        return emailRequest;
      } catch (IOException e) {
        return emailRequest;
      }
    })
        .doOnSuccess(me -> System.out.println("#############################" + me))
        .doOnError(error -> log.error("Error while downloading attachments", error));
  }

  @Data
  @AllArgsConstructor
  public static class EmailRequest {

    private NotificationTemplate notificationTemplate;
    private EmailDto emailDto;
    private Email email;

  }
}
