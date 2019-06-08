package in.codetripper.communik.messagegenerator;

import java.nio.file.Path;
import reactor.core.publisher.Mono;

public interface AttachmentDownloader {

  Mono<Path> download(String url);

}
