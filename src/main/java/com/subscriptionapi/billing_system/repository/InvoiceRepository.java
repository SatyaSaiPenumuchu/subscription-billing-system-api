package com.subscriptionapi.billing_system.repository;

import com.subscriptionapi.billing_system.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByApiKey_User_Id(Long userId);
}
