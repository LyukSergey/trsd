package com.edu.l21.withSpring;

import org.springframework.stereotype.Service;

@Service
public class Solution3ScopedProxyService {

    // Це насправді proxy, який створює новий екземпляр при кожному виклику
    private final PrototypeBeanWithProxy prototypeBeanWithProxy;

    public Solution3ScopedProxyService(PrototypeBeanWithProxy prototypeBeanWithProxy) {
        this.prototypeBeanWithProxy = prototypeBeanWithProxy;
    }

    public void doWork() {
        System.out.println("ScopedProxy - prototype: " + prototypeBeanWithProxy.hashCode());
    }
}

