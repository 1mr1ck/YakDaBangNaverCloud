package com.jxjtech.yakmanager.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "recentlogin")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecentLoginEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recentLoginId")
    private Long recentLoginId;
    @Column(name = "phoneValue")
    private String phoneValue;
    @Column(name = "snsType")
    private String snsType;
    @Column(name = "osType")
    private String osType;
    @Column(name = "buildVersion")
    private String buildVersion;
    @Column(name = "modDate")
    @UpdateTimestamp
    private Timestamp modDate;

    public RecentLoginEntity(String phoneValue, String osType, String snsType, String buildVersion) {
        this.phoneValue = phoneValue;
        this.osType = osType;
        this.snsType = snsType;
        this.buildVersion = buildVersion;
    }

    public void update(RecentLoginEntity recentLoginEntity, String buildVersion, String snsType) {
        recentLoginEntity.setBuildVersion(buildVersion);
        recentLoginEntity.setSnsType(snsType);
    }
}
