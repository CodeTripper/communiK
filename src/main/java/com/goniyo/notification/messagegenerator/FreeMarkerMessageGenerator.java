package com.goniyo.notification.messagegenerator;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

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
    public String generateMessage(@NotNull String template, @NotNull T notificationMessage) throws MessageGenerationException {
        String message;
        try {
            // TODO error/failure handling optimize?
            log.debug("message content {}", notificationMessage);
            Template templateObj = new Template("name", new StringReader(template), null);
            message = FreeMarkerTemplateUtils.processTemplateIntoString(templateObj, notificationMessage);
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
