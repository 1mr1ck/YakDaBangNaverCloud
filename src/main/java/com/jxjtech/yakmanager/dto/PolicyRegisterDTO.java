package com.jxjtech.yakmanager.dto;

import com.jxjtech.yakmanager.entity.PolicyEntity;
import lombok.Data;

@Data
public class PolicyRegisterDTO {

    private boolean pushAgree;
    private boolean marketingAgree;

    public static PolicyEntity toPolicy(PolicyRegisterDTO dto, Long memberId) {
        return PolicyEntity.builder()
                .marketingAgree(dto.isMarketingAgree())
                .pushAgree(dto.isPushAgree())
                .memberId(memberId)
                .build();
    }
}
