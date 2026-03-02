package com.edu.l21.withSpring;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Слайд 26-28: Singleton Scope (за замовчуванням)
 * Один екземпляр на весь контейнер
 */
@Component
@Scope("singleton") // можна не вказувати - це default
public class SingletonBean {

    private int counter = 0;

    public SingletonBean() {
        System.out.println("SingletonBean created: " + this.hashCode());
    }

    public int incrementAndGet() {
        return ++counter;
    }

    public int getCounter() {
        return counter;
    }
}

