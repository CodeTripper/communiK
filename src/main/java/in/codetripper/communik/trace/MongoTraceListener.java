package in.codetripper.communik.trace;

import io.opentracing.Tracer;
import io.opentracing.contrib.mongo.common.TracingCommandListener;
import io.opentracing.contrib.mongo.common.providers.PrefixSpanNameProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MongoTraceListener {
    private final Tracer tracer;

    public TracingCommandListener getListener() {
        log.info("Tracing is enabled for MongoDb");
        return new TracingCommandListener.Builder(tracer)
                .withSpanNameProvider(new PrefixSpanNameProvider("mongo."))
                .build();
    }
}
