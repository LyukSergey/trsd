package com.edu.l21.withSpring;

import org.springframework.stereotype.Component;

@Component
public class ConstructorInjectionExample {
    private final DependencyA dependencyA;
    private final DependencyB dependencyB;

    // Spring автоматично викличе конструктор
    // @Autowired можна не писати (з Spring 4.3)
    public ConstructorInjectionExample(DependencyA dependencyA, DependencyB dependencyB) {
        this.dependencyA = dependencyA;
        this.dependencyB = dependencyB;
    }

    public void doSomething() {
        dependencyA.methodA();
        dependencyB.methodB();
    }


}
