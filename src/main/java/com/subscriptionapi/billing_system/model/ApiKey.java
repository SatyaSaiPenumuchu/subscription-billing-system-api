package com.subscriptionapi.billing_system.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "api_keys")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "`key`", unique = true, nullable = false, length = 64)
    private String key;

    private LocalDateTime createdAt;

    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private SubscriptionPlan subscriptionPlan;

    @OneToMany(mappedBy = "apiKey", cascade = CascadeType.ALL)
    private java.util.List<ApiUsage> usageLogs;

    @OneToMany(mappedBy = "apiKey", cascade = CascadeType.ALL)
    private java.util.List<Invoice> invoices;

    @Column(name = "requests_this_month")
    private Integer requestsThisMonth = 0;



}
