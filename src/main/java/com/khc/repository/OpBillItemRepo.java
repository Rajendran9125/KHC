package com.khc.repository;

import com.khc.model.OpBillItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface OpBillItemRepo extends JpaRepository<OpBillItem, Long> {

    @Query("SELECT i.serviceCategory as category, COUNT(i) as count, SUM(i.totalPrice) as total " +
           "FROM OpBillItem i GROUP BY i.serviceCategory")
    List<Map<String, Object>> getServiceWiseRevenue();
}
