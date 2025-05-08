package com.example.subscription.controller;

import com.example.subscription.dto.SubscriptionRequest;
import com.example.subscription.entity.Subscription;
import com.example.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SubscriptionController {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionController.class);
    private final SubscriptionService service;

    @PostMapping("/users/{id}/subscriptions")
    public Subscription add(@PathVariable Long id, @RequestBody SubscriptionRequest request) {
        logger.info("Adding subscription '{}' for user ID {}", request.getName(), id);
        Subscription sub = service.addSubscription(id, request.getName());
        logger.debug("Created subscription: {}", sub);
        return sub;
    }

    @GetMapping("/users/{id}/subscriptions")
    public List<Subscription> get(@PathVariable Long id) {
        logger.info("Fetching subscriptions for user ID {}", id);
        return service.getSubscriptions(id);
    }

    @GetMapping("/subscriptions")
    public List<Subscription> getAll() {
        logger.info("Fetching all subscriptions");
        return service.getAllSubscriptions();
    }

    @DeleteMapping("/users/{id}/subscriptions/{subId}")
    public void delete(@PathVariable Long id, @PathVariable Long subId) {
        logger.info("Deleting subscription ID {} for user ID {}", subId, id);
        service.removeSubscription(subId);
        logger.debug("Deleted subscription ID {}", subId);
    }

    @GetMapping("/subscriptions/top")
    public List<Object[]> top() {
        logger.info("Fetching top subscriptions");
        return service.getTopSubscriptions();
    }
}
