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
package in.codetripper.communik.trace;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import io.opentracing.Span;
import io.opentracing.contrib.spring.web.client.WebClientSpanDecorator;
import io.opentracing.tag.Tags;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebClientDecorator implements WebClientSpanDecorator {

  static final String COMPONENT_NAME = "webclient";
  private final String operationName;
  private final String peerService;

  public WebClientDecorator(String operationName, String peerService) {
    this.operationName = operationName;
    this.peerService = peerService;
  }

  @Override
  public void onRequest(final ClientRequest clientRequest, final Span span) {
    span.setOperationName(operationName);
    Tags.COMPONENT.set(span, COMPONENT_NAME);
    Tags.HTTP_URL.set(span, clientRequest.url().toString());
    Tags.HTTP_METHOD.set(span, clientRequest.method().toString());
    Tags.PEER_SERVICE.set(span, peerService);
    if (clientRequest.url().getPort() != -1) {
      Tags.PEER_PORT.set(span, clientRequest.url().getPort());
    }
  }

  @Override
  public void onResponse(final ClientRequest clientRequest, final ClientResponse clientResponse,
      final Span span) {
    Tags.HTTP_STATUS.set(span, clientResponse.rawStatusCode());
  }

  @Override
  public void onError(final ClientRequest clientRequest, final Throwable throwable,
      final Span span) {
    Tags.ERROR.set(span, Boolean.TRUE);
    span.log(errorLogs(throwable));
  }

  @Override
  public void onCancel(final ClientRequest httpRequest, final Span span) {
    final Map<String, Object> logs = new HashMap<>(2);
    logs.put("event", "cancelled");
    logs.put("message", "The subscription was cancelled");
    span.log(logs);
  }

  static Map<String, Object> errorLogs(final Throwable throwable) {
    final Map<String, Object> errorLogs = new HashMap<>(2);
    errorLogs.put("event", Tags.ERROR.getKey());
    errorLogs.put("error.object", throwable);
    return errorLogs;
  }
}
