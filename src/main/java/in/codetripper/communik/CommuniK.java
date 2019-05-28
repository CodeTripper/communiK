package in.codetripper.communik;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.core.mapping.event.LoggingEventListener;

@SpringBootApplication
//ComponentScan("in.codetripper.notification.repository.mongo")
@EnableCaching
public class CommuniK {

    public static void main(String[] args) {
        SpringApplication.run(CommuniK.class, args);
    }

    public LoggingEventListener mongoEventListener() {
        return new LoggingEventListener();
    }

}
