package com.jxjtech.yakmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {

    @Schema(example = "accessToken")
    private String Authorization;
    @Schema(example = "refreshToken")
    private String RefreshToken;
    @Schema(example = "123624735737")
    private Long Authorization_Exp;
    @Schema(example = "15162626236236")
    private Long RefreshToken_Exp;
    @Schema(example = "8")
    private String memberId;
    private boolean result;

    public TokenDTO(boolean result) {
        this.result = result;
    }

    public static TokenDTO of(Map<String, String> reIssueToken) {

        if(reIssueToken.containsKey("refreshToken")) {
            return TokenDTO.builder()
                    .Authorization(reIssueToken.get("accessToken"))
                    .Authorization_Exp(Long.valueOf(reIssueToken.get("accessTokenExpiresIn")))
                    .RefreshToken(reIssueToken.get("refreshToken"))
                    .RefreshToken_Exp(Long.valueOf(reIssueToken.get("refreshTokenExpiresIn")))
                    .result(true)
                    .build();
        } else {
            return TokenDTO.builder()
                    .Authorization(reIssueToken.get("accessToken"))
                    .Authorization_Exp(Long.valueOf(reIssueToken.get("accessTokenExpiresIn")))
                    .RefreshToken(reIssueToken.get("notIssue"))
                    .RefreshToken_Exp(null)
                    .result(true)
                    .build();
        }
    }
}
