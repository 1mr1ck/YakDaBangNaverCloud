package com.jxjtech.yakmanager.repository;

import com.jxjtech.yakmanager.entity.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {

    @Query(value = "SELECT * FROM notice WHERE noticeCategory = ?", nativeQuery = true)
    List<NoticeEntity> findAllByCategory(String category);
}
