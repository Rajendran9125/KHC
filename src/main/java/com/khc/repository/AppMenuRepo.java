package com.khc.repository;

import com.khc.model.AppMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppMenuRepo extends JpaRepository<AppMenu, Long> {
    List<AppMenu> findByIsActiveTrueOrderByDisplayOrderAsc();
    boolean existsByMenuCode(String menuCode);
}
