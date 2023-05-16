package com.jxjtech.yakmanager.repository;

import com.jxjtech.yakmanager.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    @Query(value = "SELECT * FROM refreshtoken where memberId = ?", nativeQuery = true)
    Optional<RefreshTokenEntity> findByMemberId(Long memberId);

    void deleteByMemberId(Long memberId);

    Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);
}
