package com.subscriptionapi.billing_system.controller;

import com.subscriptionapi.billing_system.model.SubscriptionPlan;
import com.subscriptionapi.billing_system.service.SubscriptionPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/plans")
@RequiredArgsConstructor
public class AdminSubscriptionPlanController {

    private final SubscriptionPlanService subscriptionPlanService;

    @GetMapping
    public List<SubscriptionPlan> getAllPlans() {
        return subscriptionPlanService.getAllPlans();
    }

    @GetMapping("/{id}")
    public SubscriptionPlan getPlanById(@PathVariable Long id) {
        return subscriptionPlanService.getPlanById(id);
    }

    @PostMapping
    public SubscriptionPlan createPlan(@RequestBody SubscriptionPlan plan) {
        return subscriptionPlanService.createPlan(plan);
    }

    @PutMapping("/{id}")
    public SubscriptionPlan updatePlan(@PathVariable Long id, @RequestBody SubscriptionPlan updatedPlan) {
        return subscriptionPlanService.updatePlan(id, updatedPlan);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlan(@PathVariable Long id) {
        subscriptionPlanService.deletePlan(id);
        return ResponseEntity.ok().build();
    }
}
