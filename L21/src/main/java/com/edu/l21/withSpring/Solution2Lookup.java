package com.edu.l21.withSpring;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;

@Service
public abstract class Solution2Lookup {

    // Spring override цей метод і поверне новий екземпляр
    @Lookup
    protected abstract PrototypeBean getPrototypeBean();

    public void doWork() {
        PrototypeBean prototype = getPrototypeBean();
        System.out.println("@Lookup - new prototype: " + prototype.hashCode());
    }
}

