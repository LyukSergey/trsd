package com.edu.l31.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.edu.l31.service.impl.UserRegistrationServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TransactionalRollbackIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private UserRegistrationServiceImpl registrationService;
    @Autowired
    private UserRepository userRepository;

    @Test
    void testTransactional_successfulRegistration() {
        var user = registrationService.registerUserWithProfileAndRole(
                "Григорій", "Сковорода",
                "skovoroda@ukr.net", "+380501112233",
                "PHILOSOPHER"
        );

        assertNotNull(user.getId());
        assertNotNull(user.getProfile());
        assertEquals(1, user.getRoles().size());
    }

    @Test
    void testTransactional_rollbackOnError() {
        long countBefore = userRepository.count();

        assertThrows(RuntimeException.class, () ->
                registrationService.registerUserThatFails("Fail", "User", "fail@test.com")
        );

        // Юзер НЕ збережений — транзакція відкотилась
        long countAfter = userRepository.count();
        assertEquals(countBefore, countAfter);
    }


}
