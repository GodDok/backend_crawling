package com.example.godogbe.Repository;

import com.example.godogbe.Entity.PolicySearch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PolicySearchRepository extends JpaRepository<PolicySearch,Long> {
    List<PolicySearch> findByPolicyId(String PolicyId);
    List<PolicySearch> findBySearchCondition(String searchCondition);
    List<PolicySearch> findByPolicyIdAndSearchCondition(String policyId, String searchCondition);
}
