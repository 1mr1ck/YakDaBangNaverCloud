package com.jxjtech.yakmanager.entity;

import com.jxjtech.yakmanager.dto.InvitationDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "invitation")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InvitationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invitationId")
    private Long invitationId;
    @Column(name = "invitationCode")
    private String invitationCode;
    @Column(name = "pharmacyId")
    private Long pharmacyId;
    @Column(name = "regDate")
    private Long regDate;

    public InvitationEntity(InvitationDTO dto) {
        this.invitationCode = dto.getInvitationCode();
        this.pharmacyId = dto.getPharmacyId();
        this.regDate = dto.getRegDate();
    }
}
