package com.jxjtech.yakmanager.entity;

import com.jxjtech.yakmanager.dto.MemberDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "member")
@NoArgsConstructor
@AllArgsConstructor
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberId")
    private Long memberId;
    @Column(name = "memberEmail")
    private String memberEmail;
    @Column(name = "memberPassword")
    private String memberPassword;
    @Column(name = "memberName")
    private String memberName;
    @Column(name = "memberNickName")
    private String memberNickName;
    @Column(name = "memberRole")
    @Enumerated(EnumType.STRING)
    private Authority memberRole;
    @Column(name = "snsType")
    private String snsType;
    @Column(name = "timeZone")
    private String timeZone;
    @Column(name = "joinDate")
    @CreationTimestamp
    private Timestamp joinDate;
    @Column(name = "lastAccess")
    @UpdateTimestamp
    private Timestamp lastAccess;
    @Column(name = "memberAction")
    private int memberAction;

    public MemberEntity(MemberDTO dto) {
        this.memberEmail = dto.getMemberEmail();
        this.memberPassword = dto.getMemberPassword();
        this.memberNickName = dto.getMemberNickName();
        this.snsType = dto.getSnsType();
        this.memberRole = dto.getMemberRole();
        this.timeZone = dto.getTimeZone();
    }

    public static MemberEntity actionUp(MemberEntity member, String timeZone) {
        MemberEntity result = member;
        result.setMemberAction(result.getMemberAction() + 1);
        result.setTimeZone(timeZone);

        return  result;
    }
}
