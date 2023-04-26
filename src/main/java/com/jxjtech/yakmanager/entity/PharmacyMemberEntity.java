package com.jxjtech.yakmanager.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "pharmacy_member")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PharmacyMemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pharmacyMemberId")
    private Long pharmacyMemberId;
    @Column(name = "memberId")
    private Long memberId;
    @Column(name = "pharmacyId")
    private Long pharmacyId;
    @Column(name = "pharmacyMemberRole")
    @Enumerated(EnumType.STRING)
    private Authority pharmacyMemberRole;

    public PharmacyMemberEntity(PharmacyEntity pharmacyEntity) {
        this.memberId = pharmacyEntity.getMemberId();
        this.pharmacyId = pharmacyEntity.getPharmacyId();
        this.pharmacyMemberRole = Authority.admin;
    }

    public PharmacyMemberEntity(Long memberId, Long pharmacyId) {
        this.memberId = memberId;
        this.pharmacyId = pharmacyId;
        this.pharmacyMemberRole = Authority.user;
    }
}
