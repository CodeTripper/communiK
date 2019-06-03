package in.codetripper.communik;

import io.jaegertracing.internal.JaegerTracer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.mapping.event.LoggingEventListener;

@SpringBootApplication
//ComponentScan("in.codetripper.notification.repository.mongo")
public class CommuniK {

    public static void main(String[] args) {
        SpringApplication.run(CommuniK.class, args);
    }

    @Bean
    public io.opentracing.Tracer tracer() {
        return new JaegerTracer.Builder("communik-service").build();
    }

    public LoggingEventListener mongoEventListener() {
        return new LoggingEventListener();
    }

}
