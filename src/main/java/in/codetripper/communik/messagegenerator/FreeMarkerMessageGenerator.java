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
package in.codetripper.communik.messagegenerator;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FreeMarkerMessageGenerator<T> implements MessageGenerator<T> {

  private final Configuration configuration;

  @Override
  public String generateMessage(String template, T notificationMessage, Locale locale) {
    String message;
    try {
      StringWriter result = new StringWriter();
      Template t = new Template("name", new StringReader(template), configuration);
      Environment env = t.createProcessingEnvironment(notificationMessage, result);
      env.setLocale(locale);
      env.setNumberFormat(",##0.00");
      env.process();
      message = result.toString();
      log.debug("Generated {} message {} from template with {}", locale.toString(), message,
          notificationMessage);

    } catch (IOException | TemplateException e) {
      log.error("Unable to generate message", e);
      throw new MessageGenerationException("Unable to create message from template", e);
    }

    return message;

  }


}
