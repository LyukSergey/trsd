package com.edu.l21.withSpring;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class Solution4ApplicationContext {

    private final ApplicationContext applicationContext;

    public Solution4ApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void doWork() {
        // Вручну отримуємо bean з контексту
        PrototypeBean prototype = applicationContext.getBean(PrototypeBean.class);
        System.out.println("ApplicationContext - new prototype: " + prototype.hashCode());
    }
}
