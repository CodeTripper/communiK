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

import static in.codetripper.communik.email.Constants.SENDGRID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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
@Qualifier(SENDGRID)
@RequiredArgsConstructor
public class SendGrid implements EmailNotifier<Email> {

  private String className = DummyMailer.class.getSimpleName();
  private final ProviderService providerService;
  protected String providerId = "11002";
  private boolean logRequestResponse = false;
  private final Tracer tracer;

  @Override
  public Mono<NotificationStatusResponse> send(Email email) throws NotificationSendFailedException {

    Mono<NotificationStatusResponse> response = null;
    Provider provider = providerService.getProvider(providerId);
    if (provider.getType().equalsIgnoreCase(Type.EMAIL.toString())) {
      log.debug("Sending email via provider: {}", provider);
      SendGridRequest sendGridRequest = createSendGridRequest(provider, email);
      try {
        WebClient client = WebClient.builder()
            .clientConnector(
                new ReactorClientHttpConnector(HttpClient.create().wiretap(logRequestResponse)))
            .filter(new TracingExchangeFilterFunction(tracer,
                Collections.singletonList(new WebClientDecorator("email.send", className))))
            .baseUrl(provider.getEndpoints().getBase()).build();
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
    sendGridRequest.setPersonalizations(Arrays.asList(personalization));
    return sendGridRequest;
  }

  @Override
  public boolean isPrimary() {
    Provider provider = providerService.getProvider(providerId);
    if (provider != null) {
      return providerService.getProvider(providerId).isPrimary();
    } else {
      return false;
    }
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  @Data
  public static class SendGridRequest {

    private List<Personalization> personalizations;
    private Personalization.EmailEntity from;
    private String subject;
    private List<Map<String, String>> content;
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

    public void addTo(Personalization.EmailEntity newEmail) {
      if (tos == null) {
        tos = new ArrayList<>();
        tos.add(newEmail);
      } else {
        tos.add(newEmail);
      }
    }
  }

}
