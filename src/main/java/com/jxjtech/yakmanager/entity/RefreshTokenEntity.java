package com.jxjtech.yakmanager.entity;

import com.jxjtech.yakmanager.dto.TokenDTO;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "refreshtoken")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refreshTokenId")
    private Long refreshTokenId;
    @Column(name = "memberId")
    private String memberId;
    @Column(name = "refreshToken")
    private String refreshToken;
    @UpdateTimestamp
    @Column(name = "modDate")
    private Timestamp modDate;

    public static RefreshTokenEntity of(TokenDTO tokenDTO) {
        return RefreshTokenEntity.builder()
                .memberId(tokenDTO.getMemberId())
                .refreshToken(tokenDTO.getRefreshToken())
                .build();
    }

}
