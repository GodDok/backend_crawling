package com.example.godogbe.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class getPolicySupportInformationByPolicyIdRequestDto {
    private List<String> policyIds;
}
