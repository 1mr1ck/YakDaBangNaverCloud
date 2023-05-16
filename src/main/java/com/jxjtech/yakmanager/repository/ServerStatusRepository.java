package com.jxjtech.yakmanager.repository;

import com.jxjtech.yakmanager.entity.ServerStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ServerStatusRepository extends JpaRepository<ServerStatusEntity, Long> {

    @Query(value = "SELECT * FROM serverstatus where serverStatusId = 1", nativeQuery = true)
    Optional<ServerStatusEntity> findFirst();
}
