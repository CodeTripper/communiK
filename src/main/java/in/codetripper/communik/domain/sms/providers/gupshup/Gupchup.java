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
package in.codetripper.communik.domain.sms.providers.gupshup;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import in.codetripper.communik.domain.notification.NotificationStatusResponse;
import in.codetripper.communik.domain.notification.Type;
import in.codetripper.communik.domain.provider.Provider;
import in.codetripper.communik.domain.provider.ProviderService;
import in.codetripper.communik.domain.sms.Sms;
import in.codetripper.communik.domain.sms.SmsNotifier;
import in.codetripper.communik.exceptions.NotificationSendFailedException;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;


@Service
@Slf4j
@RequiredArgsConstructor
public class Gupchup implements SmsNotifier<Sms> {

  private final ProviderService providerService;
  String providerId = "12001";

  @Override
  public Mono<NotificationStatusResponse> send(Sms sms) throws NotificationSendFailedException {
    Mono<NotificationStatusResponse> response = null;
    Provider provider = providerService.getProvider(providerId);
    GupchupRequest gupchupRequest = new GupchupRequest();
    gupchupRequest.setBody(sms.getBodyTobeSent());
    String to = (String) sms.getTo().get(0);
    gupchupRequest.setTo(to);
    // gupchupRequest.setResponseId(sms.get); from where?
    if (provider.getType().equalsIgnoreCase(Type.SMS.toString())) {
      log.debug("Sending sms via provider: {} with data {}", provider, sms);
      try {
        WebClient client = WebClient.create(provider.getEndpoints().getBase());
        response = client.post().uri(provider.getEndpoints().getSendUri())
            .contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromObject(gupchupRequest))
            .retrieve().bodyToMono(GupchupResponse.class).map(gupchupResponse -> {
              NotificationStatusResponse notificationStatusResponse =
                  new NotificationStatusResponse();
              notificationStatusResponse.setStatus(200);
              notificationStatusResponse.setTimestamp(LocalDateTime.now());
              return notificationStatusResponse;
            }).doOnSuccess((message -> log.debug("sent sms successfully"))).doOnError((error -> {
              log.debug("sms sending failed", error);
              NotificationStatusResponse notificationStatusResponse =
                  new NotificationStatusResponse();
              notificationStatusResponse.setStatus(500);
              notificationStatusResponse.setTimestamp(LocalDateTime.now());
            }));
      } catch (WebClientException webClientException) {
        log.error("webClientException", webClientException);
        throw new NotificationSendFailedException("webClientException received",
            webClientException);
      } catch (Exception ex) {
        log.error("ex", ex);
        throw new NotificationSendFailedException("webClientException received", ex);
      }
    } else {
      log.warn("Wrong providerid {} configured for {} ", providerId, Gupchup.class);
    }
    return response;
  }

  @Override
  public boolean isPrimary() {
    return providerService.getProvider(providerId).isPrimary();
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  @Data
  public static class GupchupResponse {

    private boolean status;
    private String responseId;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  @Data
  public static class GupchupRequest {

    private String to;
    private String body;
    private String requestId;
  }


}
