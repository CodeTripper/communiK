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
package in.codetripper.communik.repository.mongo;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import in.codetripper.communik.domain.provider.Provider;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "Providers")
public class ProviderRepoDto<K> {

  private @Id String id;
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
  private K from;
  private BearerAuthentication bearerAuthentication;
  private BasicAuthentication basicAuthentication;
  private Provider.Server server;

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
