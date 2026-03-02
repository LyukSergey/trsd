package com.edu.l21.withSpring;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class PrototypeBean {

    private int value;

    public PrototypeBean() {
        System.out.println("PrototypeBean created: " + this.hashCode());
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

