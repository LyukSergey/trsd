package com.edu.l21.withSpring;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Слайд 23-25: Слухач Events
 */
@Component
public class MyEventListener {

    @EventListener
    public void handleCustomEvent(CustomEvent event) {
        System.out.println("Event received: " + event.getMessage());
    }
}

