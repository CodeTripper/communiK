package com.goniyo.notification.template;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TemplateCreatedListener<T> {
    //    @EventListener(condition = "#event.success") TODO filter events
    @EventListener
    @Async
    public void onApplicationEvent(@NonNull T event) {
        log.debug("Event {}", event);
    }
}