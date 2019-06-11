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
package in.codetripper.communik.exceptions;

import static in.codetripper.communik.exceptions.ExceptionConstants.ERROR_BAD_REQUEST;
import static in.codetripper.communik.exceptions.ExceptionConstants.ERROR_UNABLE_TO_PROCESS;

import io.opentracing.Tracer;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

@Slf4j
@Component
class CustomErrorAttributes extends DefaultErrorAttributes {
  private final Tracer tracer;

  private CustomErrorAttributes(Tracer tracer) {
    super(false);
    this.tracer = tracer;
  }

  @Override
  public Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
    final var error = getError(request);
    final var errorAttributes = super.getErrorAttributes(request, false);
    log.debug("tracer {}", tracer.scopeManager().activeSpan());
    if (tracer.scopeManager().activeSpan() != null) {
      errorAttributes.put(ErrorAttribute.TRACE_ID.value,
          tracer.scopeManager().activeSpan().context().toTraceId());
    }
    if (error instanceof NotificationSendFailedException
        || error instanceof NotificationPersistenceException) {
      log.debug("Caught an instance of: {}, err: {}", NotificationSendFailedException.class, error);
      errorAttributes.replace(ErrorAttribute.STATUS.value, 500);
      errorAttributes.replace(ErrorAttribute.ERROR.value, ERROR_UNABLE_TO_PROCESS);
      return errorAttributes;
    } else if (error instanceof InvalidRequestException) {
      log.debug("Caught an instance of: {}, err: {}", InvalidRequestException.class, error);
      errorAttributes.replace(ErrorAttribute.STATUS.value, 400);
      errorAttributes.replace(ErrorAttribute.ERROR.value, ERROR_BAD_REQUEST);
      return errorAttributes;
    }
    return errorAttributes;
  }


  enum ErrorAttribute {
    STATUS("status"), ERROR("error"), TRACE_ID("traceId");

    private final String value;

    ErrorAttribute(String value) {
      this.value = value;
    }
  }

}
