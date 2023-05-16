package com.jxjtech.yakmanager.repository;

import com.jxjtech.yakmanager.entity.RecentLoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RecentLoginRepository extends JpaRepository<RecentLoginEntity, Long> {

    @Query(value = "SELECT * FROM recentlogin WHERE phoneValue = ?", nativeQuery = true)
    Optional<RecentLoginEntity> findByPhoneValue(String phoneValue);

    boolean existsByPhoneValue(String phoneValue);
}
