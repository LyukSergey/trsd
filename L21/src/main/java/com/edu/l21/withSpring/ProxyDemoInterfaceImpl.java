package com.edu.l21.withSpring;

import org.springframework.stereotype.Service;

/**
 * Слайд 50-51: JDK Dynamic Proxy
 * Spring створить JDK Dynamic Proxy, бо є інтерфейс
 */
@Service
public class ProxyDemoInterfaceImpl implements ProxyDemoInterface {

    @Override
//    @Transactional
    public void interfaceMethod() {
        System.out.println("Method with JDK Dynamic Proxy");
    }
}

