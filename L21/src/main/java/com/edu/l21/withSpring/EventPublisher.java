package com.edu.l21.withSpring;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public EventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publishEvent(String message) {
        System.out.println("Publishing event: " + message);
        CustomEvent event = new CustomEvent(this, message);
        eventPublisher.publishEvent(event);
    }
}

