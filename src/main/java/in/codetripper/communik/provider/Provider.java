package in.codetripper.communik.provider;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ToString
public class Provider {
    private String id;
    private String name;
    private String comment;
    private List<String> email;
    private List<String> contact;
    private String type;
    private int timeOutInMs;
    private boolean active;
    private boolean primary;
    private LocalDateTime activatedOn;
    private Endpoints endpoints;
    private String authType;
    private String from;
    private BearerAuthentication bearerAuthentication;
    private BasicAuthentication basicAuthentication;
    private Server server;

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

    @Data
    @NoArgsConstructor
    public static class Server {
        private String host;
        private int port;
        private String protocol;
        private boolean tls;
    }
}

