package com.example.godogbe.Repository;

import com.example.godogbe.Entity.PolicySearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface PolicySearchRepository extends JpaRepository<PolicySearch,Long> {
    List<PolicySearch> findByPolicyId(String PolicyId);
    List<PolicySearch> findBySearchCondition(String searchCondition);
    List<PolicySearch> findByPolicyIdAndSearchCondition(String policyId, String searchCondition);


    @Modifying
    @Query("delete from PolicySearch c where c.id in :ids")
    void deleteAllByIds(@Param("ids") List<String> ids);
}
