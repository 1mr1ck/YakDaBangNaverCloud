package com.jxjtech.yakmanager.entity;

import com.jxjtech.yakmanager.dto.PharmacyDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "pharmacy")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PharmacyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pharmacyId")
    private Long pharmacyId;
    @Column(name = "pharmacyName")
    private String pharmacyName;
    @Column(name = "memberId")
    private Long memberId;
    @Column(name = "totalTitle")
    private int totalTitle;

    public PharmacyEntity(Long memberId, String pharmacyName) {
        this.pharmacyName = pharmacyName;
        this.memberId = memberId;
    }

    public PharmacyEntity(PharmacyDTO dto) {
        this.pharmacyId = dto.getPharmacyId();
        this.pharmacyName = dto.getPharmacyName();
        this.memberId = dto.getMemberId();
        this.totalTitle = dto.getTotalTitle();
    }
}
