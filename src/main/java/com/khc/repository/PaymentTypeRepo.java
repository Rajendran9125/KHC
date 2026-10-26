package com.khc.repository;

import com.khc.model.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentTypeRepo extends JpaRepository<PaymentType, Long> {
    List<PaymentType> findByIsActiveTrue();
}
