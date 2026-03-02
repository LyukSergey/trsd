package com.edu.l21.withSpring;

import org.springframework.stereotype.Component;

@Component
public class DependencyA {

    public void methodA() {
        System.out.println("Method A executed");
    }

}
