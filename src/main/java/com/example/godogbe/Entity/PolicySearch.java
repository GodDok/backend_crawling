package com.example.godogbe.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class PolicySearch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "policy_id")
    private String policyId;

    @Column(name = "search_condition")
    private String searchCondition;

    @Builder
    public PolicySearch( String policyId, String searchCondition) {
        this.policyId = policyId;
        this.searchCondition = searchCondition;
    }
}
