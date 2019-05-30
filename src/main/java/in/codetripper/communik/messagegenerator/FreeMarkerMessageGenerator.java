package in.codetripper.communik.messagegenerator;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.io.StringReader;

@Service
@Slf4j
public class FreeMarkerMessageGenerator<T> implements MessageGenerator<T>, HtmlGenerator {


    @Override
    public String generateMessage(String template, T notificationMessage) {
        log.debug("Generating message from template {}", template);
        String message = "";
        try {
            Template templateObj = new Template("name", new StringReader(template), null);
            message = FreeMarkerTemplateUtils.processTemplateIntoString(templateObj, notificationMessage);
            log.debug("Generated message from template with {}", notificationMessage);
        } catch (IOException | TemplateException e) {
            //   throw new MessageGenerationException("Unable to find template", e);
            log.error("Unable to generate message", e);
        }

        return message;

    }


    @Override
    public String generateHtml(String templateId, Object notificationMessage) throws MessageGenerationException {
        // TODO
        return null;
    }
}
