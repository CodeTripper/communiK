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

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import in.codetripper.communik.trace.MongoTraceListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MongoConfig extends AbstractReactiveMongoConfiguration {

  private final MongoTraceListener mongoTracer;
  @Value("${mongodb.dbname:communik}")
  private String dbName;

  @Value("${mongodb.uri:mongodb://localhost:27017}")
  private String uri;
  @Autowired
  MongoClient mongoClient;

  @Override
  public MongoClient reactiveMongoClient() {
    log.debug("Creating MongoClient with trace");
    MongoClient mongoClient = MongoClients
        .create(MongoClientSettings.builder().applyConnectionString(new ConnectionString(uri))
            .addCommandListener(mongoTracer.getListener()).build());
    return mongoClient;
  }

  @Override
  protected String getDatabaseName() {
    return dbName;
  }

  @Bean
  public ReactiveMongoTemplate reactiveMongoTemplate() {
    return new ReactiveMongoTemplate(reactiveMongoClient(), getDatabaseName());
  }
}
