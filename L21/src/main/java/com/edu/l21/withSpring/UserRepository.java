package com.edu.l21.withSpring;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    public String findUserById(Long id) {
        return "Found user: " + id;
    }
}
