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
package in.codetripper.communik.email.providers;

import static in.codetripper.communik.Constants.TRACE_EMAIL_OPERATION_NAME;
import static in.codetripper.communik.email.Constants.DUMMYMAILER;
import static io.netty.util.CharsetUtil.UTF_8;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import in.codetripper.communik.email.Email;
import in.codetripper.communik.email.EmailNotifier;
import in.codetripper.communik.exceptions.NotificationSendFailedException;
import in.codetripper.communik.notification.NotificationStatusResponse;
import in.codetripper.communik.notification.Type;
import in.codetripper.communik.provider.Provider;
import in.codetripper.communik.provider.ProviderService;
import in.codetripper.communik.trace.WebClientDecorator;
import io.opentracing.Tracer;
import io.opentracing.contrib.spring.web.client.TracingExchangeFilterFunction;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;


@Service
@Slf4j
@Qualifier(DUMMYMAILER)
public class DummyMailer implements EmailNotifier<Email> {

  private String providerId = "11404";
  private final Tracer tracer;
  private Provider provider;
  private WebClient webClient;

  public DummyMailer(ProviderService providerService, Tracer tracer) {
    this.tracer = tracer;
    provider = providerService.getProvider(providerId);
    String className = DummyMailer.class.getSimpleName();
    webClient = WebClient.builder()
        .filter(new TracingExchangeFilterFunction(this.tracer,
            Collections
                .singletonList(new WebClientDecorator(TRACE_EMAIL_OPERATION_NAME, className))))
        .clientConnector(
            new ReactorClientHttpConnector(HttpClient.create().wiretap(false)))
        .baseUrl(provider.getEndpoints().getBase()).build();
  }

  @Override
  public Mono<NotificationStatusResponse> send(Email email) throws NotificationSendFailedException {
    log.debug("tracer {} ", tracer.scopeManager().activeSpan().context().toTraceId());
    DummyMailerRequest dummyMailerRequest = new DummyMailerRequest();
    dummyMailerRequest.setTo(email.getTo());
    dummyMailerRequest.setSubject(email.getSubject());
    dummyMailerRequest.setMessage(email.getBodyTobeSent());
    dummyMailerRequest.setCc(email.getCc());
    dummyMailerRequest.setBcc(email.getBcc());
    dummyMailerRequest.setReplyTo(email.getReplyTo());
    Mono<NotificationStatusResponse> response = null;
    if (provider != null && provider.getType().equalsIgnoreCase(Type.EMAIL.toString())) {
      log.debug("Sending email via provider: {}", provider);
      try {

        response = webClient.post().uri(provider.getEndpoints().getSendUri())
            .header("Authorization",
                "Basic " + Base64Utils.encodeToString((provider.getBasicAuthentication().getUserId()
                    + ":" + provider.getBasicAuthentication().getPassword()).getBytes(UTF_8)))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromObject(dummyMailerRequest)).retrieve()
            .bodyToMono(DummyMailerResponse.class).map(sendGridResponse -> {
              NotificationStatusResponse notificationStatusResponse =
                  new NotificationStatusResponse();
              notificationStatusResponse.setStatus(200);
              notificationStatusResponse.setTimestamp(LocalDateTime.now());
              return notificationStatusResponse;
            }).doOnSuccess((message -> log.debug("sent email via DummyMailer successfully")))
            .doOnError((error -> {
              log.error("email via DummyMailer failed ", error);
            }));
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

  @Override
  public boolean isPrimary() {
    return provider != null && provider.isPrimary();
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  @Data
  public static class DummyMailerResponse {

    private boolean status;
    private String id;
    private String message;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  @Data
  public static class DummyMailerRequest {

    private List<String> to;
    private String message;
    private String attachment;
    private String subject;
    private String from;
    private String replyTo;
    private String cc;
    private String bcc;
  }

}
