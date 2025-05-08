package com.example.subscription.service;

import com.example.subscription.entity.User;
import com.example.subscription.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public User create(User user) {
        logger.info("Saving new user: {}", user.getEmail());
        User saved = userRepository.save(user);
        logger.debug("Saved user: {}", saved);
        return saved;
    }

    public Optional<User> get(Long id) {
        logger.info("Fetching user by ID: {}", id);
        return userRepository.findById(id);
    }

    public List<User> getAll() {
        logger.info("Fetching all users");
        return userRepository.findAll();
    }

    @Transactional
    public User update(Long id, User updatedUser) {
        logger.info("Updating user with ID: {}", id);
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("User with ID {} not found for update", id);
                    return new NoSuchElementException("User not found with id " + id);
                });

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());

        User saved = userRepository.save(existingUser);
        logger.debug("Updated user: {}", saved);
        return saved;
    }

    public void delete(Long id) {
        logger.info("Deleting user with ID: {}", id);
        userRepository.deleteById(id);
        logger.debug("User with ID {} deleted", id);
    }
}
