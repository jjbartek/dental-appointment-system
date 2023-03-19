package com.das.repositories;

import com.das.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Page<User> findByEmailContainingIgnoreCaseOrNameContainingIgnoreCase(String email, String username, Pageable p);

    default Page<User> findByEmailOrName(String email, String username, Pageable p) {
        return findByEmailContainingIgnoreCaseOrNameContainingIgnoreCase(email, username, p);
    }
}
