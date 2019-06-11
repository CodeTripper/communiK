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
package in.codetripper.communik.domain.provider;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
