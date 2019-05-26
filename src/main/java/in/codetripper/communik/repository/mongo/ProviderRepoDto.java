package in.codetripper.communik.repository.mongo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "Providers")
public class ProviderRepoDto {
    private @Id
    String id;
    private String name;
    private String comment;
    private List<String> email;
    private List<String> contact;
    private String type;
    private int timeOutInMs;
    private boolean active;
    private LocalDateTime activatedOn;
    private Endpoints endpoints;
    private String authType;
    private BearerAuthentication bearerAuthentication;
    private BasicAuthentication basicAuthentication;

    @Data
    @NoArgsConstructor
    public static class BearerAuthentication {
        private String apiKey;
        private String authUrl;
    }

    @Data
    @NoArgsConstructor
    public static class BasicAuthentication {
        private String userId;
        private String password;
    }

    @Data
    @NoArgsConstructor
    public static class Endpoints {
        private String base;
        private String sendUri;
        private String statusUri;
    }
}

