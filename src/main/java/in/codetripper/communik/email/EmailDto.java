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

import static in.codetripper.communik.exceptions.ExceptionConstants.VALIDATION_EMAIL_EMPTY_EMAIL;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import in.codetripper.communik.notification.Type;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
public class EmailDto implements Serializable {

    @NotEmpty(message = VALIDATION_EMAIL_EMPTY_EMAIL)
    // @Email(message = VALIDATION_EMAIL_INVALID_EMAIL)
    private List<String> to;
    private String subject;
    private Type type = Type.EMAIL;
    private Container body;
    private Container attachment;
    private String templateId;
    private String providerName;

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
