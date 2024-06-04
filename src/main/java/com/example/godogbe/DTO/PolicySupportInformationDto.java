package com.example.godogbe.DTO;

import com.example.godogbe.Entity.PolicySupportInformation;
import jakarta.persistence.Column;
import lombok.*;


@Getter
@Setter
@Builder
public class PolicySupportInformationDto {

    private String id;

    private String deadlineForApplication;

    private String title;

    private String businessOverview;

    private String amount;
    
    @Column(name = "url", length = 4000)
    private String url;

    private String institutionName;

    private String supplyLocation;

    public PolicySupportInformationDto(String id, String deadlineForApplication, String title, String businessOverview, String amount, String url, String institutionName, String supplyLocation) {
        this.id = id;
        this.deadlineForApplication = deadlineForApplication;
        this.title = title;
        this.businessOverview = businessOverview;
        this.amount = amount;
        this.url = url;
        this.institutionName = institutionName;
        this.supplyLocation = supplyLocation;
    }

    public PolicySupportInformation toEntity(PolicySupportInformationDto requestDTO) {
        return PolicySupportInformation.builder()
                .id(requestDTO.getId())
                .deadlineForApplication(requestDTO.getDeadlineForApplication())
                .title(requestDTO.getTitle())
                .businessOverview(requestDTO.getBusinessOverview())
                .amount(requestDTO.getAmount())
                .url(requestDTO.getUrl())
                .institutionName(requestDTO.getInstitutionName())
                .supplyLocation(requestDTO.getSupplyLocation())
                .build();
    }
}
