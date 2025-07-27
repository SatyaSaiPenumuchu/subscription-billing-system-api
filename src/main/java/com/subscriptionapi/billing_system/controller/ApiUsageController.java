package com.subscriptionapi.billing_system.controller;

import com.subscriptionapi.billing_system.model.ApiKey;
import com.subscriptionapi.billing_system.model.ApiUsage;
import com.subscriptionapi.billing_system.model.User;
import com.subscriptionapi.billing_system.service.ApiUsageService;
import com.subscriptionapi.billing_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class ApiUsageController {

    private final UserService userService;
    private final ApiUsageService apiUsageService;

    @GetMapping
    public List<ApiUsage> getLogs(Authentication authentication) {
        User user = userService.getUserByEmail(authentication.getName());

        if (user.getApiKeys().isEmpty()) {
            throw new RuntimeException("No API key found for user.");
        }

        ApiKey apiKey = user.getApiKeys().get(0);

        // ✅ Check if subscription allows acess to logs
        if (!apiKey.getSubscriptionPlan().isLogAccessEnabled()) {
            throw new RuntimeException("Access to logs is not enabled for your subscription plan.");
        }

        return apiUsageService.getUsageByApiKeyId(apiKey.getId());
    }
}
