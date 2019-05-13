package com.goniyo.notification.messagegenerator;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;

@Service
public class FreeMarkerMessageGenerator<T> implements MessageGenerator<T> {
    private static final Logger logger = LoggerFactory.getLogger(FreeMarkerMessageGenerator.class);
    private final Configuration templates;

    public FreeMarkerMessageGenerator(Configuration templates) {
        this.templates = templates;
    }

    @Override
    public String generateMessage(String templateId, T notificationMessage) throws MessageGenerationException {
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
