package com.example.subscription.service;

import com.example.subscription.entity.Subscription;
import com.example.subscription.entity.User;
import com.example.subscription.repository.SubscriptionRepository;
import com.example.subscription.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionService.class);

    private final SubscriptionRepository subscriptionRepo;
    private final UserRepository userRepo;

    public Subscription addSubscription(Long userId, String name) {
        logger.info("Adding subscription '{}' to user ID {}", name, userId);
        User user = userRepo.findById(userId)
                .orElseThrow(() -> {
                    logger.warn("User with ID {} not found when adding subscription", userId);
                    return new IllegalArgumentException("User not found with ID " + userId);
                });

        Subscription sub = new Subscription(null, name, user);
        Subscription saved = subscriptionRepo.save(sub);
        logger.debug("Saved subscription: {}", saved);
        return saved;
    }

    public List<Subscription> getSubscriptions(Long userId) {
        logger.info("Getting subscriptions for user ID {}", userId);
        return subscriptionRepo.findByUserId(userId);
    }

    public List<Subscription> getAllSubscriptions() {
        logger.info("Fetching all subscriptions");
        return subscriptionRepo.findAll();
    }

    public void removeSubscription(Long subId) {
        logger.info("Removing subscription ID {}", subId);
        subscriptionRepo.deleteById(subId);
        logger.debug("Subscription ID {} removed", subId);
    }

    public List<Object[]> getTopSubscriptions() {
        logger.info("Fetching top 3 subscriptions");
        List<Object[]> result = subscriptionRepo.findTopSubscriptions().stream().limit(3).toList();
        logger.debug("Top subscriptions result: {}", result);
        return result;
    }
}
