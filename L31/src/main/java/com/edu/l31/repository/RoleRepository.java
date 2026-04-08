package com.edu.l31.repository;

import com.edu.l31.entity.Role;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

    @Transactional
    List<Role> findByUsersId(Long userId);


}
