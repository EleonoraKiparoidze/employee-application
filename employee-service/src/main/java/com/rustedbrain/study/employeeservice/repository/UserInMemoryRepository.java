package com.rustedbrain.study.employeeservice.repository;

import com.rustedbrain.study.employeeservice.model.security.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserInMemoryRepository {

    private static final AtomicLong ID_SEQUENCE_GENERATOR = new AtomicLong();

    private Map<Long, User> userMap = new HashMap<>();

    public User getById(Long id) {
        return userMap.get(id);
    }

    public Optional<User> findByUsername(String username) {
        return userMap.values().stream().filter(user -> user.getUsername().equals(username)).findAny();
    }

    public User save(User user) {
        user.setId(ID_SEQUENCE_GENERATOR.getAndIncrement());
        userMap.put(user.getId(), user);
        return user;
    }

    public boolean isUsernameUsed(String username) {
        return userMap.values().stream().anyMatch(user -> user.getUsername().equals(username));
    }
}
