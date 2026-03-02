package com.edu.l21.withSpring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SetterInjectionExample {

    private DependencyA dependencyA;

    // Setter Injection - опціональна залежність
    @Autowired(required = false)
    public void setDependencyA(DependencyA dependencyA) {
        this.dependencyA = dependencyA;
    }

    public void doSomething() {
        if (dependencyA != null) {
            dependencyA.methodA();
        }
    }


}
