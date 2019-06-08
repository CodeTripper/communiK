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

import io.jaegertracing.internal.JaegerTracer;
import io.jaegertracing.internal.reporters.InMemoryReporter;
import io.jaegertracing.internal.samplers.ConstSampler;
import io.jaegertracing.internal.samplers.ProbabilisticSampler;
import io.jaegertracing.spi.Reporter;
import io.jaegertracing.spi.Sampler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class CommunikTracer {

  @Value("${spring.application.name}")
  private String name;
  @Value("${opentracing.jaeger.samplingrate}")
  private double samplingRate;

  @Bean
  @ConditionalOnProperty(value = "opentracing.jaeger.enabled", havingValue = "true",
      matchIfMissing = true)
  public io.opentracing.Tracer tracer() {
    log.info("Building a jaegar tracer with a sampling rate of {}", samplingRate);
    if (samplingRate == 1.0) {
      log.warn(" You should not have jaegar tracer with a sampling rate of {} in production",
          samplingRate);
    }

    return new JaegerTracer.Builder(name).withSampler(new ProbabilisticSampler(samplingRate))
        .build();
  }


  @Bean
  @ConditionalOnProperty(value = "opentracing.jaeger.enabled", havingValue = "false")
  public io.opentracing.Tracer jaegerTracer() {
    log.warn("Building a dummy jaegar tracer as tracing.jaeger.enabled is false");
    final Reporter reporter = new InMemoryReporter();
    final Sampler sampler = new ConstSampler(false);
    return new JaegerTracer.Builder("untraced-service").withReporter(reporter).withSampler(sampler)
        .build();
  }
}
