package com.edu.l31.service.impl;

import com.edu.l31.entity.Role;
import com.edu.l31.entity.User;
import com.edu.l31.entity.UserProfile;
import com.edu.l31.repository.RoleRepository;
import com.edu.l31.repository.UserProfileRepository;
import com.edu.l31.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegistrationServiceImpl {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public User registerUserWithProfileAndRole(String name, String surname,
            String email, String phone, String roleName) {

        // 1. Створюємо юзера
        User user = new User();
        user.setName(name);
        user.setSurname(surname);
//        user = userRepository.save(user);

        UserProfile profile = new UserProfile();
        profile.setEmail(email);
        profile.setPhone(phone);
        profile.setUser(user);
//        userProfileRepository.save(profile);
        user.setProfile(profile);

        // 3. Знаходимо або створюємо роль (ManyToMany)
        Role role = roleRepository.findByName(roleName)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName(roleName);
                    return roleRepository.save(newRole);
                });
        user.getRoles().add(role);

        return userRepository.save(user);
    }

    /**
     * Приклад з примусовим rollback через RuntimeException
     */
    @Transactional
    public User registerUserThatFails(String name, String surname, String email) {
        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user = userRepository.save(user);

        UserProfile profile = new UserProfile();
        profile.setEmail(email);
        profile.setPhone("invalid");
        profile.setUser(user);
        userProfileRepository.save(profile);

        // Симулюємо помилку — все що вище буде відкочено!
        throw new RuntimeException("Щось пішло не так — транзакція відкотиться!");
    }



}
