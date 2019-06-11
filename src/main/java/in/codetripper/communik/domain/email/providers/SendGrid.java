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
package in.codetripper.communik.domain.email.providers;

import static in.codetripper.communik.Constants.TRACE_EMAIL_OPERATION_NAME;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;
import in.codetripper.communik.domain.email.Email;
import in.codetripper.communik.domain.email.EmailNotifier;
import in.codetripper.communik.domain.notification.NotificationMessage;
import in.codetripper.communik.domain.notification.NotificationStatusResponse;
import in.codetripper.communik.domain.notification.Type;
import in.codetripper.communik.domain.provider.Provider;
import in.codetripper.communik.domain.provider.ProviderService;
import in.codetripper.communik.exceptions.NotificationSendFailedException;
import in.codetripper.communik.trace.WebClientDecorator;
import io.opentracing.Tracer;
import io.opentracing.contrib.spring.web.client.TracingExchangeFilterFunction;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;


@Service
@Slf4j
public class SendGrid implements EmailNotifier<Email> {

  private String providerId = "11002";
  private final Tracer tracer;
  private Provider provider;
  private WebClient client;

  public SendGrid(ProviderService providerService, Tracer tracer) {
    this.tracer = tracer;
    provider = providerService.getProvider(providerId);
    String className = DummyMailer.class.getSimpleName();
    client = WebClient.builder()
        .filter(new TracingExchangeFilterFunction(this.tracer,
            Collections
                .singletonList(new WebClientDecorator(TRACE_EMAIL_OPERATION_NAME, className))))
        .clientConnector(
            new ReactorClientHttpConnector(HttpClient.create().wiretap(true)))
        .baseUrl(provider.getEndpoints().getBase()).build();
  }

  @Override
  public Mono<NotificationStatusResponse> send(Email email) throws NotificationSendFailedException {

    Mono<NotificationStatusResponse> response = null;
    if (provider.getType().equalsIgnoreCase(Type.EMAIL.toString())) {
      log.debug("Sending email via provider: {}", provider);
      SendGridRequest sendGridRequest = createSendGridRequest(provider, email);
      try {
        response = client.post().uri(provider.getEndpoints().getSendUri())
            .header("Authorization", "Bearer " + provider.getBearerAuthentication().getApiKey())
            .contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromObject(sendGridRequest))
            .exchange().map(sgresponse -> {
              log.debug("sgresponse: {}", sgresponse.statusCode().toString());
              NotificationStatusResponse notificationStatusResponse =
                  new NotificationStatusResponse();
              notificationStatusResponse.setTimestamp(LocalDateTime.now());
              if (sgresponse.statusCode().is2xxSuccessful()) {
                notificationStatusResponse.setStatus(200);
              } else {
                throw new NotificationSendFailedException(
                    "Sending mail via sendgrid failed. Reason:"
                        + sgresponse.statusCode().toString());
              }

              return notificationStatusResponse;
            })
            .doOnSuccess((message -> log.debug("sent email via SendGrid successfully {}", message)))
            .doOnError((error -> {
              log.error("email via SendGrid failed ", error);
            }));

      } catch (WebClientException webClientException) {
        log.error("webClientException");
        throw new NotificationSendFailedException("webClientException received",
            webClientException);
      }
    } else {
      log.warn("Wrong providerid {} configured for {} ", providerId, SendGrid.class);
    }
    return response;
  }

  private SendGridRequest createSendGridRequest(Provider provider, Email email) {

    SendGridRequest sendGridRequest = new SendGridRequest();
    Personalization personalization = new Personalization();
    String fromStr = email.getFrom();
    if (Strings.isNullOrEmpty(fromStr)) {
      fromStr = provider.getFrom();
    }
    var from = new Personalization.EmailEntity(fromStr);
    email.getTo().stream().forEach(t -> {
      Personalization.EmailEntity to = new Personalization.EmailEntity((String) t);
      personalization.addTo(to);
    });

    sendGridRequest.setFrom(from);
    sendGridRequest.setSubject(email.getSubject());
    sendGridRequest
        .setContent(Arrays.asList(Map.of("type", "text/html", "value", email.getBodyTobeSent())));
    sendGridRequest.setPersonalizations(Collections.singletonList(personalization));
    List<NotificationMessage.Attachment> atts = email.getAttachments();
    List<Attachment> attachments = atts.stream().map(att -> {
      Attachment sendgridAttachment = new Attachment();
      sendgridAttachment.setDisposition(att.getPlacement());
      sendgridAttachment.setType(att.getMediaType());
      sendgridAttachment.setContent_id(UUID.randomUUID().toString());
      sendgridAttachment.setFileName(att.getName());
      sendgridAttachment.setContent(encodeAttachment(att.getContent()));
      return sendgridAttachment;
    }).collect(Collectors.toList());
    sendGridRequest.setAttachments(attachments);
    return sendGridRequest;
  }

  @Override
  public boolean isPrimary() {
    return provider != null && provider.isPrimary();
  }

  private String encodeAttachment(byte[] attachment) {
    return Base64.getEncoder().encodeToString(attachment);
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  @Data
  public static class SendGridRequest {

    private List<Personalization> personalizations;
    private Personalization.EmailEntity from;
    private String subject;
    private List<Map<String, String>> content;
    private List<SendGrid.Attachment> attachments;
  }

  @Data
  @NoArgsConstructor
  private static class Attachment {

    private String type;
    private String content;
    private String fileName;
    private String disposition;
    private String content_id;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  @Data
  public static class Personalization {

    @JsonProperty("to")
    private List<EmailEntity> tos;

    @Data
    @AllArgsConstructor
    public static class EmailEntity {

      private String email;
      // private String email;
    }

    @Data
    @AllArgsConstructor
    public static class ToHolder {

      private List<EmailEntity> to;
      // private String email;
    }

    @JsonProperty("to")
    public List<EmailEntity> getTos() {
      if (tos == null) {
        return Collections.emptyList();
      }
      return tos;
    }

    private void addTo(Personalization.EmailEntity newEmail) {
      if (tos == null) {
        tos = new ArrayList<>();
        tos.add(newEmail);
      } else {
        tos.add(newEmail);
      }
    }

  }

}
