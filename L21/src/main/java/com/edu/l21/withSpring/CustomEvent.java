package com.edu.l21.withSpring;

import org.springframework.context.ApplicationEvent;

/**
 * Слайд 23-25: ApplicationContext Events
 */
public class CustomEvent extends ApplicationEvent {

    private final String message;

    public CustomEvent(Object source, String message) {
        super(source);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

