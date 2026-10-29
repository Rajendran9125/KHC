package com.khc.repository;

import com.khc.model.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StateRepo extends JpaRepository<State, Integer> {
    List<State> findByCountryId(Integer countryId);
    List<State> findByCountryIdAndIsActiveTrue(Integer countryId);
    List<State> findByIsActiveTrue();
}
