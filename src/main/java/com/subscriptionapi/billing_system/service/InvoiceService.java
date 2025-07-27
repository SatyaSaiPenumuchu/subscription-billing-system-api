package com.subscriptionapi.billing_system.service;

import com.subscriptionapi.billing_system.model.ApiKey;
import com.subscriptionapi.billing_system.model.Invoice;
import com.subscriptionapi.billing_system.model.InvoiceStatus;
import com.subscriptionapi.billing_system.repository.ApiKeyRepository;
import com.subscriptionapi.billing_system.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ApiKeyRepository apiKeyRepository;

    public Invoice generateInvoice(Long apiKeyId) {
        ApiKey apiKey = apiKeyRepository.findById(apiKeyId)
                .orElseThrow(() -> new RuntimeException("API Key not found"));

        int requests = apiKey.getRequestsThisMonth() != null ? apiKey.getRequestsThisMonth() : 0;
        double pricePerPlan = apiKey.getSubscriptionPlan().getPrice();
        double amount = pricePerPlan; // or custom calculation if needed

        Invoice invoice = Invoice.builder()
                .billingDate(LocalDate.now())
                .totalRequests(requests)
                .amount(amount)
                .status(InvoiceStatus.UNPAID)
                .apiKey(apiKey)
                .build();

        return invoiceRepository.save(invoice);
    }

    public List<Invoice> getInvoicesByUserId(Long userId) {
        return invoiceRepository.findByApiKey_User_Id(userId);
    }
    public Invoice getInvoiceByIdForUser(Long invoiceId, Long userId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        if (!invoice.getApiKey().getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access to invoice");
        }

        return invoice;
    }

    public void markInvoiceAsPaid(Long invoiceId, Long userId) {
        Invoice invoice = getInvoiceByIdForUser(invoiceId, userId);

        invoice.setStatus(InvoiceStatus.PAID);
        invoiceRepository.save(invoice);
    }

}
