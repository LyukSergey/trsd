package com.edu.l31.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.edu.l31.entity.User;
import com.edu.l31.entity.UserProfile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OneToOneIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;

    @Test
    void testOneToOne_UserHasOneProfile() {
        User user = new User();
        user.setName("Іван");
        user.setSurname("Франко");
        user = userRepository.save(user);

        UserProfile profile = new UserProfile();
        profile.setEmail("franko@ukr.net");
        profile.setPhone("+380991234567");
        profile.setUser(user);
        userProfileRepository.save(profile);

        // Перевіряємо зв'язок
        UserProfile found = userProfileRepository.findByUserId(user.getId()).orElseThrow();
        assertEquals("franko@ukr.net", found.getEmail());
        assertEquals(user.getId(), found.getUser().getId());
    }

}
