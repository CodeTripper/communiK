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

import org.springframework.stereotype.Component;
import io.opentracing.Tracer;
import io.opentracing.contrib.mongo.common.TracingCommandListener;
import io.opentracing.contrib.mongo.common.providers.PrefixSpanNameProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class MongoTraceListener {

  private final Tracer tracer;
  private String PREFIX = "mongo.";

  public TracingCommandListener getListener() {
    log.info("Tracing is enabled for MongoDb");
    // TODO disable mongo document log
    return new TracingCommandListener.Builder(tracer)
        .withSpanNameProvider(new PrefixSpanNameProvider(PREFIX)).build();
  }
}
