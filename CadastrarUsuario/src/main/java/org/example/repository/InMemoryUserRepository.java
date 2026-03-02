package org.example.repository;

import org.example.model.User;

import java.util.*;

public class InMemoryUserRepository implements UserRepository {

    private final Map<UUID, User> storage = new HashMap<>();

    @Override
    public boolean existsByEmail(String email) {
        if (email == null) return false;
        return storage.values().stream().anyMatch(u -> email.equalsIgnoreCase(u.getEmail()));
    }

    @Override
    public boolean existsByCpf(String cpf) {
        if (cpf == null) return false;
        return storage.values().stream().anyMatch(u -> cpf.equals(u.getCpf()));
    }

    @Override
    public User save(User user) {
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }
}