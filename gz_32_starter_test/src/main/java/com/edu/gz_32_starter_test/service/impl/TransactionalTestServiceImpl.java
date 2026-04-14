package com.edu.gz_32_starter_test.service.impl;

import com.edu.gz_32_starter_test.service.TransactionalTestService;
import org.springframework.stereotype.Service;

@Service
public class TransactionalTestServiceImpl implements TransactionalTestService {

    @Override
    public void transactionalTestMethod() {
        System.out.println("Transaction method executed.");
    }
}
