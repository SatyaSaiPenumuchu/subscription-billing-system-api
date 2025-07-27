package com.subscriptionapi.billing_system.config;

import com.subscriptionapi.billing_system.repository.ApiKeyRepository;
import com.subscriptionapi.billing_system.repository.ApiUsageRepository;
import com.subscriptionapi.billing_system.security.ApiKeyAuthFilter;
import com.subscriptionapi.billing_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true) // Enable @PreAuthorize annotations
@RequiredArgsConstructor
public class SecurityConfig {

    private final ApiKeyRepository apiKeyRepository;
    private final ApiUsageRepository apiUsageRepository;
    private final @Lazy UserService userService;

    @Bean
    public ApiKeyAuthFilter apiKeyAuthFilter() {
        return new ApiKeyAuthFilter(apiKeyRepository, userService, apiUsageRepository);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // disable CSRF for APIs; enable if you have forms
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/register").permitAll()    // public registration
                        .requestMatchers("/admin/**").hasRole("ADMIN")          // admin
                        .requestMatchers("/api/**").authenticated()             // all other API calls require auth
                        .anyRequest().denyAll()                                  // deny everything else by default
                )
                .addFilterBefore(apiKeyAuthFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
