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
package in.codetripper.communik;

import static in.codetripper.communik.Constants.APPLICATION_BASE_PACKAGE;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.mapping.event.LoggingEventListener;

@SpringBootApplication
@ComponentScan(APPLICATION_BASE_PACKAGE)
public class CommuniK {

  public static void main(String[] args) {
    SpringApplication.run(CommuniK.class, args);
  }


  public LoggingEventListener mongoEventListener() {
    return new LoggingEventListener();
  }

}
