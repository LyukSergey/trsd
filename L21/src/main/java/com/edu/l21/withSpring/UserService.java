package com.edu.l21.withSpring;

import org.springframework.stereotype.Service;

/**
 * Слайд 17-19: Bean через @Component/@Service
 */
@Service
public class UserService {

    public String getUser(Long id) {
        return "User #" + id;
    }
}

