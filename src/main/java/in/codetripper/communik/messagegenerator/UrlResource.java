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

import java.io.IOException;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service("url")
public class UrlResource {

  public Mono<Path> download(String url) {
    Mono<Path> result = null;
    Flux<DataBuffer> data = WebClient.create(url).get().retrieve().bodyToFlux(DataBuffer.class);
    try {
      log.debug("downloading url {}", url);
      Path file = Files.createTempFile("communik-url", null);
      WritableByteChannel channel = Files.newByteChannel(file, StandardOpenOption.WRITE);
      result =
          DataBufferUtils.write(data, channel).map(DataBufferUtils::release).then(Mono.just(file));
    } catch (IOException e) {
      log.error("Error while downloading", e);
    }
    return result;
  }

}
