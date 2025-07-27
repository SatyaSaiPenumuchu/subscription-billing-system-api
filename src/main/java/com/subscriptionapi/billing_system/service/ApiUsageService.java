package com.subscriptionapi.billing_system.service;

import com.subscriptionapi.billing_system.model.ApiUsage;
import com.subscriptionapi.billing_system.repository.ApiUsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiUsageService {

    private final ApiUsageRepository apiUsageRepository;

    public List<ApiUsage> getUsageByApiKeyId(Long apiKeyId) {
        return apiUsageRepository.findByApiKey_Id(apiKeyId);
    }
}
