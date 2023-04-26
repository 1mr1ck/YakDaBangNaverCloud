package com.jxjtech.yakmanager.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "policy")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "policyId")
    private Long policyId;
    @Column(name = "memberId")
    private Long memberId;
    @Column(name = "pushAgree")
    private boolean pushAgree;
    @Column(name = "marketingAgree")
    private boolean marketingAgree;
    @Column(name = "acceptDate")
    @CreationTimestamp
    private Timestamp acceptDate;
}
