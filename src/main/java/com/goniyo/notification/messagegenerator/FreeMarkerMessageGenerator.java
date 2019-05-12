package com.goniyo.notification.messagegenerator;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class FreeMarkerMessageGenerator<T> implements MessageGenerator<T> {
    private final Configuration templates;

    public FreeMarkerMessageGenerator(Configuration templates) {
        this.templates = templates;
    }

    @Override
    public String generateMessage(String templateId, T notificationMessage) {
        Template t = null;
        String message = null;
        try {
            t = templates.getTemplate(templateId);
            Map<String, String> map = new HashMap<>();
            message = FreeMarkerTemplateUtils.processTemplateIntoString(t, notificationMessage);
        } catch (IOException e) {
            // TODO fix execptions
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }

        return message;
    }
}
