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
package in.codetripper.communik.email;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import in.codetripper.communik.notification.Type;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
public class EmailDto implements Serializable {

  @EmailList
  private List<String> to;
  @NotBlank(message = "Subject must be between {min} and {max} characters long")
  @Size(min = 1, max = 100)
  private String subject;
  @JsonIgnore
  private Type type = Type.EMAIL;
  private Container body;
  private Container attachment;
  @Size(min = 1, max = 100, message = "The templateId must be between {min} and {max} characters long")
  private String templateId;
  @Size(min = 1, max = 100, message = "The providerName must be between {min} and {max} characters long")
  private String providerName;
  @Size(min = 1, max = 100, message = "The locale' must be between {min} and {max} characters long")
  private String locale;
  @Email
  @Size(min = 1, max = 100, message = "The replyTo email id  must be between {min} and {max} characters long")
  private String replyTo;
  @JsonIgnore
  private String ipAddress;
  @Size(min = 1, max = 100, message = "The mediaType must be between {min} and {max} characters long")
  private String mediaType;

  @Data
  @NoArgsConstructor
  public static class Container {

    private String message;
    Map<String, Object> data = new LinkedHashMap<>();

    @JsonAnySetter
    void setDetail(String key, Object value) {
      data.put(key, value);

    }
  }
}
