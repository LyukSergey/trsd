package com.edu.l21.withSpring;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * Слайд 41-43: Рішення 3 - Scoped Proxy Spring створить proxy, який делегує виклики до нового екземпляра
 */
@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PrototypeBeanWithProxy {

    private int value;

    public PrototypeBeanWithProxy() {
        System.out.println("PrototypeBeanWithProxy created: " + this.hashCode());
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

