package com.prova.e_commerce.security.security1;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface User1Repository extends JpaRepository<User1, Long> {
    Optional<User1> findByUsername(String username);

    boolean existsByUsername(String username);
}
