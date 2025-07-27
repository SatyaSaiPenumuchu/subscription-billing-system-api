package com.subscriptionapi.billing_system.repository;

import com.subscriptionapi.billing_system.model.ApiKey;
import com.subscriptionapi.billing_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
    Optional<ApiKey> findByKey(String key);

    // ✅ Add this method to fix your error
    Optional<ApiKey> findByUser(User user);
}
