package com.subscriptionapi.billing_system.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "subscription_plans")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String planName;

    private int monthlyLimit;   // API call limit

    private int rateLimitPerSecond;

    private double price;

    private boolean analyticsEnabled;

    private boolean logAccessEnabled;

    @Column(name = "daily_limit")
    private Integer dailyLimit; // null or -1 means unlimited

}
