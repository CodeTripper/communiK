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
package in.codetripper.communik.webhooks;

import in.codetripper.communik.notification.NotificationMessage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

@SuppressWarnings("ALL")
@Component
@Slf4j
public class WebhookHandler implements PropertyChangeListener {

    private List<WebhookClient> webbookClients;

    // initialize webhookClients on startup from DB
    public void addWebhookClient(WebhookClient webhookClient) {
        webbookClients.add(webhookClient);
    }

    public void removeWebhookClient(WebhookClient webhookClient) {
        webbookClients.remove(webhookClient);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        log.debug("Webhook: " + evt.getOldValue());
        hook((NotificationMessage) evt.getNewValue());
        // TODO List of webhook listeners, stored as well as runtime
        // iterate through webhookClients and check active compare their interests with o and call the
        // webhook non blocking

    }

    private boolean hook(NotificationMessage notificationMessage) {
        // TODO Connection refused check
        try {
            WebClient client = WebClient.create("http://localhost:9999");
            String t =
                    client.post().uri("/api/webhook")
                            .body(BodyInserters.fromObject(notificationMessage))
                            .retrieve().bodyToMono(String.class).block();
        } catch (WebClientException webClientException) {
            log.error("webClientException");
        }
        log.debug("webhook called:" + notificationMessage);
        // create payload
        // call post
        return true;
    }
}
