package com.edu.l31.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.edu.l31.entity.Bank;
import com.edu.l31.entity.User;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OneToManyIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private BankRepository bankRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void testOneToMany_BankHasManyUsers() {
        // Банк з test-data вже існує (id=1), але створимо свій для ізоляції
        Bank bank = new Bank();
        bank.setName("Test Bank");
        bank.setTotalAmount(1000000);
        bank = bankRepository.save(bank);

        User user1 = new User();
        user1.setName("Тарас");
        user1.setSurname("Шевченко");
        user1.setBank(bank);

        User user2 = new User();
        user2.setName("Леся");
        user2.setSurname("Українка");
        user2.setBank(bank);

        userRepository.save(user1);
        userRepository.save(user2);

        // Перевіряємо ManyToOne
        User user = userRepository.findById(user1.getId()).orElseThrow();
        final Optional<Bank> userBank = bankRepository.findByUsersId(user.getId());
        assertEquals("Test Bank", userBank.get().getName());
        //Приклад з AssertJ
        assertThat(userBank.get().getName()).isEqualTo("Test Bank");

        // Перевіряємо OneToMany
        var users = userRepository.findAllByBankId(bank.getId());
        assertEquals(2, users.size());
        assertThat(users).hasSize(2);
    }

}
