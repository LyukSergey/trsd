package com.edu.l21.withSpring;

import org.springframework.stereotype.Component;

@Component
public class DependencyB {

    public void methodB() {
        System.out.println("Method B executed");
    }

}
