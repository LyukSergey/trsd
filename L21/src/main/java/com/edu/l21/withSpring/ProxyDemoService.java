package com.edu.l21.withSpring;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Слайд 47-49: Spring Proxies (CGLIB) Spring створить CGLIB proxy для методів з @Transactional
 */
@Service
@RequiredArgsConstructor
public class ProxyDemoService {

    private final ProxyDemoService proxyDemoService;


    //    @Transactional
    public void transactionalMethod() {
        System.out.println("This method is wrapped in proxy for transaction management");
        internalMethod(); // Self-invocation - proxy НЕ спрацює!
    }

    public void callTransactionalMethod() {
        // Правильно - виклик ззовні, через proxy
        proxyDemoService.transactionalMethod();
    }

    //    @Transactional
    public void internalMethod() {
        System.out.println("Internal method - proxy won't work on self-invocation!");
    }

}

