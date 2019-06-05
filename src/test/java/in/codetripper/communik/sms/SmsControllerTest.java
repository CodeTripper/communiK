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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import in.codetripper.communik.Unit;
import in.codetripper.communik.notification.NotificationStatusResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = SmsController.class)
@RunWith(SpringRunner.class)
@Category(Unit.class)

public class SmsControllerTest {

  private SmsSampleRequest emailSampleRequest;
  @Autowired
  private WebTestClient webClient;
  @MockBean
  private SmsService smsService;

  @Before
  public void setup() throws Exception {
    String TO = "hkdoley@gmail.com";
    String LOCALE = "en_IN";
    String TEMPLATE = "sms-test-template";
    String PROVIDER = "dummyMailer";
    String MESSAGE = "MR HK";
    emailSampleRequest = new SmsSampleRequest();
    emailSampleRequest.setTo(Arrays.asList(TO));
    emailSampleRequest.setLocale(LOCALE);
    emailSampleRequest.setTemplateId(TEMPLATE);
    emailSampleRequest.setProviderName(PROVIDER);
    Map<String, Object> b1 = new HashMap<>();
    emailSampleRequest.setBody(b1);
    SmsDto smsDto = new SmsDto();
    smsDto.setTo(Arrays.asList(TO));
    smsDto.setLocale(LOCALE);
    smsDto.setProviderName(PROVIDER);
    smsDto.setTemplateId(TEMPLATE);
    SmsDto.Container container = new SmsDto.Container();
    Map<String, Object> b = new HashMap<>();
    container.setData(b);
    smsDto.setBody(container);

    NotificationStatusResponse notificationStatusResponse = new NotificationStatusResponse();
    notificationStatusResponse.setStatus(200);
    notificationStatusResponse.setMessage("SUCCESS");
    notificationStatusResponse.setResponseId(UUID.randomUUID().toString());
    given(smsService.sendSms(smsDto)).willReturn(Mono.just(notificationStatusResponse));
    // given(smsService.sendEmail(any(EmailDto.class))).willReturn(Mono.just(notificationStatusResponse));
  }


  @Test
  public void testSms() throws Exception {

    SmsSampleResponse response =
        webClient.post().uri("/sms").contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromObject(emailSampleRequest)).exchange().expectStatus().isOk()
            .expectBody(SmsSampleResponse.class).returnResult().getResponseBody();

    assertEquals("SUCCESS", response.getMessage());
    assertTrue(!response.getResponseId().isEmpty());
  }


  @JsonIgnoreProperties(ignoreUnknown = true)

  public static class SmsSampleResponse {

    private String timestamp;
    private int status;
    private String message;
    private String responseId;
    private String traceId;
    private String providerResponseId;
    private String providerResponseMessage;

    public String getTimestamp() {
      return timestamp;
    }

    public void setTimestamp(String timestamp) {
      this.timestamp = timestamp;
    }

    public int getStatus() {
      return status;
    }

    public void setStatus(int status) {
      this.status = status;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }

    public String getResponseId() {
      return responseId;
    }

    public void setResponseId(String responseId) {
      this.responseId = responseId;
    }

    public String getTraceId() {
      return traceId;
    }

    public void setTraceId(String traceId) {
      this.traceId = traceId;
    }

    public String getProviderResponseId() {
      return providerResponseId;
    }

    public void setProviderResponseId(String providerResponseId) {
      this.providerResponseId = providerResponseId;
    }

    public String getProviderResponseMessage() {
      return providerResponseMessage;
    }

    public void setProviderResponseMessage(String providerResponseMessage) {
      this.providerResponseMessage = providerResponseMessage;
    }
  }
}
