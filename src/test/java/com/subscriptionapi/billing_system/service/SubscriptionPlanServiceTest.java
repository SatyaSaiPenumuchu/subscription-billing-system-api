package com.subscriptionapi.billing_system.service;

import com.subscriptionapi.billing_system.model.SubscriptionPlan;
import com.subscriptionapi.billing_system.repository.SubscriptionPlanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SubscriptionPlanServiceTest {

    @Mock
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @InjectMocks
    private SubscriptionPlanService subscriptionPlanService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPlans() {
        List<SubscriptionPlan> plans = List.of(
                new SubscriptionPlan(1L, "Basic", 1000, 5, 10.0, false, false, 100),
                new SubscriptionPlan(2L, "Pro", 5000, 10, 30.0, true, true, 500)
        );
        when(subscriptionPlanRepository.findAll()).thenReturn(plans);
        //plan
        List<SubscriptionPlan> result = subscriptionPlanService.getAllPlans();

        assertThat(result).hasSize(2);
        verify(subscriptionPlanRepository, times(1)).findAll();
    }

    @Test
    void testGetPlanById() {
        SubscriptionPlan plan = new SubscriptionPlan(1L, "Basic", 1000, 5, 10.0, false, false, 100);
        when(subscriptionPlanRepository.findById(1L)).thenReturn(Optional.of(plan));

        SubscriptionPlan result = subscriptionPlanService.getPlanById(1L);

        assertThat(result.getPlanName()).isEqualTo("Basic");
    }

    @Test
    void testCreatePlan() {
        SubscriptionPlan newPlan = new SubscriptionPlan(null, "Starter", 500, 2, 5.0, false, false, 50);
        SubscriptionPlan savedPlan = new SubscriptionPlan(3L, "Starter", 500, 2, 5.0, false, false, 50);

        when(subscriptionPlanRepository.save(newPlan)).thenReturn(savedPlan);

        SubscriptionPlan result = subscriptionPlanService.createPlan(newPlan);

        assertThat(result.getId()).isEqualTo(3L);
        verify(subscriptionPlanRepository).save(newPlan);
    }
}
