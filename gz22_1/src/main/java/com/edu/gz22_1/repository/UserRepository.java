package com.edu.gz22_1.repository;

import com.edu.gz22_1.entity.UserEntity;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class UserRepository {

    private List<UserEntity> users = new ArrayList<>();

    @PostConstruct
    public void init() {
        UserEntity user1 = new UserEntity();
        user1.setId(1L);
        user1.setName("John Doe");
        user1.setAge(19);
        UserEntity user2 = new UserEntity();
        user2.setId(2L);
        user2.setName("John Jones");
        user2.setAge(20);
        users.add(user1);
        users.add(user2);
    }

    public List<UserEntity> getAllUsers() {
        return List.copyOf(users);
    }

    public Optional<UserEntity> getUserById(Long id) {
        return getAllUsers().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    public Optional<UserEntity> save(UserEntity userEntity) {
        if (users.add(userEntity)) {
            return Optional.ofNullable(userEntity);
        }
        return Optional.empty();
    }

    public void deleteUserById(Long id) {
        users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .ifPresent(users::remove);
    }
}
