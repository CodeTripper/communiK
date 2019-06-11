package in.codetripper.communik.messagegenerator;

import java.io.IOException;
import java.nio.file.Files;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service("download")
public class UrlAttachmentHandler<T> implements AttachmentHandler<T, byte[]> {

  @Qualifier("url")
  private final UrlResource urlResource;

  @Override
  public Mono<byte[]> get(String source, T data, String locale) {
    return urlResource.download(source).map(t -> {
      byte[] bytes = new byte[0];
      try {
        bytes = Files.readAllBytes(t);
      } catch (IOException e) {
        log.error("Error while downloading", e);
      }
      return bytes;
    });
  }
}
