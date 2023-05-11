package com.jxjtech.yakmanager.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Table(name = "narcoticOTP")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NarcoticOTPEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "narcoticOTPId")
    private Long narcoticOTPId;
    @Column(name = "OTPCode")
    private String OTPCode;
    @Column(name = "regDate")
    @CreationTimestamp
    private Timestamp regDate;
    @Column(name = "memberId")
    private Long memberId;


    public NarcoticOTPEntity(Long memberId, String toString) {
        this.memberId = memberId;
        this.OTPCode = toString;
    }
}
