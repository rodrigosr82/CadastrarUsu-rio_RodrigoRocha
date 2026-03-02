package org.example.repository;

import org.example.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpfNormalized);

    User save(User user);
    Optional<User> findById(UUID id);
}