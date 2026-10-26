package com.khc.repository;

import com.khc.model.PatientType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientTypeRepo extends JpaRepository<PatientType, Long> {
    List<PatientType> findByIsActiveTrue();
}
