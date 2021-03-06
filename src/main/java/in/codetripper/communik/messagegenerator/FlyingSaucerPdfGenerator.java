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

import java.io.ByteArrayOutputStream;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service("pdf")
public class FlyingSaucerPdfGenerator<T> implements MessageGenerator<T, byte[]> {


  public Mono<byte[]> generateMessage(String template, T data, String locale)
      throws MessageGenerationException {
    return Mono.create(sink -> {
      ITextRenderer renderer = new ITextRenderer();
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      renderer.setDocumentFromString(template);
      renderer.layout();
      renderer.createPDF(outputStream);
      sink.success(outputStream.toByteArray());
    });

  }

}
