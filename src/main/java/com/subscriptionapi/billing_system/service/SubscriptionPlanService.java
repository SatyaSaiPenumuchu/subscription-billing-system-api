package com.subscriptionapi.billing_system.service;

import com.subscriptionapi.billing_system.model.SubscriptionPlan;
import com.subscriptionapi.billing_system.repository.SubscriptionPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;

    public List<SubscriptionPlan> getAllPlans() {
        return subscriptionPlanRepository.findAll();
    }

    public SubscriptionPlan getPlanById(Long id) {
        return subscriptionPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found"));
    }

    public SubscriptionPlan createPlan(SubscriptionPlan plan) {
        return subscriptionPlanRepository.save(plan);
    }

    public SubscriptionPlan updatePlan(Long id, SubscriptionPlan updatedPlan) {
        SubscriptionPlan existingPlan = getPlanById(id);
        existingPlan.setPlanName(updatedPlan.getPlanName());
        existingPlan.setMonthlyLimit(updatedPlan.getMonthlyLimit());
        existingPlan.setDailyLimit(updatedPlan.getDailyLimit());
        // Update other fields as necessary
        return subscriptionPlanRepository.save(existingPlan);
    }

    public void deletePlan(Long id) {
        subscriptionPlanRepository.deleteById(id);
    }
}
