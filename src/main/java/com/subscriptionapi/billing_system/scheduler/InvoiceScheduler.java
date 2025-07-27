package com.subscriptionapi.billing_system.scheduler;

import com.subscriptionapi.billing_system.model.ApiKey;
import com.subscriptionapi.billing_system.model.Invoice;
import com.subscriptionapi.billing_system.model.InvoiceStatus;
import com.subscriptionapi.billing_system.repository.ApiKeyRepository;
import com.subscriptionapi.billing_system.repository.InvoiceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class InvoiceScheduler {

    private final ApiKeyRepository apiKeyRepository;
    private final InvoiceRepository invoiceRepository;

    // Run on 1st of every month at 2:00 AM
    @Scheduled(cron = "0 0 2 1 * *")
    @Transactional
    public void generateMonthlyInvoices() {
        log.info("Invoice generation started...");

        List<ApiKey> apiKeys = apiKeyRepository.findAll();

        for (ApiKey apiKey : apiKeys) {
            int requests = apiKey.getRequestsThisMonth() != null ? apiKey.getRequestsThisMonth() : 0;
            if (requests == 0) continue;

            double price = apiKey.getSubscriptionPlan().getPrice(); // flat monthly price

            Invoice invoice = Invoice.builder()
                    .apiKey(apiKey)
                    .billingDate(LocalDate.now())
                    .totalRequests(requests)
                    .amount(price)
                    .status(InvoiceStatus.UNPAID)
                    .build();

            invoiceRepository.save(invoice);

            apiKey.setRequestsThisMonth(0); // reset counter
            apiKeyRepository.save(apiKey);
        }

        log.info("Invoice generation completed.");
    }
}
