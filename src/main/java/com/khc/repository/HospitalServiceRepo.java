package com.khc.repository;

import com.khc.model.HospitalService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalServiceRepo extends JpaRepository<HospitalService, Long> {

    List<HospitalService> findByIsActiveTrue();

    @Query("SELECT s FROM HospitalService s WHERE " +
           "(:query IS NULL OR LOWER(s.serviceName) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(s.header) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(s.department) LIKE LOWER(CONCAT('%', :query, '%'))) " +
           "ORDER BY s.header ASC, s.serviceName ASC")
    List<HospitalService> searchServices(@Param("query") String query);

    List<HospitalService> findByHeaderIgnoreCase(String header);

    List<HospitalService> findByDepartmentIgnoreCase(String department);
}
