package com.subscriptionapi.billing_system.controller;

import com.subscriptionapi.billing_system.model.SubscriptionPlan;
import com.subscriptionapi.billing_system.model.User;
import com.subscriptionapi.billing_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscription")
@RequiredArgsConstructor
public class SubscriptionPlanController {

    private final UserService userService;

    // Get current user's subscription plan
    @GetMapping("/me")
    public SubscriptionPlan getMyPlan(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.getUserByEmail(email);
        return userService.getUserSubscriptionPlan(user.getId());
    }

    // Change subscription plan
    @PostMapping("/change")
    public ResponseEntity<?> changePlan(Authentication authentication, @RequestParam String planName) {
        String email = authentication.getName();
        User user = userService.getUserByEmail(email);
        userService.changeUserSubscriptionPlan(user.getId(), planName);
        return ResponseEntity.ok("Subscription plan updated.");
    }
}
