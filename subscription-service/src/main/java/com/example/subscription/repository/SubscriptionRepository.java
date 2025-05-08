package com.example.subscription.repository;

import com.example.subscription.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByUserId(Long userId);

    @Query("SELECT s.name, COUNT(s.id) as count FROM Subscription s GROUP BY s.name ORDER BY count DESC")
    List<Object[]> findTopSubscriptions();
}