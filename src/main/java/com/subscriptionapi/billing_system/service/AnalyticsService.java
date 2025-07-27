package com.subscriptionapi.billing_system.service;

import com.subscriptionapi.billing_system.model.ApiUsage;
import com.subscriptionapi.billing_system.model.User;
import com.subscriptionapi.billing_system.repository.ApiUsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final ApiUsageRepository apiUsageRepository;

    public Map<String, Object> getUsageStats(User user) {
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime now = LocalDateTime.now();

        // Assuming user has only one active key
        Long apiKeyId = user.getApiKeys().get(0).getId();

        List<ApiUsage> usages = apiUsageRepository.findByApiKey_IdAndTimestampBetween(apiKeyId, startOfMonth, now);

        long requestsToday = usages.stream()
                .filter(u -> u.getTimestamp().isAfter(startOfToday))
                .count();

        long requestsThisMonth = usages.size();

        Map<String, Long> mostUsedEndpoints = usages.stream()
                .collect(Collectors.groupingBy(ApiUsage::getEndpoint, Collectors.counting()));

        Map<String, Object> stats = new HashMap<>();
        stats.put("requestsToday", requestsToday);
        stats.put("requestsThisMonth", requestsThisMonth);
        stats.put("mostUsedEndpoints", sortByValueDesc(mostUsedEndpoints));

        return stats;
    }

    private Map<String, Long> sortByValueDesc(Map<String, Long> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new
                ));
    }
}
