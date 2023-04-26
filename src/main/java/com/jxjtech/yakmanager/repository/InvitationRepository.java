package com.jxjtech.yakmanager.repository;

import com.jxjtech.yakmanager.entity.InvitationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface InvitationRepository extends JpaRepository<InvitationEntity, Long> {

    @Query(value = "SELECT * FROM invitation WHERE pharmacyId = ?", nativeQuery = true)
    Optional<InvitationEntity> findByPharmacyId(Long pharmacyId);

    @Query(value = "SELECT * FROM invitation WHERE invitationCode = ?", nativeQuery = true)
    Optional<InvitationEntity> findByInvitationCode(String invitationCode);
}
