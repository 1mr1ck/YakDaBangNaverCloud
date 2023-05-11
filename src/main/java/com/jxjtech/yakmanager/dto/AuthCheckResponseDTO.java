package com.jxjtech.yakmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthCheckResponseDTO {

    boolean result;
    String msg;
}
