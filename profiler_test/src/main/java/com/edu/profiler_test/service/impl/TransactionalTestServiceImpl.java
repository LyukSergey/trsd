package com.edu.profiler_test.service.impl;

import com.edu.profiler_test.service.TransactionalTestService;
import org.springframework.stereotype.Service;

@Service
public class TransactionalTestServiceImpl implements TransactionalTestService {

    @Override
    public void transactionalTestMethod() {
        System.out.println("Transaction method executed.");
    }
}
