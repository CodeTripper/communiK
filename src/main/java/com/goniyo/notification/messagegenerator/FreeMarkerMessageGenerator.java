package com.goniyo.notification.messagegenerator;

import com.goniyo.notification.template.TemplateService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.StringReader;

@Service
@Slf4j
public class FreeMarkerMessageGenerator<T> implements MessageGenerator<T> {
    private final Configuration templates;
    @Autowired
    private TemplateService templateService;
    public FreeMarkerMessageGenerator(Configuration templates) {
        this.templates = templates;
    }

    @Override
    public String generateMessage(@NotNull String templateId, @NotNull T notificationMessage) throws MessageGenerationException {
        String message;
        try {
            String t = getTemplate(templateId);
            Template template = new Template("name", new StringReader(t),
                    null);
            message = FreeMarkerTemplateUtils.processTemplateIntoString(template, notificationMessage);
        } catch (IOException e) {
            throw new MessageGenerationException("Unable to find template", e);
        } catch (TemplateException e) {
            throw new MessageGenerationException("Unable to create message from template", e);
        }
        return message;
    }

    private String getTemplate(String id) {
        String template = templateService.get(id).block().getBody();
        return template;
    }
}
