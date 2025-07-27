package com.subscriptionapi.billing_system.controller;

import com.subscriptionapi.billing_system.model.Invoice;
import com.subscriptionapi.billing_system.model.User;
import com.subscriptionapi.billing_system.service.InvoiceService;
import com.subscriptionapi.billing_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final UserService userService;

    @PostMapping("/generate")
    public Invoice generateInvoice(Authentication authentication) {
        User user = userService.getUserByEmail(authentication.getName());

        if (user.getApiKeys().isEmpty()) {
            throw new RuntimeException("No API key found for user");
        }

        Long apiKeyId = user.getApiKeys().get(0).getId();
        return invoiceService.generateInvoice(apiKeyId);
    }

    @GetMapping
    public List<Invoice> getInvoices(Authentication authentication) {
        User user = userService.getUserByEmail(authentication.getName());
        return invoiceService.getInvoicesByUserId(user.getId());
    }
    @GetMapping("/{id}")
    public Invoice getInvoiceById(@PathVariable Long id, Authentication authentication) {
        User user = userService.getUserByEmail(authentication.getName());
        return invoiceService.getInvoiceByIdForUser(id, user.getId());
    }
    @PostMapping("/{id}/pay")
    public String markAsPaid(@PathVariable Long id, Authentication authentication) {
        User user = userService.getUserByEmail(authentication.getName());
        invoiceService.markInvoiceAsPaid(id, user.getId());
        return "Invoice marked as paid.";
    }

}
