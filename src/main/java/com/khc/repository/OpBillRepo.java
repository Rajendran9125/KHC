package com.khc.repository;

import com.khc.model.OpBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OpBillRepo extends JpaRepository<OpBill, Long> {
    Optional<OpBill> findByBillNumber(String billNumber);

    List<OpBill> findAllByOrderByBillDateDesc();

    @Query("SELECT b FROM OpBill b WHERE " +
           "(:query IS NULL OR LOWER(b.billNumber) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(b.patient.firstName) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(b.patient.lastName) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(b.patient.mrn) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR b.patient.mobile LIKE CONCAT('%', :query, '%')) " +
           "ORDER BY b.billDate DESC")
    List<OpBill> searchBills(@Param("query") String query);

    List<OpBill> findByBillDateBetweenOrderByBillDateDesc(LocalDateTime start, LocalDateTime end);

    @Query("SELECT SUM(b.totalAmount) FROM OpBill b")
    Double sumTotalRevenue();

    @Query("SELECT SUM(COALESCE(b.paidAmount, 0) - COALESCE(b.refundedAmount, 0)) FROM OpBill b")
    Double sumPaidRevenue();
}
