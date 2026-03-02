package com.edu.l21.withSpring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FieldInjectionExample {

    // Field Injection - anti-pattern
    @Autowired
    private DependencyA dependencyA;

    public void doSomething() {
        dependencyA.methodA();
    }
}
