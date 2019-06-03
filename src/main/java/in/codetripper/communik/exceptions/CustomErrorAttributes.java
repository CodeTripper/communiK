package in.codetripper.communik.exceptions;

import io.opentracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

import static in.codetripper.communik.Constants.ERROR_BAD_REQUEST;
import static in.codetripper.communik.Constants.ERROR_UNABLE_TO_PROCESS;

@Slf4j
@Component
class CustomErrorAttributes extends DefaultErrorAttributes {
    @Autowired
    private final Tracer tracer;

    private CustomErrorAttributes(Tracer tracer) {
        super(false);
        this.tracer = tracer;
    }

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        final var error = getError(request);

        final var errorAttributes = super.getErrorAttributes(request, false);
        errorAttributes.put(ErrorAttribute.TRACE_ID.value, "");
        if (error instanceof NotificationSendFailedException || error instanceof NotificationPersistenceException) {
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
        STATUS("status"),
        ERROR("error"),
        TRACE_ID("traceId");

        private final String value;

        ErrorAttribute(String value) {
            this.value = value;
        }
    }

}