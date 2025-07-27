package com.subscriptionapi.billing_system.controller;

import com.subscriptionapi.billing_system.repository.ApiUsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/admin/usage")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUsageController {

    private final ApiUsageRepository apiUsageRepository;

    // 1. Summary by API Key
    @GetMapping("/summary")
    public ResponseEntity<?> getUsageSummary() {
        var summary = apiUsageRepository.countRequestsGroupedByApiKey();
        return ResponseEntity.ok(summary);
    }

    // 2. Daily Usage for the past 7 days
    @GetMapping("/daily")
    public ResponseEntity<?> getDailyUsage() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        var stats = apiUsageRepository.countDailyRequestsSince(sevenDaysAgo);
        return ResponseEntity.ok(stats);
    }

    // 3. Top Users
    @GetMapping("/top-users")
    public ResponseEntity<?> getTopUsers() {
        var topUsers = apiUsageRepository.findTopUsersByRequestCount();
        return ResponseEntity.ok(topUsers);
    }

    // 4. Most accessed endpoints
    @GetMapping("/endpoints")
    public ResponseEntity<?> getMostHitEndpoints() {
        var endpoints = apiUsageRepository.countRequestsByEndpoint();
        return ResponseEntity.ok(endpoints);
    }
}
