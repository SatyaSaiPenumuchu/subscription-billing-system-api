package com.subscriptionapi.billing_system.repository;

import com.subscriptionapi.billing_system.model.ApiUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ApiUsageRepository extends JpaRepository<ApiUsage, Long> {

    List<ApiUsage> findByApiKey_Id(Long apiKeyId);

    List<ApiUsage> findByApiKey_IdAndTimestampBetween(Long apiKeyId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT a.apiKey.key, COUNT(a) FROM ApiUsage a GROUP BY a.apiKey.key")
    List<Object[]> countRequestsGroupedByApiKey();

    @Query("SELECT DATE(a.timestamp), COUNT(a) FROM ApiUsage a WHERE a.timestamp >= :since GROUP BY DATE(a.timestamp)")
    List<Object[]> countDailyRequestsSince(LocalDateTime since);

    @Query("SELECT a.apiKey.user.email, COUNT(a) FROM ApiUsage a GROUP BY a.apiKey.user.email ORDER BY COUNT(a) DESC")
    List<Object[]> findTopUsersByRequestCount();

    @Query("SELECT a.endpoint, COUNT(a) FROM ApiUsage a GROUP BY a.endpoint ORDER BY COUNT(a) DESC")
    List<Object[]> countRequestsByEndpoint();
}
