package com.example.godogbe.Entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class PolicySupportInformation {

    @Id
    private String id;

    @Column(name = "deadline_for_application")
    private String deadlineForApplication;

    @Column(name = "title")
    private String title;

    @Column(name = "busines_overview")
    private String businessOverview;

    @Column(name = "amount")
    private String amount;

    @Column(name = "url", length = 2000)
    private String url;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    public PolicySupportInformation(String id, String deadlineForApplication, String title, String businessOverview, String amount, String url) {
        this.id = id;
        this.deadlineForApplication = deadlineForApplication;
        this.title = title;
        this.businessOverview = businessOverview;
        this.amount = amount;
        this.url = url;
    }

}
