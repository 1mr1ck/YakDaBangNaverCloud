package com.jxjtech.yakmanager.repository;

import com.jxjtech.yakmanager.entity.NarcoticTitleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NarcoticTitleRepository extends JpaRepository<NarcoticTitleEntity, Long> {

    @Query(value = "SELECT * FROM narcotictitle where narcoticTitleName = ?", nativeQuery = true)
    Optional<NarcoticTitleEntity> findByOrigName(String origName);

    @Query(value = "select auto_increment from information_schema.tables where table_name = 'narcotictitle' and table_schema = database();", nativeQuery = true)
    Long findMaxId();

    @Query(value = "SELECT * FROM narcotictitle where memberId = ?", nativeQuery = true)
    List<NarcoticTitleEntity> findAllByMemberId(Long memberId);
}
