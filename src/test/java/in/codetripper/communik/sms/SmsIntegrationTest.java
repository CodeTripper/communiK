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

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.tomakehurst.wiremock.WireMockServer;
import in.codetripper.communik.Integration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@RunWith(SpringRunner.class)
@Category(Integration.class)
public class SmsIntegrationTest {

  // TODO add in memory mongo
  private SmsSampleRequest smsSampleRequest;
  @Autowired
  private WebTestClient webClient;
  private WireMockServer wireMockServer;

  @Before
  public void setup() throws Exception {
    wireMockServer = new WireMockServer(wireMockConfig().port(9999)); // No-args constructor will
    // start on port 8080, no
    // HTTPS
    wireMockServer.start();

    smsSampleRequest = new SmsSampleRequest();
    smsSampleRequest.setTo(Arrays.asList("9999999999"));
    smsSampleRequest.setLocale("en_IN");
    smsSampleRequest.setTemplateId("sms-test-template");
    smsSampleRequest.setProviderName("dummyMailer");
    Map<String, Object> body = new HashMap<>();
    body.put("to", "Mr HK");
    body.put("message", 125000);
    body.put("timestamp", 125000);
    var dynamicData = new HashMap<>();
    dynamicData.put("mykey", "Mr HK");
    dynamicData.put("mykey1", 125000);
    body.put("mapTest", dynamicData);
    smsSampleRequest.setBody(body);
  }

  @After
  public void clean() {
    wireMockServer.stop();
  }

  @Test
  public void testSms() throws Exception {

    // BDDMockito.given(this.emailService.sendEmail()).willReturn()

    webClient.post().uri("/sms").contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromObject(smsSampleRequest)).exchange().expectStatus().isOk()
        .expectBody(SmsSampleResponse.class).returnResult();

  }


  @JsonIgnoreProperties(ignoreUnknown = false)
  @Data
  @NoArgsConstructor
  public static class SmsSampleResponse {

    private String timestamp;
    private int status;
    private String message;
    private String responseId;
    private String traceId;
    private String providerResponseId;
    private String providerResponseMessage;
  }
}
