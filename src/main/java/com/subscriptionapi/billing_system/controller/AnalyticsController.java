package com.subscriptionapi.billing_system.controller;

import com.subscriptionapi.billing_system.model.User;
import com.subscriptionapi.billing_system.service.AnalyticsService;
import com.subscriptionapi.billing_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/usage")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final UserService userService;

    @GetMapping("/stats")
    public Map<String, Object> getUsageStats(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.getUserByEmail(email);
        return analyticsService.getUsageStats(user);
    }
}
