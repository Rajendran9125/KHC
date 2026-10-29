package com.khc.repository;

import com.khc.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepo extends JpaRepository<City, Integer> {
    List<City> findByStateId(Integer stateId);
    List<City> findByStateIdAndIsActiveTrue(Integer stateId);
    List<City> findByIsActiveTrue();
}
