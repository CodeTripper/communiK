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
package in.codetripper.communik.template;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NotificationTemplatePersistence {

  Mono<NotificationTemplate> create(NotificationTemplate notificationTemplate);

  Mono<NotificationTemplate> update(NotificationTemplate notificationTemplate);

  Flux<NotificationTemplate> getAll();

  Mono<NotificationTemplate> get(String id);

  Mono<Void> delete(String id);

  String getDefaultTemplate();
}
