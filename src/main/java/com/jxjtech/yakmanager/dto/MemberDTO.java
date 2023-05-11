package com.jxjtech.yakmanager.dto;

import com.jxjtech.yakmanager.entity.Authority;
import com.jxjtech.yakmanager.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {

    private Long memberId;
    private String memberEmail;
    private String memberPassword;
    private String memberName;
    private String memberNickName;
    private Authority memberRole;
    private String snsType;
    private Timestamp joinDate;
    private Timestamp lastAccess;
    private int memberAction;

    public static MemberDTO of(RegisterResponseDTO dto) {
        MemberDTO result = new MemberDTO();

        result.setMemberEmail(dto.getMemberEmail());
        result.setMemberNickName(dto.getMemberNickName());
        result.setSnsType(dto.getSnsType());
        result.setMemberRole(Authority.user);

        return result;
    }

    public MemberDTO(MemberEntity member) {
        this.memberId = member.getMemberId();
        this.memberEmail = member.getMemberEmail();
        this.memberNickName = member.getMemberNickName();
        this.memberName = member.getMemberName();
        this.memberRole = member.getMemberRole();
        this.snsType = member.getSnsType();
        this.joinDate = member.getJoinDate();
        this.lastAccess = member.getLastAccess();
    }
}
