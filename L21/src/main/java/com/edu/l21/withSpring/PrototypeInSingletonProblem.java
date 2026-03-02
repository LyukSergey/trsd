package com.edu.l21.withSpring;

import org.springframework.stereotype.Service;

/**
 * Слайд 32-34: ПРОБЛЕМА - Prototype in Singleton Prototype створюється лише раз при створенні Singleton!
 */
@Service
public class PrototypeInSingletonProblem {

    // Проблема: prototype створюється лише один раз
    private final PrototypeBean prototypeBean;

    public PrototypeInSingletonProblem(PrototypeBean prototypeBean) {
        this.prototypeBean = prototypeBean;
        System.out.println("PrototypeInSingletonProblem created");
    }

    public void doWork() {
        System.out.println("Singleton: " + this.hashCode());
        System.out.println("Using prototype: " + prototypeBean.hashCode());
        // Завжди той самий екземпляр!
    }
}

