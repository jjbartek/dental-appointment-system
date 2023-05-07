package com.das.repositories;

import com.das.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

    @Query("""
            SELECT u FROM User u
            WHERE LOWER(u.email) LIKE LOWER(concat('%', :value, '%'))
            OR LOWER(u.name) LIKE LOWER(concat('%', :value, '%'))
            """)
    Page<User> findByEmailOrName(@Param("value") String value, Pageable p);
}
