package com.subscriptionapi.billing_system.security;

import com.subscriptionapi.billing_system.model.ApiKey;
import com.subscriptionapi.billing_system.model.ApiUsage;
import com.subscriptionapi.billing_system.model.SubscriptionPlan;
import com.subscriptionapi.billing_system.model.User;
import com.subscriptionapi.billing_system.repository.ApiKeyRepository;
import com.subscriptionapi.billing_system.repository.ApiUsageRepository;
import com.subscriptionapi.billing_system.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private final ApiKeyRepository apiKeyRepository;
    private final UserService userService;
    private final ApiUsageRepository apiUsageRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String apiKeyValue = request.getHeader("X-API-KEY");

        if (!StringUtils.hasText(apiKeyValue)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "API key is missing");
            return;
        }

        ApiKey apiKey = apiKeyRepository.findByKey(apiKeyValue).orElse(null);

        if (apiKey == null || !apiKey.isActive()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or inactive API key");
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();

        long usageCount = apiUsageRepository
                .findByApiKey_IdAndTimestampBetween(apiKey.getId(), startOfDay, now)
                .size();

        Integer dailyLimit = apiKey.getSubscriptionPlan().getDailyLimit();

        if (dailyLimit != null && dailyLimit > 0 && usageCount >= dailyLimit) {
            response.sendError(429, "Daily API limit exceeded");
            return;
        }


        // Usage quota check
        SubscriptionPlan plan = apiKey.getSubscriptionPlan();
        if (plan == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Subscription plan not found.");
            return;
        }

        int currentUsage = apiKey.getRequestsThisMonth() != null ? apiKey.getRequestsThisMonth() : 0;

        if (currentUsage >= plan.getMonthlyLimit()) {
            response.setStatus(429);
            response.getWriter().write("API quota exceeded for this month.");
            return;
        }

        // Increment request count
        apiKey.setRequestsThisMonth(currentUsage + 1);
        apiKeyRepository.save(apiKey);

        // Load user with role eagerly
        User user = userService.getUserByIdEager(apiKey.getUser().getId());

        var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        var authToken = new UsernamePasswordAuthenticationToken(user, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // Track request time and log usage
        long startTime = System.currentTimeMillis();
        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;

            ApiUsage usageLog = ApiUsage.builder()
                    .apiKey(apiKey)
                    .endpoint(request.getRequestURI())
                    .httpStatus(response.getStatus())
                    .timestamp(LocalDateTime.now())
                    .clientIp(getClientIp(request))
                    .responseTimeMs(duration)
                    .build();

            apiUsageRepository.save(usageLog);
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded == null || forwarded.isBlank()) {
            return request.getRemoteAddr();
        }
        return forwarded.split(",")[0].trim();
    }
}
