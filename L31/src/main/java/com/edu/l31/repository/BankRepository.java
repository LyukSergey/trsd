package com.edu.l31.repository;

import com.edu.l31.entity.Bank;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BankRepository extends JpaRepository<Bank, Long> {

    @Transactional
    Optional<Bank> findByUsersId(Long userId);

    @Query("SELECT b FROM Bank b JOIN FETCH b.users WHERE b.name = :name")
    Optional<Bank> findByUsersIdJoinFetch(String name);
}
