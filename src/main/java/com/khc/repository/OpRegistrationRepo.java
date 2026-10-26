package com.khc.repository;

import com.khc.model.OpRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OpRegistrationRepo extends JpaRepository<OpRegistration, Long> {
    Optional<OpRegistration> findByOpNumber(String opNumber);

    List<OpRegistration> findAllByOrderByRegistrationDateDesc();

    @Query("SELECT r FROM OpRegistration r WHERE " +
           "(:query IS NULL OR LOWER(r.opNumber) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(r.patient.firstName) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(r.patient.lastName) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(r.patient.mrn) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR r.patient.mobile LIKE CONCAT('%', :query, '%')) " +
           "ORDER BY r.registrationDate DESC")
    List<OpRegistration> searchRegistrations(@Param("query") String query);

    List<OpRegistration> findByRegistrationDateBetweenOrderByRegistrationDateDesc(
            LocalDateTime start, LocalDateTime end);

    long countByVisitType(String visitType);
}
