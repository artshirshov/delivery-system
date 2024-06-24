package com.example.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.authservice.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(String name);

    void deleteByName(String name);
}
