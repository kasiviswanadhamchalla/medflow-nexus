package com.microservices.billing_service.repository;

import com.microservices.billing_service.entity.Bill;
import com.microservices.billing_service.entity.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findByPatientId(Long patientId);
    List<Bill> findByStatus(InvoiceStatus status);
}
