package com.subscriptionapi.billing_system.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "api_usage")
@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String endpoint;

    private int httpStatus;

    private LocalDateTime timestamp;

    private String clientIp;

    private long responseTimeMs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "api_key_id")
    private ApiKey apiKey;
}
