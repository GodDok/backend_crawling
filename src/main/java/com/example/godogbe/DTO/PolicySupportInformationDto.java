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

    public PolicySupportInformationDto(String id, String deadlineForApplication, String title, String businessOverview, String amount, String url) {
        this.id = id;
        this.deadlineForApplication = deadlineForApplication;
        this.title = title;
        this.businessOverview = businessOverview;
        this.amount = amount;
        this.url = url;
    }

    public PolicySupportInformation toEntity(PolicySupportInformationDto requestDTO) {
        return PolicySupportInformation.builder()
                .id(requestDTO.getId())
                .deadlineForApplication(requestDTO.getDeadlineForApplication())
                .title(requestDTO.getTitle())
                .businessOverview(requestDTO.getBusinessOverview())
                .amount(requestDTO.getAmount())
                .url(requestDTO.getUrl())
                .build();
    }
}
