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
import static io.netty.util.CharsetUtil.UTF_8;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Strings;
import in.codetripper.communik.domain.email.EmailId;
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
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;


@Service
@Slf4j
public class MailGun implements EmailNotifier<EmailId> {

  private String providerId = "11001";
  private final Tracer tracer;
  private Provider provider;
  private WebClient client;

  public MailGun(ProviderService providerService, Tracer tracer) {
    this.tracer = tracer;
    provider = providerService.getProvider(providerId);
    String className = DummyMailer.class.getSimpleName();
    client = WebClient.builder()
        .filter(new TracingExchangeFilterFunction(this.tracer,
            Collections
                .singletonList(new WebClientDecorator(TRACE_EMAIL_OPERATION_NAME, className))))
        .clientConnector(new ReactorClientHttpConnector(HttpClient.create().wiretap(false)))
        .baseUrl(provider.getEndpoints().getBase()).build();
  }

  @Override
  public Mono<NotificationStatusResponse> send(NotificationMessage<EmailId> email)
      throws NotificationSendFailedException {
    Mono<NotificationStatusResponse> response = null;

    if (provider.getType().equalsIgnoreCase(Type.EMAIL.toString())) {
      log.debug("Sending email via provider: {}", provider);
      MultiValueMap<String, Object> formMap = getStringObjectMultiValueMap(email, provider);
      log.debug("Sending email with data : {}", formMap);
      try {
        response = client.post().uri(provider.getEndpoints().getSendUri())
            .header("Authorization",
                "Basic " + Base64Utils.encodeToString((provider.getBasicAuthentication().getUserId()
                    + ":" + provider.getBasicAuthentication().getPassword()).getBytes(UTF_8)))

            .contentType(MediaType.MULTIPART_FORM_DATA).syncBody(formMap).retrieve()
            .bodyToMono(MailGunResponse.class).map(this::getNotificationStatusResponse)
            .doOnSuccess(message -> log.debug("sent email via MailGun successfully"))
            .doOnError((error -> log.error("email via MailGun failed ", error)));
      } catch (WebClientException webClientException) {
        log.error("webClientException");
        throw new NotificationSendFailedException("webClientException received",
            webClientException);
      }
    } else {
      log.warn("Wrong providerid {} configured for {} ", providerId, DummyMailer.class);
    }
    return response;
  }

  private NotificationStatusResponse getNotificationStatusResponse(
      MailGunResponse mailgunResponse) {
    log.debug("mailgunResponse {}", mailgunResponse);
    NotificationStatusResponse notificationStatusResponse = new NotificationStatusResponse();
    notificationStatusResponse.setStatus(200);
    notificationStatusResponse.setTimestamp(LocalDateTime.now());
    notificationStatusResponse.setProviderResponseId(mailgunResponse.getId());
    notificationStatusResponse.setProviderResponseMessage(mailgunResponse.getMessage());
    return notificationStatusResponse;
  }

  private MultiValueMap<String, Object> getStringObjectMultiValueMap(
      NotificationMessage<EmailId> email, Provider provider) {
    MultiValueMap<String, Object> formMap = new LinkedMultiValueMap<>();
    EmailId from = email.getFrom();
    if (Strings.isNullOrEmpty(from.getId())) {
      from = (EmailId) provider.getFrom();
    }
    formMap.add("from", from);
    if (email.getCc() != null) {
      formMap.add("cc", email.getCc());
    }
    if (email.getBcc() != null) {
      formMap.add("bcc", email.getBcc());
    }

    formMap.add("subject", email.getSubject());
    // TODO
    // String tos = (String) email.getTo().stream().collect(Collectors.joining(","));
    // formMap.add("to", tos);
    formMap.add("html", email.getBodyTobeSent());
    // formMap.add("attachment", email.getA());
    return formMap;
  }

  @Override
  public boolean isPrimary() {
    return provider != null && provider.isPrimary();
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  @Data
  public static class MailGunResponse implements Serializable {

    private boolean status;
    private String id;
    private String message;
  }


}
