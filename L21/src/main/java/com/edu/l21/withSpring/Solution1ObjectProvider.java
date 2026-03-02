package com.edu.l21.withSpring;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

@Service
public class Solution1ObjectProvider {

    private final ObjectProvider<PrototypeBean> prototypeBeanProvider;

    public Solution1ObjectProvider(ObjectProvider<PrototypeBean> prototypeBeanProvider) {
        this.prototypeBeanProvider = prototypeBeanProvider;
    }

    public void doWork() {
        // Отримуємо НОВИЙ екземпляр
        System.out.println("Singleton - prototype: " + this.hashCode());
        PrototypeBean prototype = prototypeBeanProvider.getObject();
        System.out.println("ObjectProvider - new prototype: " + prototype.hashCode());
    }
}
