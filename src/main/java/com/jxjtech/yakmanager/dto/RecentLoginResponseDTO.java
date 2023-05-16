package com.jxjtech.yakmanager.dto;

import com.jxjtech.yakmanager.entity.RecentLoginEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecentLoginResponseDTO {

    String osType;
    String phoneValue;
    String snsType;
    boolean result;

    public RecentLoginResponseDTO(RecentLoginEntity entity) {
        this.osType = entity.getOsType();
        this.phoneValue = entity.getPhoneValue();
        this.snsType = entity.getSnsType();
        this.result = true;
    }


    public RecentLoginResponseDTO(boolean check) {
        result = check;
    }
}
