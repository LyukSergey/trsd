package com.edu.l31.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.edu.l31.entity.Role;
import com.edu.l31.entity.User;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ManyToManyIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Test
    void testManyToMany_UserHasManyRoles() {
        Role admin = new Role();
        admin.setName("ADMIN");
        admin = roleRepository.save(admin);

        Role manager = new Role();
        manager.setName("MANAGER");
        manager = roleRepository.save(manager);

        User user = new User();
        user.setName("Богдан");
        user.setSurname("Хмельницький");
        user.getRoles().add(admin);
        user.getRoles().add(manager);
        user = userRepository.save(user);

        // Перевіряємо що юзер має 2 ролі
        final List<Role> roles = roleRepository.findByUsersId(user.getId());
        assertEquals(2, roles.size());

        // Перевіряємо зворотній зв'язок
        Role foundRole = roleRepository.findByName("ADMIN").orElseThrow();
        assertTrue(userRepository.findByRolesId(foundRole.getId()).stream()
                .anyMatch(u -> u.getSurname().equals("Хмельницький")));
    }


}
