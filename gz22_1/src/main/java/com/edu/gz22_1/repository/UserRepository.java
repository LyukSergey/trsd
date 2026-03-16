package com.edu.gz22_1.repository;

import com.edu.gz22_1.entity.UserEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class UserRepository {

    public List<UserEntity> getAllUsers() {
        UserEntity user1 = new UserEntity();
        user1.setId(1L);
        user1.setName("John Doe");
        user1.setAge(19);
        UserEntity user2 = new UserEntity();
        user2.setId(2L);
        user2.setName("John Jones");
        user2.setAge(20);
        return List.of(user1, user2);
    }

    public Optional<UserEntity> getUserById(Long id) {
        return getAllUsers().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

}
