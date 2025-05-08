package com.example.subscription.controller;

import com.example.subscription.entity.User;
import com.example.subscription.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @PostMapping
    public User create(@RequestBody User user) {
        logger.info("Creating user with email: {}", user.getEmail());
        User created = userService.create(user);
        logger.debug("Created user: {}", created);
        return created;
    }

    @GetMapping("/{id}")
    public User get(@PathVariable Long id) {
        logger.info("Fetching user with ID: {}", id);
        return userService.get(id)
                .orElseThrow(() -> {
                    logger.warn("User with ID {} not found", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
                });
    }

    @GetMapping
    public List<User> getAll() {
        logger.info("Fetching all users");
        return userService.getAll();
    }

    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody User user) {
        logger.info("Updating user with ID: {}", id);
        User updated = userService.update(id, user);
        logger.debug("Updated user: {}", updated);
        return updated;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        logger.info("Deleting user with ID: {}", id);
        userService.delete(id);
        logger.debug("Deleted user with ID: {}", id);
    }
}
