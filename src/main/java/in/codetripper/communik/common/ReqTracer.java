package in.codetripper.communik.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReqTracer {

    // private final Tracer tracer;

    public String traceId() {
        // return tracer.currentSpan().context().traceIdString();
        return "";
    }
}