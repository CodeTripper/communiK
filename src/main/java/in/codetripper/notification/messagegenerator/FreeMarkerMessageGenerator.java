package in.codetripper.notification.messagegenerator;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.StringReader;

@Service
@Slf4j
public class FreeMarkerMessageGenerator<T> implements MessageGenerator<T>, HtmlGenerator {
    private final Configuration templates;

    public FreeMarkerMessageGenerator(Configuration templates) {
        this.templates = templates;
    }

    @Override
    public Mono<String> generateMessage(@NotNull String template, @NotNull T notificationMessage) throws MessageGenerationException {
        log.debug("Generating message from template {}", template);
        Mono blockingWrapper = Mono.fromCallable(() -> {
            String message;
            try {
                // TODO error/failure handling optimize/ non blocking?
                Template templateObj = new Template("name", new StringReader(template), null);
                message = FreeMarkerTemplateUtils.processTemplateIntoString(templateObj, notificationMessage);
                log.debug("Generated message from template with {}", notificationMessage);
            } catch (IOException e) {
                throw new MessageGenerationException("Unable to find template", e);
            } catch (TemplateException e) {
                throw new MessageGenerationException("Unable to create message from template", e);
            }
            return message;
        });
        return blockingWrapper;


    }

    @Override
    public String generateBlockingMessage(String template, T notificationMessage) throws MessageGenerationException {
        log.debug("Generating message from template {}", template);
        String message;
        try {
            // TODO error/failure handling optimize/ non blocking?
            Template templateObj = new Template("name", new StringReader(template), null);
            message = FreeMarkerTemplateUtils.processTemplateIntoString(templateObj, notificationMessage);
            log.debug("Generated message from template with {}", notificationMessage);
        } catch (IOException e) {
            throw new MessageGenerationException("Unable to find template", e);
        } catch (TemplateException e) {
            throw new MessageGenerationException("Unable to create message from template", e);
        }
        return message;

    }


    @Override
    public String generateHtml(String templateId, Object notificationMessage) throws MessageGenerationException {
        // TODO
        return null;
    }
}
