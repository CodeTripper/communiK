package in.codetripper.communik.messagegenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("generate-html")
@Slf4j
@RequiredArgsConstructor
public class HtmlAttachmentHandler<T> implements AttachmentHandler<T, byte[]> {

  @Qualifier("html")
  private final MessageGenerator<T, String> messageGenerator;

  @Override
  public Mono<byte[]> get(String source, T data, String locale) {
    return messageGenerator.generateMessage(source, data, locale)
        .map(String::getBytes);
  }
}
