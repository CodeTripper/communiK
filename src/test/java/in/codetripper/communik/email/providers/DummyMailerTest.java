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

import static in.codetripper.communik.email.Constants.DUMMYMAILER;
import static org.junit.Assert.assertEquals;

import in.codetripper.communik.email.Email;
import in.codetripper.communik.email.EmailNotifier;
import in.codetripper.communik.notification.NotificationStatusResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 9999)
public class DummyMailerTest {

  @Autowired
  @Qualifier(DUMMYMAILER)
  private EmailNotifier service;

  @Test
  public void testDummy() throws Exception {
    Email email = new Email();
    NotificationStatusResponse response =
        (NotificationStatusResponse) this.service.send(email).block();
    assertEquals(200, response.getStatus());

  }


}
