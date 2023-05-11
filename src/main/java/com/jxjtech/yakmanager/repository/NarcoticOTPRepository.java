package com.jxjtech.yakmanager.repository;

import com.jxjtech.yakmanager.entity.NarcoticOTPEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NarcoticOTPRepository extends JpaRepository<NarcoticOTPEntity, Long> {


    Optional<NarcoticOTPEntity> findByMemberId(Long memberId);

    @Query(value = "SELECT OTPCode FROM narcoticotp", nativeQuery = true)
    List<String> findAllGetCode();


    Optional<NarcoticOTPEntity> findByOTPCode(String otp);
}
