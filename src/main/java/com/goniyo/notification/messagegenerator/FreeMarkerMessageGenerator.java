package com.goniyo.notification.messagegenerator;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.validation.constraints.NotNull;
import java.io.IOException;

@Service
@Slf4j
public class FreeMarkerMessageGenerator<T> implements MessageGenerator<T> {
    private final Configuration templates;

    public FreeMarkerMessageGenerator(Configuration templates) {
        this.templates = templates;
    }

    @Override
    public String generateMessage(@NotNull String templateId, @NotNull T notificationMessage) throws MessageGenerationException {
        String message;
        try {
            message = FreeMarkerTemplateUtils.processTemplateIntoString(templates.getTemplate(templateId), notificationMessage);
        } catch (IOException e) {
            throw new MessageGenerationException("Unable to find template", e);
        } catch (TemplateException e) {
            throw new MessageGenerationException("Unable to create message from template", e);
        }
        return message;
    }
}
