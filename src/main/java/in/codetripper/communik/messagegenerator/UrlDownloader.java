package in.codetripper.communik.messagegenerator;

import java.io.IOException;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class UrlDownloader implements AttachmentDownloader {

  @Override
  public Mono<Path> download(String url) {
    Mono<Path> result = null;
    Flux<DataBuffer> data = WebClient.create(url).get()
        //.uri(url)
        .retrieve()
        .bodyToFlux(DataBuffer.class);

    try {
      Path file = Files.createTempFile("communik", null);

      WritableByteChannel channel = Files.newByteChannel(file, StandardOpenOption.WRITE);
      result = DataBufferUtils.write(data, channel)
          .map(DataBufferUtils::release)
          .then(Mono.just(file));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }

}
