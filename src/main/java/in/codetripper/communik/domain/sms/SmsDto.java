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
package in.codetripper.communik.domain.sms;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotEmpty;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import in.codetripper.communik.domain.notification.Type;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
public class SmsDto implements Serializable {

  @NotEmpty(message = "To field cannot be empty")
  private List<String> to;
  private Type type = Type.SMS;
  private Container body;
  private String templateId;
  private String providerName;
  private String locale;
  @JsonIgnore
  private String ipAddress;

  @Data
  @NoArgsConstructor
  public static class Container implements Serializable {

    private String message;
    Map<String, Object> data = new LinkedHashMap<>();

    @JsonAnySetter
    void setDetail(String key, Object value) {
      data.put(key, value);

    }
  }
}
